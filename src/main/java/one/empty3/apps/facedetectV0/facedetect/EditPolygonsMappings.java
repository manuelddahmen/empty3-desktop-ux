/*
 *
 *  * Copyright (c) 2024. Manuel Daniel Dahmen
 *  *
 *  *
 *  *    Copyright 2024 Manuel Daniel Dahmen
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 *
 *
 */

/*
 * Created by JFormDesigner on Sat May 18 10:59:33 CEST 2024
 */

package one.empty3.apps.facedetectV0.facedetect;

import com.google.common.util.concurrent.AtomicDouble;
import one.empty3.apps.feature.app.replace.javax.imageio.ImageIO;
import net.miginfocom.swing.MigLayout;
import one.empty3.apps.morph.Main;
import one.empty3.library.*;
import one.empty3.library.objloader.E3Model;

import javax.swing.*;
import one.empty3.library.Point;
import one.empty3.libs.*;
import one.empty3.libs.Color;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author manue
 */
public class EditPolygonsMappings extends JPanel implements Runnable {
    private static final int EDIT_POINT_POSITION = 1;
    private static final int SELECT_POINT_POSITION = 2;
    private static final int SELECT_POINT_VERTEX = 4;
    public BufferedImage zBufferImage;
    private int mode;
    BufferedImage image;
    int selectedPointNo = -1;
    protected E3Model model;
    protected TestHumanHeadTexturing testHumanHeadTexturing;
    boolean threadDistanceIsNotRunning = true;
    /***
     * Contains named landkmarks points from @image
     */
    private HashMap<String, Point3D> pointsInImage = new HashMap<>();
    /***
     * contains (u,v) coordinates from project of @pointsInImage
     * projection is made with @E3Model.findUvFace(u, v)
     */
    private HashMap<String, Point3D> pointsInModel = new HashMap<>();
    protected boolean isRunning = true;
    private Point3D pFound = null;
    private String landmarkType;
    private double u;
    private double v;
    private int selectedPointNoOut = -1;
    private Point3D selectedPointOutUv = null;
    private Point3D selectedPointVertexOut;

    ITexture iTextureMorphImage = new ITexture() {
        @Override
        public MatrixPropertiesObject copy() throws CopyRepresentableError, IllegalAccessException, InstantiationException {
            return null;
        }

        @Override
        public int getColorAt(double u, double v) {
            if (distanceAB != null && !distanceAB.isInvalidArray() && image != null) {
                Point3D axPointInB = distanceAB.findAxPointInB(u, v);
                Point3D point3D = new Point3D(axPointInB.getX() * image.getWidth(), axPointInB.getY() * image.getHeight(), 0.0);//                        .multDot(new Point3D(distanceAB.aDimReduced.getWidth(), distanceAB.aDimReduced.getHeight(), 0.0));
                int rgb = image.getRGB((int) (Math.max(0, Math.min(point3D.getX(), (double) image.getWidth() - 1)))
                        , (int) (Math.max(0, Math.min((point3D.getY()), (double) image.getHeight() - 1))));
                return rgb;
            } else {
                return -1;
            }
        }
    };
    DistanceAB distanceAB;
    private boolean hasChangedAorB = true;
    boolean notMenuOpen = true;


    public EditPolygonsMappings(Window owner) {
        initComponents();
    }

    public EditPolygonsMappings() {

    }

    public JPanel getContentPanel() {
        return contentPanel;
    }

    private void panelModelViewMouseDragged(MouseEvent e) {
        java.awt.Point point = e.getPoint();
        if (image != null && model != null && selectedPointNo > -1) {
            int x = point.x;
            int y = point.y;
            ZBufferImpl.ImageMapElement ime = ((ZBufferImpl) testHumanHeadTexturing.getZ()).ime;
            Point3D pointIme = null;
            if (ime.checkCoordinates(x, y)) {
                Representable elementRepresentable = ime.getrMap()[x][y];
                if (elementRepresentable instanceof E3Model.FaceWithUv
                        && ((E3Model.FaceWithUv) elementRepresentable).model.equals(model)) {
                    u = ime.getuMap()[x][y];
                    v = ime.getvMap()[x][y];
                    pointIme = new Point3D(u, v, 0.0);//ime.getElementPoint(x, y);


                    final Point3D finalPointIme = pointIme;
                    Logger.getAnonymousLogger().log(Level.INFO, "Point final ime : " + finalPointIme);
                    pointsInModel.forEach((landmarkTypeItem, point3D) -> {
                        if (landmarkTypeItem.equals(landmarkType)) {
                            pointsInModel.put(landmarkTypeItem, finalPointIme);
                        }
                    });
                    hasChangedAorB = true;
                } else {
                    Logger.getAnonymousLogger().log(Level.INFO, "Representable null : " + elementRepresentable);
                }
            } else {
                Logger.getAnonymousLogger().log(Level.INFO, "Point out of bounds : " + pointIme);
            }
        }
    }

    private void panelPictureMouseClicked(MouseEvent e) {
        java.awt.Point point = e.getPoint();
        if (image != null && model != null) {
            Point3D[] pNear = new Point3D[]{new Point3D(point.getX() / panelPicture.getWidth(),
                    point.getY() / panelPicture.getHeight(), 0.)};
            AtomicDouble distanceMin = new AtomicDouble(Double.MAX_VALUE);
            int[] i = new int[]{0};
            pointsInImage.forEach((s, point3D) -> {
                if (Point3D.distance(pNear[0], point3D) < distanceMin.get()) {
                    distanceMin.set(Point3D.distance(pNear[0], point3D));
                    pFound = point3D;
                    landmarkType = s;
                    selectedPointNo = i[0];
                }
                i[0]++;

            });
        }
    }

    private void panelModelViewMouseClicked(MouseEvent e) {
        java.awt.Point point = e.getPoint();
        if (image != null && model != null && mode == SELECT_POINT_VERTEX) {
            int x = point.x;
            int y = point.y;
            ZBufferImpl.ImageMapElement ime = ((ZBufferImpl) testHumanHeadTexturing.getZ()).ime;
            Point3D pointIme = null;
            if (ime.checkCoordinates(x, y)) {
                u = ime.getuMap()[x][y];
                v = ime.getvMap()[x][y];
                pointIme = ime.getElementPoint(x, y);
            }
            Point3D finalPointIme = pointIme;
            int[] i = new int[]{0};
            selectedPointNoOut = -1;
            AtomicReference<Double> dist = new AtomicReference<>(Double.MAX_VALUE);
            pointsInModel.forEach((s, point3D) -> {
                if (Point3D.distance(finalPointIme, point3D) < dist.get()) {
                    dist.set(Point3D.distance(finalPointIme, point3D));
                    pointsInModel.put(s, finalPointIme);
                    selectedPointNoOut = i[0];
                    selectedPointVertexOut = point3D;
                    i[0]++;
                }
            });

        } else if (image != null && model != null && mode == SELECT_POINT_POSITION) {
            int x = point.x;
            int y = point.y;
            ZBufferImpl.ImageMapElement ime = ((ZBufferImpl) testHumanHeadTexturing.getZ()).ime;
            Point3D pointIme = null;
            if (ime.checkCoordinates(x, y)) {
                u = ime.getuMap()[x][y];
                v = ime.getvMap()[x][y];
                pointIme = ime.getElementPoint(x, y);
            }
            selectedPointOutUv = new Point3D(u, v);
        }

    }

    private void panelModelViewComponentResized(ComponentEvent e) {
        int w = e.getComponent().getWidth();
        int h = e.getComponent().getHeight();
        if (testHumanHeadTexturing != null) {
            testHumanHeadTexturing.loop(false);
            testHumanHeadTexturing.setMaxFrames(0);
            testHumanHeadTexturing.stop();
        }
        testHumanHeadTexturing = TestHumanHeadTexturing.startAll(this, image, model);
        if (model != null && image != null) {
            model.texture(iTextureMorphImage);
            testHumanHeadTexturing.setJpg(image);
            testHumanHeadTexturing.setObj(model);
        }
        hasChangedAorB = true;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        var panel1 = new JPanel();
        splitPane1 = new JSplitPane();
        splitPane2 = new JSplitPane();
        panelPicture = new JPanel();
        panelModelView = new JPanel();
        scrollPane1 = new JScrollPane();
        textAreaTextOutput = new JTextArea();

        //======== this ========
        setPreferredSize(new Dimension(1280, 780));
        setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setMinimumSize(new Dimension(800, 600));
                contentPanel.setPreferredSize(new Dimension(0, 0));
                contentPanel.setLayout(new MigLayout(
                    "fill,hidemode 3",
                    // columns
                    "[fill]",
                    // rows
                    "[fill]"));

                //======== panel1 ========
                {
                    panel1.setMinimumSize(new Dimension(800, 600));
                    panel1.setLayout(new MigLayout(
                        "fill,hidemode 3",
                        // columns
                        "[fill]",
                        // rows
                        "[fill]"));

                    //======== splitPane1 ========
                    {
                        splitPane1.setOrientation(JSplitPane.VERTICAL_SPLIT);
                        splitPane1.setResizeWeight(0.5);

                        //======== splitPane2 ========
                        {
                            splitPane2.setResizeWeight(0.5);

                            //======== panelPicture ========
                            {
                                panelPicture.setMinimumSize(new Dimension(400, 300));
                                panelPicture.setPreferredSize(new Dimension(0, 0));
                                panelPicture.addMouseListener(new MouseAdapter() {
                                    @Override
                                    public void mouseClicked(MouseEvent e) {
                                        panelPictureMouseClicked(e);
                                    }
                                });
                                panelPicture.setLayout(new MigLayout(
                                    "fill,hidemode 3",
                                    // columns
                                    "[fill]" +
                                    "[fill]",
                                    // rows
                                    "[]" +
                                    "[]" +
                                    "[]"));
                            }
                            splitPane2.setLeftComponent(panelPicture);

                            //======== panelModelView ========
                            {
                                panelModelView.setMinimumSize(new Dimension(400, 300));
                                panelModelView.setPreferredSize(new Dimension(0, 0));
                                panelModelView.addMouseMotionListener(new MouseMotionAdapter() {
                                    @Override
                                    public void mouseDragged(MouseEvent e) {
                                        panelModelViewMouseDragged(e);
                                    }
                                });
                                panelModelView.addComponentListener(new ComponentAdapter() {
                                    @Override
                                    public void componentResized(ComponentEvent e) {
                                        panelModelViewComponentResized(e);
                                    }
                                });
                                panelModelView.addMouseListener(new MouseAdapter() {
                                    @Override
                                    public void mouseClicked(MouseEvent e) {
                                        panelModelViewMouseClicked(e);
                                    }
                                });
                                panelModelView.setLayout(new MigLayout(
                                    "fill,hidemode 3",
                                    // columns
                                    "[fill]" +
                                    "[fill]",
                                    // rows
                                    "[]" +
                                    "[]" +
                                    "[]"));
                            }
                            splitPane2.setRightComponent(panelModelView);
                        }
                        splitPane1.setTopComponent(splitPane2);

                        //======== scrollPane1 ========
                        {
                            scrollPane1.setMinimumSize(new Dimension(180, 320));

                            //---- textAreaTextOutput ----
                            textAreaTextOutput.setMinimumSize(new Dimension(800, 300));
                            textAreaTextOutput.setPreferredSize(new Dimension(0, 0));
                            textAreaTextOutput.setRows(3);
                            textAreaTextOutput.setColumns(150);
                            scrollPane1.setViewportView(textAreaTextOutput);
                        }
                        splitPane1.setBottomComponent(scrollPane1);
                    }
                    panel1.add(splitPane1, "cell 0 0");
                }
                contentPanel.add(panel1, "cell 0 0");
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);
        }
        add(dialogPane, BorderLayout.CENTER);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JSplitPane splitPane1;
    private JSplitPane splitPane2;
    private JPanel panelPicture;
    JPanel panelModelView;
    private JScrollPane scrollPane1;
    private JTextArea textAreaTextOutput;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on

    public void loadImage(File selectedFile) {
        this.image = ImageIO.read(selectedFile);
        if (image != null && testHumanHeadTexturing != null)
            testHumanHeadTexturing.setJpg(image);
        Logger.getAnonymousLogger().log(Level.INFO, "Loaded image", pointsInImage.size());

    }

    public void run() {
        testHumanHeadTexturing = TestHumanHeadTexturing.startAll(this, image, model);
        hasChangedAorB = true;
        while (isRunning) {
            try {
                zBufferImage = testHumanHeadTexturing.getJpgFile();
                // Display 3D scene
                if (zBufferImage != null && zBufferImage.getWidth() == panelModelView.getWidth() && zBufferImage.getHeight() == panelModelView.getHeight()) {
                    Graphics graphics = panelModelView.getGraphics();
                    if (graphics != null) {
                        graphics.drawImage(zBufferImage, 0, 0, panelModelView.getWidth(), panelModelView.getHeight(), null);
                        displayPointsOut(pointsInModel);
                    }
                }
                if (image != null) {
                    Graphics graphics = panelPicture.getGraphics();
                    if (graphics != null) {
                        graphics.drawImage(image, 0, 0, panelPicture.getWidth(), panelPicture.getHeight(), null);
                        displayPointsIn(pointsInImage);
                    }
                }
                Thread.sleep(20);


                if ((pointsInImage != null && panelModelView != null && !pointsInImage.isEmpty()
                        && !pointsInModel.isEmpty() && model != null && image != null
                        && threadDistanceIsNotRunning) || hasChangedAorB) {
                    Thread thread = new Thread(() -> {
                        long l = System.nanoTime();
                        threadDistanceIsNotRunning = false;
                        if (hasChangedAorB()) {
                            Logger.getAnonymousLogger().log(Level.INFO, "All loaded resources finished. Starts distance calculation");

                            distanceAB = new DistanceBezier2(pointsInImage.values().stream().toList(),
                                    pointsInModel.values().stream().toList(), new Dimension(panelPicture.getWidth(), panelPicture.getHeight()),
                                    new Dimension(panelModelView.getWidth(),
                                            panelModelView.getHeight()));
                            if (!distanceAB.isInvalidArray()) {
                                distanceAB.setModel(model);
                                // Display 3D scene
                                if (model != null) {
                                    model.texture(iTextureMorphImage);
                                    hasChangedAorB = false;
                                } else {
                                    hasChangedAorB = true;
                                }
                            } else if (distanceAB.isInvalidArray()) {
                                hasChangedAorB = true;
                                Logger.getAnonymousLogger().log(Level.INFO, "Invalid array in DistanceAB");
                            }


                            l = System.nanoTime() - l;
                            Logger.getAnonymousLogger().log(Level.INFO, "Distance calculation finished" + (l / 1000000.0));
                            threadDistanceIsNotRunning = true;
                        }
                    });
                    thread.start();
                    Logger.getAnonymousLogger().log(Level.INFO, "Thread texture creation started");
                }
                if (!threadDistanceIsNotRunning)
                    Logger.getAnonymousLogger().log(Level.INFO, "Thread 'Texture creation' still in progress...");

            } catch (RuntimeException | InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    private boolean isNotMenuOpen() {
        return notMenuOpen;
    }


    private boolean hasChangedAorB() {
        return hasChangedAorB;
    }

    private void displayPointsIn(HashMap<String, Point3D> points) {
        JPanel panelDraw = panelPicture;
        try {
            Thread.sleep(200);
            if (image != null && panelDraw != null) {
                Graphics graphics = panelDraw.getGraphics();
                if (graphics != null) {
                    int[] i = new int[]{0};
                    points.forEach((s, point3D) -> {
                        Graphics graphics1 = panelDraw.getGraphics();
                        if (selectedPointNo == i[0])
                            graphics1.setColor(Color.ORANGE);
                        else
                            graphics1.setColor(Color.GREEN);
                        graphics1.fillOval((int) (double) (point3D.getX() * panelDraw.getWidth()) - 3,
                                (int) (double) (point3D.getY() * panelDraw.getHeight()) - 3, 7, 7);
                        i[0]++;
                    });
                    // Display 3D scene
                }
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void displayPointsOut(HashMap<String, Point3D> points) {
        JPanel panelDraw = panelModelView;
        if (image != null && panelDraw != null && testHumanHeadTexturing != null && testHumanHeadTexturing.getZ().la() == panelModelView.getWidth()
                && testHumanHeadTexturing.getZ().ha() == panelModelView.getHeight()) {
            // Display image
            int[] i = new int[]{0};
            if (points != null) {
                try {
                    points.forEach((s, uvCoordinates) -> {
                        if (testHumanHeadTexturing.camera() != null && uvCoordinates != null) {
                            // +++ Model 3DObj : calculerPoint3D(u,v) +++
                            Point3D uvFace = model.findUvFace(
                                    uvCoordinates.getX(),
                                    uvCoordinates.getY());
                            if (uvFace != null) {
                                Point point = testHumanHeadTexturing.scene().cameraActive().coordinatesPoint2D(uvFace, testHumanHeadTexturing.getZ());
                                point.setLocation(point.getX() / testHumanHeadTexturing.getZ().la() * panelDraw.getWidth(),
                                        point.getY() / testHumanHeadTexturing.getZ().ha() * panelDraw.getHeight());
                                Graphics graphics = panelDraw.getGraphics();
                                if (selectedPointNo == i[0])
                                    graphics.setColor(Color.ORANGE);
                                else
                                    graphics.setColor(Color.GREEN);

                                // point.setLocation(point.getX(), point.getY());

                                if (testHumanHeadTexturing.getZ().checkScreen(point)) {
                                    graphics.fillOval((int) (point.getX() - 3),
                                            (int) ((point.getY()) - 3),
                                            7, 7);
                                } else {
                                    graphics = panelDraw.getGraphics();
                                    graphics.setColor(Color.YELLOW);
                                    graphics.fillRect(0, 0, 10, 10);

                                }
                            } else {
                                Graphics graphics = panelDraw.getGraphics();
                                graphics.setColor(Color.GREEN);
                                graphics.fillRect(0, 0, 10, 10);

                            }

                        }

                        i[0]++;
                    });
                } catch (ConcurrentModificationException ex) {
                    Logger.getAnonymousLogger().log(Level.SEVERE, "Concurrent exception while drawing");
                }
            }

            if (mode == SELECT_POINT_POSITION && selectedPointOutUv != null) {
                Point3D uvFace = model.findUvFace(selectedPointOutUv.getX(), selectedPointOutUv.getY());
                Point point = testHumanHeadTexturing.scene().cameraActive().coordinatesPoint2D(uvFace, testHumanHeadTexturing.getZ());
                point.setLocation(point.getX() / testHumanHeadTexturing.getZ().la() * panelDraw.getWidth(),
                        point.getY() / testHumanHeadTexturing.getZ().ha() * panelDraw.getHeight());
                Graphics graphics = panelDraw.getGraphics();
                graphics.setColor(Color.YELLOW);
                graphics.fillOval((int) (point.getX() - 3),
                        (int) ((point.getY()) - 3),
                        7, 7);

            } else if (mode == SELECT_POINT_POSITION && selectedPointVertexOut != null && selectedPointNoOut >= 0) {
                Point point = testHumanHeadTexturing.scene().cameraActive().coordinatesPoint2D(selectedPointVertexOut, testHumanHeadTexturing.getZ());
                point.setLocation(point.getX() / testHumanHeadTexturing.getZ().la() * panelDraw.getWidth(),
                        point.getY() / testHumanHeadTexturing.getZ().ha() * panelDraw.getHeight());
                Graphics graphics = panelDraw.getGraphics();
                graphics.setColor(Color.YELLOW);
                graphics.fillOval((int) (point.getX() - 3),
                        (int) ((point.getY()) - 3),
                        7, 7);

            }
        }
    }

    public void add3DModel(File selectedFile) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(selectedFile));
            model = new E3Model(bufferedReader, true, selectedFile.getAbsolutePath());
            model.texture(iTextureMorphImage);
            testHumanHeadTexturing.setObj(model);
            Logger.getAnonymousLogger().log(Level.INFO, "Loaded model");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadTxt(File selectedFile) {
        if (image != null && model != null) {
            pointsInImage = new HashMap<String, Point3D>();
            try {
                Scanner bufferedReader = new Scanner(new FileReader(selectedFile));
                String line = "";
                while (bufferedReader.hasNextLine()) {
                    line = bufferedReader.nextLine();
                    Point3D point = new Point3D();
                    String landmarkType;
                    double x;
                    double y;
                    if (!line.isEmpty()) {
                        if (Character.isLetter(line.charAt(0))) {
                            landmarkType = line;
                            // X
                            line = bufferedReader.nextLine();
                            x = Double.parseDouble(line);
                            // Y
                            line = bufferedReader.nextLine();
                            y = Double.parseDouble(line);
                            // Blank line
                            line = bufferedReader.nextLine();

                            pointsInImage.put(landmarkType, new Point3D(x / image.getWidth(), y / image.getHeight(), 0.0));
                        }
                    }
                }
                Logger.getAnonymousLogger().log(Level.INFO, "Loaded {0} points in image", pointsInImage.size());
                bufferedReader.close();

                // Initialize surface bezier

                pointsInModel = new HashMap<>();
                if (!testHumanHeadTexturing.scene().getObjets().getData1d().isEmpty() && testHumanHeadTexturing.scene().getObjets().getElem(0) instanceof E3Model e3Model) {
                    pointsInImage.forEach((s, point3D) -> {
                        Point3D copy = new Point3D(point3D);
                        pointsInModel.put(s, copy);
                    });
                }


                hasChangedAorB = true;

                Logger.getAnonymousLogger().log(Level.INFO, "Loaded {0} points in model view", pointsInImage.size());
            } catch (IOException | RuntimeException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            Logger.getAnonymousLogger().log(Level.INFO, "Loaded image first before points", pointsInImage.size());
        }


    }

    public void editPointPosition() {
        mode = EDIT_POINT_POSITION;
    }

    public void selectPointPosition() {
        mode = SELECT_POINT_POSITION;
    }

    public void loadTxtOut(File selectedFile) {
        if (image != null && model != null) {
            pointsInModel = new HashMap<>();
            try {
                Scanner bufferedReader = new Scanner(new FileReader(selectedFile));
                String line = "";
                while (bufferedReader.hasNextLine()) {
                    line = bufferedReader.nextLine();
                    String landmarkType;
                    double x;
                    double y;
                    if (!line.isEmpty()) {
                        if (Character.isLetter(line.charAt(0))) {
                            landmarkType = line;
                            // X
                            line = bufferedReader.nextLine();
                            x = Double.parseDouble(line);
                            // Y
                            line = bufferedReader.nextLine();
                            y = Double.parseDouble(line);
                            // Blank line
                            line = bufferedReader.nextLine();

                            pointsInModel.put(landmarkType, new Point3D(x, y, 0.0));
                        }
                    }
                }
                Logger.getAnonymousLogger().log(Level.INFO, "Loaded {0} points in image", pointsInModel.size());
                bufferedReader.close();

                hasChangedAorB = true;

            } catch (IOException | RuntimeException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            Logger.getAnonymousLogger().log(Level.INFO, "Load image and model first before points", pointsInModel.size());
        }

    }

    public void saveTxtOut(File selectedFile) {
        PrintWriter dataWriter = null;
        try {
            dataWriter = new PrintWriter(selectedFile);
            PrintWriter finalDataWriter = dataWriter;
            pointsInModel.forEach((s, point3D) -> {
                finalDataWriter.println(s);
                finalDataWriter.println(point3D.getX());
                finalDataWriter.println(point3D.getY());
                finalDataWriter.println();
            });
            dataWriter.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
