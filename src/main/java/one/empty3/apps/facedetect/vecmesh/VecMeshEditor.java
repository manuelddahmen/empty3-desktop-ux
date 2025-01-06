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

package one.empty3.apps.facedetect.vecmesh;
//heights = ((1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1),(1,1,1,1,1,1,1,1,2,2,2,2,2,2,2,1,1,1,1,1,1,1),(1,1,1,,1,1,1,1,1,1,3,3,3,3,1,1,1,1,1,1,1,1,1),(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1),(1,1,1,1,1,1,1,1,1,1,2,2,2,1,1,1,1,1,1,1,1,1),(1,1,1,1,1,1,1,1,1,1,2,2,2,1,1,1,1,1,1,1,1,1),(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1),(1,1,1,1,1,4,1,1,1,1,1,1,1,1,1,1,4,1,1,1,1,1),(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1),(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1))

import one.empty3.apps.facedetect.JFrameEditPolygonsMappings;
import one.empty3.library.*;
import one.empty3.library.core.nurbs.CourbeParametriquePolynomiale;
import one.empty3.library.core.nurbs.FctXY;
import one.empty3.library.core.nurbs.ParametricSurface;
import one.empty3.library.core.tribase.Plan3D;
import one.empty3.library.core.tribase.Tubulaire3;
import one.empty3.library.objloader.E3Model;
import one.empty3.library1.shader.Vec;
import one.empty3.library1.tree.AlgebraicTree;
import one.empty3.library1.tree.ListInstructions;

import javax.swing.*;

import one.empty3.library.Point;
import one.empty3.libs.*;
import one.empty3.libs.Color;
import one.empty3.libs.Image;

import java.awt.*;

public class VecMeshEditor implements Runnable {
    private VecMeshEditorGui vecMeshEditorGui;
    private JFrameEditPolygonsMappings parent2;
    public Rotate rotate;
    private boolean runningDisplay = false;
    private ZBufferImpl zBuffer;
    private Scene scene;
    private Representable model;

    public VecMeshEditor(JFrameEditPolygonsMappings parent) {
        this.parent2 = parent;
        this.rotate = parent.getRotate();
        this.model = parent.getEditPolygonsMappings2().getModel();
        this.vecMeshEditorGui = new VecMeshEditorGui((E3Model) parent2.getEditPolygonsMappings2().getModel(),
                parent2);

    }

    public void run() {
        Thread thread = null;
        setRunningDisplay(true);
        thread = new Thread(() -> {
            while (isRunningDisplay()) {
                try {
                    long t1 = System.currentTimeMillis();

                    if (model != null) {

                        if (rotate == null)
                            rotate = new Rotate(model, vecMeshEditorGui.getPanelGraphics());
                        else {
                            rotate.setRepresentable(model);
                            rotate.updateRepresentableCoordinates();
                        }

                        zBuffer = vecMeshEditorGui.getZBuffer();

                        if (vecMeshEditorGui.getFileTexture() != null) {
                            model.texture(new ImageTexture(vecMeshEditorGui.getFileTexture()));
                            System.err.println("Texture file chosen : " + vecMeshEditorGui.getFileTexture());
                        } else {
                            model.texture(new ColorTexture(Color.BLUE.getRGB()));
                        }
                        rotate.setZBuffer(zBuffer);
                        Scene scene = new Scene();
                        scene.add(model);
                        this.scene = scene;
                        //scene.lumieres().add(new LumierePonctuelle(Point3D.O0, javaAnd.awt.Color.YELLOW));
                        zBuffer.scene(scene);
                        Camera camera = new Camera(Point3D.Z.mult(20), Point3D.O0, Point3D.Y);
                        scene.cameraActive(camera);
                        zBuffer.camera(camera);
                        zBuffer.draw();
                        Image ecBufferedImage = zBuffer.imageInvX();
                        JPanel panelGraphics = vecMeshEditorGui.getPanelGraphics();
                        Graphics graphics = panelGraphics.getGraphics();
                        graphics.drawImage(ecBufferedImage, 0, 0, null);
                        //Output.println("Drawn");
                        zBuffer.idzpp();
                        long t2 = System.currentTimeMillis();
                        Output.println("Matrix was : " + rotate.getRotationMatrix() + " FPS : " + 1.0 / ((t2 - t1) / 1000.));
                    }
                } catch (RuntimeException ex) {
                }

            }
            setRunningDisplay(false);
        });

        thread.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.err.println(e);
        }

    }


    public boolean isRunningDisplay() {
        return runningDisplay;
    }

    public void setRunningDisplay(boolean b) {
        this.runningDisplay = b;
    }

    public Scene getScene() {
        return scene;
    }

    public Rotate getRotate() {
        return rotate;
    }

    public void setRotate(Rotate rotate) {
        this.rotate = rotate;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void setRepresentable(E3Model model) {
        this.rotate = new Rotate(model, this.vecMeshEditorGui.getPanelGraphics());
    }
}
