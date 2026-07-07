/*
 *
 *  * Copyright (c) 2026. Manuel Daniel Dahmen
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
 */
package one.empty3.apps.facedetect3;

import one.empty3.library.Point3D;
import one.empty3.library.ZBufferImpl;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FaceDetectUI extends JFrame {

    private JPanel mainPanel;
    private ImagePanel imagePanel;
    private JTable pointTable;
    private LandmarkTableModel landmarkTableModel;
    private File lastDirectory;

    private final List<FaceDetectView> views = new ArrayList<>();
    private FaceDetectView currentView;

    private JMenu viewsMenu;
    private JMenu renderMenu;
    private JRadioButtonMenuItem viewTypeImageRadio;
    private JRadioButtonMenuItem viewTypeObjModelRadio;

    // Render Targets
    private FaceDetectView image1View;
    private FaceDetectView modelView;
    private FaceDetectView image3View;
    private FaceDetectView text2View;

    private MasksRenderer masksRenderer;

    public FaceDetectUI() {
        setTitle("FaceDetect3 - Multi-View");
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
            if (currentView != null && currentView.getPanelType() == PanelType.OBJ_MODEL) {
                OBJModel model = currentView.getObjModel();
                if (model != null) {
                    action.accept(model);
                    imagePanel.setDisplayLines(quickViewBox.isSelected());
                    imagePanel.repaint();
                }
            }
        };

        quickViewBox.addActionListener(e -> {
            boolean isQuick = quickViewBox.isSelected();
            imagePanel.setDisplayLines(isQuick);
            imagePanel.repaint();
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

        imagePanel = new ImagePanel(this);
        mainPanel.add(imagePanel, BorderLayout.CENTER);

        landmarkTableModel = new LandmarkTableModel();
        pointTable = new JTable(landmarkTableModel);
        JScrollPane tableScrollPane = new JScrollPane(pointTable);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(tableScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Add");
        JButton modifyButton = new JButton("Modify");
        JButton deleteButton = new JButton("Delete");

        addButton.addActionListener(e -> {
            if (currentView != null) {
                String defaultName = "Point " + (currentView.getLandmarkPoints().size() + 1);
                landmarkTableModel.addLandmarkPoint(new LandmarkPoint(defaultName, null));
                int lastRow = landmarkTableModel.getRowCount() - 1;
                pointTable.setRowSelectionInterval(lastRow, lastRow);
            }
        });

        modifyButton.addActionListener(e -> {
            int selectedRow = pointTable.getSelectedRow();
            if (selectedRow != -1 && currentView != null) {
                String currentName = currentView.getLandmarkPoints().get(selectedRow).getName();
                String newName = JOptionPane.showInputDialog(FaceDetectUI.this, "Enter new name for the point:",
                        currentName);
                if (newName != null && !newName.trim().isEmpty()) {
                    landmarkTableModel.setValueAt(newName.trim(), selectedRow, 0);
                    imagePanel.repaint();
                }
            } else {
                JOptionPane.showMessageDialog(FaceDetectUI.this, "Please select a point from the list to modify.",
                        "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = pointTable.getSelectedRow();
            if (selectedRow != -1) {
                landmarkTableModel.removeLandmarkPoint(selectedRow);
                imagePanel.repaint();
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

        pointTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                imagePanel.repaint();
            }
        });

        // Setup Menus
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        viewsMenu = new JMenu("Views");
        renderMenu = new JMenu("Render Settings");
        JMenu masksMenu = new JMenu("Masks");

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewsMenu);
        menuBar.add(renderMenu);
        menuBar.add(masksMenu);

        // File Menu Items
        JMenuItem loadImageItem = new JMenuItem("Load Image");
        loadImageItem.addActionListener(e -> loadImage());
        fileMenu.add(loadImageItem);

        JMenuItem loadModelItem = new JMenuItem("Load OBJ Model");
        loadModelItem.addActionListener(e -> loadObjModel());
        fileMenu.add(loadModelItem);

        fileMenu.addSeparator();

        JMenu viewTypeMenu = new JMenu("Select View Type");
        ButtonGroup vtGroup = new ButtonGroup();
        viewTypeImageRadio = new JRadioButtonMenuItem("Image", true);
        viewTypeImageRadio.addActionListener(e -> setViewType(PanelType.IMAGE));
        vtGroup.add(viewTypeImageRadio);
        viewTypeMenu.add(viewTypeImageRadio);

        viewTypeObjModelRadio = new JRadioButtonMenuItem("OBJ Model", false);
        viewTypeObjModelRadio.addActionListener(e -> setViewType(PanelType.OBJ_MODEL));
        vtGroup.add(viewTypeObjModelRadio);
        viewTypeMenu.add(viewTypeObjModelRadio);
        fileMenu.add(viewTypeMenu);

        fileMenu.addSeparator();

        JMenuItem savePointsItem = new JMenuItem("Save Points");
        savePointsItem.addActionListener(e -> savePoints());
        fileMenu.add(savePointsItem);

        JMenuItem loadPointsItem = new JMenuItem("Load Points");
        loadPointsItem.addActionListener(e -> loadPoints());
        fileMenu.add(loadPointsItem);

        // Edit Menu Items
        JMenuItem clearPointsItem = new JMenuItem("Clear Points");
        clearPointsItem.addActionListener(e -> clearPoints());
        editMenu.add(clearPointsItem);

        JMenuItem removeLastPointItem = new JMenuItem("Remove Last Point");
        removeLastPointItem.addActionListener(e -> removeLastPoint());
        editMenu.add(removeLastPointItem);

        // Masks Menu Items
        JMenuItem renderLocalItem = new JMenuItem("Render Local");
        renderLocalItem.addActionListener(e -> masksRenderer.render(false));
        masksMenu.add(renderLocalItem);

        JMenuItem renderRemoteItem = new JMenuItem("Render Remote (Cloud Run)");
        renderRemoteItem.addActionListener(e -> masksRenderer.render(true));
        masksMenu.add(renderRemoteItem);

        masksMenu.addSeparator();

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

        JMenu settingsMenu = new JMenu("Settings");
        JCheckBoxMenuItem hdTexturesItem = new JCheckBoxMenuItem("HD Textures", false);
        hdTexturesItem.addActionListener(e -> masksRenderer.setHdTextures(hdTexturesItem.isSelected()));
        settingsMenu.add(hdTexturesItem);

        JCheckBoxMenuItem bezierItem = new JCheckBoxMenuItem("Bezier Mode", false);
        bezierItem.addActionListener(e -> masksRenderer.setBezier(bezierItem.isSelected()));
        settingsMenu.add(bezierItem);
        masksMenu.add(settingsMenu);

        // Create default views
        FaceDetectView defaultView1 = new FaceDetectView("View 1");
        views.add(defaultView1);
        FaceDetectView defaultView2 = new FaceDetectView("Model View");
        defaultView2.setPanelType(PanelType.OBJ_MODEL);
        views.add(defaultView2);

        image1View = defaultView1;
        modelView = defaultView2;
        text2View = defaultView2;

        setCurrentView(defaultView1);

        rebuildViewsMenu();
        rebuildRenderMenu();

        setJMenuBar(menuBar);
        getContentPane().add(mainPanel);
    }

    private void setCurrentView(FaceDetectView view) {
        this.currentView = view;
        imagePanel.setView(view);
        if (view != null) {
            landmarkTableModel.setLandmarkPoints(view.getLandmarkPoints());
            if (view.getPanelType() == PanelType.IMAGE) {
                viewTypeImageRadio.setSelected(true);
            } else {
                viewTypeObjModelRadio.setSelected(true);
            }
            imagePanel.setBorder(BorderFactory.createTitledBorder(view.getName() + " (" + (view.getPanelType() == PanelType.IMAGE ? "Image" : "OBJ Model") + ")"));
        } else {
            landmarkTableModel.setLandmarkPoints(null);
            imagePanel.setBorder(BorderFactory.createTitledBorder("No View"));
        }
        imagePanel.repaint();
    }

    private void setViewType(PanelType type) {
        if (currentView != null) {
            currentView.setPanelType(type);
            currentView.setImage(null);
            currentView.setImageFile(null);
            currentView.setObjModel(null);
            currentView.setModelFile(null);
            setCurrentView(currentView);
        }
    }

    private void loadImage() {
        if (currentView == null) return;
        JFileChooser fileChooser = new JFileChooser();
        if (lastDirectory != null) {
            fileChooser.setCurrentDirectory(lastDirectory);
        }
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            imagePanel.loadImage(selectedFile);
            lastDirectory = selectedFile.getParentFile();
            setCurrentView(currentView);
        }
    }

    private void loadObjModel() {
        if (currentView == null) return;
        JFileChooser fileChooser = new JFileChooser();
        if (lastDirectory != null) {
            fileChooser.setCurrentDirectory(lastDirectory);
        }
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            imagePanel.loadObjModel(selectedFile);
            lastDirectory = selectedFile.getParentFile();
            setCurrentView(currentView);
        }
    }

    private void savePoints() {
        if (currentView == null) return;
        JFileChooser fileChooser = new JFileChooser();
        if (lastDirectory != null) {
            fileChooser.setCurrentDirectory(lastDirectory);
        }
        fileChooser.setDialogTitle("Save Points (" + currentView.getName() + ")");
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try (PrintWriter dataWriter = new PrintWriter(selectedFile)) {
                for (LandmarkPoint lp : currentView.getLandmarkPoints()) {
                    Point3D p = lp.getPoint();
                    if (p != null) {
                        dataWriter.println(lp.getName());
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

    private void loadPoints() {
        if (currentView == null) return;
        JFileChooser fileChooser = new JFileChooser();
        if (lastDirectory != null) {
            fileChooser.setCurrentDirectory(lastDirectory);
        }
        fileChooser.setDialogTitle("Load Points (" + currentView.getName() + ")");
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try (Scanner scanner = new Scanner(selectedFile)) {
                List<LandmarkPoint> list = currentView.getLandmarkPoints();
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    if (!line.isEmpty()) {
                        String name = line;
                        if (scanner.hasNextLine()) {
                            double x = Double.parseDouble(scanner.nextLine().trim());
                            if (scanner.hasNextLine()) {
                                double y = Double.parseDouble(scanner.nextLine().trim());
                                if (scanner.hasNextLine()) {
                                    scanner.nextLine();
                                }
                                Point3D point = new Point3D(x, y, 0.0);
                                LandmarkPoint found = null;
                                for (LandmarkPoint lp : list) {
                                    if (name.equalsIgnoreCase(lp.getName())) {
                                        found = lp;
                                        break;
                                    }
                                }
                                if (found != null) {
                                    found.setPoint(point);
                                } else {
                                    list.add(new LandmarkPoint(name, point));
                                }
                            }
                        }
                    }
                }
                landmarkTableModel.fireTableDataChanged();
                imagePanel.repaint();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error loading points file:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            lastDirectory = selectedFile.getParentFile();
        }
    }

    private void clearPoints() {
        landmarkTableModel.clear();
        imagePanel.repaint();
    }

    private void removeLastPoint() {
        if (landmarkTableModel.getRowCount() > 0) {
            landmarkTableModel.removeLandmarkPoint(landmarkTableModel.getRowCount() - 1);
            imagePanel.repaint();
        }
    }

    public LandmarkTableModel getLandmarkTableModel() {
        return landmarkTableModel;
    }

    public JTable getPointTable() {
        return pointTable;
    }

    public ImagePanel getImagePanel() {
        return imagePanel;
    }

    private void rebuildViewsMenu() {
        viewsMenu.removeAll();

        JMenuItem addViewItem = new JMenuItem("Add View");
        addViewItem.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this, "Enter name for the new view:", "View " + (views.size() + 1));
            if (name != null && !name.trim().isEmpty()) {
                FaceDetectView newView = new FaceDetectView(name.trim());
                views.add(newView);
                setCurrentView(newView);
                rebuildViewsMenu();
                rebuildRenderMenu();
            }
        });
        viewsMenu.add(addViewItem);

        JMenuItem closeViewItem = new JMenuItem("Close Current View");
        closeViewItem.addActionListener(e -> {
            if (currentView != null) {
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to close view '" + currentView.getName() + "'?", "Close View", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    views.remove(currentView);
                    if (image1View == currentView) image1View = null;
                    if (modelView == currentView) modelView = null;
                    if (image3View == currentView) image3View = null;
                    if (text2View == currentView) text2View = null;

                    FaceDetectView nextView = null;
                    if (!views.isEmpty()) {
                        nextView = views.get(views.size() - 1);
                    }
                    setCurrentView(nextView);
                    rebuildViewsMenu();
                    rebuildRenderMenu();
                }
            }
        });
        viewsMenu.add(closeViewItem);

        viewsMenu.addSeparator();

        ButtonGroup bg = new ButtonGroup();
        for (FaceDetectView v : views) {
            JRadioButtonMenuItem item = new JRadioButtonMenuItem(v.getName(), v == currentView);
            item.addActionListener(e -> setCurrentView(v));
            bg.add(item);
            viewsMenu.add(item);
        }
    }

    private void rebuildRenderMenu() {
        renderMenu.removeAll();

        JMenu img1Menu = new JMenu("Image 1");
        ButtonGroup bg1 = new ButtonGroup();
        for (FaceDetectView v : views) {
            JRadioButtonMenuItem item = new JRadioButtonMenuItem(v.getName(), v == image1View);
            item.addActionListener(e -> {
                image1View = v;
                rebuildRenderMenu();
            });
            bg1.add(item);
            img1Menu.add(item);
        }
        renderMenu.add(img1Menu);

        JMenu modelMenu = new JMenu("Model");
        ButtonGroup bgModel = new ButtonGroup();
        for (FaceDetectView v : views) {
            JRadioButtonMenuItem item = new JRadioButtonMenuItem(v.getName(), v == modelView);
            item.addActionListener(e -> {
                modelView = v;
                rebuildRenderMenu();
            });
            bgModel.add(item);
            modelMenu.add(item);
        }
        renderMenu.add(modelMenu);

        JMenu img3Menu = new JMenu("Image 3");
        ButtonGroup bg3 = new ButtonGroup();
        JRadioButtonMenuItem noneItem = new JRadioButtonMenuItem("None", image3View == null);
        noneItem.addActionListener(e -> {
            image3View = null;
            rebuildRenderMenu();
        });
        bg3.add(noneItem);
        img3Menu.add(noneItem);
        for (FaceDetectView v : views) {
            JRadioButtonMenuItem item = new JRadioButtonMenuItem(v.getName(), v == image3View);
            item.addActionListener(e -> {
                image3View = v;
                rebuildRenderMenu();
            });
            bg3.add(item);
            img3Menu.add(item);
        }
        renderMenu.add(img3Menu);

        renderMenu.addSeparator();

        JMenuItem t1Item = new JMenuItem("Text 1 (Image 1 Landmarks): " + (image1View != null ? image1View.getName() : "None"));
        t1Item.setEnabled(false);
        renderMenu.add(t1Item);

        JMenu t2Menu = new JMenu("Text 2 (Model Landmarks)");
        ButtonGroup bgT2 = new ButtonGroup();
        for (FaceDetectView v : views) {
            JRadioButtonMenuItem item = new JRadioButtonMenuItem(v.getName(), v == text2View);
            item.addActionListener(e -> {
                text2View = v;
                rebuildRenderMenu();
            });
            bgT2.add(item);
            t2Menu.add(item);
        }
        renderMenu.add(t2Menu);

        JMenuItem t3Item = new JMenuItem("Text 3 (Image 3 Landmarks): " + (image3View != null ? image3View.getName() : "None"));
        t3Item.setEnabled(false);
        renderMenu.add(t3Item);
    }

    public FaceDetectView getImage1View() {
        return image1View;
    }

    public FaceDetectView getModelView() {
        return modelView;
    }

    public FaceDetectView getImage3View() {
        return image3View;
    }

    public FaceDetectView getText2View() {
        return text2View;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FaceDetectUI ui = new FaceDetectUI();
            ui.setVisible(true);
        });
    }
}