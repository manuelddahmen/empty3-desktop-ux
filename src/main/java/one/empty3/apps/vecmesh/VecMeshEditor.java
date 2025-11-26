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

package one.empty3.apps.vecmesh;
//heights = ((1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1),(1,1,1,1,1,1,1,1,2,2,2,2,2,2,2,1,1,1,1,1,1,1),(1,1,1,,1,1,1,1,1,1,3,3,3,3,1,1,1,1,1,1,1,1,1),(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1),(1,1,1,1,1,1,1,1,1,1,2,2,2,1,1,1,1,1,1,1,1,1),(1,1,1,1,1,1,1,1,1,1,2,2,2,1,1,1,1,1,1,1,1,1),(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1),(1,1,1,1,1,4,1,1,1,1,1,1,1,1,1,1,4,1,1,1,1,1),(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1),(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1))

import one.empty3.library.*;
import one.empty3.library.core.nurbs.CourbeParametriquePolynomiale;
import one.empty3.library.core.nurbs.FctXY;
import one.empty3.library.core.nurbs.ParametricSurface;
import one.empty3.library.core.tribase.Plan3D;
import one.empty3.library.core.tribase.Tubulaire3;
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
    private final VecMeshEditorGui vecMeshEditorGui;
    private Rotate rotate;
    private boolean runningDisplay = false;
    private ZBufferImpl zBuffer;
    private Scene scene;

    public VecMeshEditor() {
        vecMeshEditorGui = new VecMeshEditorGui();
        vecMeshEditorGui.setVisible(true);
        vecMeshEditorGui.setModel(this);

    }

    public static void main(String[] strings) {
        VecMeshEditor vecMeshEditor = new VecMeshEditor();
        Thread thread = new Thread(vecMeshEditor);
        thread.start();
    }

    public ParametricSurface getParametricSurface(double halfSize) {
        ParametricSurface shape = null;
        Class<? extends Representable> representableClass = vecMeshEditorGui.getRepresentableClass();
        if (representableClass.equals(Sphere.class)) {
            shape = new Sphere(Point3D.O0, halfSize);
        } else if (representableClass.equals(Tubulaire3.class)) {
            shape = new Tubulaire3();
            ((CourbeParametriquePolynomiale) ((Tubulaire3) shape).getSoulCurve().getElem()).getCoefficients().
                    setElem(Point3D.Y.mult(halfSize / 2), 0);
            ((CourbeParametriquePolynomiale) ((Tubulaire3) shape).getSoulCurve().getElem()).getCoefficients().
                    setElem(Point3D.Y.mult(0), 1);
            ((CourbeParametriquePolynomiale) ((Tubulaire3) shape).getSoulCurve().getElem()).getCoefficients().
                    setElem(Point3D.Y.mult(-halfSize / 2), 2);
            ((Tubulaire3) shape).getDiameterFunction().setElem(new FctXY() {
                @Override
                public double result(double input) {
                    return halfSize / 2.0;
                }
            });
        } else if (representableClass.equals(Plan3D.class)) {
            shape = new Plan3D(new Point3D(-halfSize, -halfSize, 0.0), new Point3D(halfSize, -halfSize, 0.0), new Point3D(-halfSize, halfSize, 0.0));
        }
        shape = shape == null ? new Plan3D(new Point3D(-halfSize, -halfSize, 0.0), new Point3D(halfSize, -halfSize, 0.0), new Point3D(-halfSize, halfSize, 0.0)) : shape;

        if (vecMeshEditorGui.getTexture() != null)
            shape.texture(new ImageTexture(new Image(vecMeshEditorGui.getTexture())));

        return shape;
    }

    public void run() {
        Thread thread = null;
        setRunningDisplay(true);
        thread = new Thread(() -> {
            while (isRunningDisplay()) {
                try {
                    long t1 = System.currentTimeMillis();
                    StructureMatrix<Double> eval = null;
                    AlgebraicTree algebraicTree = new AlgebraicTree(vecMeshEditorGui.getDefaultCode());
                    ListInstructions listInstructions = new ListInstructions();
                    listInstructions.addInstructions(vecMeshEditorGui.getDefaultCode());
                    listInstructions.runInstructions();
                    //AlgebraicTree.construct();
                    StructureMatrix<Double> heights = listInstructions.getCurrentParamsValuesVecComputed().get("heights");
                    if (heights != null && !heights.data1d.isEmpty()) {
                        Double[] doubles = new Double[heights.data1d.size()];
                        heights.data1d.toArray(doubles);
                        eval = new Vec(doubles).getVecVal();
                    }
                    long tEval = System.currentTimeMillis();
                    if (eval != null && eval.getDim() == 1 && !eval.getData1d().isEmpty()) {
                        Double[] doubles = new Double[eval.getData1d().size()];
                        for (int i = 0; i < doubles.length; i++) {
                            doubles[i] = eval.getElem(i);
                        }
                        VecHeightMap vecHeightMap = new VecHeightMap(getParametricSurface(4.0),
                                new Vec(doubles), vecMeshEditorGui.getTextFieldRows());
                        vecHeightMap.getIncrNormale().setElem(0.01);
                        if (rotate == null)
                            rotate = new Rotate(vecHeightMap, vecMeshEditorGui.getPanelGraphics());
                        else {
                            rotate.setRepresentable(vecHeightMap);
                            rotate.updateRepresentableCoordinates();
                        }
                        vecHeightMap.setIncrU(0.08);
                        vecHeightMap.setIncrV(0.08);

                        zBuffer = vecMeshEditorGui.getZBuffer();

                        if (vecMeshEditorGui.getFileTexture() != null) {
                            vecHeightMap.texture(new ImageTexture(vecMeshEditorGui.getFileTexture()));
                            System.err.println("Texture file chosen : " + vecMeshEditorGui.getFileTexture());
                        } else {
                            vecHeightMap.texture(new ColorTexture(Color.BLUE.getRGB()));
                        }
                        rotate.setZBuffer(zBuffer);
                        Scene scene = new Scene();
                        scene.add(vecHeightMap);
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
                        Output.println("Matrix was : " + vecHeightMap.getVec() + " FPS : " + 1.0 / ((t2 - t1) / 1000.));

                    }
                } catch (RuntimeException ex) {
                    System.err.println(ex);
                    Output.println(ex.getLocalizedMessage());
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

    private void setRunningDisplay(boolean b) {
        this.runningDisplay = b;
    }

    public Scene getScene() {
        return scene;
    }
}
