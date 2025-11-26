/*
 *
 *  *
 *  *  * Copyright (c) 2025. Manuel Daniel Dahmen
 *  *  *
 *  *  *
 *  *  *    Copyright 2024 Manuel Daniel Dahmen
 *  *  *
 *  *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *    you may not use this file except in compliance with the License.
 *  *  *    You may obtain a copy of the License at
 *  *  *
 *  *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  *    Unless required by applicable law or agreed to in writing, software
 *  *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *    See the License for the specific language governing permissions and
 *  *  *    limitations under the License.
 *  *
 *  *
 *
 *
 *
 *  * Created by $user $date
 *
 *
 */

/*
 * Created by JFormDesigner on Sat Dec 16 16:30:23 CET 2023
 */

package one.empty3.apps.vecmesh;

import one.empty3.apps.feature.app.replace.javax.imageio.ImageIO;
import net.miginfocom.swing.MigLayout;
import one.empty3.library.*;
import one.empty3.library.core.export.ObjExport;
import one.empty3.library.core.export.STLExport;
import one.empty3.library.core.tribase.Plan3D;
import one.empty3.library.core.tribase.Tubulaire3;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import one.empty3.library.Point;
import one.empty3.libs.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Manuel Dahmen
 * 18-12-2023
 */
public class VecMeshEditorGui extends JFrame {
    private static final int TYPE_CONTAINER_CLASS_VEC_HEIGHT_MAP = 0;
    private static final int TYPE_CONTAINER_CLASS_VORONOI_HEIGHTS = 1;
    private static int instanceCount = 0;
    private BufferedImage texture;
    private File currentFile;
    private Class<? extends Representable> defaultClassRepresentable = Tubulaire3.class;
    private Class<? extends Representable> representableClass = defaultClassRepresentable;
    private ZBufferImpl zBuffer;
    private VecMeshEditor model;
    private int resX;
    private int resY;
    private Config config;
    private Collection<File> imagesTextures = new HashSet<>();
    private File fileTexture = null;
    private int containerClassType = 0;

    public VecMeshEditorGui() {
        initComponents();


        config = new Config();

        currentFile = new File(config.getDefaultCodeVecMesh());

        setDefaultFile();

        Output.setGetText(buttonOutput);

        instanceCount++;
        Logger.getAnonymousLogger().log(Level.INFO, "Instance==1 : " + (instanceCount == 1));
    }

    private void menuItemSave(ActionEvent e) {
        String text = getTextAreaCode().getText();
        int columns = getTextFieldRows();
        if (currentFile != null && currentFile.exists()) {
            try {
                PrintWriter fileOutputStream = new PrintWriter(currentFile);
                fileOutputStream.println(columns);
                fileOutputStream.println(text);
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }

    }

    public void setDefaultFile() {
        getDefaultOrNewFileTextCode();
    }

    private void getDefaultOrNewFileTextCode() {
        if (!currentFile.exists()) {
            try {
                currentFile.createNewFile();
                PrintWriter printWriter = new PrintWriter(currentFile);
                printWriter.println("2");
                printWriter.println(representableClass.getCanonicalName());
                printWriter.println("heights = ((1,1),(1,1))\n" +
                        "heights.cols = 2");
                printWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            try (BufferedReader reader = Files.newBufferedReader(currentFile.toPath(), Charset.defaultCharset())) {
                String line = null;
                String columnsString = reader.readLine();
                if (columnsString != null)
                    textAreaColumsCount.setText(String.valueOf(Double.parseDouble(columnsString)));
                String classNameReader = reader.readLine();
                try {
                    Class<?> aClass = getClass().getClassLoader().loadClass(classNameReader);
                    representableClass = (Class<? extends Representable>) aClass;
                } catch (ClassNotFoundException | NullPointerException e) {
                    representableClass = defaultClassRepresentable;
                }
                StringBuffer sb = new StringBuffer();
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                getTextAreaCode().setText(sb.toString());
            }
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
        }
    }

    public String getDefaultCode() {
        return getTextAreaCode().getText();
    }

    private void menuItemSaveAs(ActionEvent e) {
        JFileChooser ui = new JFileChooser();
        ui.setDialogType(JFileChooser.SAVE_DIALOG);
        ui.setDialogTitle("Save as text");
        if (ui.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = ui.getSelectedFile();
            String text = getTextAreaCode().getText();

            int columns = getTextFieldRows();

            try {
                PrintWriter fileOutputStream = new PrintWriter(selectedFile);
                fileOutputStream.println(columns);
                fileOutputStream.println(representableClass.getCanonicalName());
                fileOutputStream.println(text);

                fileOutputStream.close();

                currentFile = selectedFile;

            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);

            }

        }
    }

    private void menuItemOpen(ActionEvent e) {
        JFileChooser ui = new JFileChooser();
        ui.setDialogType(JFileChooser.OPEN_DIALOG);
        ui.setDialogTitle("Load text");
        if (ui.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = ui.getSelectedFile();
            String text = getTextAreaCode().getText();

            int columns = getTextFieldRows();

            try (BufferedReader reader = Files.newBufferedReader(currentFile.toPath(), Charset.defaultCharset())) {
                String line = "";
                String columnsString = reader.readLine();
                if (columnsString != null)
                    textAreaColumsCount.setText(String.valueOf(Double.parseDouble(columnsString)));
                String classNameReader = reader.readLine();
                try {
                    Class<?> aClass = getClass().getClassLoader().loadClass(classNameReader);
                    Object o = aClass.getConstructor().newInstance();
                    if (o instanceof Representable r) {
                        representableClass = r.getClass();
                    }
                } catch (ClassNotFoundException | InvocationTargetException | InstantiationException |
                         IllegalAccessException | NoSuchMethodException | NullPointerException ex1) {
                    representableClass = defaultClassRepresentable;
                }
                if (representableClass == null && defaultClassRepresentable != null)
                    representableClass = defaultClassRepresentable;
                else if (representableClass == null && defaultClassRepresentable == null) {
                    defaultClassRepresentable = Tubulaire3.class;
                    representableClass = defaultClassRepresentable;
                }

                StringBuffer sb = new StringBuffer();
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                getTextAreaCode().setText(sb.toString());
                reader.close();
            } catch (RuntimeException | IOException ex) {
                ex.printStackTrace();
            }

            currentFile = selectedFile;

        }
    }

    private void menuItemSphere(ActionEvent e) {
        this.representableClass = Sphere.class;
    }

    private void menuItemTube(ActionEvent e) {
        this.representableClass = Tubulaire3.class;
    }

    private void menuItemRectangle(ActionEvent e) {
        this.representableClass = Plan3D.class;
    }

    private void menuItemCube(ActionEvent e) {
        this.representableClass = Cube.class;
    }

    private void renderPoints(ActionEvent e) {
        zBuffer.setDisplayType(ZBufferImpl.SURFACE_DISPLAY_POINTS);
    }

    private void renderLines(ActionEvent e) {
        zBuffer.setDisplayType(ZBufferImpl.SURFACE_DISPLAY_LINES);
    }

    private void renderQuadsCol(ActionEvent e) {
        zBuffer.setDisplayType(ZBufferImpl.SURFACE_DISPLAY_COL_QUADS);

    }

    private void renderQuadsTextured(ActionEvent e) {
        zBuffer.setDisplayType(ZBufferImpl.SURFACE_DISPLAY_TEXT_QUADS);

    }

    private void renderAll(ActionEvent e) {
        zBuffer.setDisplayType(ZBufferImpl.DISPLAY_ALL);

    }

    private void menuItemExportAs(ActionEvent e) {
        JFileChooser jFileChooser = new JFileChooser(currentFile.getParentFile());
        jFileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        jFileChooser.setDialogTitle("Choisir où exporter les fichiers");
        if (jFileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            currentFile = jFileChooser.getSelectedFile();
            File currentFile1 = currentFile;
            File dir = currentFile1;
            try {
                if (!currentFile1.isDirectory() && currentFile1.exists()) {
                    return;
                }
                if (!currentFile1.isDirectory()) {
                    currentFile1.mkdir();
                }
                if (currentFile1.exists() && !currentFile1.isDirectory()) {
                    dir = new File(currentFile1.getParentFile().getAbsolutePath());
                }
                File xml = new File(dir.getAbsolutePath() + File.separator + "scene.xml");
                File moo = new File(dir.getAbsolutePath() + File.separator + "scene.moo");
                File stl = new File(dir.getAbsolutePath() + File.separator + "scene.stl");
                File obj = new File(dir.getAbsolutePath() + File.separator + "scene.obj");
                STLExport.save(stl, model.getScene(), false);
                ObjExport.save(obj, model.getScene(), false);
                PrintWriter printWriter = new PrintWriter(moo);
                printWriter.println(model.getScene().toString());
                printWriter.close();
                printWriter = new PrintWriter(xml);
                StringBuilder stringBuilder = new StringBuilder();
                model.getScene().getObjets().getElem(0).xmlRepresentation(dir.getAbsolutePath(),
                        stringBuilder, model.getScene());
                printWriter.println(stringBuilder.toString());
                printWriter.close();
            } catch (IOException ex) {
                System.err.println(ex);
            }
        }
    }

    private void resolution(ActionEvent e) {
        DimensionZBuffer dimension = new DimensionZBuffer(this);
        dimension.setVisible(true);
        int x = dimension.getX();
        int y = dimension.getY();
    }

    private void menuItemSettings(ActionEvent e) {
        config = new Config();
    }

    private void menuAddImages(ActionEvent e) {
        File direFile = currentFile == null ? new Config().getDefaultFileOutput() : currentFile;
        JFileChooser jFileChooser = new JFileChooser(direFile);
        jFileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jFileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return (f.getName().toLowerCase().endsWith(".jpg") || f.getName().toLowerCase().endsWith(".png")
                        || f.getName().toLowerCase().endsWith(".jpeg") || f.getName().toLowerCase().endsWith(".bmp"));
            }

            @Override
            public String getDescription() {
                return null;

            }
        });
        if (jFileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f1 = jFileChooser.getSelectedFile();
            File[] selectedFiles = null;
            if (f1 != null) {
                selectedFiles = new File[]{f1};
            } else {
                selectedFiles = jFileChooser.getSelectedFiles();
            }
            for (File f : selectedFiles) {
                if (!getImages().contains(f) && f != null) {
                    if (getImages().add(f)) {
                        JMenuItem jMenuItem = new JMenuItem(f.getAbsolutePath());
                        jMenuItem.addActionListener(new AbstractAction() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                File file = new File(((JMenuItem) e.getSource()).getText());
                                if (file.exists() && file.isFile()) {
                                    texture = ImageIO.read(file);
                                    fileTexture = file;
                                    Logger.getAnonymousLogger().log(Level.INFO, "texture: " + file.getAbsolutePath());
                                } else
                                    System.err.println("File" + file + " doesn't exist");
                            }
                        });
                        menuImages.add(jMenuItem);

                    }

                }
            }
        }
    }

    private Collection<File> getImages() {
        return imagesTextures;
    }

    private void menuAddImage(ActionEvent e) {
        // TODO add your code here
    }

    private void menuItemHeightMap(ActionEvent e) {
        this.containerClassType = TYPE_CONTAINER_CLASS_VEC_HEIGHT_MAP;
    }

    private void menuItemVoronoi(ActionEvent e) {
        this.containerClassType = TYPE_CONTAINER_CLASS_VORONOI_HEIGHTS;
    }

    private void menuItemEditMeshHeightsForm(ActionEvent e) {

    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        ResourceBundle bundle = ResourceBundle.getBundle("one.empty3.library.core.testing.Bundle");
        menuBar1 = new JMenuBar();
        menu1 = new JMenu();
        menuItem12 = new JMenuItem();
        menuItem1 = new JMenuItem();
        menuItem2 = new JMenuItem();
        menuItem3 = new JMenuItem();
        menuItem11 = new JMenuItem();
        menuItem5 = new JMenuItem();
        menuItem6 = new JMenuItem();
        menu3 = new JMenu();
        menuItem4 = new JMenuItem();
        menuItem7 = new JMenuItem();
        menuItem8 = new JMenuItem();
        menuItem9 = new JMenuItem();
        menuItem10 = new JMenuItem();
        menu5 = new JMenu();
        menuItem13 = new JMenuItem();
        menuItem14 = new JMenuItem();
        menu2 = new JMenu();
        menuItemRender = new JMenuItem();
        menuItemResolution = new JMenuItem();
        menuItemRenderPoints = new JMenuItem();
        menuItemRenderLines = new JMenuItem();
        menuItemRenderQuadsCol = new JMenuItem();
        menuItemRenderQuadsTextured = new JMenuItem();
        menuItemRenderAll = new JMenuItem();
        menu4 = new JMenu();
        menuImages = new JMenu();
        menu7 = new JMenuItem();
        menu6 = new JMenu();
        menuItem15 = new JMenuItem();
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        splitPane1 = new JSplitPane();
        scrollPane1 = new JScrollPane();
        textAreaCode = new JTextArea();
        panelGraphics = new JPanel();
        buttonBar = new JPanel();
        scrollPane2 = new JScrollPane();
        textAreaColumsCount = new JTextArea();
        comboBox1 = new JComboBox();
        okButton = new JButton();
        cancelButton = new JButton();
        buttonOutput = new JLabel();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== menuBar1 ========
        {

            //======== menu1 ========
            {
                menu1.setText(bundle.getString("VecMeshEditorGui.menu1.text"));

                //---- menuItem12 ----
                menuItem12.setText(bundle.getString("VecMeshEditorGui.menuItem12.text"));
                menu1.add(menuItem12);

                //---- menuItem1 ----
                menuItem1.setText(bundle.getString("VecMeshEditorGui.menuItem1.text"));
                menuItem1.addActionListener(e -> menuItemOpen(e));
                menu1.add(menuItem1);

                //---- menuItem2 ----
                menuItem2.setText(bundle.getString("VecMeshEditorGui.menuItem2.text"));
                menuItem2.addActionListener(e -> menuItemSave(e));
                menu1.add(menuItem2);

                //---- menuItem3 ----
                menuItem3.setText(bundle.getString("VecMeshEditorGui.menuItem3.text"));
                menuItem3.addActionListener(e -> menuItemSaveAs(e));
                menu1.add(menuItem3);

                //---- menuItem11 ----
                menuItem11.setText(bundle.getString("VecMeshEditorGui.menuItem11.text"));
                menuItem11.addActionListener(e -> menuItemExportAs(e));
                menu1.add(menuItem11);

                //---- menuItem5 ----
                menuItem5.setText(bundle.getString("VecMeshEditorGui.menuItem5.text"));
                menuItem5.addActionListener(e -> menuItemSettings(e));
                menu1.add(menuItem5);

                //---- menuItem6 ----
                menuItem6.setText(bundle.getString("VecMeshEditorGui.menuItem6.text"));
                menu1.add(menuItem6);
            }
            menuBar1.add(menu1);

            //======== menu3 ========
            {
                menu3.setText(bundle.getString("VecMeshEditorGui.menu3.text"));

                //---- menuItem4 ----
                menuItem4.setText(bundle.getString("VecMeshEditorGui.menuItem4.text"));
                menuItem4.addActionListener(e -> {
			menuItemSphere(e);
			menuItemSphere(e);
		});
                menu3.add(menuItem4);

                //---- menuItem7 ----
                menuItem7.setText(bundle.getString("VecMeshEditorGui.menuItem7.text"));
                menuItem7.addActionListener(e -> menuItemTube(e));
                menu3.add(menuItem7);

                //---- menuItem8 ----
                menuItem8.setText(bundle.getString("VecMeshEditorGui.menuItem8.text"));
                menuItem8.addActionListener(e -> menuItemRectangle(e));
                menu3.add(menuItem8);

                //---- menuItem9 ----
                menuItem9.setText(bundle.getString("VecMeshEditorGui.menuItem9.text"));
                menuItem9.addActionListener(e -> menuItemCube(e));
                menu3.add(menuItem9);

                //---- menuItem10 ----
                menuItem10.setText(bundle.getString("VecMeshEditorGui.menuItem10.text"));
                menuItem10.addActionListener(e -> menuItemEditMeshHeightsForm(e));
                menu3.add(menuItem10);
            }
            menuBar1.add(menu3);

            //======== menu5 ========
            {
                menu5.setText(bundle.getString("VecMeshEditorGui.menu5.text"));

                //---- menuItem13 ----
                menuItem13.setText(bundle.getString("VecMeshEditorGui.menuItem13.text"));
                menuItem13.addActionListener(e -> menuItemHeightMap(e));
                menu5.add(menuItem13);

                //---- menuItem14 ----
                menuItem14.setText(bundle.getString("VecMeshEditorGui.menuItem14.text"));
                menu5.add(menuItem14);
            }
            menuBar1.add(menu5);

            //======== menu2 ========
            {
                menu2.setText(bundle.getString("VecMeshEditorGui.menu2.text"));

                //---- menuItemRender ----
                menuItemRender.setText(bundle.getString("VecMeshEditorGui.menuItemRender.text"));
                menu2.add(menuItemRender);

                //---- menuItemResolution ----
                menuItemResolution.setText(bundle.getString("VecMeshEditorGui.menuItemResolution.text"));
                menuItemResolution.addActionListener(e -> resolution(e));
                menu2.add(menuItemResolution);

                //---- menuItemRenderPoints ----
                menuItemRenderPoints.setText(bundle.getString("VecMeshEditorGui.menuItemRenderPoints.text"));
                menuItemRenderPoints.addActionListener(e -> renderPoints(e));
                menu2.add(menuItemRenderPoints);

                //---- menuItemRenderLines ----
                menuItemRenderLines.setText(bundle.getString("VecMeshEditorGui.menuItemRenderLines.text"));
                menuItemRenderLines.addActionListener(e -> renderLines(e));
                menu2.add(menuItemRenderLines);

                //---- menuItemRenderQuadsCol ----
                menuItemRenderQuadsCol.setText(bundle.getString("VecMeshEditorGui.menuItemRenderQuadsCol.text"));
                menuItemRenderQuadsCol.addActionListener(e -> renderQuadsCol(e));
                menu2.add(menuItemRenderQuadsCol);

                //---- menuItemRenderQuadsTextured ----
                menuItemRenderQuadsTextured.setText(bundle.getString("VecMeshEditorGui.menuItemRenderQuadsTextured.text"));
                menuItemRenderQuadsTextured.addActionListener(e -> renderQuadsTextured(e));
                menu2.add(menuItemRenderQuadsTextured);

                //---- menuItemRenderAll ----
                menuItemRenderAll.setText(bundle.getString("VecMeshEditorGui.menuItemRenderAll.text"));
                menuItemRenderAll.addActionListener(e -> renderAll(e));
                menu2.add(menuItemRenderAll);
            }
            menuBar1.add(menu2);

            //======== menu4 ========
            {
                menu4.setText(bundle.getString("VecMeshEditorGui.menu4.text"));

                //======== menuImages ========
                {
                    menuImages.setText(bundle.getString("VecMeshEditorGui.menuImages.text"));

                    //---- menu7 ----
                    menu7.setText(bundle.getString("VecMeshEditorGui.menu7.text"));
                    menu7.addActionListener(e -> {
			menuAddImage(e);
			menuAddImage(e);
			menuAddImages(e);
		});
                    menuImages.add(menu7);
                }
                menu4.add(menuImages);

                //======== menu6 ========
                {
                    menu6.setText(bundle.getString("VecMeshEditorGui.menu6.text"));

                    //---- menuItem15 ----
                    menuItem15.setText(bundle.getString("VecMeshEditorGui.menuItem15.text"));
                    menu6.add(menuItem15);
                }
                menu4.add(menu6);
            }
            menuBar1.add(menu4);
        }
        setJMenuBar(menuBar1);

        //======== dialogPane ========
        {
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new MigLayout(
                    "fill,insets dialog,hidemode 3",
                    // columns
                    "[fill]" +
                    "[fill]" +
                    "[fill]",
                    // rows
                    "[]" +
                    "[]" +
                    "[]" +
                    "[]"));

                //======== splitPane1 ========
                {
                    splitPane1.setMinimumSize(new Dimension(640, 480));
                    splitPane1.setResizeWeight(0.5);

                    //======== scrollPane1 ========
                    {

                        //---- textAreaCode ----
                        textAreaCode.setMinimumSize(new Dimension(320, 480));
                        textAreaCode.setRows(20);
                        textAreaCode.setText(bundle.getString("VecMeshEditorGui.textAreaCode.text"));
                        scrollPane1.setViewportView(textAreaCode);
                    }
                    splitPane1.setLeftComponent(scrollPane1);

                    //======== panelGraphics ========
                    {
                        panelGraphics.setMinimumSize(new Dimension(640, 480));
                        panelGraphics.setLayout(new MigLayout(
                            "fill,hidemode 3",
                            // columns
                            "[fill]" +
                            "[fill]",
                            // rows
                            "[]" +
                            "[]" +
                            "[]"));
                    }
                    splitPane1.setRightComponent(panelGraphics);
                }
                contentPanel.add(splitPane1, "cell 0 0 3 4,dock center,wmin 800,hmin 600");
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setLayout(new MigLayout(
                    "insets dialog,alignx right",
                    // columns
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[button,fill]" +
                    "[button,fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[fill]",
                    // rows
                    "[]" +
                    "[]"));

                //======== scrollPane2 ========
                {

                    //---- textAreaColumsCount ----
                    textAreaColumsCount.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
                    textAreaColumsCount.setText(bundle.getString("VecMeshEditorGui.textAreaColumsCount.text"));
                    scrollPane2.setViewportView(textAreaColumsCount);
                }
                buttonBar.add(scrollPane2, "cell 0 0 11 1");
                buttonBar.add(comboBox1, "cell 11 0");

                //---- okButton ----
                okButton.setText(bundle.getString("VecMeshEditorGui.okButton.text"));
                buttonBar.add(okButton, "cell 34 0");

                //---- cancelButton ----
                cancelButton.setText(bundle.getString("VecMeshEditorGui.cancelButton.text"));
                buttonBar.add(cancelButton, "cell 35 0");

                //---- buttonOutput ----
                buttonOutput.setText(bundle.getString("VecMeshEditorGui.buttonOutput.text"));
                buttonBar.add(buttonOutput, "cell 0 1 44 1");
            }
            dialogPane.add(buttonBar, BorderLayout.PAGE_END);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JMenuBar menuBar1;
    private JMenu menu1;
    private JMenuItem menuItem12;
    private JMenuItem menuItem1;
    private JMenuItem menuItem2;
    private JMenuItem menuItem3;
    private JMenuItem menuItem11;
    private JMenuItem menuItem5;
    private JMenuItem menuItem6;
    private JMenu menu3;
    private JMenuItem menuItem4;
    private JMenuItem menuItem7;
    private JMenuItem menuItem8;
    private JMenuItem menuItem9;
    private JMenuItem menuItem10;
    private JMenu menu5;
    private JMenuItem menuItem13;
    private JMenuItem menuItem14;
    private JMenu menu2;
    private JMenuItem menuItemRender;
    private JMenuItem menuItemResolution;
    private JMenuItem menuItemRenderPoints;
    private JMenuItem menuItemRenderLines;
    private JMenuItem menuItemRenderQuadsCol;
    private JMenuItem menuItemRenderQuadsTextured;
    private JMenuItem menuItemRenderAll;
    private JMenu menu4;
    private JMenu menuImages;
    private JMenuItem menu7;
    private JMenu menu6;
    private JMenuItem menuItem15;
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JSplitPane splitPane1;
    private JScrollPane scrollPane1;
    private JTextArea textAreaCode;
    private JPanel panelGraphics;
    private JPanel buttonBar;
    private JScrollPane scrollPane2;
    private JTextArea textAreaColumsCount;
    private JComboBox comboBox1;
    private JButton okButton;
    private JButton cancelButton;
    private JLabel buttonOutput;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on

    public JTextArea getTextAreaCode() {
        return textAreaCode;
    }

    public int getTextFieldRows() {
        return (int) (Double.parseDouble(textAreaColumsCount.getText()));
    }

    public JPanel getPanelGraphics() {
        return panelGraphics;
    }

    public Class<? extends Representable> getRepresentableClass() {
        return representableClass;
    }

    public void setRepresentableClass(Class<? extends Representable> representableClass) {
        this.representableClass = representableClass;
    }

    public ZBufferImpl getZBuffer() {
        if (zBuffer == null || zBuffer.la() != getPanelGraphics().getWidth() ||
                zBuffer.ha() != getPanelGraphics().getHeight()) {
            ZBufferImpl zBuffer1 = zBuffer;
            zBuffer = new ZBufferImpl(getPanelGraphics().getWidth(),
                    getPanelGraphics().getHeight());
            if (zBuffer1 != null) {
                zBuffer.setDisplayType(zBuffer1.getDisplayType());
            } else {
                zBuffer.setDisplayType(ZBufferImpl.SURFACE_DISPLAY_COL_QUADS);
            }
        }
        return zBuffer;
    }

    public void setModel(VecMeshEditor vecMeshEditor) {
        this.model = vecMeshEditor;
    }

    public void setResY(int i) {
        this.resY = i;
    }

    public void setResX(int j) {
        this.resX = j;
    }

    public int getResX() {
        return resX;
    }

    public int getResY() {
        return resY;
    }

    public BufferedImage getTexture() {
        return texture;
    }

    public void setTexture(BufferedImage texture) {
        this.texture = texture;
    }

    public File getFileTexture() {
        return fileTexture;
    }
}
