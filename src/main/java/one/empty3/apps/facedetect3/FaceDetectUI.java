/*
 *
 *  *
 *  *  * Copyright (c) 2026. Manuel Daniel Dahmen
 *  *  *
 *  *  *
 *  *  *    Copyright 2026 Manuel Daniel Dahmen
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
package one.empty3.apps.facedetect3;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.io.*;
import java.util.Scanner;
import one.empty3.library.Point3D;

public class FaceDetectUI extends JFrame {

    private JPanel mainPanel;
    private ImagePanel imagePanel1;
    private ImagePanel imagePanel2;
    private JTable pointMatchTable;
    private PointMatchTableModel pointMatchTableModel;
    private File lastDirectory;
    private ImagePanel lastClickedPanel;
    private PanelType leftPanelType = PanelType.IMAGE;
    private PanelType rightPanelType = PanelType.IMAGE;
    private MasksRenderer masksRenderer;

    public FaceDetectUI() {
        setTitle("FaceDetect3");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        masksRenderer = new MasksRenderer(this);

        mainPanel = new JPanel(new BorderLayout());

        JToolBar navBar = new JToolBar("3D Navigation");
        JButton zoomInBtn = new JButton("Zoom +");
        JButton zoomOutBtn = new JButton("Zoom -");
        JButton rotXPlusBtn = new JButton("Rot X+");
        JButton rotXMinusBtn = new JButton("Rot X-");
        JButton rotYPlusBtn = new JButton("Rot Y+");
        JButton rotYMinusBtn = new JButton("Rot Y-");
        JButton rotZPlusBtn = new JButton("Rot Z+");
        JButton rotZMinusBtn = new JButton("Rot Z-");
        JButton transXPlusBtn = new JButton("Trans X+");
        JButton transXMinusBtn = new JButton("Trans X-");
        JButton transYPlusBtn = new JButton("Trans Y+");
        JButton transYMinusBtn = new JButton("Trans Y-");
        JButton transZPlusBtn = new JButton("Trans Z+");
        JButton transZMinusBtn = new JButton("Trans Z-");
        JButton resetBtn = new JButton("Reset View");

        JCheckBox quickViewBox = new JCheckBox("Quick View", true);

        java.util.function.Consumer<java.util.function.Consumer<OBJModel>> applyToActiveModel = action -> {
            if (lastClickedPanel != null && lastClickedPanel.getPanelType() == PanelType.OBJ_MODEL) {
                OBJModel model = lastClickedPanel.getObjModel();
                if (model != null) {
                    action.accept(model);
                    ((ImagePanel) (lastClickedPanel)).setDisplayLines(quickViewBox.isSelected());
                    lastClickedPanel.repaint();
                }
            }
        };

        quickViewBox.addActionListener(e -> {
            boolean isQuick = quickViewBox.isSelected();
            if (imagePanel1 != null && imagePanel1.getPanelType() == PanelType.OBJ_MODEL) {
                imagePanel1.setDisplayLines(isQuick);
                imagePanel1.repaint();
            }
            if (imagePanel2 != null && imagePanel2.getPanelType() == PanelType.OBJ_MODEL) {
                imagePanel2.setDisplayLines(isQuick);
                imagePanel2.repaint();
            }
        });

        double rotAngle = Math.PI / 12;
        double transStep = 0.05;

        zoomInBtn.addActionListener(e -> applyToActiveModel.accept(OBJModel::zoomIn));
        zoomOutBtn.addActionListener(e -> applyToActiveModel.accept(OBJModel::zoomOut));
        rotXPlusBtn.addActionListener(e -> applyToActiveModel.accept(m -> m.rotateX(rotAngle)));
        rotXMinusBtn.addActionListener(e -> applyToActiveModel.accept(m -> m.rotateX(-rotAngle)));
        rotYPlusBtn.addActionListener(e -> applyToActiveModel.accept(m -> m.rotateY(rotAngle)));
        rotYMinusBtn.addActionListener(e -> applyToActiveModel.accept(m -> m.rotateY(-rotAngle)));
        rotZPlusBtn.addActionListener(e -> applyToActiveModel.accept(m -> m.rotateZ(rotAngle)));
        rotZMinusBtn.addActionListener(e -> applyToActiveModel.accept(m -> m.rotateZ(-rotAngle)));
        transXPlusBtn.addActionListener(e -> applyToActiveModel.accept(m -> m.translateView(transStep, 0, 0)));
        transXMinusBtn.addActionListener(e -> applyToActiveModel.accept(m -> m.translateView(-transStep, 0, 0)));
        transYPlusBtn.addActionListener(e -> applyToActiveModel.accept(m -> m.translateView(0, transStep, 0)));
        transYMinusBtn.addActionListener(e -> applyToActiveModel.accept(m -> m.translateView(0, -transStep, 0)));
        transZPlusBtn.addActionListener(e -> applyToActiveModel.accept(m -> m.translateView(0, 0, transStep)));
        transZMinusBtn.addActionListener(e -> applyToActiveModel.accept(m -> m.translateView(0, 0, -transStep)));
        resetBtn.addActionListener(e -> applyToActiveModel.accept(OBJModel::resetView));

        navBar.add(zoomInBtn);
        navBar.add(zoomOutBtn);
        navBar.addSeparator();
        navBar.add(rotXPlusBtn);
        navBar.add(rotXMinusBtn);
        navBar.add(rotYPlusBtn);
        navBar.add(rotYMinusBtn);
        navBar.add(rotZPlusBtn);
        navBar.add(rotZMinusBtn);
        navBar.addSeparator();
        navBar.add(transXPlusBtn);
        navBar.add(transXMinusBtn);
        navBar.add(transYPlusBtn);
        navBar.add(transYMinusBtn);
        navBar.add(transZPlusBtn);
        navBar.add(transZMinusBtn);
        navBar.addSeparator();
        navBar.add(resetBtn);
        navBar.addSeparator();
        navBar.add(quickViewBox);

        mainPanel.add(navBar, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        imagePanel1 = new ImagePanel(panel -> lastClickedPanel = panel, this);
        imagePanel2 = new ImagePanel(panel -> lastClickedPanel = panel, this);

        splitPane.setLeftComponent(imagePanel1);
        splitPane.setRightComponent(imagePanel2);

        imagePanel1.setBorder(BorderFactory.createTitledBorder("Image"));
        imagePanel2.setBorder(BorderFactory.createTitledBorder("Image"));

        pointMatchTableModel = new PointMatchTableModel();
        pointMatchTable = new JTable(pointMatchTableModel);
        JScrollPane tableScrollPane = new JScrollPane(pointMatchTable);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(tableScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Add");
        JButton modifyButton = new JButton("Modify");
        JButton deleteButton = new JButton("Delete");

        addButton.addActionListener(e -> {
            pointMatchTableModel
                    .addPointMatch(new PointMatch(null, null, "Point " + (pointMatchTableModel.getRowCount() + 1)));
            int lastRow = pointMatchTableModel.getRowCount() - 1;
            pointMatchTable.setRowSelectionInterval(lastRow, lastRow);
        });

        modifyButton.addActionListener(e -> {
            int selectedRow = pointMatchTable.getSelectedRow();
            if (selectedRow != -1) {
                String currentName = (String) pointMatchTableModel.getValueAt(selectedRow, 2);
                String newName = JOptionPane.showInputDialog(FaceDetectUI.this, "Enter new name for the point:",
                        currentName);
                if (newName != null) {
                    pointMatchTableModel.setValueAt(newName, selectedRow, 2);
                }
            } else {
                JOptionPane.showMessageDialog(FaceDetectUI.this, "Please select a point from the list to modify.",
                        "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = pointMatchTable.getSelectedRow();
            if (selectedRow != -1) {
                pointMatchTableModel.removePointMatch(selectedRow);
                imagePanel1.repaint();
                imagePanel2.repaint();
            } else {
                JOptionPane.showMessageDialog(FaceDetectUI.this, "Please select a point from the list to delete.",
                        "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(modifyButton);
        buttonPanel.add(deleteButton);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        pointMatchTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                imagePanel1.repaint();
                imagePanel2.repaint();
            }
        });

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        JMenu masksMenu = new JMenu("Masks");

        JMenuItem renderLocalItem = new JMenuItem("Render Local");
        renderLocalItem.addActionListener(e -> masksRenderer.render(false));
        masksMenu.add(renderLocalItem);

        JMenuItem renderRemoteItem = new JMenuItem("Render Remote (Cloud Run)");
        renderRemoteItem.addActionListener(e -> masksRenderer.render(true));
        masksMenu.add(renderRemoteItem);

        masksMenu.addSeparator();

        // Algorithm Submenu
        JMenu algoMenu = new JMenu("Select Algorithm");
        ButtonGroup algoGroup = new ButtonGroup();
        String[] algoNames = {
            "0: Disabled / Linear 1",
            "1: Mini face icon (Linear 2)",
            "2: Mini face icon 2 (Linear 3)",
            "3: Project into another point's set (Linear 4)",
            "4: Disabled / Linear 5",
            "5: Disabled / Linear 6",
            "6: Unknown (Linear 42)",
            "7: Face on face 1 (Linear 43)",
            "8: Face on face 2 (Linear 44_2 - Best)",
            "9: Linear projection (u,v)"
        };
        for (int i = 0; i < algoNames.length; i++) {
            final int algoIdx = i;
            JRadioButtonMenuItem item = new JRadioButtonMenuItem(algoNames[i], i == 8);
            item.addActionListener(e -> masksRenderer.setSelectedAlgorithm(algoIdx));
            algoGroup.add(item);
            algoMenu.add(item);
        }
        masksMenu.add(algoMenu);

        // Settings Submenu
        JMenu settingsMenu = new JMenu("Settings");
        JCheckBoxMenuItem hdTexturesItem = new JCheckBoxMenuItem("HD Textures", false);
        hdTexturesItem.addActionListener(e -> masksRenderer.setHdTextures(hdTexturesItem.isSelected()));
        settingsMenu.add(hdTexturesItem);

        JCheckBoxMenuItem bezierItem = new JCheckBoxMenuItem("Bezier Mode", false);
        bezierItem.addActionListener(e -> masksRenderer.setBezier(bezierItem.isSelected()));
        settingsMenu.add(bezierItem);

        masksMenu.add(settingsMenu);

        menuBar.add(masksMenu);

        JMenu leftPanelTypeMenu = new JMenu("Left Panel Type");
        ButtonGroup leftPanelGroup = new ButtonGroup();
        JRadioButtonMenuItem leftImageRadio = new JRadioButtonMenuItem("Image", true);
        leftImageRadio.addActionListener(e -> setPanelType(true, PanelType.IMAGE));
        leftPanelGroup.add(leftImageRadio);
        leftPanelTypeMenu.add(leftImageRadio);
        JRadioButtonMenuItem leftObjModelRadio = new JRadioButtonMenuItem("OBJ Model");
        leftObjModelRadio.addActionListener(e -> setPanelType(true, PanelType.OBJ_MODEL));
        leftPanelGroup.add(leftObjModelRadio);
        leftPanelTypeMenu.add(leftObjModelRadio);
        fileMenu.add(leftPanelTypeMenu);

        JMenu rightPanelTypeMenu = new JMenu("Right Panel Type");
        ButtonGroup rightPanelGroup = new ButtonGroup();
        JRadioButtonMenuItem rightImageRadio = new JRadioButtonMenuItem("Image", true);
        rightImageRadio.addActionListener(e -> setPanelType(false, PanelType.IMAGE));
        rightPanelGroup.add(rightImageRadio);
        rightPanelTypeMenu.add(rightImageRadio);
        JRadioButtonMenuItem rightObjModelRadio = new JRadioButtonMenuItem("OBJ Model");
        rightObjModelRadio.addActionListener(e -> setPanelType(false, PanelType.OBJ_MODEL));
        rightPanelGroup.add(rightObjModelRadio);
        rightPanelTypeMenu.add(rightObjModelRadio);
        fileMenu.add(rightPanelTypeMenu);

        JMenuItem loadImageItem = new JMenuItem("Load Left");
        loadImageItem.addActionListener(e -> load(true));
        fileMenu.add(loadImageItem);

        JMenuItem loadModelItem = new JMenuItem("Load Right");
        loadModelItem.addActionListener(e -> load(false));
        fileMenu.add(loadModelItem);

        JMenuItem savePointsLeftItem = new JMenuItem("Save Points Left");
        savePointsLeftItem.addActionListener(e -> savePoints(true));
        fileMenu.add(savePointsLeftItem);

        JMenuItem savePointsRightItem = new JMenuItem("Save Points Right");
        savePointsRightItem.addActionListener(e -> savePoints(false));
        fileMenu.add(savePointsRightItem);

        JMenuItem loadPointsLeftItem = new JMenuItem("Load Points Left");
        loadPointsLeftItem.addActionListener(e -> loadPoints(true));
        fileMenu.add(loadPointsLeftItem);

        JMenuItem loadPointsRightItem = new JMenuItem("Load Points Right");
        loadPointsRightItem.addActionListener(e -> loadPoints(false));
        fileMenu.add(loadPointsRightItem);

        JMenuItem clearPointsItem = new JMenuItem("Clear Points");
        clearPointsItem.addActionListener(e -> clearPoints());
        editMenu.add(clearPointsItem);

        JMenuItem removeLastPointItem = new JMenuItem("Remove Last Point");
        removeLastPointItem.addActionListener(e -> removeLastPoint());
        editMenu.add(removeLastPointItem);

        setJMenuBar(menuBar);

        getContentPane().add(mainPanel);
    }

    private void setPanelType(boolean isLeft, PanelType type) {
        if (isLeft) {
            leftPanelType = type;
            imagePanel1.setPanelType(type);
            imagePanel1.setBorder(BorderFactory.createTitledBorder(type == PanelType.IMAGE ? "Image" : "OBJ Model"));
        } else {
            rightPanelType = type;
            imagePanel2.setPanelType(type);
            imagePanel2.setBorder(BorderFactory.createTitledBorder(type == PanelType.IMAGE ? "Image" : "OBJ Model"));
        }
    }

    private void load(boolean isLeft) {
        PanelType type = isLeft ? leftPanelType : rightPanelType;
        ImagePanel panel = isLeft ? imagePanel1 : imagePanel2;

        if (type == PanelType.IMAGE) {
            loadImage(panel);
        } else {
            loadObjModel(panel);
        }
    }

    private void loadImage(ImagePanel panel) {
        JFileChooser fileChooser = new JFileChooser();
        if (lastDirectory != null) {
            fileChooser.setCurrentDirectory(lastDirectory);
        }
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            panel.loadImage(selectedFile);
            lastDirectory = selectedFile.getParentFile();
        }
    }

    private void loadObjModel(ImagePanel panel) {
        JFileChooser fileChooser = new JFileChooser();
        if (lastDirectory != null) {
            fileChooser.setCurrentDirectory(lastDirectory);
        }
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            panel.loadObjModel(selectedFile);
            lastDirectory = selectedFile.getParentFile();
        }
    }

    private void savePoints(boolean isLeft) {
        JFileChooser fileChooser = new JFileChooser();
        if (lastDirectory != null) {
            fileChooser.setCurrentDirectory(lastDirectory);
        }
        fileChooser.setDialogTitle("Save Points (" + (isLeft ? "Left Panel" : "Right Panel") + ")");
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try (PrintWriter dataWriter = new PrintWriter(selectedFile)) {
                for (PointMatch pm : pointMatchTableModel.getPointMatches()) {
                    Point3D p = isLeft ? pm.getLeftPoint() : pm.getRightPoint();
                    if (p != null) {
                        dataWriter.println(pm.getName());
                        dataWriter.println(p.getX());
                        dataWriter.println(p.getY());
                        dataWriter.println();
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saving points file:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            lastDirectory = selectedFile.getParentFile();
        }
    }

    private void loadPoints(boolean isLeft) {
        JFileChooser fileChooser = new JFileChooser();
        if (lastDirectory != null) {
            fileChooser.setCurrentDirectory(lastDirectory);
        }
        fileChooser.setDialogTitle("Load Points (" + (isLeft ? "Left Panel" : "Right Panel") + ")");
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try (Scanner scanner = new Scanner(selectedFile)) {
                java.util.List<PointMatch> matches = new java.util.ArrayList<>(pointMatchTableModel.getPointMatches());
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    if (!line.isEmpty()) {
                        String name = line;
                        if (scanner.hasNextLine()) {
                            double x = Double.parseDouble(scanner.nextLine().trim());
                            if (scanner.hasNextLine()) {
                                double y = Double.parseDouble(scanner.nextLine().trim());
                                // Skip blank line if present
                                if (scanner.hasNextLine()) {
                                    scanner.nextLine();
                                }
                                Point3D point = new Point3D(x, y, 0.0);
                                // Find existing PointMatch by name
                                PointMatch found = null;
                                for (PointMatch pm : matches) {
                                    if (name.equalsIgnoreCase(pm.getName())) {
                                        found = pm;
                                        break;
                                    }
                                }
                                if (found != null) {
                                    if (isLeft) {
                                        found.setLeftPoint(point);
                                    } else {
                                        found.setRightPoint(point);
                                    }
                                } else {
                                    PointMatch newPm;
                                    if (isLeft) {
                                        newPm = new PointMatch(point, null, name);
                                    } else {
                                        newPm = new PointMatch(null, point, name);
                                    }
                                    matches.add(newPm);
                                }
                            }
                        }
                    }
                }
                pointMatchTableModel.setPointMatches(matches);
                imagePanel1.repaint();
                imagePanel2.repaint();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error loading points file:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            lastDirectory = selectedFile.getParentFile();
        }
    }

    private void clearPoints() {
        pointMatchTableModel.clear();
        imagePanel1.repaint();
        imagePanel2.repaint();
    }

    private void removeLastPoint() {
        if (pointMatchTableModel.getRowCount() > 0) {
            pointMatchTableModel.removePointMatch(pointMatchTableModel.getRowCount() - 1);
            imagePanel1.repaint();
            imagePanel2.repaint();
        }
    }

    public PointMatchTableModel getPointMatchTableModel() {
        return pointMatchTableModel;
    }

    public JTable getPointMatchTable() {
        return pointMatchTable;
    }

    public ImagePanel getImagePanel1() {
        return imagePanel1;
    }

    public ImagePanel getImagePanel2() {
        return imagePanel2;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FaceDetectUI ui = new FaceDetectUI();
            ui.setVisible(true);
        });
    }
}