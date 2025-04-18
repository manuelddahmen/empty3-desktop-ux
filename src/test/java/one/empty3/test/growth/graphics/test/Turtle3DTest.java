
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
package one.empty3.test.growth.graphics.test;


import junit.framework.TestCase;
import one.empty3.growth.LSystem;
import one.empty3.growth.NotWellFormattedSystem;
import one.empty3.growth.Symbol;
import one.empty3.growth.SymbolSequence;
import one.empty3.growth.graphics.Rotation;
import one.empty3.growth.graphics.Rotation2;
import one.empty3.growth.graphics.Turtle3D_1;
import one.empty3.library.*;
import one.empty3.library.core.testing.jvm.TestObjetStub;
import one.empty3.library.core.tribase.TRISphere;
import one.empty3.libs.Color;
import one.empty3.test.growth.test.TestCaseExtended;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import one.empty3.libs.Image;

import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@RunWith(JUnit4.class)

public class Turtle3DTest extends TestCaseExtended {

    @Override
    public void setUp() throws Exception {
        super.setUp();

    }

    public ZBuffer fct() {
        ZBuffer z = ZBufferFactory.instance(1600, 1200);
        z.backgroundTexture(new ColorTexture(new Color(Color.newCol(90, 160, 50))));
        Turtle3D_1 turtle3D;
        Camera camera = (new Camera(new Point3D(0., 0., -200.), new Point3D(0., 0., 0.)));
        Scene scene = new Scene();
        scene.cameraActive(camera);
        z.scene(scene);
        turtle3D = new Turtle3D_1(z);

        turtle3D.setColor(new Color(java.awt.Color.BLACK.getRGB()));
        turtle3D.line(100);
        turtle3D.rotateU(Math.PI / 2);
        turtle3D.rotateU(Math.PI / 2);
        turtle3D.line(100);
        turtle3D.rotateU(Math.PI / 2);
        turtle3D.line(100);
        turtle3D.rotateU(Math.PI / 2);
        turtle3D.line(100);
        return z;
    }

    @Test
    public void testSquaresXYZaxis() {
        //writeImage(fct());

    }

    @Test
    public void testSquaresXYZaxis_3() {
//        ZBuffer z = ZBufferFactory.instance(1600, 1200);
//        Scene scene = new Scene();
//        z.scene(scene);
//        scene.cameraActive(new Camera(new Point3D(0., 0., -200.), new Point3D(0., 0., 0.)));
//        Turtle3D_3 turtle3D = new Turtle3D_3();
//
//        turtle3D.setzBuffer(z);
//
//
//        turtle3D.setColor(java.awt.Color.BLACK.getRGB();
//        turtle3D.line(100);
//        turtle3D.rotL(Math.PI / 2);
//        turtle3D.line(100);
//        turtle3D.rotL(Math.PI / 2);
//        turtle3D.line(100);
//        turtle3D.rotL(Math.PI / 2);
//        turtle3D.line(100);
//        turtle3D.rotL(Math.PI / 2);
//
//
//        writeImage(turtle3D.getzBuffer());

    }


    public static class TestBasic extends TestCase {
        @Override
        public void setUp() throws Exception {
            super.setUp();
        }

        public void generate1(LSystem lSystem) throws NotWellFormattedSystem {
            lSystem.applyRules();
        }

        @Test
        public void test0() {
            assertTrue(new SymbolSequence(new Symbol('A')).equals(new SymbolSequence(new Symbol('A'))));
        }

        @Test
        public void testA() throws NotWellFormattedSystem {

            LSystem lSystem = new LSystem();
            lSystem.init();
            SymbolSequence a1 = new SymbolSequence(new Symbol('A'));
            SymbolSequence a = new SymbolSequence(new Symbol('A'));
            lSystem.addRule(a, a1);


            lSystem.setCurrentSymbols("A");

            Logger.getAnonymousLogger().log(Level.INFO, "" + lSystem);

            lSystem.applyRules();


            Logger.getAnonymousLogger().log(Level.INFO, "" + lSystem);

            SymbolSequence a2 = new SymbolSequence(new Symbol('A'));
            a2.add(new Symbol('A'));

            assertTrue(lSystem.getCurrentSymbols().equals(a2));//??

        }

        public void generateN(LSystem lSystem, int n) throws NotWellFormattedSystem {
            SymbolSequence ab = new SymbolSequence();
            ab.add(new Symbol('A'));
            ab.add(new Symbol('B'));

            SymbolSequence a1 = new SymbolSequence(new Symbol('A'));
            SymbolSequence b = new SymbolSequence(new Symbol('B'));
            SymbolSequence a = new SymbolSequence(new Symbol('A'));

            lSystem.addRule(a, ab);// a -> ab
            lSystem.addRule(b, a1);// b -> a

            Logger.getAnonymousLogger().log(Level.INFO, "BEFORE" + lSystem.toString());
            for (int i = 0; i < n; i++) {

                generate1(lSystem);

                //Logger.getAnonymousLogger().log(Level.INFO, "AFTER " + (i + 1) + " PASS : " + lSystem.toString());
            }
        }

        @Test
        public void testGenerate2() throws NotWellFormattedSystem {
            LSystem lSystem = new LSystem();
            lSystem.init();
            lSystem.getCurrentSymbols().add(new Symbol('A'));
            generateN(lSystem, 2);
            SymbolSequence symbolSequence = new SymbolSequence();
            symbolSequence.add(new Symbol('A'));
            symbolSequence.add(new Symbol('B'));
            symbolSequence.add(new Symbol('A'));
            //assertTrue(lSystem.getCurrentSymbols().equals(symbolSequence));
        }

        @Test
        public void testGenerate1() throws NotWellFormattedSystem {

            LSystem lSystem = new LSystem();
            lSystem.init();
            lSystem.getCurrentSymbols().add(new Symbol('A'));

            // run
            generateN(lSystem, 1);

            // Waited for:
            SymbolSequence symbolSequence = new SymbolSequence();
            symbolSequence.add(new Symbol('A'));
            symbolSequence.add(new Symbol('B'));

            // evaluate the answer
            //assertTrue(lSystem.getCurrentSymbols().equals(symbolSequence));
        }
    }

    public static class TestMethodEqualsSymbol extends TestCase {
        @Override
        public void setUp() throws Exception {
            super.setUp();
        }

        @Test
        public void testEmpty() {
            SymbolSequence a = new SymbolSequence();
            SymbolSequence b = new SymbolSequence();

            assertTrue(a.equals(b));
        }

        @Test
        public void testEmpty2() {
            assertFalse(new SymbolSequence().equals(new SymbolSequence(new Symbol('a'))));
        }

        @Test
        public void test1symbolDiff() {
            SymbolSequence a = new SymbolSequence(new Symbol('a'));
            SymbolSequence b = new SymbolSequence(new Symbol('b'));

            assertFalse(a.equals(b));
        }

        @Test
        public void test1symbolId() {
            SymbolSequence a = new SymbolSequence(new Symbol('a'));
            SymbolSequence b = new SymbolSequence(new Symbol('a'));

            assertTrue(a.equals(b));
        }
    }

    public static class TestRotation extends TestCase {
        @Override
        public void setUp() throws Exception {
            super.setUp();
        }


        @Test
        public void testRotationIdent1() {
            Point3D x = Rotation.rotate(Point3D.O0, Point3D.X,
                    2 * Math.PI, Point3D.Y);
            Point3D y = Point3D.Y;

            assertEqualsPoint3D(x, y, 0.1);

        }

        @Test
        public void testRotationIdent2() {
            Point3D x = Rotation.rotate(Point3D.O0, Point3D.X,
                    2 * Math.PI, Point3D.X);
            Point3D y = Point3D.X;

            assertEqualsPoint3D(x, y, 0.1);

        }

        @Test
        public void testRotationIdent3() {
            Point3D x = Rotation.rotate(Point3D.O0, Point3D.X,
                    2 * Math.PI, Point3D.Z);
            Point3D y = Point3D.Z;

            assertEqualsPoint3D(x, y, 0.1);

        }

        @Test
        public void testRotation90() {
            Point3D x = Rotation.rotate(Point3D.O0, Point3D.X,
                    Math.PI, Point3D.Z);
            Point3D y = Point3D.Z.mult(-1);

            assertEqualsPoint3D(x, y, 0.1);


        }

        @Test
        public void testRotationNonO() {
            Point3D x = Rotation.rotate(Point3D.X, new Point3D(10., 0., 0.),
                    Math.PI, new Point3D(3., 5., 5.));
            Point3D y = new Point3D(3., -5., -5.);

            assertEqualsPoint3D(x, y, 0.1);

        }

        @Test
        public void testRotation180() {
            Point3D x = Rotation.rotate(new Point3D(11., 0., 0.), new Point3D(10., 0., 0.),
                    Math.PI, new Point3D(3., 5., 0.));
            Point3D y = new Point3D(3., -5., 0.);

            assertEqualsPoint3D(x, y, 0.1);

        }

        @Test
        public void testRotation30deg() {
            Point3D x = new Point3D(3., 5., 5.);
            Point3D y = x;

            for (int i = 0; i < 12 * 2; i++) {
                x = Rotation.rotate(Point3D.X, new Point3D(10., 0., 0.),
                        Math.PI / 6, x);
            }


            assertEqualsPoint3D(x, y, 0.1);

        }

        @Test
        public void testRotation0degRandomPoint() {
            Point3D x = Point3D.random(10.);

            Point3D y = Rotation.rotate(Point3D.X, new Point3D(10., 0., 0.),
                    0., x);

            assertEqualsPoint3D(x, y, 0.1);

        }

        @Test
        public void testRotation0degRandomAxe() {
            Point3D a = Point3D.random(10.);
            Point3D b = Point3D.random(10.);

            Point3D y = Rotation.rotate(a, b,
                    0., Point3D.X);

            assertEqualsPoint3D(Point3D.X, y, 0.1);

        }

        @Test
        public void testRotation360degRandomAxe() {
            Point3D a = Point3D.random(10.);
            Point3D b = Point3D.random(10.);

            Point3D y = Rotation.rotate(a, b,
                    2 * Math.PI, Point3D.X);

            assertEqualsPoint3D(Point3D.X, y, 0.1);

        }

        public void assertEqualsPoint3D(Point3D x, Point3D y, double delta) {
            for (int i = 0; i < 3; i++) {
                assertEquals(y.get(i), x.get(i), delta);
            }
        }

        @Test
        public void testRotationMethode2() {
            Rotation2 rotation2 = new Rotation2();

            Point3D intersection = rotation2.projection(Point3D.X, Point3D.Y, new Point3D(6., 5., 6.));
            assertEqualsPoint3D(intersection, new Point3D(1., 5., 0.), 0.001);

        }

    }

    public static class TestRotation2 extends TestObjetStub {


        private Color random() {
            float r = Math.abs((float) Math.random());
            float g = Math.abs((float) Math.random());
            float b = Math.abs((float) Math.random());
            double n = Math.sqrt(r * r + g * g + b * b);
            return Color.newCol((float) (r / n), (float) (g / n), (float) (b / n));
        }

        @Test
        public void testScene() {
            double MAX = 200.0;
            double MAXCERLE = 10;
            Point3D p0 = Point3D.Y;
            p0.texture(new ColorTexture(java.awt.Color.BLACK.getRGB()));
            for (int axeNo = 0; axeNo < 10; axeNo++) {
                Point3D random = Point3D.random(10.);
                Point3D random2 = Point3D.random(10.);
                Color color1 = random();
                Color color2 = random();
                for (int i = 0; i < MAX; i++) {
                    //Color color = CouleurOutils.couleurRatio(color1, color2, i/MAX);
                    Rotation rotation = new Rotation(random, Point3D.Y, 2 * Math.PI * i / MAX);
                    Point3D p = rotation.rotate(random2);
                    p.texture(new ColorTexture(color1));
                    LineSegment segmentDroite = new LineSegment(p0, p);
                    segmentDroite.texture(new ColorTexture(color1));
                    TRISphere triSphere = new TRISphere(p, 1.0);
                    triSphere.texture(new ColorTexture(color1));
                    scene().add(segmentDroite);
                    p0 = p;
                    camera().setEye(new Point3D(0., 0., -1000.0));
                }
            }
        }

        public static void main(String[] args) {
            TestRotation2 test = new TestRotation2();
            test.loop(false);
            new Thread(test).start();
        }
    }

    public static class TestRotation2_methodeBis extends TestCaseExtended {
        @Override
        public void setUp() throws Exception {
            super.setUp();
        }


        private Rotation2 rot = new Rotation2();

        private Point3D rotate(Point3D A, Point3D B,
                               double angle, Point3D X) {
            return rot.rotation(X, A, B, angle);
        }

        @Test
        public void testRotationIdent1() {
            Point3D x = rot.rotation(Point3D.Y, Point3D.X,
                    Point3D.O0, 2 * Math.PI);
            Point3D y = Point3D.Y;

            assertEqualsPoint3D(x, y, 0.1);

        }

        @Test
        public void testRotationIdent2() {
            Point3D x = rotate(Point3D.O0, Point3D.X,
                    2 * Math.PI, Point3D.X);
            Point3D y = Point3D.X;

            assertEqualsNaNPoint3D(x);
        }


        @Test
        public void testRotationIdent3() {
            Point3D x = rotate(Point3D.O0, Point3D.X,
                    2 * Math.PI, Point3D.Z);
            Point3D y = Point3D.Z;

            assertEqualsPoint3D(x, y, 0.1);

        }

        @Test
        public void testRotation90() {
            Point3D x = rotate(Point3D.O0, Point3D.X,
                    Math.PI, Point3D.Z);
            Point3D y = Point3D.Z.mult(-1);

            assertEqualsPoint3D(x, y, 0.1);


        }

        @Test
        public void testRotationNonO() {
            Point3D x = rotate(Point3D.X, new Point3D(10., 0., 0.),
                    Math.PI, new Point3D(3., 5., 5.));
            Point3D y = new Point3D(3., -5., -5.);

            assertEqualsPoint3D(x, y, 0.1);

        }

        @Test
        public void testRotation180() {
            Point3D x = rotate(new Point3D(11., 0., 0.), new Point3D(10., 0., 0.),
                    Math.PI, new Point3D(3., 5., 0.));
            Point3D y = new Point3D(3., -5., 0.);

            assertEqualsPoint3D(x, y, 0.1);

        }

        @Test
        public void testRotation30deg() {
            Point3D x = new Point3D(3., 5., 5.);
            Point3D y = x;

            for (int i = 0; i < 12 * 2; i++) {
                x = rotate(Point3D.X, new Point3D(10., 0., 0.),
                        Math.PI / 6, x);
            }


            assertEqualsPoint3D(x, y, 0.1);

        }

        @Test
        public void testRotation30degRandomAxe() {
            Point3D A = Point3D.random(100.);
            Point3D B = Point3D.random(100.);
            Point3D X = Point3D.random(100.);
            Point3D Y = X;

            for (int i = 0; i < 12; i++) {
                X = rotate(A, B, Math.PI / 6, X);
            }


            assertEqualsPoint3D(X, Y, 0.1);

        }

        @Test
        public void testRotation0degRandomPoint() {
            Point3D x = Point3D.random(10.);

            Point3D y = rotate(Point3D.X, new Point3D(10., 0., 0.),
                    0., x);

            assertEqualsPoint3D(x, y, 0.1);

        }

        @Test
        public void testRotation0degRandomAxe() {
            Point3D a = Point3D.random(10.);
            Point3D b = Point3D.random(10.);

            Point3D y = rotate(a, b,
                    0, Point3D.X);

            assertEqualsPoint3D(Point3D.X, y, 0.1);

        }

        @Test
        public void testRotation360degRandomAxe() {
            Point3D a = Point3D.random(10.);
            Point3D b = Point3D.random(10.);

            Point3D y = rotate(a, b,
                    2 * Math.PI, Point3D.X);

            assertEqualsPoint3D(Point3D.X, y, 0.1);

        }

        @Test
        public void testRotation360deg300RandomAxe() {
            Image image = new Image(1600, 1200, Image.TYPE_INT_RGB);
            Graphics graphics = image.getGraphics();
            graphics.setColor(Color.YELLOW);
            Point3D y = Point3D.O0;
            for (double angle = 0; angle < 2 * Math.PI; angle += 2 * Math.PI / 1000) {
                Point3D a = Point3D.random(50.);
                Point3D b = Point3D.random(50.);


                y = rotate(a, b,
                        angle, y);

                Point3D plus = y.plus(new Point3D(image.getWidth() / 2., image.getHeight() / 2., 0.));
                graphics.drawLine((int) (double) plus.getX(), (int) (double) plus.getY(), (int) (double) plus.getX(), (int) (double) plus.getY());

                Point3D y2 = y;

                for (int i = 0; i < 1000; i++) {
                    Point3D c = Point3D.random(50.);
                    Point3D d = Point3D.random(50.);
                    double angleB = 2 * Math.PI / 1000;
                    y2 = rotate(c, d,
                            angleB, y2);
                    plus = y2.plus(new Point3D(image.getWidth() / 2., image.getHeight() / 2., 0.));
                    graphics.drawLine((int) (double) plus.getX(), (int) (double) plus.getY(), (int) (double) plus.getX(), (int) (double) plus.getY());

                }
            }
            writeImage(image);
        }


        @Test
        public void testRotationMethode2() {
            Rotation2 rotation2 = new Rotation2();

            Point3D intersection = rotation2.projection(Point3D.X, Point3D.Y, new Point3D(6., 5., 6.));
            assertEqualsPoint3D(intersection, new Point3D(1., 5., 0.), 0.001);

        }

    }
}
