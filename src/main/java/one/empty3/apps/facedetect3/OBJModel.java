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

import one.empty3.library.*;
import one.empty3.library.objloader.E3Model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class OBJModel extends E3Model {
    private volatile boolean isRendering = false;
    private volatile BufferedImage renderedImage;
    private ZBufferImpl zBuffer;

    // View navigation parameters
    private double zoomFactor = 1.0;
    private double rotX = 0.0;
    private double rotY = 0.0;
    private double rotZ = 0.0;
    private double transX = 0.0;
    private double transY = 0.0;
    private double transZ = 0.0;
    private volatile boolean needsRender = true;
    private Runnable repaintCallback;

    public void setRepaintCallback(Runnable repaintCallback) {
        this.repaintCallback = repaintCallback;
    }

    public void zoomIn() {
        zoomFactor *= 1.2;
        needsRender = true;
        triggerRepaint();
    }

    public void zoomOut() {
        zoomFactor /= 1.2;
        needsRender = true;
        triggerRepaint();
    }

    public void rotateX(double angle) {
        rotX += angle;
        needsRender = true;
        triggerRepaint();
    }

    public void rotateY(double angle) {
        rotY += angle;
        needsRender = true;
        triggerRepaint();
    }

    public void rotateZ(double angle) {
        rotZ += angle;
        needsRender = true;
        triggerRepaint();
    }

    public void translateView(double percentX, double percentY, double percentZ) {
        double distance = Math.max(Math.max(rightpoint - leftpoint, toppoint - bottompoint), farpoint - nearpoint);
        transX += distance * percentX;
        transY += distance * percentY;
        transZ += distance * percentZ;
        needsRender = true;
        triggerRepaint();
    }

    public void resetView() {
        zoomFactor = 1.0;
        rotX = 0.0;
        rotY = 0.0;
        rotZ = 0.0;
        transX = 0.0;
        transY = 0.0;
        transZ = 0.0;
        needsRender = true;
        triggerRepaint();
    }

    private void triggerRepaint() {
        if (repaintCallback != null) {
            javax.swing.SwingUtilities.invokeLater(repaintCallback);
        }
    }

    private Point3D rotate(Point3D p, double ax, double ay, double az) {
        double x = p.getX();
        double y = p.getY();
        double z = p.getZ();

        if (ax != 0.0) {
            double cos = Math.cos(ax);
            double sin = Math.sin(ax);
            double y1 = y * cos - z * sin;
            double z1 = y * sin + z * cos;
            y = y1;
            z = z1;
        }

        if (ay != 0.0) {
            double cos = Math.cos(ay);
            double sin = Math.sin(ay);
            double x1 = x * cos + z * sin;
            double z1 = -x * sin + z * cos;
            x = x1;
            z = z1;
        }

        if (az != 0.0) {
            double cos = Math.cos(az);
            double sin = Math.sin(az);
            double x1 = x * cos - y * sin;
            double y1 = x * sin + y * cos;
            x = x1;
            y = y1;
        }

        return new Point3D(x, y, z);
    }

    public OBJModel(File file) throws InstantiationException {
        // Call the parent constructor with the provided file
        byte[] bytes = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            bytes = fileInputStream.readAllBytes();
        } catch (IOException e) {
            throw new InstantiationException();
        }
        super(new BufferedReader(new StringReader(new String(bytes))), true, file.getAbsolutePath());
    }

    public void draw(java.awt.Graphics g, int width, int height, int displayType) {
        if (needsRender && !isRendering) {
            needsRender = false;
            isRendering = true;
            new Thread(() -> {
                zBuffer = new ZBufferImpl(width, height);
                Scene scene = new Scene();
                scene.add(this);
                zBuffer.setDisplayType(displayType);
                Point3D center = new Point3D(
                        (leftpoint + rightpoint) / 2,
                        (toppoint + bottompoint) / 2,
                        (nearpoint + farpoint) / 2);

                double distance = Math.max(
                        Math.max(rightpoint - leftpoint, toppoint - bottompoint),
                        farpoint - nearpoint);

                // Add axes (Red = X, Green = Y, Blue = Z)
                double axisLength = distance / 2;
                Point3D xAxisEnd = center.plus(new Point3D(axisLength, 0.0, 0.0));
                Point3D yAxisEnd = center.plus(new Point3D(0.0, axisLength, 0.0));
                Point3D zAxisEnd = center.plus(new Point3D(0.0, 0.0, axisLength));

                scene.add(new LineSegment(center, xAxisEnd,
                        new ColorTexture(new one.empty3.libs.Color(java.awt.Color.RED.getRGB()))));
                scene.add(new LineSegment(center, yAxisEnd,
                        new ColorTexture(new one.empty3.libs.Color(java.awt.Color.GREEN.getRGB()))));
                scene.add(new LineSegment(center, zAxisEnd,
                        new ColorTexture(new one.empty3.libs.Color(java.awt.Color.BLUE.getRGB()))));

                // Camera calculations with rotation, zoom, translation
                Point3D defaultOffset = new Point3D(0.0, 0.0, -distance * 2 / zoomFactor);
                Point3D rotatedOffset = rotate(defaultOffset, rotX, rotY, rotZ);
                Point3D rotatedUp = rotate(Point3D.Y, rotX, rotY, rotZ);

                Point3D translation = new Point3D(transX, transY, transZ);
                Point3D target = center.plus(translation);
                Point3D eye = target.plus(rotatedOffset);

                Camera camera = new Camera(eye, target, rotatedUp);
                camera.angleXY(width, height, Math.PI / 3, Axis.Y);

                zBuffer.scene(scene);
                zBuffer.camera(camera);
                zBuffer.draw();
                renderedImage = zBuffer.image().getBi();
                isRendering = false;

                if (needsRender && repaintCallback != null) {
                    javax.swing.SwingUtilities.invokeLater(repaintCallback);
                }
            }).start();
        }

        if (renderedImage != null) {
            g.drawImage(renderedImage, 0, 0, null);
        }
    }

    public ZBufferImpl getzBuffer() {
        return zBuffer;
    }

    private java.util.List<Point3D> points = new java.util.ArrayList<>();

    public java.util.List<Point3D> getPoints() {
        return points;
    }

    public void setPoints(java.util.List<Point3D> points) {
        this.points = points;
    }
}