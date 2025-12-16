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

package one.empty3.apps.sculpt;

import com.google.api.services.vision.v1.model.FaceAnnotation;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;
import one.empty3.apps.facedetect.jvm.FaceDetectApp;
import one.empty3.library.*;
import one.empty3.library.Polygon;
import one.empty3.library.core.nurbs.CourbeParametriquePolynomialeBezier;
import one.empty3.library.core.nurbs.FctXY;
import one.empty3.library.core.nurbs.ParametricSurface;
import one.empty3.library.core.tribase.Plan3D;
//import one.empty3.library.core.tribase.T3D;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Empty3Design extends JFrame {


    private boolean generateByFaceDetetion = false;

    class HeightMapSurface1 extends HeightMapSurface {
        List<Point3D> point3DS1 = new ArrayList<>();


        @Override
        public double heightDouble(double u, double v) {
            return search(u, v).getZ();
        }
        public void setList(List<Point3D> point3DS) {
            this.point3DS1 = point3DS;
        }

        public Point3D search(double u, double v) {
            Point3D current = new Point3D(u, v, 0.0);

            int near = -1;
            for (double i = getStartU(); i < getEndU(); i += getIncrU()) {
                for (double j = getStartU(); j < getEndV(); j += getIncrV()) {
                    Double distance = Double.MAX_VALUE;
                    for (Point3D comparing : point3DS1) {
                        if (comparing.distance(current) < distance) {
                            distance = comparing.distance(current);
                            near = point3DS1.indexOf(comparing);
                        }
                    }
                }
            }
            if(near>=0) {
                return point3DS1.get(near);
            }else {
                return Point3D.O0;
            }
        }
    }
    private File currentDirectoryTexture;
    private File currentDirectoryHeightMap;
    private File currentDirectoryProject;
    private JCheckBoxMenuItem autoLoadMenuItem;
    private JPanel heightMapPanel;
    private GLJPanel glJPanel;
    private JPanel texturePanel;

    private BufferedImage heightMapImage;
    private BufferedImage textureImage;
    private JLabel heightMapLabel = new JLabel("Image de la carte de hauteur");
    private JLabel textureLabel = new JLabel("Image de la texture");

    private ParametricSurface surface;
    private final GLU glu = new GLU();

    // Mouse control variables
    private float rotateX = 0.0f;
    private float rotateY = 0.0f;
    private float zoom = 5.0f;
    private int prevMouseX;
    private int prevMouseY;
    private boolean isT3d;
    private JCheckBoxMenuItem displayHeightMapOnlyMenuItem;
    private HeightMapSurface1 surface1;

    public Empty3Design() {
        setTitle("Empty3 Designer");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Config config = new Config();
        File defaultFileOutput = config.getDefaultFileOutput();
        if (defaultFileOutput != null) {
            currentDirectoryHeightMap = defaultFileOutput;
            currentDirectoryTexture = defaultFileOutput;
        } else {
            currentDirectoryHeightMap = new File("./heightmaps");
            currentDirectoryTexture = new File("./textures");
            currentDirectoryProject = new File("./projects");
        }
        if (!currentDirectoryHeightMap.exists()) {
            currentDirectoryHeightMap.mkdirs();
        }
        if (!currentDirectoryTexture.exists()) {
            currentDirectoryTexture.mkdirs();
        }
        if (!currentDirectoryProject.exists()) {
            currentDirectoryProject.mkdirs();
        }


        initMenuBar();
        initPanels();
    }

    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // File Menu
        JMenu fileMenu = new JMenu("Fichiers");
        JMenuItem openDesignItem = new JMenuItem("Ouvrir design");
        openDesignItem.addActionListener(e -> openDesign());
        JMenuItem saveDesignItem = new JMenuItem("Sauver Design");
        saveDesignItem.addActionListener(e -> saveDesign());
        JMenuItem importHeightMapItem = new JMenuItem("Importer carte de hauteur");
        importHeightMapItem.addActionListener(e -> importHeightMap());
        JMenuItem importTextureItem = new JMenuItem("Importer texture");
        importTextureItem.addActionListener(e -> importTexture());

        fileMenu.add(openDesignItem);
        fileMenu.add(saveDesignItem);
        fileMenu.addSeparator();
        fileMenu.add(importHeightMapItem);
        fileMenu.add(importTextureItem);
        menuBar.add(fileMenu);

        // Generate Menu
        JMenu generateMenu = new JMenu("Générer");
        JMenuItem fromFaceDetectItem = new JMenuItem("Carte de hauteur depuis visage");
        fromFaceDetectItem.addActionListener(e -> generateHeightMapFromFaceAction());
        generateMenu.add(fromFaceDetectItem);
        menuBar.add(generateMenu);

        // Settings Menu
        JMenu settingsMenu = new JMenu("Paramètres");
        autoLoadMenuItem = new JCheckBoxMenuItem("Charger automatiquement à partir du fichier image");
        autoLoadMenuItem.addActionListener(e -> toggleAutoLoad());
        settingsMenu.add(autoLoadMenuItem);


        displayHeightMapOnlyMenuItem = new JCheckBoxMenuItem("Afficher uniquement la carte de hauteur");
        displayHeightMapOnlyMenuItem.setSelected(false);
        displayHeightMapOnlyMenuItem.addActionListener(e -> {
                    if (((JCheckBoxMenuItem) (e.getSource())).isSelected()) {
                        setT3d(false);
                        System.out.println("T3D is false");
                    } else {
                        setT3d(true);
                        System.out.println("T3D is true");
                    }

                }
        );
        settingsMenu.add(displayHeightMapOnlyMenuItem);


        menuBar.add(settingsMenu);

        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem websiteLinkItem = new JMenuItem("Lien vers le site empty3.one");
        websiteLinkItem.addActionListener(e -> openWebsite());
        JMenuItem viewLicenseItem = new JMenuItem("Voir licence");
        viewLicenseItem.addActionListener(e -> showLicense());
        helpMenu.add(websiteLinkItem);
        helpMenu.add(viewLicenseItem);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private void generateHeightMapFromFaceAction() {
        try {
            if(textureImage==null) {
                setGenerateByFaceDetetion(false);
                return;
            }
            File texture = File.createTempFile("texture", ".png");

            new one.empty3.libs.Image(textureImage).saveFile(texture);
            one.empty3.apps.sculpt.FaceDetectApp faceDetectApp = new one.empty3.apps.sculpt.FaceDetectApp(FaceDetectApp.getVisionService());
            List<FaceAnnotation> faceAnnotations = faceDetectApp.detectFaces(texture.toPath(), 1000);
            if(faceAnnotations.size()==0) {
                setGenerateByFaceDetetion(false);
                return;
            }
            List<Point3D> point3DS = faceDetectApp.writeFaceDataWithZ(new one.empty3.libs.Image(textureImage), faceAnnotations.get(0));

            if(!point3DS.isEmpty()) {
                surface1 = new HeightMapSurface1();
                surface1.setList(point3DS);
                setGenerateByFaceDetetion(true);
                return;
            }
        } catch (IOException | GeneralSecurityException e) {
            throw new RuntimeException(e);
        }

        setGenerateByFaceDetetion(false);


    }

    private void initPanels() {
        // Left panel for height map
        heightMapPanel = new JPanel(new BorderLayout());
        heightMapPanel.setBorder(BorderFactory.createTitledBorder("Height Map (heightMapSurface)"));
        heightMapLabel.setHorizontalAlignment(SwingConstants.CENTER);
        heightMapPanel.add(heightMapLabel, BorderLayout.CENTER);

        // Right panel for 3D view and texture
        JPanel rightContainer = new JPanel(new BorderLayout());

        // 3D View (JOGL)
        GLProfile glp = GLProfile.get(GLProfile.GL2);
        GLCapabilities caps = new GLCapabilities(glp);
        caps.setRedBits(8);
        caps.setGreenBits(8);
        caps.setBlueBits(8);
        caps.setAlphaBits(8);
        caps.setDepthBits(24);
        caps.setDoubleBuffered(true);
        caps.setHardwareAccelerated(true);

        glJPanel = new GLJPanel(caps);
        glJPanel.addGLEventListener(new GLEventListener() {
            @Override
            public void init(GLAutoDrawable drawable) {
                GL2 gl = drawable.getGL().getGL2();
                gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
                gl.glEnable(GL.GL_DEPTH_TEST);
            }

            @Override
            public void dispose(GLAutoDrawable drawable) {
            }

            @Override
            public void display(GLAutoDrawable drawable) {
                GL2 gl = drawable.getGL().getGL2();
                gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
                gl.glLoadIdentity();

                // Setup camera with zoom
                glu.gluLookAt(0, 0, zoom, 0, 0, 0, 0, 1, 0);

                // Apply rotations
                gl.glRotatef(rotateX, 1.0f, 0.0f, 0.0f);
                gl.glRotatef(rotateY, 0.0f, 1.0f, 0.0f);

                if (surface != null) {
                    draw(surface, glu, gl);
                }
            }

            @Override
            public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
                GL2 gl = drawable.getGL().getGL2();
                gl.glViewport(0, 0, width, height);
                gl.glMatrixMode(GL2.GL_PROJECTION);
                gl.glLoadIdentity();
                if (height == 0) height = 1;
                glu.gluPerspective(45.0, (double) width / (double) height, 0.1, 100.0);
                gl.glMatrixMode(GL2.GL_MODELVIEW);
                gl.glLoadIdentity();
            }
        });

        // Add mouse controls for the 3D view
        addMouseControls(glJPanel);

        // Texture Panel
        texturePanel = new JPanel(new BorderLayout());
        texturePanel.setBorder(BorderFactory.createTitledBorder("Texture 4K"));
        textureLabel.setHorizontalAlignment(SwingConstants.CENTER);
        texturePanel.add(textureLabel, BorderLayout.CENTER);
        texturePanel.setPreferredSize(new Dimension(0, 200)); // Example height

        JSplitPane rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, glJPanel, texturePanel);
        rightSplitPane.setResizeWeight(0.8);
        rightContainer.add(rightSplitPane, BorderLayout.CENTER);

        // Main SplitPane
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(heightMapPanel), rightContainer);
        mainSplitPane.setResizeWeight(0.3);

        add(mainSplitPane, BorderLayout.CENTER);
    }

    private void createObject() {
        surface = new T3D();
        HeightMapSurface surface0 = null;
        if (heightMapImage != null) {
            surface0 = new HeightMapSurface() {
                @Override
                public double heightDouble(double u, double v) {
                    int x = (int) (u * (heightMapImage.getWidth() - 1));
                    int y = (int) (v * (heightMapImage.getHeight() - 1));
                    if(x>=0&&x<heightMapImage.getWidth()&&y>=0&&y<heightMapImage.getHeight()) {
                        return ( heightMapImage.getRGB(x, y)&0xff) / 256.0f;
                    } else
                        return 0.0;
                }
                @Override
                public Point3D calculerPoint3D(double u, double v) {
                    int x = (int) (u * (heightMapImage.getWidth() - 1));
                    int y = (int) (v * (heightMapImage.getHeight() - 1));
                    float height = new Color(heightMapImage.getRGB(x, y)).getRed() / 255.0f;
                    return new Point3D(u , v , (double) height);
                }
            };
            surface0.setIncrU(0.05);
            surface0.setIncrV(0.05);
        }
        ((T3D) surface).getSurfaceUV().setElem(surface0);
        CourbeParametriquePolynomialeBezier courbeParametriquePolynomialeBezier = new CourbeParametriquePolynomialeBezier();
        courbeParametriquePolynomialeBezier.getCoefficients().setElem(Point3D.O0, 0);
        courbeParametriquePolynomialeBezier.getCoefficients().setElem(Point3D.Z.mult(1.0),1);
        courbeParametriquePolynomialeBezier.getCoefficients().setElem(Point3D.Z.mult(2.0),2);
        courbeParametriquePolynomialeBezier.getCoefficients().setElem(Point3D.Z.mult(3.0),3);
        ((T3D) surface).getSoulCurve().setElem(courbeParametriquePolynomialeBezier);
        ((T3D) surface).getDiameterFunction().setElem(new FctXY() {
            @Override
            public double result(double input) {
                return 3.0;
            }
        });
        if (!isT3d()) {
            surface = surface0;
        }
        if (textureImage != null&&surface != null) {
            surface.texture(new ImageTexture(new one.empty3.libs.Image(textureImage)));
        }

        if(surface!=null) {
            System.out.println("surface!=null");
            surface.setIncrU(0.05);
            surface.setIncrV(0.05);
        } else {
            System.out.println("surface==null");
        }

        if(isGenerateByFaceDetetion()) {
            if (surface.getClass().isAssignableFrom(HeightMapSurface.class)) {
                surface = surface1;
            }
            if (surface.getClass().isAssignableFrom(T3D.class)) {
                ((T3D) surface).getSurfaceUV().setElem(surface1);
            }
        }


        glJPanel.display(); // Redraw
    }

    private boolean isGenerateByFaceDetetion() {
        return generateByFaceDetetion;
    }
    private void setGenerateByFaceDetetion(boolean b) {
        generateByFaceDetetion = b;
    }
    private boolean isT3d() {
        return isT3d;
    }

    private void setT3d(boolean t) {
        isT3d = t;
        createObject();
    }

    protected void draw(ParametricSurface s, GLU glu, GL2 gl) {
        if (s == null) return;
        gl.glBegin(GL2.GL_TRIANGLES);
        for (double i = s.getStartU(); i < s.getEndU(); i += s.getIncrU()) {
            for (double j = s.getStartV(); j < s.getEndV(); j += s.getIncrV()) {
                Polygon elementSurface = s.getElementSurface(i, s.getIncrU(), j, s.getIncrV());
                draw2(new TRI(elementSurface.getPoints().getElem(0),
                        elementSurface.getPoints().getElem(1),
                        elementSurface.getPoints().getElem(2), s.texture()
                ), glu, gl, true);
                draw2(new TRI(elementSurface.getPoints().getElem(2),
                                elementSurface.getPoints().getElem(3),
                                elementSurface.getPoints().getElem(0), s.texture()),
                        glu, gl, true);
            }
        }
        gl.glEnd();
    }

    private void draw2(TRI tri, GLU glu, GL2 gl, boolean useTexture) {
        if (tri == null) return;
        for (int i = 0; i < 3; i++) {
            Point3D p = tri.getSommet().getElem(i);
            if (useTexture && tri.getTexture() != null && p.texture() != null) {
                Color c = new Color(tri.getTexture().getColorAt((double) (p.get(0)), (double) (p.get(1))));
                gl.glColor4f(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f);
            } else {
                gl.glColor3f(0.8f, 0.8f, 0.8f);
            }
            gl.glVertex3d(p.get(0), p.get(1), p.get(2));
        }
    }


    private void addMouseControls(Component canvas) {
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                prevMouseX = e.getX();
                prevMouseY = e.getY();
            }
        });

        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                int dx = x - prevMouseX;
                int dy = y - prevMouseY;

                // Rotation
                rotateY += dx * 0.5f;
                rotateX += dy * 0.5f;

                prevMouseX = x;
                prevMouseY = y;

                glJPanel.display(); // Redraw the scene
            }
        });

        canvas.addMouseWheelListener(e -> {
            int rotation = e.getWheelRotation();
            // Zoom in/out
            zoom -= rotation * 0.5f;
            if (zoom < 1.0f) {
                zoom = 1.0f;
            }
            if (zoom > 50.0f) {
                zoom = 50.0f;
            }
            glJPanel.display(); // Redraw the scene
        });
    }

    // --- Menu Action Methods ---

    private void openDesign() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Ouvrir Design");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Fichier PNG", "png"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Fichier ZIP", "zip"));
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setCurrentDirectory(currentDirectoryProject);
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (ZipInputStream zis = new ZipInputStream(new FileInputStream(fileChooser.getSelectedFile()))) {
                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    if (entry.getName().equalsIgnoreCase("heightmap.png")) {
                        heightMapImage = ImageIO.read(zis);
                    } else if (entry.getName().equalsIgnoreCase("texture.png")) {
                        textureImage = ImageIO.read(zis);
                    }
                    zis.closeEntry();
                }
                updateImagePanels();
                createObject();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ouverture du fichier: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveDesign() {
        if (heightMapImage == null && textureImage == null) {
            JOptionPane.showMessageDialog(this, "Aucune image à sauvegarder.", "Avertissement", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Sauver Design");
        fileChooser.setCurrentDirectory(currentDirectoryProject);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Fichier ZIP", "zip"));
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getName().toLowerCase().endsWith(".zip")) {
                fileToSave = new File(fileToSave.getParentFile(), fileToSave.getName() + ".zip");
            }

            try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(fileToSave))) {
                if (heightMapImage != null) {
                    ZipEntry heightMapEntry = new ZipEntry("heightmap.png");
                    zos.putNextEntry(heightMapEntry);
                    ImageIO.write(heightMapImage, "png", zos);
                    zos.closeEntry();
                }

                if (textureImage != null) {
                    ZipEntry textureEntry = new ZipEntry("texture.png");
                    zos.putNextEntry(textureEntry);
                    ImageIO.write(textureImage, "png", zos);
                    zos.closeEntry();
                }
                // TODO: Save other design parameters (curve, rotation, etc.)

                JOptionPane.showMessageDialog(this, "Design sauvegardé avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la sauvegarde du fichier: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void importHeightMap() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Importer une image pour la carte de hauteur");
        fileChooser.setCurrentDirectory(currentDirectoryHeightMap);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Images", "png", "jpg", "jpeg", "bmp"));
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                heightMapImage = ImageIO.read(fileChooser.getSelectedFile());
                updateImagePanels();
                createObject();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Erreur lors du chargement de l'image: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void importTexture() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Importer une image pour la texture");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Images", "png", "jpg", "jpeg", "bmp"));
        fileChooser.setCurrentDirectory(currentDirectoryTexture);
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                textureImage = ImageIO.read(fileChooser.getSelectedFile());
                updateImagePanels();
                createObject();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Erreur lors du chargement de l'image: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void updateImagePanels() {
        if (heightMapImage != null) {
            heightMapLabel.setIcon(new ImageIcon(heightMapImage.getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
            heightMapLabel.setText(null);
        } else {
            heightMapLabel.setIcon(null);
            heightMapLabel.setText("Image de la carte de hauteur");
        }
        if (textureImage != null) {
            textureLabel.setIcon(new ImageIcon(textureImage.getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
            textureLabel.setText(null);
        } else {
            textureLabel.setIcon(null);
            textureLabel.setText("Image de la texture");
        }
        revalidate();
        repaint();
    }

    private void toggleAutoLoad() {
        // TODO: Implement logic for auto-loading from image file
        System.out.println("Action: Toggle Auto-Load. New state: " + autoLoadMenuItem.isSelected());
    }

    private void openWebsite() {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI("http://empty3.one"));
            }
        } catch (Exception e) {
            // TODO: Handle exceptions (e.g., show an error dialog)
            e.printStackTrace();
        }
    }

    private void showLicense() {
        JDialog licenseDialog = new JDialog(this, "Licence", true);
        String licenseText = "                                 Apache License\n" +
                "                           Version 2.0, January 2004\n" +
                "                        http://www.apache.org/licenses/\n" +
                "\n" +
                "   TERMS AND CONDITIONS FOR USE, REPRODUCTION, AND DISTRIBUTION\n" +
                "\n" +
                "   1. Definitions.\n" +
                "\n" +
                "      \"License\" shall mean the terms and conditions for use, reproduction,\n" +
                "      and distribution as defined by Sections 1 through 9 of this document.\n" +
                "\n" +
                "      \"Licensor\" shall mean the copyright owner or entity authorized by\n" +
                "      the copyright owner that is granting the License.\n" +
                "\n" +
                "      \"Legal Entity\" shall mean the union of the acting entity and all\n" +
                "      other entities that control, are controlled by, or are under common\n" +
                "      control with that entity. For the purposes of this definition,\n" +
                "      \"control\" means (i) the power, direct or indirect, to cause the\n" +
                "      direction or management of such entity, whether by contract or\n" +
                "      otherwise, or (ii) ownership of fifty percent (50%) or more of the\n" +
                "      outstanding shares, or (iii) beneficial ownership of such entity.\n" +
                "\n" +
                "      \"You\" (or \"Your\") shall mean an individual or Legal Entity\n" +
                "      exercising permissions granted by this License.\n" +
                "\n" +
                "      \"Source\" form shall mean the preferred form for making modifications,\n" +
                "      including but not limited to software source code, documentation\n" +
                "      source, and configuration files.\n" +
                "\n" +
                "      \"Object\" form shall mean any form resulting from mechanical\n" +
                "      transformation or translation of a Source form, including but\n" +
                "      not limited to compiled object code, generated documentation,\n" +
                "      and conversions to other media types.\n" +
                "\n" +
                "      \"Work\" shall mean the work of authorship, whether in Source or\n" +
                "      Object form, made available under the License, as indicated by a\n" +
                "      copyright notice that is included in or attached to the work\n" +
                "      (an example is provided in the Appendix below).\n" +
                "\n" +
                "      \"Derivative Works\" shall mean any work, whether in Source or Object\n" +
                "      form, that is based on (or derived from) the Work and for which the\n" +
                "      editorial revisions, annotations, elaborations, or other modifications\n" +
                "      represent, as a whole, an original work of authorship. For the purposes\n" +
                "      of this License, Derivative Works shall not include works that remain\n" +
                "      separable from, or merely link (or bind by name) to the interfaces of,\n" +
                "      the Work and Derivative Works thereof.\n" +
                "\n" +
                "      \"Contribution\" shall mean any work of authorship, including\n" +
                "      the original version of the Work and any modifications or additions\n" +
                "      to that Work or Derivative Works thereof, that is intentionally\n" +
                "      submitted to Licensor for inclusion in the Work by the copyright owner\n" +
                "      or by an individual or Legal Entity authorized to submit on behalf of\n" +
                "      the copyright owner. For the purposes of this definition, \"submitted\"\n" +
                "      means any form of electronic, verbal, or written communication sent\n" +
                "      to the Licensor or its representatives, including but not limited to\n" +
                "      communication on electronic mailing lists, source code control systems,\n" +
                "      and issue tracking systems that are managed by, or on behalf of, the\n" +
                "      Licensor for the purpose of discussing and improving the Work, but\n" +
                "      excluding communication that is conspicuously marked or otherwise\n" +
                "      designated in writing by the copyright owner as \"Not a Contribution.\"\n" +
                "\n" +
                "      \"Contributor\" shall mean Licensor and any individual or Legal Entity\n" +
                "      on behalf of whom a Contribution has been received by Licensor and\n" +
                "      subsequently incorporated within the Work.\n" +
                "\n" +
                "   2. Grant of Copyright License. Subject to the terms and conditions of\n" +
                "      this License, each Contributor hereby grants to You a perpetual,\n" +
                "      worldwide, non-exclusive, no-charge, royalty-free, irrevocable\n" +
                "      copyright license to reproduce, prepare Derivative Works of,\n" +
                "      publicly display, publicly perform, sublicense, and distribute the\n" +
                "      Work and such Derivative Works in Source or Object form.\n" +
                "\n" +
                "   3. Grant of Patent License. Subject to the terms and conditions of\n" +
                "      this License, each Contributor hereby grants to You a perpetual,\n" +
                "      worldwide, non-exclusive, no-charge, royalty-free, irrevocable\n" +
                "      (except as stated in this section) patent license to make, have made,\n" +
                "      use, offer to sell, sell, import, and otherwise transfer the Work,\n" +
                "      where such license applies only to those patent claims licensable\n" +
                "      by such Contributor that are necessarily infringed by their\n" +
                "      Contribution(s) alone or by combination of their Contribution(s)\n" +
                "      with the Work to which such Contribution(s) was submitted. If You\n" +
                "      institute patent litigation against any entity (including a\n" +
                "      cross-claim or counterclaim in a lawsuit) alleging that the Work\n" +
                "      or a Contribution incorporated within the Work constitutes direct\n" +
                "      or contributory patent infringement, then any patent licenses\n" +
                "      granted to You under this License for that Work shall terminate\n" +
                "      as of the date such litigation is filed.\n" +
                "\n" +
                "   4. Redistribution. You may reproduce and distribute copies of the\n" +
                "      Work or Derivative Works thereof in any medium, with or without\n" +
                "      modifications, and in Source or Object form, provided that You\n" +
                "      meet the following conditions:\n" +
                "\n" +
                "      (a) You must give any other recipients of the Work or\n" +
                "          Derivative Works a copy of this License; and\n" +
                "\n" +
                "      (b) You must cause any modified files to carry prominent notices\n" +
                "          stating that You changed the files; and\n" +
                "\n" +
                "      (c) You must retain, in the Source form of any Derivative Works\n" +
                "          that You distribute, all copyright, patent, trademark, and\n" +
                "          attribution notices from the Source form of the Work,\n" +
                "          excluding those notices that do not pertain to any part of\n" +
                "          the Derivative Works; and\n" +
                "\n" +
                "      (d) If the Work includes a \"NOTICE\" text file as part of its\n" +
                "          distribution, then any Derivative Works that You distribute must\n" +
                "          include a readable copy of the attribution notices contained\n" +
                "          within such NOTICE file, excluding those notices that do not\n" +
                "          pertain to any part of the Derivative Works, in at least one\n" +
                "          of the following places: within a NOTICE text file distributed\n" +
                "          as part of the Derivative Works; within the Source form or\n" +
                "          documentation, if provided along with the Derivative Works; or,\n" +
                "          within a display generated by the Derivative Works, if and\n" +
                "          wherever such third-party notices normally appear. The contents\n" +
                "          of the NOTICE file are for informational purposes only and\n" +
                "          do not modify the License. You may add Your own attribution\n" +
                "          notices within Derivative Works that You distribute, alongside\n" +
                "          or as an addendum to the NOTICE text from the Work, provided\n" +
                "          that such additional attribution notices cannot be construed\n" +
                "          as modifying the License.\n" +
                "\n" +
                "      You may add Your own copyright statement to Your modifications and\n" +
                "      may provide additional or different license terms and conditions\n" +
                "      for use, reproduction, or distribution of Your modifications, or\n" +
                "      for any such Derivative Works as a whole, provided Your use,\n" +
                "      reproduction, and distribution of the Work otherwise complies with\n" +
                "      the conditions stated in this License.\n" +
                "\n" +
                "   5. Submission of Contributions. Unless You explicitly state otherwise,\n" +
                "      any Contribution intentionally submitted for inclusion in the Work\n" +
                "      by You to the Licensor shall be under the terms and conditions of\n" +
                "      this License, without any additional terms or conditions.\n" +
                "      Notwithstanding the above, nothing herein shall supersede or modify\n" +
                "      the terms of any separate license agreement you may have executed\n" +
                "      with Licensor regarding such Contributions.\n" +
                "\n" +
                "   6. Trademarks. This License does not grant permission to use the trade\n" +
                "      names, trademarks, service marks, or product names of the Licensor,\n" +
                "      except as required for reasonable and customary use in describing the\n" +
                "      origin of the Work and reproducing the content of the NOTICE file.\n" +
                "\n" +
                "   7. Disclaimer of Warranty. Unless required by applicable law or\n" +
                "      agreed to in writing, Licensor provides the Work (and each\n" +
                "      Contributor provides its Contributions) on an \"AS IS\" BASIS,\n" +
                "      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or\n" +
                "      implied, including, without limitation, any warranties or conditions\n" +
                "      of TITLE, NON-INFRINGEMENT, MERCHANTABILITY, or FITNESS FOR A\n" +
                "      PARTICULAR PURPOSE. You are solely responsible for determining the\n" +
                "      appropriateness of using or redistributing the Work and assume any\n" +
                "      risks associated with Your exercise of permissions under this License.\n" +
                "\n" +
                "   8. Limitation of Liability. In no event and under no legal theory,\n" +
                "      whether in tort (including negligence), contract, or otherwise,\n" +
                "      unless required by applicable law (such as deliberate and grossly\n" +
                "      negligent acts) or agreed to in writing, shall any Contributor be\n" +
                "      liable to You for damages, including any direct, indirect, special,\n" +
                "      incidental, or consequential damages of any character arising as a\n" +
                "      result of this License or out of the use or inability to use the\n" +
                "      Work (including but not limited to damages for loss of goodwill,\n" +
                "      work stoppage, computer failure or malfunction, or any and all\n" +
                "      other commercial damages or losses), even if such Contributor\n" +
                "      has been advised of the possibility of such damages.\n" +
                "\n" +
                "   9. Accepting Warranty or Additional Liability. While redistributing\n" +
                "      the Work or Derivative Works thereof, You may choose to offer,\n" +
                "      and charge a fee for, acceptance of support, warranty, indemnity,\n" +
                "      or other liability obligations and/or rights consistent with this\n" +
                "      License. However, in accepting such obligations, You may act only\n" +
                "      on Your own behalf and on Your sole responsibility, not on behalf\n" +
                "      of any other Contributor, and only if You agree to indemnify,\n" +
                "      defend, and hold each Contributor harmless for any liability\n" +
                "      incurred by, or claims asserted against, such Contributor by reason\n" +
                "      of your accepting any such warranty or additional liability.\n" +
                "\n" +
                "   END OF TERMS AND CONDITIONS";
        JTextArea textArea = new JTextArea(licenseText);
        textArea.setEditable(false);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        licenseDialog.add(new JScrollPane(textArea));
        licenseDialog.setSize(600, 400);
        licenseDialog.setLocationRelativeTo(this);
        licenseDialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Empty3Design designer = new Empty3Design();
            designer.setVisible(true);
        });
    }
}
