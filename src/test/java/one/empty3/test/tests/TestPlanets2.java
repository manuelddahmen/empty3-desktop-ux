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

package one.empty3.test.tests;

import one.empty3.library.core.testing.jvm.TestObjetSub;
import one.empty3.library.*;
import one.empty3.library.core.testing.jvm.TestObjetUx;
import one.empty3.libs.*;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestPlanets2 extends TestObjetSub {
    public static final int SECONDS = 8;
    public static final int FPS = 50;
    private static final int TURNS = 2;
    private final File planets = new File("res\\img\\planets2");
    private File[] planetsImagesFiles;
    private int i = -1;
    private Image image;
    private Sphere sphere;
    private Logger logger;
    private Point3D axeVerticalVideo = Point3D.Y;
    private Point3D[] axeViseeVideo = new Point3D[]{Point3D.X, Point3D.Z};
    private Point3D[] axesSphereHorizontaux = new Point3D[]{Point3D.X, Point3D.Z};
    private ITexture textureImg;

    private static double getaDouble() {
        return TURNS * FPS * SECONDS;
    }

    public static void main(String[] args) {
        TestPlanets2 testPlanets = new TestPlanets2();
        testPlanets.loop(true);
        testPlanets.setResolution(320, 240);
        Thread thread = new Thread(testPlanets);
        thread.start();
    }

    @Override
    public void ginit() {
        logger = Logger.getLogger(this.getClass().getCanonicalName());
        planetsImagesFiles = planets.listFiles();

        assert planetsImagesFiles != null;
        setMaxFrames(planetsImagesFiles.length * FPS * SECONDS);

        //z().ratioVerticalAngle();


        z().setDisplayType(ZBufferImpl.DISPLAY_ALL);


        incr();

    }

    @Override
    public void testScene() throws Exception {

    }

    public void incr() {
        int i1 = (int) ((1.0*frame() / (FPS * SECONDS))*planetsImagesFiles.length);
        if (i1 != i) {
            i = i1;
            if (i1 < planetsImagesFiles.length) {
                while ((planetsImagesFiles[i1] == null || !planetsImagesFiles[i1].exists() || !planetsImagesFiles[i1].isFile())&& i1<planetsImagesFiles.length) {
                    i1 ++;
                }
                if(i1<planetsImagesFiles.length) {
                    image = (Image) Image.loadFile(planetsImagesFiles[i1]);
                    textureImg = new ImageTexture(image);
                    if (image != null)
                        Logger.getLogger(this.getClass().getCanonicalName()).info("Color at center of texure : " + textureImg.getColorAt(0.5, 0.5));
                }
            }
        }
    }

    public void vecDirRotate(Point3D vecOrigX, Point3D vecOrigY, double ratio,
                             Point3D outX, Point3D outY) {
        outX.changeTo(vecOrigX.mult(Math.cos(2 * Math.PI * ratio)).plus(
                vecOrigY.mult(Math.sin(2 * Math.PI * ratio))));
        outY.changeTo(vecOrigX.mult(-Math.sin(2 * Math.PI * ratio)).plus(
                vecOrigY.mult(Math.cos(2 * Math.PI * ratio))));
    }

    @Override
    public void afterRenderFrame() {

    }

    @Override
    public void finit() throws Exception {
        incr();

        scene().clear();


        Camera c = new Camera(axeViseeVideo[1].mult(5), Point3D.O0, axeVerticalVideo);
        c.declareProperties();
        camera(c);
        scene().cameraActive(c);
        i = -1;

        //z().texture(new ColorTexture(0x00FF0000));
        int numFaces = 1;

        sphere = new Sphere(new Axe(axeVerticalVideo.mult(1.0), axeVerticalVideo.mult(-1.0)), 2.0);

        z().texture(new ColorTexture(java.awt.Color.BLACK.getRGB()));
        scene().texture(new ColorTexture(java.awt.Color.BLACK.getRGB()));
        //double v = 1.0/Math.sqrt(1.0/(64.0 *z().la()*z().ha() / numFaces/Math.pow(surfaceBoundingCube, 2./3.)));
        double v = 2.0 * Math.pow(1.0 * z().la() * z().ha() *sphere.getIncrU()*sphere.getIncrV(), .5) + 1.0;
        if (v == Double.POSITIVE_INFINITY || v == Double.NEGATIVE_INFINITY || Double.isNaN(v) || v == 0.0) {
            v = ((double) (z().la() * z().ha())) / numFaces + 1;
        }
        v = v;
        z().setIncrementOptimizer(
                new ZBufferImpl.IncrementOptimizer(
                        ZBufferImpl.IncrementOptimizer.Strategy.ENSURE_MINIMUM_DETAIL,v
                )
        );
        System.out.println("v = " + v);

        sphere.setIncrU(.003);
        sphere.setIncrV(.003);
        scene().clear();
        scene().add(sphere);


        sphere.texture(textureImg);

        double v2 = (frame() % (FPS * SECONDS)) / getaDouble();


        //setZ(new ZBufferImpl(getResx(), getResy()));

        Circle circle = sphere.getCircle();
        circle.setVectZ(axeVerticalVideo);
        circle.getAxis().getElem().getP1().setElem(axeVerticalVideo.mult(1.0));
        circle.getAxis().getElem().getP2().setElem(axeVerticalVideo.mult(-1.0));
        circle.setVectX(axesSphereHorizontaux[0].mult(Math.cos(2 * Math.PI * v2))
                .plus(axesSphereHorizontaux[1].mult(Math.sin(2 * Math.PI * v2))));
        circle.setVectY(axesSphereHorizontaux[0].mult(-Math.sin(2 * Math.PI * v2))
                .plus(axesSphereHorizontaux[1].mult(Math.cos(2 * Math.PI * v2))));
        circle.setPosition(Point3D.O0);
        sphere.setPosition(Point3D.O0);
        circle.setCalculerRepere1(true);
        sphere.setCircle(circle);
        Logger.getAnonymousLogger().log(Level.INFO, "Camera t : " + 2);

        for (int j = 0; j < (sphere.getVectors()).getData1d().size(); j++) {
            if(sphere.getVectors().getData1d().get(j)!=null);

        }
    }

    @Override
    public void afterRender() {

    }

    @Override
    public void publishResult() {

    }
}
