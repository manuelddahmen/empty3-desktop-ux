package one.empty3.test;/*
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

import one.empty3.growth.graphics.Rotation;
import one.empty3.library.ColorTexture;
import one.empty3.library.LineSegment;
import one.empty3.library.Point3D;
import one.empty3.library.core.testing.TestObjetStub;
import one.empty3.library.core.tribase.TRISphere;

import one.empty3.libs.*;

public class TestRotation2 extends TestObjetStub {


    private Color random() {
        float r = Math.abs((float) Math.random());
        float g = Math.abs((float) Math.random());
        float b = Math.abs((float) Math.random());
        double n = Math.sqrt(r * r + g * g + b * b);
        return new Color((float) (r / n), (float) (g / n), (float) (b / n));
    }

    public void testScene() {
        double MAX = 200.0;
        double MAXCERLE = 10.;
        Point3D p0 = Point3D.Y;
        p0.texture(new ColorTexture(java.awt.Color.BLACK));
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
