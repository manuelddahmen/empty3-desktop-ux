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

import one.empty3.library.Point3D;
import one.empty3.library.ZBufferImpl;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ImagePanel extends JPanel {
    private BufferedImage image;
    private OBJModel objModel;
    private PanelType panelType = PanelType.IMAGE;
    private Consumer<ImagePanel> clickListener;
    private FaceDetectUI faceDetectUI;

    private int draggedPointIndex = -1;
    private Point pressPoint = null;
    private boolean isDragging = false;
    private boolean displayLines = true;
    private File imageFile;
    private File modelFile;

    public ImagePanel(Consumer<ImagePanel> clickListener, FaceDetectUI faceDetectUI) {
        this.clickListener = clickListener;
        this.faceDetectUI = faceDetectUI;

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                pressPoint = e.getPoint();
                isDragging = false;
                draggedPointIndex = getPointIndexNear(pressPoint);
                if (draggedPointIndex != -1) {
                    faceDetectUI.getPointMatchTable().setRowSelectionInterval(draggedPointIndex, draggedPointIndex);
                    repaint();
                }
                clickListener.accept(ImagePanel.this);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (draggedPointIndex != -1) {
                    isDragging = true;
                    Point3D newPoint = null;
                    if (panelType == PanelType.IMAGE) {
                        newPoint = new Point3D((double) e.getX() / getWidth(), (double) e.getY() / getHeight(), 0.0);
                    } else if (panelType == PanelType.OBJ_MODEL && objModel != null) {
                        ZBufferImpl zBuffer = objModel.getzBuffer();
                        if (zBuffer != null && zBuffer.ime != null) {
                            int x = Math.max(0, Math.min(getWidth() - 1, e.getX()));
                            int y = Math.max(0, Math.min(getHeight() - 1, e.getY()));
                            double u = zBuffer.ime.getuMap()[x][y];
                            double v = zBuffer.ime.getvMap()[x][y];
                            newPoint = new Point3D(u, v, 0.0);
                        }
                    }
                    if (newPoint != null) {
                        PointMatchTableModel model = faceDetectUI.getPointMatchTableModel();
                        if (ImagePanel.this == faceDetectUI.getImagePanel1()) {
                            model.setValueAt(newPoint, draggedPointIndex, 0);
                        } else {
                            model.setValueAt(newPoint, draggedPointIndex, 1);
                        }
                        faceDetectUI.getImagePanel1().repaint();
                        faceDetectUI.getImagePanel2().repaint();
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (isDragging) {
                    isDragging = false;
                    draggedPointIndex = -1;
                } else {
                    if (draggedPointIndex == -1) {
                        Point3D point = null;
                        if (panelType == PanelType.IMAGE) {
                            point = new Point3D((double) e.getX() / getWidth(), (double) e.getY() / getHeight(), 0.0);
                        } else if (panelType == PanelType.OBJ_MODEL && objModel != null) {
                            ZBufferImpl zBuffer = objModel.getzBuffer();
                            if (zBuffer != null && zBuffer.ime != null) {
                                int x = Math.max(0, Math.min(getWidth() - 1, e.getX()));
                                int y = Math.max(0, Math.min(getHeight() - 1, e.getY()));
                                double u = zBuffer.ime.getuMap()[x][y];
                                double v = zBuffer.ime.getvMap()[x][y];
                                point = new Point3D(u, v, 0.0);
                            }
                        }
                        if (point != null) {
                            int selectedRow = faceDetectUI.getPointMatchTable().getSelectedRow();
                            if (selectedRow != -1) {
                                PointMatchTableModel model = faceDetectUI.getPointMatchTableModel();
                                if (ImagePanel.this == faceDetectUI.getImagePanel1()) {
                                    model.setValueAt(point, selectedRow, 0);
                                } else {
                                    model.setValueAt(point, selectedRow, 1);
                                }
                            } else {
                                if (ImagePanel.this == faceDetectUI.getImagePanel1()) {
                                    faceDetectUI.getPointMatchTableModel().addPointMatch(new PointMatch(point, null,
                                            "Point " + (faceDetectUI.getPointMatchTableModel().getRowCount() + 1)));
                                } else {
                                    faceDetectUI.getPointMatchTableModel().addPointMatch(new PointMatch(null, point,
                                            "Point " + (faceDetectUI.getPointMatchTableModel().getRowCount() + 1)));
                                }
                            }
                            faceDetectUI.getImagePanel1().repaint();
                            faceDetectUI.getImagePanel2().repaint();
                            clickListener.accept(ImagePanel.this);
                        }
                    }
                    draggedPointIndex = -1;
                }
            }
        };

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }

    public void setPanelType(PanelType panelType) {
        this.panelType = panelType;
        this.image = null;
        this.objModel = null;
        this.imageFile = null;
        this.modelFile = null;
        repaint();
    }

    public void loadImage(File file) {
        try {
            image = ImageIO.read(file);
            imageFile = file;
            modelFile = null;
            panelType = PanelType.IMAGE;
            repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadObjModel(File file) {
        try {
            objModel = new OBJModel(file);
            objModel.setRepaintCallback(this::repaint);
            modelFile = file;
            imageFile = null;
            panelType = PanelType.OBJ_MODEL;
            repaint();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    private Point getPixelForPoint3D(Point3D point) {
        if (panelType == PanelType.IMAGE) {
            int x = (int) (point.getX() * getWidth());
            int y = (int) (point.getY() * getHeight());
            return new Point(x, y);
        } else if (panelType == PanelType.OBJ_MODEL && objModel != null) {
            ZBufferImpl zBuffer = objModel.getzBuffer();
            if (zBuffer == null || zBuffer.ime == null) {
                return null;
            }
            double[][] uMap = zBuffer.ime.getuMap();
            double[][] vMap = zBuffer.ime.getvMap();
            if (uMap == null || vMap == null) {
                return null;
            }
            double u = point.getX();
            double v = point.getY();
            int bestX = -1;
            int bestY = -1;
            double minDistance = Double.MAX_VALUE;

            int step = 4;
            for (int x = 0; x < uMap.length; x += step) {
                for (int y = 0; y < uMap[x].length; y += step) {
                    double du = uMap[x][y] - u;
                    double dv = vMap[x][y] - v;
                    double dist = du * du + dv * dv;
                    if (dist < minDistance) {
                        minDistance = dist;
                        bestX = x;
                        bestY = y;
                    }
                }
            }
            if (bestX != -1) {
                int startX = Math.max(0, bestX - step);
                int endX = Math.min(uMap.length - 1, bestX + step);
                int startY = Math.max(0, bestY - step);
                int endY = Math.min(uMap[0].length - 1, bestY + step);
                for (int x = startX; x <= endX; x++) {
                    for (int y = startY; y <= endY; y++) {
                        double du = uMap[x][y] - u;
                        double dv = vMap[x][y] - v;
                        double dist = du * du + dv * dv;
                        if (dist < minDistance) {
                            minDistance = dist;
                            bestX = x;
                            bestY = y;
                        }
                    }
                }
            }
            if (bestX != -1 && minDistance < 0.05) {
                return new Point(bestX, bestY);
            }
        }
        return null;
    }

    private int getPointIndexNear(Point clickPoint) {
        if (faceDetectUI == null || faceDetectUI.getPointMatchTableModel() == null) {
            return -1;
        }
        List<PointMatch> list = faceDetectUI.getPointMatchTableModel().getPointMatches();
        for (int i = 0; i < list.size(); i++) {
            PointMatch pm = list.get(i);
            Point3D p3d = (ImagePanel.this == faceDetectUI.getImagePanel1()) ? pm.getLeftPoint() : pm.getRightPoint();
            if (p3d != null) {
                Point p = getPixelForPoint3D(p3d);
                if (p != null) {
                    double dist = clickPoint.distance(p);
                    if (dist < 10.0) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (panelType == PanelType.IMAGE && image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        } else if (panelType == PanelType.OBJ_MODEL && objModel != null) {
            objModel.draw(g, getWidth(), getHeight(), displayLines ? ZBufferImpl.SURFACE_DISPLAY_LINES : ZBufferImpl.DISPLAY_ALL);
        }

        if (faceDetectUI != null && faceDetectUI.getPointMatchTableModel() != null) {
            List<PointMatch> list = faceDetectUI.getPointMatchTableModel().getPointMatches();
            int selectedRow = faceDetectUI.getPointMatchTable() != null
                    ? faceDetectUI.getPointMatchTable().getSelectedRow()
                    : -1;

            for (int i = 0; i < list.size(); i++) {
                PointMatch pm = list.get(i);
                Point3D p3d = (ImagePanel.this == faceDetectUI.getImagePanel1()) ? pm.getLeftPoint()
                        : pm.getRightPoint();
                if (p3d != null) {
                    Point p = getPixelForPoint3D(p3d);
                    if (p != null) {
                        if (i == selectedRow) {
                            g.setColor(Color.GREEN);
                            g.fillOval(p.x - 7, p.y - 7, 14, 14);
                            g.setColor(Color.BLACK);
                            g.drawOval(p.x - 7, p.y - 7, 14, 14);
                        } else {
                            g.setColor(Color.RED);
                            g.fillOval(p.x - 5, p.y - 5, 10, 10);
                            g.setColor(Color.BLACK);
                            g.drawOval(p.x - 5, p.y - 5, 10, 10);
                        }
                        g.setColor(Color.WHITE);
                        g.drawString(
                                pm.getName() != null && !pm.getName().isEmpty() ? pm.getName() : String.valueOf(i + 1),
                                p.x + 8, p.y + 5);
                    }
                }
            }
        }
    }

    public List<Point> getPoints() {
        List<Point> result = new ArrayList<>();
        if (faceDetectUI != null && faceDetectUI.getPointMatchTableModel() != null) {
            List<PointMatch> list = faceDetectUI.getPointMatchTableModel().getPointMatches();
            for (PointMatch pm : list) {
                Point3D p3d = (ImagePanel.this == faceDetectUI.getImagePanel1()) ? pm.getLeftPoint()
                        : pm.getRightPoint();
                if (p3d != null) {
                    Point p = getPixelForPoint3D(p3d);
                    if (p != null) {
                        result.add(p);
                    }
                }
            }
        }
        return result;
    }

    public FaceDetectUI getFaceDetectUI() {
        return faceDetectUI;
    }

    public OBJModel getObjModel() {
        return objModel;
    }

    public PanelType getPanelType() {
        return panelType;
    }

    public void setDisplayLines(boolean b) {
        this.displayLines = b;
        if (objModel != null) {
            if (b)
                objModel.setDisplayType(ZBufferImpl.SURFACE_DISPLAY_LINES);
            else
                objModel.setDisplayType(ZBufferImpl.DISPLAY_ALL);
        }
    }

    public File getImageFile() {
        return imageFile;
    }

    public BufferedImage getImage() {
        return image;
    }

    public File getModelFile() {
        return modelFile;
    }
}