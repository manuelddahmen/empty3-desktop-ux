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

public class ImagePanel extends JPanel {
    private FaceDetectView view;
    private FaceDetectUI faceDetectUI;

    private int draggedPointIndex = -1;
    private Point pressPoint = null;
    private boolean isDragging = false;
    private boolean displayLines = true;

    public ImagePanel(FaceDetectUI faceDetectUI) {
        this.faceDetectUI = faceDetectUI;

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (view == null) return;
                pressPoint = e.getPoint();
                isDragging = false;
                draggedPointIndex = getPointIndexNear(pressPoint);
                if (draggedPointIndex != -1) {
                    faceDetectUI.getPointTable().setRowSelectionInterval(draggedPointIndex, draggedPointIndex);
                    repaint();
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (view == null || draggedPointIndex == -1) return;
                isDragging = true;
                Point3D newPoint = null;
                if (view.getPanelType() == PanelType.IMAGE) {
                    newPoint = new Point3D((double) e.getX() / getWidth(), (double) e.getY() / getHeight(), 0.0);
                } else if (view.getPanelType() == PanelType.OBJ_MODEL && view.getObjModel() != null) {
                    ZBufferImpl zBuffer = view.getObjModel().getzBuffer();
                    if (zBuffer != null && zBuffer.ime != null) {
                        int x = Math.max(0, Math.min(getWidth() - 1, e.getX()));
                        int y = Math.max(0, Math.min(getHeight() - 1, e.getY()));
                        double u = zBuffer.ime.getuMap()[x][y];
                        double v = zBuffer.ime.getvMap()[x][y];
                        newPoint = new Point3D(u, v, 0.0);
                    }
                }
                if (newPoint != null) {
                    view.getLandmarkPoints().get(draggedPointIndex).setPoint(newPoint);
                    faceDetectUI.getLandmarkTableModel().fireTableRowsUpdated(draggedPointIndex, draggedPointIndex);
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (view == null) return;
                if (isDragging) {
                    isDragging = false;
                    draggedPointIndex = -1;
                } else {
                    if (draggedPointIndex == -1) {
                        Point3D point = null;
                        if (view.getPanelType() == PanelType.IMAGE) {
                            point = new Point3D((double) e.getX() / getWidth(), (double) e.getY() / getHeight(), 0.0);
                        } else if (view.getPanelType() == PanelType.OBJ_MODEL && view.getObjModel() != null) {
                            ZBufferImpl zBuffer = view.getObjModel().getzBuffer();
                            if (zBuffer != null && zBuffer.ime != null) {
                                int x = Math.max(0, Math.min(getWidth() - 1, e.getX()));
                                int y = Math.max(0, Math.min(getHeight() - 1, e.getY()));
                                double u = zBuffer.ime.getuMap()[x][y];
                                double v = zBuffer.ime.getvMap()[x][y];
                                point = new Point3D(u, v, 0.0);
                            }
                        }
                        if (point != null) {
                            int selectedRow = faceDetectUI.getPointTable().getSelectedRow();
                            if (selectedRow != -1) {
                                view.getLandmarkPoints().get(selectedRow).setPoint(point);
                                faceDetectUI.getLandmarkTableModel().fireTableRowsUpdated(selectedRow, selectedRow);
                            } else {
                                String defaultName = "Point " + (view.getLandmarkPoints().size() + 1);
                                view.getLandmarkPoints().add(new LandmarkPoint(defaultName, point));
                                faceDetectUI.getLandmarkTableModel().fireTableDataChanged();
                            }
                            repaint();
                        }
                    }
                    draggedPointIndex = -1;
                }
            }
        };

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }

    public void loadImage(File file) {
        if (view == null) return;
        try {
            BufferedImage image = ImageIO.read(file);
            view.setImage(image);
            view.setImageFile(file);
            view.setModelFile(null);
            view.setObjModel(null);
            view.setPanelType(PanelType.IMAGE);
            repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadObjModel(File file) {
        if (view == null) return;
        try {
            OBJModel objModel = new OBJModel(file);
            objModel.setRepaintCallback(this::repaint);
            view.setObjModel(objModel);
            view.setModelFile(file);
            view.setImageFile(null);
            view.setImage(null);
            view.setPanelType(PanelType.OBJ_MODEL);
            repaint();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    private Point getPixelForPoint3D(Point3D point) {
        if (view == null) return null;
        if (view.getPanelType() == PanelType.IMAGE) {
            int x = (int) (point.getX() * getWidth());
            int y = (int) (point.getY() * getHeight());
            return new Point(x, y);
        } else if (view.getPanelType() == PanelType.OBJ_MODEL && view.getObjModel() != null) {
            ZBufferImpl zBuffer = view.getObjModel().getzBuffer();
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
        if (view == null) {
            return -1;
        }
        List<LandmarkPoint> list = view.getLandmarkPoints();
        for (int i = 0; i < list.size(); i++) {
            LandmarkPoint lp = list.get(i);
            Point3D p3d = lp.getPoint();
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
        if (view == null) return;

        if (view.getPanelType() == PanelType.IMAGE && view.getImage() != null) {
            g.drawImage(view.getImage(), 0, 0, getWidth(), getHeight(), this);
        } else if (view.getPanelType() == PanelType.OBJ_MODEL && view.getObjModel() != null) {
            view.getObjModel().draw(g, getWidth(), getHeight(), displayLines ? ZBufferImpl.SURFACE_DISPLAY_LINES : ZBufferImpl.DISPLAY_ALL);
        }

        List<LandmarkPoint> list = view.getLandmarkPoints();
        int selectedRow = faceDetectUI.getPointTable() != null
                ? faceDetectUI.getPointTable().getSelectedRow()
                : -1;

        for (int i = 0; i < list.size(); i++) {
            LandmarkPoint lp = list.get(i);
            Point3D p3d = lp.getPoint();
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
                            lp.getName() != null && !lp.getName().isEmpty() ? lp.getName() : String.valueOf(i + 1),
                            p.x + 8, p.y + 5);
                }
            }
        }
    }

    public List<Point> getPoints() {
        List<Point> result = new ArrayList<>();
        if (view != null) {
            for (LandmarkPoint lp : view.getLandmarkPoints()) {
                if (lp.getPoint() != null) {
                    Point p = getPixelForPoint3D(lp.getPoint());
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

    public FaceDetectView getView() {
        return view;
    }

    public void setView(FaceDetectView view) {
        this.view = view;
        if (view != null) {
            if (view.getObjModel() != null) {
                view.getObjModel().setDisplayType(displayLines ? ZBufferImpl.SURFACE_DISPLAY_LINES : ZBufferImpl.DISPLAY_ALL);
            }
        }
        repaint();
    }

    public void setDisplayLines(boolean b) {
        this.displayLines = b;
        if (view != null && view.getObjModel() != null) {
            if (b)
                view.getObjModel().setDisplayType(ZBufferImpl.SURFACE_DISPLAY_LINES);
            else
                view.getObjModel().setDisplayType(ZBufferImpl.DISPLAY_ALL);
        }
    }
}