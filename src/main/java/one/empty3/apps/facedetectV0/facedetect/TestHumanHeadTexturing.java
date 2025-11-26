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

package one.empty3.apps.facedetectV0.facedetect;

import one.empty3.feature.PixM;
import one.empty3.library.Camera;
import one.empty3.library.Point3D;
import one.empty3.library.ZBufferImpl;
import one.empty3.library.core.testing.jvm.Resolution;
import one.empty3.library.core.testing.jvm.TestObjetStub;
import one.empty3.library.objloader.E3Model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestHumanHeadTexturing extends TestObjetStub {
    private Rectangle rectangleFace;
    private BufferedImage trueFace;
    private BufferedImage jpgFile;
    private E3Model objFile;
    private EditPolygonsMappings editPolygonsMappings;
    protected BufferedImage zBufferImage;

    public TestHumanHeadTexturing() {
    }


    public void setImageIn(PixM face) {
        this.trueFace = face.getImage();
    }

    @Override
    public void ginit() {
/*
        super.ginit();
        if (objFile != null) {
            z().scene().getObjets().getData1d().clear();
            z().scene().getObjets().setElem(objFile, 0);
        }
*/
    }

    @Override
    public void finit() {
        super.finit();
        if (editPolygonsMappings.model != null) {
            setObj(editPolygonsMappings.model);
        }
        if (editPolygonsMappings.image != null) {
            setJpg(editPolygonsMappings.image);
        }

        z().setDisplayType(ZBufferImpl.DISPLAY_ALL);
        File intPart = new File("faceSkin.txt");
        PrintWriter printWriter;
        try {
            printWriter = new PrintWriter(intPart);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Camera c = new Camera();
        c.getEye().setZ(c.getEye().getX() / 8);
        c.getEye().setX(0.0);
        c.calculerMatrice(Point3D.Y.mult(-1));
        c.setAngleYr(60, 1.0 * z().la() / z().ha());
        camera(c);
        scene().cameraActive(c);

        if (jpgFile != null && objFile != null) {
            printWriter.println("# Face elements without eyes month and nose");
        /*AtomicInteger i = new AtomicInteger(0);
        ((RepresentableConteneur) (scene().getObjets().getElem(0))).
                getListRepresentable().forEach(representable -> {
                    int r = (int) Math.min((i.get() / (256)) % 256, 255);
                    int g = (int) Math.min(i.get() % (256 * 256), 255);
                    int b = i.get() % 256;
                    Color def = new Color(r, g, b);
                    if ((g < 222 && b > 16) || i.get() <= 221) {
                        def = java.awt.Color.BLACK;
                        printWriter.println(i);
                    } else {
                        def = Color.WHITE;
                    }
                    representable.setTexture(new ColorTexzture(def));
                    i.getAndIncrement();
                });*/
            printWriter.flush();
            printWriter.close();

        }
        if (editPolygonsMappings.model != null
                && !scene().getObjets().getData1d().contains(editPolygonsMappings.model)) {
            scene().add(editPolygonsMappings.model);
        } else if (editPolygonsMappings.model != null) {
            //scene().getObjets().setElem(editPolygonsMappings.model, 0);
        }
        z().scene(scene);
        z().camera(c);
    }

    @Override
    public void afterRender() {
        editPolygonsMappings.zBufferImage = getPicture();
        this.zBufferImage = editPolygonsMappings.zBufferImage;
    }

    /*

@Override
    public void afterRender() {
        if (jpgFile != null && objFile != null) {

            rectangleFace = new Rectangle(img().getWidth(), img().getHeight(), 0, 0);
            // Step 2 cadrer les polygones
            ((RepresentableConteneur) scene().getObjets().getElem(0)).getListRepresentable().forEach(representable -> {
                if (representable instanceof Polygon p && representable.texture().getColorAt(0.5, 0.5) == java.awt.Color.BLACK.getRGB()) {
                    p.getPoints().getData1d().forEach(point3D -> {
                        Point point = camera().coordinatesPoint2D(point3D, z());
                        Rectangle r2 = new Rectangle(rectangleFace.x, rectangleFace.y,
                                rectangleFace.width, rectangleFace.height);
                        if (point.getX() < rectangleFace.x) {
                            r2.x = point.x;
                        }
                        if (point.getY() < rectangleFace.y) {
                            r2.y = point.y;
                        }
                        if (point.getX() - rectangleFace.x > rectangleFace.width) {
                            r2.width = (int) (point.getX() - rectangleFace.x);
                        }
                        if (point.getY() - rectangleFace.y > rectangleFace.height) {
                            r2.height = (int) (point.getY() - rectangleFace.y);
                        }

                        rectangleFace = r2;
                    });
                }
            });
        }
    }
*/

    public static TestHumanHeadTexturing startAll(EditPolygonsMappings editPolygonsMappings, BufferedImage jpg, E3Model obj) {
        Logger.getAnonymousLogger().log(Level.INFO, "Jpg Obj Mapping...");

        TestHumanHeadTexturing testHumanHeadTexturing = new TestHumanHeadTexturing();
        testHumanHeadTexturing.editPolygonsMappings = editPolygonsMappings;
        testHumanHeadTexturing.setGenerate(GENERATE_IMAGE);
        testHumanHeadTexturing.setJpg(jpg);
        testHumanHeadTexturing.setObj(obj);
        testHumanHeadTexturing.loop(true);
        testHumanHeadTexturing.setMaxFrames(Integer.MAX_VALUE);
        testHumanHeadTexturing.setPublish(false);
        testHumanHeadTexturing.setDimension(new Resolution(editPolygonsMappings.panelModelView.getWidth(), editPolygonsMappings.panelModelView.getHeight()));

        new Thread(testHumanHeadTexturing).start();

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        return testHumanHeadTexturing;
    }

    protected void setJpg(BufferedImage jpgFile) {
        this.jpgFile = jpgFile;
    }

    public BufferedImage getJpgFile() {
        return zBufferImage == null ? getPicture() : zBufferImage;
    }

    void setObj(E3Model objFile) {
        if (objFile != null) {
            this.objFile = objFile;
            scene().getObjets().setElem(objFile, 0);
        }
    }

    public Rectangle getRectangleFace() {
        return rectangleFace;
    }

    public void setRectangleFace(Rectangle rectangleFace) {
        this.rectangleFace = rectangleFace;
    }

    public BufferedImage getTrueFace() {
        return trueFace;
    }

    public void setTrueFace(BufferedImage trueFace) {
        this.trueFace = trueFace;
    }
}
