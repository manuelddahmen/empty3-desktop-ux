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
import java.io.InputStream;
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
            faceDetectApp.initStructurePolygons();
            List<FaceAnnotation> faceAnnotations = faceDetectApp.detectFaces(texture.toPath(), 1000);
            if(faceAnnotations.isEmpty()) {
                setGenerateByFaceDetetion(false);
                return;
            }
            List<Point3D> point3DS = faceDetectApp.writeFaceDataWithZ(new one.empty3.libs.Image(textureImage), faceAnnotations.get(0));

            normalizeZ(point3DS);

            for (Point3D point3D : point3DS) {
                System.out.println(point3D.toString());
                point3D.setZ(point3D.getZ() +1);
            }

            // Creates surface from face data if available
            if(!point3DS.isEmpty()) {
                surface1 = new HeightMapSurface1();
                surface1.setList(point3DS);
                System.out.printf("Face successfully imported : %d\n", point3DS.size());
                System.out.printf("Face successfully imported : %d\n", point3DS.size());
                setGenerateByFaceDetetion(true);
                createObject();

                return;
            }
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }

        setGenerateByFaceDetetion(false);


    }

    private void normalizeZ(List<Point3D> point3DS) {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        // Determines minimum and maximum Z values
        for (Point3D point3D : point3DS) {
            if (point3D.getZ() <= min) {
                min = point3D.getZ();
            }
            if (point3D.getZ() >= max) {
                max = point3D.getZ();
            }
        }

        for (Point3D point3D : point3DS) {
            point3D.setZ((point3D.getZ() - min) / (max - min));
        }
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
        // Implements OpenGL event handling for rendering and interaction
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
        // Creates surface from heightmap or face detection; textures it
        if (heightMapImage != null) {
            if(isGenerateByFaceDetetion()) {
                // Assigns face‑detected surface or sets UV coordinates
                if (!isT3d && surface1 != null) {
                    surface = surface1;
                } else if (isT3d && surface1 != null) {
                    ((T3D) surface).getSurfaceUV().setElem(surface1);

                }
                surface.texture(new ImageTexture(new one.empty3.libs.Image(textureImage)));
            } else {
                // Creates heightmap surface from image data
                surface0 = new HeightMapSurface() {
                    @Override
                    public double heightDouble(double u, double v) {
                        int x = (int) (u * (heightMapImage.getWidth() - 1));
                        int y = (int) (v * (heightMapImage.getHeight() - 1));
                        // Returns height value if within image bounds
                        if (x >= 0 && x < heightMapImage.getWidth() && y >= 0 && y < heightMapImage.getHeight()) {
                            return (heightMapImage.getRGB(x, y) & 0x00ff0000) / 256.0f;
                        } else
                            return 0.0;
                    }

                    // Computes heightmap point from image coordinates and color
                    @Override
                    public Point3D calculerPoint3D(double u, double v) {
                        int x = (int) (u * (heightMapImage.getWidth() - 1));
                        int y = (int) (v * (heightMapImage.getHeight() - 1));
                        float height = new Color(heightMapImage.getRGB(x, y)).getRed() / 255.0f;
                        return new Point3D(u, v, (double) height);
                    }

                };

                if(isT3d) {
                    ((T3D) surface).getSurfaceUV().setElem(surface0);
                    ((T3D) surface).getSurfaceUV().getElem().texture(new ImageTexture(new one.empty3.libs.Image(textureImage)));
                } else {
                    surface0.texture(new ImageTexture(new one.empty3.libs.Image(textureImage)));
                    surface = surface0;
                }

            }
            // Sets curve and diameter for 3D surface
            if(isT3d) {
                CourbeParametriquePolynomialeBezier courbeParametriquePolynomialeBezier = new CourbeParametriquePolynomialeBezier();
                courbeParametriquePolynomialeBezier.getCoefficients().setElem(Point3D.O0, 0);
                courbeParametriquePolynomialeBezier.getCoefficients().setElem(new Point3D(0.0, 0.1, 0.25), 1);
                courbeParametriquePolynomialeBezier.getCoefficients().setElem(new Point3D(0.1, 0.1, 0.50), 1);
                courbeParametriquePolynomialeBezier.getCoefficients().setElem(new Point3D(0.1, 0.0, 0.75), 1);
                courbeParametriquePolynomialeBezier.getCoefficients().setElem(new Point3D(0.0, 0.0, 1.00), 1);
                ((T3D) surface).getSoulCurve().setElem(courbeParametriquePolynomialeBezier);
                ((T3D) surface).getDiameterFunction().setElem(new FctXY() {
                    @Override
                    public double result(double input) {
                        return 1.0;
                    }
                });
                ((FctXY)((T3D) surface).getDiameterFunction().getElem()).setFormulaX("1.0");
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

            glJPanel.display(); // Redraw
        }
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
        // Iterates surface elements; draws two triangles each
        for (double i = s.getStartU(); i < s.getEndU(); i += s.getIncrU()) {
            // Iterates surface elements; draws two triangles each
            for (double j = s.getStartV(); j < s.getEndV(); j += s.getIncrV()) {
                Polygon elementSurface = s.getElementSurface(i, s.getIncrU(), j, s.getIncrV());
                double u = (i-s.getStartU())/(s.getEndU()-s.getStartU());
                double v = (j-s.getStartV())/(s.getEndV()-s.getStartV());
                // Draws first triangle from parametric surface element
                draw2(new TRI(elementSurface.getPoints().getElem(0),
                        elementSurface.getPoints().getElem(1),
                        elementSurface.getPoints().getElem(2), s.texture()
                ), glu, gl, true, u, v);
                // Draws second triangle from parametric surface element
                draw2(new TRI(elementSurface.getPoints().getElem(2),
                                elementSurface.getPoints().getElem(3),
                                elementSurface.getPoints().getElem(0), s.texture()),
                        glu, gl, true, u, v);
            }
        }
        gl.glEnd();
    }

    private void draw2(TRI tri, GLU glu, GL2 gl, boolean useTexture, double u, double v) {
        if (tri == null) return;
        // Iterates triangle vertices; sets color; draws vertex
        for (int i = 0; i < 3; i++) {
            Point3D p = tri.getSommet().getElem(i);
            // Chooses color based on texture availability
            if (useTexture && tri.getTexture() != null && p.texture() != null) {
                Color c = new Color(tri.getTexture().getColorAt(u, v));
                // Sets vertex color based on texture sample
                gl.glColor4f(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f);
            } else {
                gl.glColor3f(0.8f, 0.8f, 0.8f);
            }
            gl.glVertex3d(p.get(0), p.get(1), p.get(2));
        }
    }


    /**
     * Adds mouse controls for rotation and zoom
     */
    private void addMouseControls(Component canvas) {
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                prevMouseX = e.getX();
                prevMouseY = e.getY();
            }
        });

        // Implements mouse drag for scene rotation
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

    /**
     * Opens design file; handles image loading errors
     */
    private void openDesign() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Ouvrir Design");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Fichier PNG", "png"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Fichier ZIP", "zip"));
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setCurrentDirectory(currentDirectoryProject);
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            // Opens zipped design file; handles image loading errors
            try (ZipInputStream zis = new ZipInputStream(new FileInputStream(fileChooser.getSelectedFile()))) {
                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    // Reads heightmap and texture images from zip entry
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

    /**
     * Saves design images to zipped file
     */
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

    /**
     * Imports heightmap image from user selection
     */
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

    /**
     * Imports texture image from user selection
     */
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
        // Updates height map label with scaled image or placeholder
        if (heightMapImage != null) {
            heightMapLabel.setIcon(new ImageIcon(heightMapImage.getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
            heightMapLabel.setText(null);
        } else {
            heightMapLabel.setIcon(null);
            heightMapLabel.setText("Image de la carte de hauteur");
        }
        // Updates texture label with scaled image or placeholder
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
            // Opens default browser to project website
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI("http://empty3.one"));
            }
        } catch (Exception e) {
            // TODO: Handle exceptions (e.g., show an error dialog)
            e.printStackTrace();
        }
    }

    private String loadLicenseFromResource() {
        // Loads license text from resource file
        try (InputStream is = getClass().getResourceAsStream("/LICENSE.txt")) {
            if (is == null) {
                return "License file not found.";
            }
            StringBuilder sb = new StringBuilder();
            int c;
            while ((c = is.read()) != -1) {
                sb.append((char) c);
            }
            return sb.toString();
        } catch (IOException e) {
            return "Error loading license: " + e.getMessage();
        }
    }

    private void showLicense() {
        JDialog licenseDialog = new JDialog(this, "Licence", true);
        String licenseText = loadLicenseFromResource();
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
