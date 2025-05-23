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

import one.empty3.feature.PixM;
import one.empty3.apps.feature.app.replace.javax.imageio.ImageIO;
import one.empty3.library.*;
import one.empty3.library.core.extra.VoronoiImageTexture;
import one.empty3.library.core.nurbs.CourbeParametriquePolynomialeBezier;
import one.empty3.library.core.nurbs.FctXY;
import one.empty3.library.core.testing.jvm.Resolution;
import one.empty3.library.core.testing.jvm.TestObjetSub;
import one.empty3.library.core.tribase.Tubulaire3;

import one.empty3.library.Point;
import one.empty3.libs.*;

import java.awt.image.BufferedImage;
import java.io.File;

public class Galerie extends TestObjetSub {
    /*    class Tubulaire3 extends one.empty3.library.core.tribase.Tubulaire3 {
            @Override
            public Point3D calculerPoint3D(double u, double v) {
                return super.calculerPoint3D(v, u);
            }
        }
      */
    private static final int VUE_1 = 30;
    private static final int FPS = 50;
    Plane polygon = new Plane();
    Plane polygon1 = new Plane();
    ImageTexture imageTextureTrunk;
    private ITexture sol_sableux;
    private ITexture ciel_ensoleille;

    public static void main(String[] args) {
        Galerie sunset = new Galerie();
        sunset.loop(true);
        sunset.setMaxFrames(VUE_1 * FPS);
        sunset.setDimension(new Resolution(640, 480));
        new Thread(sunset).start();
    }

    @Override
    public void ginit() {
        super.ginit();
        BufferedImage read = ImageIO.read(new File("resources/dup12138.jpg"));
        VoronoiImageTexture voronoiImageTexture = new VoronoiImageTexture();
        PixM image = voronoiImageTexture.processInMemory(new PixM(read));
        try {
            polygon1.texture(new ImageTexture(new Image(image.getImage())));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        imageTextureTrunk = new ImageTexture(new Image(ImageIO.read(new File("resources/dup12138.jpg"))));
        ciel_ensoleille = new ImageTexture(new Image(ImageIO.read(new File("resources/ciel_ensoleille.jpg"))));
        sol_sableux = new ImageTexture(new Image(ImageIO.read(new File("resources/sol_sableux.jpg"))));

        frame = 1200;
    }

    @Override
    public void finit() throws Exception {
        super.finit();

        z().setDisplayType(ZBufferImpl.DISPLAY_ALL);

        if (frame() < VUE_1 * FPS) {
            //z().setDisplayType(Representable.DISPLAY_ALL);
            z().texture(new ColorTexture(java.awt.Color.BLACK.getRGB()));
            Plane polygon = new Plane();
            Plane polygon1 = new Plane();
            StructureMatrix<Point3D> mat = new StructureMatrix<>(2, Point.class);
            mat.setElem(new Point3D(-10d, 0d, -10d), 0, 0);
            mat.setElem(new Point3D(10d, 0d, -10d), 1, 0);
            mat.setElem(new Point3D(10d, 0d, 10d), 1, 1);
            mat.setElem(new Point3D(-10d, 0d, 10d), 0, 1);
        /*polygon.setMatrix( new Point3D[][]{
                {mat.getElem(0, 0), mat.getElem(1, 0)}, {mat.getElem(1, 0), mat.getElem(1, 1)}
        });*/

            scene().clear();

/*
            polygon.setPoints(
                    new Point3D[]{
                            mat.getElem(0, 0), mat.getElem(1, 0), mat.getElem(1, 1), mat.getElem(0, 1)
                    });
            polygon.setP0(mat.getElem(0, 0));

            polygon1.setPoints(
                    new Point3D[]{
                            mat.getElem(0, 0).plus(Point3D.Y), mat.getElem(1, 0).plus(Point3D.Y),
                            mat.getElem(1, 1).plus(Point3D.Y), mat.getElem(0, 1).plus(Point3D.Y)
                    });
*/
            Point3D[] vects = new Point3D[]{mat.getElem(0, 0), mat.getElem(0, 1), mat.getElem(1, 0)};

            StructureMatrix<Point3D>[] v = new StructureMatrix[]{
                    new StructureMatrix<Point3D>(0, Point3D.class),
                    new StructureMatrix<Point3D>(0, Point3D.class),
                    new StructureMatrix<Point3D>(0, Point3D.class)};


            v[0].setElem(vects[0]);
            v[1].setElem(vects[1]);
            v[2].setElem(vects[2]);

            polygon.setP0(v[0]);
            polygon.setvX(v[1]);
            polygon.setvY(v[2]);

            polygon.texture(new ColorTexture(new Color(Color.newCol(0f, 0.2f, 1.0f))));

            StructureMatrix<Point3D>[] v1 = new StructureMatrix[]{
                    new StructureMatrix<Point3D>(0, Point3D.class),
                    new StructureMatrix<Point3D>(0, Point3D.class),
                    new StructureMatrix<Point3D>(0, Point3D.class)};


            v1[0].setElem(vects[0].plus(Point3D.Y));
            v1[1].setElem(vects[1].plus(Point3D.Y));
            v1[2].setElem(vects[2].plus(Point3D.Y));


            polygon1.setP0(v1[0]);
            polygon1.setvX(v1[1]);
            polygon1.setvY(v1[2]);


            polygon.setIncrU(0.03);
            polygon.setIncrU(0.03);
            polygon1.setIncrV(0.03);
            polygon1.setIncrV(0.03);

            for (int i = 0; i < 10; i++)
                for (int j = 0; j < 10; j++) {
                    Point3D p1 = polygon.calculerPoint3D(1.0 * i / 10, 1.0 * j / 10);
                    Point3D p2 = polygon1.calculerPoint3D(1.0 * i / 10, 1.0 * j / 10);

                    Tubulaire3 t3 = new Tubulaire3();
                    FctXY fctXY = new FctXY();
                    fctXY.setFormulaX(String.valueOf(p1.moins(p2).norme() / 10));
                    t3.getDiameterFunction().setElem(fctXY);
                    CourbeParametriquePolynomialeBezier cppb = new CourbeParametriquePolynomialeBezier();
                    cppb.getCoefficients().add(p1);
                    cppb.getCoefficients().add(p2);
                    t3.getSoulCurve().setElem(cppb);


                    t3.texture(imageTextureTrunk);
                    scene().add(t3);
                }

            polygon1.texture(sol_sableux);
            sol_sableux.setRepeatX(10);
            sol_sableux.setRepeatY(10);
            polygon.texture(ciel_ensoleille);

            scene().add(polygon);
            scene().add(polygon1);
            Point3D eye = new Point3D(5.0, 0.2, 5.0);
            Point3D lookAt = new Point3D(-5.0, 0.2, 5.0);
            Point3D pos = eye.plus(lookAt.moins(eye).mult(1.0 * frame() / (FPS * VUE_1)));
            Camera camera = new Camera(pos, lookAt, Point3D.Y);
            //camera.calculerMatrice(Point3D.Y);
            scene().cameraActive(camera);
        }

    }
}
