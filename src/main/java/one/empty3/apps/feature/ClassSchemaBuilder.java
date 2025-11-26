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

package one.empty3.apps.feature;

import one.empty3.apps.feature.facemorph.RunFeatures;
import one.empty3.feature.*;


import net.miginfocom.swing.MigLayout;
import one.empty3.apps.feature.*;
import one.empty3.apps.feature.Hist4Contour2;
//import one.empty3.feature.MagnitudeProcess;
import one.empty3.apps.feature.ProxyValue;
import one.empty3.apps.feature.ProxyValue2;
import one.empty3.apps.feature.RegionLineCorner;
import one.empty3.apps.feature.Transform1;
import one.empty3.apps.feature.TreeDiagram;
import one.empty3.apps.feature.TreeNodeDiagram;
import one.empty3.apps.feature.TrueHarrisProcess;
import one.empty3.apps.feature.Voronoi;
import one.empty3.apps.feature.gui.LiveEffect;
import one.empty3.apps.feature.histograms.Histogram;
import one.empty3.apps.feature.histograms.*;
import one.empty3.apps.feature.selection.HighlightFeatures;
import one.empty3.apps.feature.tryocr.ReadLines;
import one.empty3.apps.feature.tryocr.SelectColor;
import one.empty3.io.ProcessFile;
import one.empty3.io.ProcessNFiles;
import one.empty3.library.Config;
import one.empty3.library.Point2D;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import one.empty3.libs.*;
import one.empty3.libs.Color;

import java.awt.*;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * @author Manuel Dahmen
 */
public class ClassSchemaBuilder extends JFrame implements Serializable {

    private static UUID uuid = UUID.randomUUID();
    private int idFrame = 0;
    public boolean processed = false;
    private PartElement selectedPart;
    private String directory = ".";
    private boolean selectAndClickClass = false;
    public List<File[]> files = new ArrayList<>();
    private boolean selectedActionDeleteClass = false;
    private boolean selectedActionDeleteLink = false;
    private boolean selectsAddLink = false;
    private LiveEffect liveEffect;
    private boolean cam;
    private int maxRes = 0;
    private String fileChooserDir = ".";
    public String tempDir = new Config().getMap().get("folderoutput") + File.separator + "temp";
    private File fileOut;
    private one.empty3.apps.feature.TreeDiagram treeDiagram;
    private File webcamFile;

    public void setWebcamFile() {
        this.webcamFile = new File(tempDir + File.separator + getUuid() + "_" + (this.idFrame++) + "_" + "webcam.jpg");
    }

    public File getWebcamFile() {
        return webcamFile;
    }

    public DiagramElement getSelectedElement() {
        return selectedElement;
    }

    public class DiagramElement implements Serializable {
        protected int x = getWidth() / 2;
        protected int y = getHeight() / 2;
        protected String label = "DIAGRAM ELEMENT";

        public DiagramElement() {

        }

        public void moveTo(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return "DiagramElement{" +
                    "x=" + x +
                    ", y=" + y +
                    ", label='" + label + '\'' +
                    '}';
        }
    }

    class ListFilesElement extends DiagramElement {
        File[] files;

        public File[] getFiles() {
            return files;
        }

        public void setFiles(File[] files) {
            this.files = files;
        }

        @Override
        public String toString() {
            return "ListFilesElement{" +
                    "files=" + (files == null ? "" : Arrays.toString(files)) +
                    ", x=" + x +
                    ", y=" + y +
                    ", label='" + label + '\'' +
                    '}';
        }
    }

    private DiagramElement selectedElement;
    private ClassElement selectedClassElement;
    private Then selectedArrow;

    class PartElement extends DiagramElement {
        DiagramElement referenceElement;
        DiagramElement element;//Nature de l'élément.
        int x, y;
        String label = "";

        public DiagramElement getReferenceElement() {
            return referenceElement;
        }

        public void setReferenceElement(DiagramElement referenceElement) {
            this.referenceElement = referenceElement;
        }

        public DiagramElement getElement() {
            return element;
        }

        public void setElement(DiagramElement element) {
            this.element = element;
        }

        @Override
        public int getX() {
            return x;
        }

        @Override
        public void setX(int x) {
            this.x = x;
        }

        @Override
        public int getY() {
            return y;
        }

        @Override
        public void setY(int y) {
            this.y = y;
        }

        @Override
        public String getLabel() {
            return label;
        }

        @Override
        public void setLabel(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return "PartElement{" +
                    "referenceElement=" + referenceElement +
                    ", element=" + element +
                    ", x=" + x +
                    ", y=" + y +
                    ", label='" + label + '\'' +
                    ", x=" + x +
                    ", y=" + y +
                    ", label='" + label + '\'' +
                    '}';
        }
    }

    class ClassElement extends DiagramElement {
        protected Class theClass;
        protected PartElement partAfter;

        protected File[] files = new File[]{new File(".")};
        protected String tmpFilename = tempDir +
                File.separator + "temp-" + getUuid() + ".jpg";
        private ProcessFile instance;

        public ClassElement() {
            x = getWidth() / 2;
            y = getHeight() / 2;
            partAfter = new PartElement();
            partAfter.setReferenceElement(this);
            partElements.add(partAfter);
        }

        public Class getTheClass() {
            return theClass;
        }

        public void setTheClass(Class theClass) {
            this.theClass = theClass;
        }

        public PartElement getPartAfter() {
            return partAfter;
        }

        public void setPartAfter(PartElement partAfter) {
            this.partAfter = partAfter;
        }

        public void setInputFiles(File[] filesP) {
            this.files = filesP;
        }

        @Override
        public String toString() {
            return "ClassElement{" +
                    "theClass=" + theClass +
                    ", partAfter=" + partAfter +
                    ", files=" + (files == null ? "" : Arrays.toString(files)) +
                    ", tmpFilename='" + tmpFilename + '\'' +
                    ", x=" + x +
                    ", y=" + y +
                    ", label='" + label + '\'' +
                    '}';
        }

        public ProcessFile getInstance() {
            if (instance == null) {
                try {
                    instance = (ProcessFile) theClass
                            .getDeclaredConstructor().newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
            return instance;
        }
    }

    class ClassMultiInputElement extends ClassElement {

        @Override
        public String toString() {
            return super.toString();
        }
    }

    class Then extends DiagramElement {
        ClassElement a, b;

        public Then() {
            x = getWidth() / 2;
            y = getHeight() / 2;
        }

        public ClassElement getA() {
            return a;
        }

        public void setA(ClassElement a) {
            this.a = a;
        }

        public ClassElement getB() {
            return b;
        }

        public void setB(ClassElement b) {
            this.b = b;
        }


        @Override
        public String toString() {
            return "Then{" +
                    "x=" + x +
                    ", y=" + y +
                    ", label='" + label + '\'' +
                    ", a=" + a +
                    ", b=" + b +
                    '}';
        }
    }

    private List<DiagramElement> diagramElements = new ArrayList<DiagramElement>();
    private List<PartElement> partElements = new ArrayList<PartElement>();

    ArrayList<ProcessFile> listProcessClasses = new ArrayList<>();

    public ClassSchemaBuilder() {
        initComponents();

        addMouseMotionListener(new MouseMotionAdapter() {
            private java.awt.Point point;
            private DiagramElement element;
            private java.awt.Point source;
            private boolean isMouseDragged = false;

            @Override
            public void mouseDragged(MouseEvent e) {
                if (!isMouseDragged && e.getButton() == 0 && (selectedElement == selectFromPoint(e.getX(), e.getY())
                        || (selectedPart == selectPartFromPoint(e.getX(), e.getY()) || selectedPart == null))
                ) {
                    source = e.getPoint();
                    element = selectFromPoint(source.x, source.y);
                    if (element != null) {
                        labelStatus.setText("Drag element: " + element.label);
                        selectedElement = element;
                    }

                }
                point = e.getPoint();
                if (!isMouseDragged && e.getButton() == 0)
                    isMouseDragged = true;
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (isMouseDragged && element != null && point != null && e.getButton() == 0) {
                    element.moveTo(point.x, point.y);
                    PartElement partElement = selectPartFromPoint(e.getX(), e.getY());
                    isMouseDragged = false;
                    element = null;
                }
                isMouseDragged = false;
            }
        });

        addMouseListener(new MouseListener() {
            private static final int ADD_1 = 1;
            private static final int ADD_2 = 2;
            private int currentAction = 0;

            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    if (selectAndClickClass) {
                        DiagramElement diagramElement = selectFromPoint(e.getX(), e.getY());
                        if (diagramElement instanceof ClassElement) {
                            ((ClassElement) diagramElement).setInputFiles(files.get(files.size() - 1));
                        }
                        selectAndClickClass = false;
                    } else if (selectedActionDeleteClass) {
                        DiagramElement diagramElement = selectFromPoint(e.getX(), e.getY());
                        partElements.remove(((ClassElement) diagramElement).partAfter);
                        ((ClassElement) diagramElement).partAfter = new PartElement();
                        diagramElements.remove(diagramElement);
                        buttonDeleteClass.setSelected(false);
                        selectedActionDeleteClass = false;
                    } else if (selectedActionDeleteLink) {
                        DiagramElement diagramElement = selectFromPoint(e.getX(), e.getY());
                        PartElement partElement = selectPartFromPoint(e.getX(), e.getY());
                        if (diagramElement instanceof ClassElement && partElement != null && partElement instanceof PartElement) {
                            ((ClassElement) (diagramElement)).partAfter.referenceElement = null;
                            ((ClassElement) (diagramElement)).partAfter.element = null;
                            buttonDeleteLink.setSelected(false);
                            selectedActionDeleteLink = false;
                            currentAction = 0;
                        }
                    } else if (selectsAddLink) {
                        if (currentAction == 0) {
                            if (selectFromPoint(e.getX(), e.getY()) != null &&
                                    selectFromPoint(e.getX(), e.getY()).getClass().equals(ClassElement.class)) {
                                selectedElement = selectFromPoint(e.getX(), e.getY());
                                selectedPart = selectPartFromPoint(e.getX(), e.getY());
                                currentAction = ADD_1;
                                labelStatus.setText("Add link: Selected class : " + selectedElement.label + " Select class2 ...");
                            }
                        } else if (currentAction == ADD_1) {
                            DiagramElement diagramElement = selectFromPoint(e.getX(), e.getY());
                            if (diagramElement instanceof ClassElement && selectedElement instanceof ClassElement) {
                                if (((ClassElement) diagramElement).partAfter.element != null &&
                                        ((ClassElement) diagramElement).partAfter.element.equals(selectedClassElement)) {
                                    ((ClassElement) diagramElement).partAfter.element = null;
                                }
                                ((ClassElement) selectedElement).partAfter.referenceElement = diagramElement;
                                ((ClassElement) selectedElement).partAfter.element = diagramElement;
                                currentAction = 0;
                                labelStatus.setText("LINK ADDED : Selected element: " + selectedElement.label + " Class 2 element:" + diagramElement.label);
                                selectsAddLink = false;
                                buttonAddLink.setSelected(false);
                            }


                        }
                    }
                } catch (NullPointerException ex) {
                    System.out.printf("Error : null");
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        Thread thread = new Thread(() -> {
            while (true) {
                drawAllElements();
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();


        setWebcamFile();
    }

    private static UUID getUuid() {
        return uuid;
    }


    private void buttonThenActionPerformed(ActionEvent e) {
        diagramElements.add(new Then());
    }

    private void buttonAddToActionPerformed(ActionEvent e) {
        ClassElement classElement = new ClassElement();
        classElement.theClass = comboBox1.getSelectedItem().getClass();
        diagramElements.add(classElement);
    }

    private void buttonAdd2filters(ActionEvent e) {
        ClassElement classElement = new ClassElement();
        classElement.theClass = comboBox2.getSelectedItem().getClass();
        diagramElements.add(classElement);
    }

    private void buttonFilesActionPerformed(ActionEvent e) {
        ListFilesElement listFilesElement = new ListFilesElement();
        FileDialog fileDialog = new FileDialog(this, "Select file to open/save", FileDialog.LOAD);
        //fileDialog.setLayout(new MigLayout());
        fileDialog.setMultipleMode(true);
        fileDialog.setVisible(true);
        if ((fileDialog.getFiles()) != null) {
            listFilesElement.setFiles(fileDialog.getFiles());
            files.add(fileDialog.getFiles());
        }
        diagramElements.add(listFilesElement);
        selectAndClickClass = true;
    }

    public void setMaxRes(int maxRes) {
        this.maxRes = maxRes;
    }

    public int getMaxRes() {
        return maxRes;
    }

    public void buttonGOActionPerformed(ActionEvent e) {
        processed = false;
        System.out.printf("Run processes\n");
        System.out.printf("Check stack of processes (linked)\n");
        System.out.printf("Check inputs (images files jpg mp4 decoder)\n");
        System.out.printf("Check outputs (folders overwrite/change folder)\n");
        System.out.printf("Run on images\n");

        // select class which have no predecessor and have files[] attached.
        List<ProcessFile> processes = new ArrayList<>();
        for (DiagramElement classElement : diagramElements) {
            if (classElement instanceof ClassElement) {
                ClassElement ce = (ClassElement) classElement;
                ClassElement first = ce;
                ClassElement last = ce;
                Class theClass = ce.getTheClass();
                processes.add(ce.getInstance());

                if (ce.partAfter.element != null && ce.partAfter.element != ce) {
                    if (ProcessFile.class.isAssignableFrom(theClass)) {
                        System.out.printf("Start process stack\n");
                        int n = 1;
                        while (ce.partAfter.element != null && ce.partAfter.element != ce) {
                            ce = (ClassElement) ce.partAfter.element;
                            processes.add(ce.getInstance());
                            last.getInstance().getProperties().sharePropertiesWith(ce.instance.getProperties());

                            last = ce;

                            processes.get(processes.size() - 1).setMaxRes(maxRes);
                        }
                    }
                }
                last = ce;
            }
        }
        //ResourceBundle globalSettings = ResourceBundle.getBundle("settings.properties");
        Properties globalSettings = new Properties();
        try {
            if (!new File("settings.properties").exists()) {
                new File("settings.properties").createNewFile();
                if (new File("settings.properties").canWrite()) {
                    FileOutputStream fileOutputStream = new FileOutputStream(new File("settings.properties"));
                    fileOutputStream.write("tempDir=temp/".getBytes());
                } else {
                    Logger.getAnonymousLogger().log(Level.WARNING, "Cannot write to settings.properties file");
                }
            }
            globalSettings.load(new FileInputStream("settings.properties"));
        } catch (Exception ex) {

            ex.printStackTrace();
        }
        if (tempDir == null)
            tempDir = "temp/";
        new File(tempDir).mkdirs();
        if (files == null) {
            System.out.printf("Error first.files==null");
        }


        for (File[] files : this.files) {
            for (File f : files) {
                System.out.printf("Run %d processes on file%s\n", processes.size(), f.getAbsolutePath());
                if (f.getName().contains("webcam")) {
                    //f = getWebcamFile();
                }
                if (!processes.isEmpty() && f != null) {
                    int nunEffect = 0;
                    ProcessFile ce = processes.get(0);
                    int processNameOrder = listProcessClasses.indexOf(ce.getClass());
                    StringBuilder s = new StringBuilder();
                    s.append("-").append(listProcessClasses.indexOf(ce)).append(getUuid());
                    String s0 = "";
                    fileOut = new File((tempDir + File.separator + f.getName() + s.toString()).replace('.', '-') + ".jpg");
                    if (!fileOut.exists()) {
                        getParentFile(fileOut).mkdirs();
                    }

                    System.out.printf("Process %s \nfrom:  %s\n", processes.get(0).getClass().toString(), f.getAbsolutePath());
                    System.out.printf("Process %s \nto  :  %s\n", processes.get(0).getClass().toString(), fileOut.getAbsolutePath());

                    System.out.printf("Run process %d / %d on file %s\n", nunEffect, processes.size(), f.getAbsolutePath());

                    File fileIn = null;


                    if (ce.process(f, fileOut)) {
                        nunEffect++;
                        if (fileOut.exists() && nunEffect == 1) {
                            liveEffect.setFileIn(fileOut);
                            Logger.getAnonymousLogger().log(Level.SEVERE, "All processes run on Image");
                        } else if (!fileOut.exists()) {
                            Logger.getAnonymousLogger().log(Level.SEVERE, "FileOut doesn't exist. Can't read");
                        } else {
                            System.out.printf("process %d / %d HAS RUN\n", nunEffect - 1, processes.size(), f.getAbsolutePath());

                        }
                        Logger.getAnonymousLogger().log(Level.INFO, "Fichier IN mis à jour");
                        fileIn = fileOut;
                        if (processes.size() > 1) {
                            fileOut = new File((tempDir + File.separator + f.getName() + s.toString()).replace('.', '-') + ".jpg");
                        }

                        for (int i = 1; i < processes.size(); i++) {
                            ce = processes.get(i);
                            s0 = s.toString();
                            s.append("-").append(listProcessClasses.indexOf(ce)).append(getUuid());
                            System.out.printf("Process %s \nfrom:  %s\n", ce.getClass().toString(), f.getName());


                            ce.addSource(f);//???
                            ce.addSource(fileIn);//???
                            try {
                              /*  if (!fileOut.exists()) {
                                    fileOut.mkdirs();
                                }*/
                                nunEffect++;
                                getParentFile(fileOut).mkdirs();
                                if (ce.process(fileIn, fileOut)) {
                                    if (fileOut.exists() && nunEffect == processes.size()) {
                                        liveEffect.setFileIn(fileOut);
                                        Logger.getAnonymousLogger().log(Level.SEVERE, "All processes run on Image");
                                    } else if (!fileOut.exists()) {
                                        Logger.getAnonymousLogger().log(Level.SEVERE, "FileOut doesn't exist. Can't read");
                                    } else {
                                        System.out.printf("process %d / %d HAS RUN\n", nunEffect - 1, processes.size(), f.getAbsolutePath());

                                    }
                                    Logger.getAnonymousLogger().log(Level.INFO, "Fichier IN mis à jour");
                                    if (fileOut.exists() && nunEffect == processes.size()) {
                                        if (fileOut.exists() && nunEffect == processes.size()) {
                                            liveEffect.setFileIn(fileOut);
                                        } else {
                                            Logger.getAnonymousLogger().log(Level.SEVERE, "fileOut doesn't exist. Can't read");
                                        }
                                        System.out.printf("Run procsqess 1 . %d/%d on file %s\n", nunEffect, processes.size(), f.getAbsolutePath());
                                    } else {
                                        Logger.getAnonymousLogger().log(Level.SEVERE, "fileOut doesn't exist. Can't read");
                                    }

                                    fileIn = fileOut;
                                    if (processes.size() > nunEffect) {
                                        fileOut = new File((tempDir + File.separator + f.getName() + s.toString()).replace('.', '-') + ".jpg");
                                    }
                                    Logger.getAnonymousLogger().log(Level.INFO, "Fichier IN mis à jour");
                                }
                            } catch (RuntimeException ex) {
                                ex.printStackTrace();
                            }

                            if (!getParentFile(fileOut).exists()) {
                                getParentFile(fileOut).mkdirs();
                            }
                        }
                    } else {
                        Logger.getAnonymousLogger().log(Level.SEVERE, "Error in process : " + processes.get(nunEffect).getClass().getCanonicalName());
                    }
                }
                if (fileOut != null && fileOut.exists()) {
                    //liveEffect.setFileIn(fileOut);
                } else {
                    //Logger.getAnonymousLogger().log(Level.SEVERE, "fileOut doesn't exist. Can't read or null");
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
/*        if(fileOut!=null) {
            Logger.getAnonymousLogger().log(Level.INFO, "fileOut : " + fileOut.getAbsolutePath());
            Logger.getAnonymousLogger().log(Level.INFO, "Exists? : " + fileOut.exists());
            if (fileOut.exists()) {
                //liveEffect.setFileIn(fileOut);
                processed = true;
            } else {
                System.err.println("Le fichier fileOut n'existe pas");
            }
        } else {
            System.err.println("Le fichier fileOut est null");
        }*/
    }

    public static File getParentFile(File file) {
        return new File(file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf("/") + 1));
    }

    private void buttonDeleteClassActionPerformed(ActionEvent e) {
        selectedActionDeleteClass = ((JToggleButton) e.getSource()).isSelected();
    }

    private void buttonDeleteLinkActionPerformed(ActionEvent e) {
        selectedActionDeleteLink = ((JToggleButton) e.getSource()).isSelected();

    }

    private void buttonAddLinkActionPerformed(ActionEvent e) {
        //buttonAddLink.setSelected(!buttonAddLink.isSelected());
        if (buttonAddLink.isSelected()) {
            selectsAddLink = true;
            labelStatus.setText("Method: Use click on class1, then class2 ");
        } else if (!buttonAddLink.isSelected()) {
            selectsAddLink = false;
            labelStatus.setText("Do nothing");
        }
    }

    private void buttonLoadActionPerformed(ActionEvent e) {
        String[] currentDirin = null;
        String server = "";
        int port = 0;
        String username = "";
        String password = "";
        String[] classnamesArr = null;
        JFileChooser jFileChooser = new JFileChooser(directory);

        jFileChooser.setMultiSelectionEnabled(false);
        if (JFileChooser.APPROVE_OPTION == jFileChooser.showOpenDialog(this)) {
            File selectedFile = jFileChooser.getSelectedFile();
            Properties appFile = new Properties();
            if (selectedFile.getName().endsWith(".properties")) {
                try {
                    appFile.load(new FileInputStream(selectedFile));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                currentDirin = (appFile.getProperty("in.directory")).split(",");
                if ("local".equals(appFile.getProperty("in.device"))) {
                    server = "file";
                    port = 0;
                    username = "";
                    password = "";
                } else {
                    server = (String) appFile.getProperty("host");
                    port = Integer.parseInt(appFile.getProperty("port"));
                    username = (String) appFile.getProperty("username");
                    password = (String) appFile.getProperty("password");
                }


                String maxFilesInDir0 = appFile.getProperty("maxFilesInDir");

                String maxResStr = appFile.getProperty("maxRes");
                if (maxResStr != null)
                    maxRes = Integer.parseInt(maxResStr);

                /* String*/
                String classnames = (String) appFile.getProperty("classname");
                String class0 = (String) appFile.getProperty("class0");
                String directoryOut = (String) appFile.getProperty("out.directory");


                String sep = "";
                int i = 0;
                if (class0 == null || class0.equals("")) {
                    sep = "";
                }
                classnamesArr = classnames.split(",");

                int j0 = diagramElements.size();
                int j = j0;
                for (i = 0; i < classnamesArr.length; i++) {
                    try {
                        ClassElement classElement = new ClassElement();
                        diagramElements.add(classElement);
                        classElement.theClass = Class.forName(classnamesArr[i]);
                        j++;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                for (i = j0; i < j0 + classnamesArr.length - 1; i++) {
                    ((ClassElement) (diagramElements.get(i))).partAfter.element = diagramElements.get(i + 1);
                    ((ClassElement) (diagramElements.get(i))).partAfter.referenceElement
                            = diagramElements.get(i);
                }
            } else {
                // Load Java Data from toString.
                try {
                    ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(selectedFile));

                    Date date = (Date) objectInputStream.readObject();
                    ClassSchemaBuilder c = new ClassSchemaBuilder();
                    c.diagramElements = (List<DiagramElement>) objectInputStream.readObject();
                    c.files = (List<File[]>) objectInputStream.readObject();
                    c.partElements = (List<PartElement>) objectInputStream.readObject();
                    c.listProcessClasses = (ArrayList<ProcessFile>) objectInputStream.readObject();
                    c.setVisible(true);


                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        }

    }

    private void buttonSaveActionPerformed(ActionEvent e) {

        JFileChooser jFileChooser = new JFileChooser(directory);

        jFileChooser.setMultiSelectionEnabled(false);
        if (JFileChooser.APPROVE_OPTION == jFileChooser.showOpenDialog(this)) {
            File selectedFile = jFileChooser.getSelectedFile();
            ObjectOutputStream oos = null;
            try {
                FileOutputStream fichier = new FileOutputStream(selectedFile);
                oos = new ObjectOutputStream(fichier);
                oos.writeObject(new Date());
                oos.writeObject(this.diagramElements);
                oos.writeObject(this.files);
                oos.writeObject(this.partElements);
                oos.writeObject(this.listProcessClasses);

                oos.flush();
            } catch (final IOException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (oos != null) {
                        oos.flush();
                        oos.close();
                    }
                } catch (final IOException ex) {
                    ex.printStackTrace();
                }
            }

        }
    }

    private void buttonCamActionPerformed(ActionEvent e) {
        cam = ((JToggleButton) e.getSource()).isSelected();
        if (liveEffect == null) {
            liveEffect = new LiveEffect();
        }
        liveEffect.setVisible(true);

        liveEffect.setVisible(cam);
        liveEffect.setMainWindow(this);
        /*if (cam)
            if (!direstEffect.threadEffectDisplay.busy && direstEffect.threadEffectDisplay.isAlive())
                direstEffect.threadEffectDisplay.start();
                +/
         */

        liveEffect.setVisible(true);
    }

    private void buttonPictureRecodeActionPerformed(ActionEvent e) {
        JFileChooser jFileChooser = new JFileChooser(fileChooserDir);

        jFileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return Arrays.stream(ImageIO.getReaderFormatNames()).anyMatch(s -> f.toPath().endsWith(s.substring(s.lastIndexOf('.'))));
            }

            @Override
            public String getDescription() {
                return "All images known types";
            }
        });


        jFileChooser.setMultiSelectionEnabled(true);

        jFileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        jFileChooser.showOpenDialog(this);

        final File[] selectedFiles = jFileChooser.getSelectedFiles();

        if (selectedFiles != null) {
            //for (int i = 0; i < selectedFiles.length; i++) {
            //File selectedFile = selectedFiles[i];
            this.files.add(selectedFiles);
            buttonGOActionPerformed(null);
            //}
        } else {
            Logger.getAnonymousLogger().log(Level.INFO, "Nothing chosen");
        }
    }

    private void button3ActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    private void buttonParameters(ActionEvent e) {
        one.empty3.apps.feature.DialogMultFrames dialogMultFrames = new one.empty3.apps.feature.DialogMultFrames(this);
        dialogMultFrames.setVisible(true);
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        comboBox1 = new JComboBox();
        button2 = new JButton();
        comboBox2 = new JComboBox();
        button3 = new JButton();
        buttonLoad = new JButton();
        button1 = new JButton();
        buttonPictureRecode = new JButton();
        buttonCam = new JToggleButton();
        buttonSave = new JButton();
        panel1 = new JPanel();
        labelStatus = new JLabel();
        buttonAddLink = new JToggleButton();
        buttonFiles = new JButton();
        buttonDeleteClass = new JToggleButton();
        buttonDeleteLink = new JToggleButton();
        buttonGO = new JButton();
        button4 = new JButton();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Filter stack constructor");
        setVisible(true);
        var contentPane = getContentPane();
        contentPane.setLayout(new MigLayout(
                "insets 0,hidemode 3,gap 5 5",
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
                        "[fill]",
                // rows
                "[]" +
                        "[fill]" +
                        "[]" +
                        "[fill]" +
                        "[fill]" +
                        "[]" +
                        "[]"));

        //---- comboBox1 ----
        comboBox1.setMaximumRowCount(20);
        comboBox1.setAutoscrolls(true);
        comboBox1.setLightWeightPopupEnabled(false);
        comboBox1.setDoubleBuffered(true);
        comboBox1.setForeground(new Color(0x6666ff));
        contentPane.add(comboBox1, "cell 0 1");

        //---- button2 ----
        button2.setText("Add");
        button2.setForeground(new Color(0x6666ff));
        button2.addActionListener(e -> buttonAddToActionPerformed(e));
        contentPane.add(button2, "cell 0 1");

        //---- comboBox2 ----
        comboBox2.setForeground(new Color(0x6666ff));
        contentPane.add(comboBox2, "cell 0 1");

        //---- button3 ----
        button3.setText("Add + filter");
        button3.setForeground(new Color(0x6666ff));
        button3.addActionListener(e -> buttonAdd2filters(e));
        contentPane.add(button3, "cell 0 1");

        //---- buttonLoad ----
        buttonLoad.setText("Load");
        buttonLoad.setBackground(Color.green);
        buttonLoad.addActionListener(e -> buttonLoadActionPerformed(e));
        contentPane.add(buttonLoad, "cell 1 1");

        //---- button1 ----
        button1.setText("Open movie/recode");
        button1.setBackground(Color.green);
        contentPane.add(button1, "cell 0 2");

        //---- buttonPictureRecode ----
        buttonPictureRecode.setText("Open picture/recode");
        buttonPictureRecode.setBackground(Color.green);
        buttonPictureRecode.addActionListener(e -> buttonPictureRecodeActionPerformed(e));
        contentPane.add(buttonPictureRecode, "cell 0 2");

        //---- buttonCam ----
        buttonCam.setText("Open webcam");
        buttonCam.setBackground(new Color(0x0066cc));
        buttonCam.addActionListener(e -> buttonCamActionPerformed(e));
        contentPane.add(buttonCam, "cell 0 2");

        //---- buttonSave ----
        buttonSave.setText("Save");
        buttonSave.setBackground(Color.green);
        buttonSave.addActionListener(e -> buttonSaveActionPerformed(e));
        contentPane.add(buttonSave, "cell 1 2");

        //======== panel1 ========
        {
            panel1.setLayout(new MigLayout(
                    "hidemode 3",
                    // columns
                    "[fill]" +
                            "[fill]",
                    // rows
                    "[]" +
                            "[]" +
                            "[]"));
        }
        contentPane.add(panel1, "cell 0 3 11 2,dock center");

        //---- labelStatus ----
        labelStatus.setText("Label Status");
        labelStatus.setForeground(new Color(0x6666ff));
        contentPane.add(labelStatus, "cell 0 5");

        //---- buttonAddLink ----
        buttonAddLink.setText("Add link");
        buttonAddLink.setBackground(Color.green);
        buttonAddLink.addActionListener(e -> buttonAddLinkActionPerformed(e));
        contentPane.add(buttonAddLink, "cell 0 6");

        //---- buttonFiles ----
        buttonFiles.setText("Add input files");
        buttonFiles.setBackground(Color.green);
        buttonFiles.addActionListener(e -> buttonFilesActionPerformed(e));
        contentPane.add(buttonFiles, "cell 0 6");

        //---- buttonDeleteClass ----
        buttonDeleteClass.setText("Delete class");
        buttonDeleteClass.setBackground(Color.green);
        buttonDeleteClass.addActionListener(e -> buttonDeleteClassActionPerformed(e));
        contentPane.add(buttonDeleteClass, "cell 0 6");

        //---- buttonDeleteLink ----
        buttonDeleteLink.setText("Delete link");
        buttonDeleteLink.setBackground(Color.green);
        buttonDeleteLink.addActionListener(e -> buttonDeleteLinkActionPerformed(e));
        contentPane.add(buttonDeleteLink, "cell 0 6");

        //---- buttonGO ----
        buttonGO.setText("Process GO");
        buttonGO.setBackground(Color.green);
        buttonGO.addActionListener(e -> buttonGOActionPerformed(e));
        contentPane.add(buttonGO, "cell 1 6");

        //---- button4 ----
        button4.setText("Properties");
        button4.setBackground(Color.green);
        button4.addActionListener(e -> buttonParameters(e));
        contentPane.add(button4, "cell 2 6");
        setSize(1225, 540);
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents

        try {
            listProcessClasses.add(one.empty3.apps.feature.Classification.class.newInstance());
            listProcessClasses.add(one.empty3.apps.feature.CornerDetectProcess.class.newInstance());
            listProcessClasses.add(one.empty3.apps.feature.CurveFitting.class.newInstance());
            listProcessClasses.add(one.empty3.apps.feature.DBScanProcess.class.newInstance());
            listProcessClasses.add(DericheFilterProcess.class.newInstance());
            listProcessClasses.add(one.empty3.apps.feature.Draw.class.newInstance());
            listProcessClasses.add(one.empty3.apps.feature.ExtremaProcess.class.newInstance());
            listProcessClasses.add(GaussFilterProcess.class.newInstance());
            listProcessClasses.add(one.empty3.apps.feature.GradProcess.class.newInstance());
            listProcessClasses.add(one.empty3.apps.feature.HarrisProcess.class.newInstance());
            listProcessClasses.add(Histogram.class.newInstance());
            listProcessClasses.add(Histogram1.class.newInstance());
            listProcessClasses.add(Hist4Contour.class.newInstance());
            listProcessClasses.add(one.empty3.apps.feature.histograms.Hist4Contour2.class.newInstance());
            listProcessClasses.add(Hist1Votes.class.newInstance());
            listProcessClasses.add(Hist4Contour3.class.newInstance());
            listProcessClasses.add(one.empty3.apps.feature.Histogram0.class.newInstance());
            listProcessClasses.add(one.empty3.apps.feature.Histogram2.class.newInstance());
            listProcessClasses.add(one.empty3.apps.feature.Histogram3.class.newInstance());
            listProcessClasses.add(one.empty3.apps.feature.HoughTransform.class.newInstance());
            listProcessClasses.add(HoughTransformCircle.class.newInstance());
            listProcessClasses.add(one.empty3.apps.feature.IdentNullProcess.class.newInstance());
            listProcessClasses.add(one.empty3.apps.feature.IsleProcess.class.newInstance());
            listProcessClasses.add(one.empty3.apps.feature.KMeans.class.newInstance());
            listProcessClasses.add(one.empty3.apps.feature.Lines.class.newInstance());
            listProcessClasses.add(one.empty3.apps.feature.Lines3.class.newInstance());
            listProcessClasses.add(one.empty3.apps.feature.Lines4.class.newInstance());
            listProcessClasses.add(one.empty3.apps.feature.Lines5.class.newInstance());
            listProcessClasses.add(one.empty3.apps.feature.Lines5colors.class.newInstance());
            listProcessClasses.add(one.empty3.apps.feature.Lines6.class.newInstance());
            listProcessClasses.add(one.empty3.apps.feature.LocalExtremaProcess.class.newInstance());
            listProcessClasses.add(one.empty3.apps.feature.MagnitudeProcess.class.newInstance());
            listProcessClasses.add(one.empty3.apps.feature.ProxyValue.class.newInstance());
            listProcessClasses.add(one.empty3.apps.feature.ProxyValue2.class.newInstance());
            listProcessClasses.add(one.empty3.apps.feature.RegionLineCorner.class.newInstance());
            listProcessClasses.add(SelectColor.class.newInstance());
            listProcessClasses.add(one.empty3.apps.feature.Transform1.class.newInstance());
            listProcessClasses.add(one.empty3.apps.feature.TrueHarrisProcess.class.newInstance());
            listProcessClasses.add(one.empty3.apps.feature.Voronoi.class.newInstance());
            listProcessClasses.add(HighlightFeatures.class.newInstance());
            listProcessClasses.add(RunFeatures.class.newInstance());
            //listProcessClasses.add(GFG.class.newInstance());
            listProcessClasses.add(Hist4Contour2.class.newInstance());
            listProcessClasses.add(CustomProcessFileRGB.class.newInstance());
            listProcessClasses.add(KMeansBinaryDistances.class.newInstance());
            listProcessClasses.forEach(new Consumer<ProcessFile>() {
                @Override
                public void accept(ProcessFile processFile) {
                    comboBox1.addItem(processFile);
                }
            });
            ArrayList<ProcessNFiles> listProcessClasses2process = new ArrayList<ProcessNFiles>();
            listProcessClasses2process.add(ProcessPlusNormalize.class.newInstance());

            listProcessClasses2process.forEach(new Consumer<ProcessNFiles>() {
                @Override
                public void accept(ProcessNFiles processsX2Process) {
                    comboBox2.addItem(processsX2Process);
                }
            });

        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void drawElement(Graphics g, DiagramElement diagramElement) {
        g.drawString(diagramElement.label, diagramElement.x, diagramElement.y);
        if (diagramElement instanceof ClassElement) {

            g.setColor(Color.WHITE);
            g.drawString(((ClassElement) diagramElement).theClass.getName(), diagramElement.x, diagramElement.y + 32);

        }
        if (diagramElement instanceof ClassElement) {
            if (((ClassElement) diagramElement).partAfter.element != null) {
                int x = ((ClassElement) diagramElement).partAfter.element.x;
                int y = ((ClassElement) diagramElement).partAfter.element.y;
                g.setColor(Color.GREEN);
                g.fillOval(diagramElement.x + 40, diagramElement.y + 40, 10, 10);
                g.setColor(Color.GRAY);
                g.drawLine(diagramElement.x + 40, diagramElement.y + 40, x, y);
            }
        }
    }

    public DiagramElement selectFromPoint(int x, int y) {
        double distance = 100000;
        DiagramElement diagramElementNear = null;
        for (DiagramElement diagramElement : diagramElements) {
            if (Point2D.dist(new Point2D(x, y), new Point2D(diagramElement.x, diagramElement.y)) < distance) {
                distance = Point2D.dist(new Point2D(x, y), new Point2D(diagramElement.x, diagramElement.y));
                diagramElementNear = diagramElement;
            }
        }
        return diagramElementNear;
    }


    private PartElement selectPartFromPoint(int x, int y) {
        double distance = 100000;
        DiagramElement diagramElementNear = selectFromPoint(x, y);
        if (selectedElement == diagramElementNear) {
            for (DiagramElement diagramElement : partElements) {
                if (diagramElement != null)
                    if (Point2D.dist(new Point2D(x, y), new Point2D(diagramElement.x, diagramElement.y)) < distance) {
                        for (PartElement partElement : partElements) {
                            DiagramElement diagramElement1 = ((PartElement) partElement).getReferenceElement();
                            if (diagramElement == diagramElement1) {
                                distance = Point2D.dist(new Point2D(x, y), new Point2D(diagramElement.x, diagramElement.y));
                                selectedElement = diagramElement;
                                selectedPart = partElement;
                            }
                        }
                    }
            }
        }
        return selectedPart;
    }

    public void drawAllElements() {
        try {
            trees();

            //Logger.getAnonymousLogger().log(Level.INFO, treeDiagram);
        } catch (Throwable throwable) {
            throwable.printStackTrace(System.out);
            //throw throwable;
        }
        BufferedImage bi = new BufferedImage(panel1.getWidth(), panel1.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = bi.getGraphics();

        g.setColor(java.awt.Color.BLACK);
        g.fillRect(0, 0, panel1.getWidth(), panel1.getHeight());


        try {
            Point location = MouseInfo.getPointerInfo().getLocation();
            DiagramElement selected = selectFromPoint(location.x, location.y);
            for (DiagramElement diagramElement : diagramElements) {
                g.setColor(Color.WHITE);
                if (diagramElement == selected)
                    g.setColor(Color.GREEN);
                else if (selectedElement == diagramElement)
                    g.setColor(Color.BLUE);
                drawElement(g, diagramElement);
            }


            Graphics graphics = panel1.getGraphics();
            graphics.drawImage(bi, 0, 0, panel1.getWidth(), panel1.getHeight(), this);

        } catch (NullPointerException ex) {

        }
    }

    private void trees() {
        treeDiagram = new TreeDiagram(diagramElements);


        if (treeDiagram.head != null) {
            ArrayList<one.empty3.apps.feature.TreeNodeDiagram> diagramElements2 = new ArrayList<>();


            List<one.empty3.apps.feature.TreeNodeDiagram> explore = new ArrayList<>();

            List<TreeNodeDiagram> leaves = new ArrayList<>();

            treeDiagram.head.searchForLeaves(leaves);

            leaves.forEach(treeNodeDiagram -> {
                DiagramElement element = treeNodeDiagram.getElement();
                if (element instanceof ClassElement) {
                    treeNodeDiagram.setFile(((ClassElement) (element)).files[0]);
                }
            });
        }
    }

    public void addClassToSchema() {
        ClassElement classElement = new ClassElement();
        diagramElements.add(classElement);
    }

    public void addThenToSchema() {
        Then thenElement = new Then();
        diagramElements.add(thenElement);
    }

    public static void main(String[] args) {
        new ClassSchemaBuilder().setVisible(true);
    }


    @Override
    public String toString() {
        List<File> f = new ArrayList<>();
        for (File[] files2 : files) {
            Collections.addAll(f, files2);
        }
        return "ClassSchemaBuilder{" +
                "selectedPart=" + selectedPart +
                ", directory='" + directory + '\'' +
                ", files2=" + f +
                ", selectedElement=" + selectedElement +
                ", selectedArrow=" + selectedArrow +
                ", diagramElements=" + diagramElements +
                ", partElements=" + partElements +
                ", listProcessClasses=" + listProcessClasses +
                '}';
    }

    private JButton buttonStop;
    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JComboBox comboBox1;
    private JButton button2;
    private JComboBox comboBox2;
    private JButton button3;
    private JButton buttonLoad;
    private JButton button1;
    private JButton buttonPictureRecode;
    private JToggleButton buttonCam;
    private JButton buttonSave;
    private JPanel panel1;
    private JLabel labelStatus;
    private JToggleButton buttonAddLink;
    private JButton buttonFiles;
    private JToggleButton buttonDeleteClass;
    private JToggleButton buttonDeleteLink;
    private JButton buttonGO;
    private JButton button4;
    // JFormDesigner - End of variables declaration  //GEN-END:variables


    public List<DiagramElement> getDiagramElements() {
        return diagramElements;
    }

    public void setDiagramElements(List<DiagramElement> diagramElements) {
        this.diagramElements = diagramElements;
    }
}
