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

/*__
 * *
 * Global license : * Microsoft Public Licence
 * <p>
 * author Manuel Dahmen <manuel.dahmen@gmx.com>
 * <p>
 * *
 */
package one.empty3.test.tests.tests2.portrait;

import one.empty3.library.*;
import one.empty3.library.core.testing.jvm.TestObjetSub;
import one.empty3.library.core.tribase.TRISphere;

import one.empty3.libs.*;

/*__
 *
 * @author Manuel Dahmen <manuel.dahmen@gmx.com>
 */
public class TestPortrait extends TestObjetSub {

    public static void main(String[] args) {
        TestPortrait tp = new TestPortrait();
        tp.loop(false);
        tp.run();
    }

    @Override
    public void testScene() throws Exception {

        Cube c1 = new Cube(2.0, new Point3D(0d, 0d, 0d), new ColorTexture(new Color(Color.RED)));

        Cube c2 = new Cube(1.5, new Point3D(1d, 1d, 0d), new ColorTexture(new Color(Color.YELLOW)));

        Cube cy1 = new Cube(0.5, new Point3D(2.1, 0d, 0d), new ColorTexture(new Color(Color.GREEN)));
        Cube cy2 = new Cube(0.5, new Point3D(2.1, 1d, 1d), new ColorTexture(new Color(Color.GREEN)));

        Cube c3 = new Cube(1, new Point3D(2d, 2d, 0d), new ColorTexture(new Color(Color.WHITE.getRGB())));

        TRISphere ts = new TRISphere(new Point3D(0d, 6d, -5d), 4d);

        ts.texture(new ColorTexture(new Color(Color.WHITE.getRGB())));

        scene().add(ts);

        Camera c = new Camera(new Point3D(10d, 10d, 10d), Point3D.O0);

        scene().add(new LineSegment(Point3D.O0, Point3D.X.mult(10d), new ColorTexture(new Color(Color.RED))));
        scene().add(new LineSegment(Point3D.O0, Point3D.X.mult(10d), new ColorTexture(new Color(Color.RED))));
        scene().add(new LineSegment(Point3D.O0, Point3D.Y.mult(10d), new ColorTexture(new Color(Color.GREEN))));
        scene().add(new LineSegment(Point3D.O0, Point3D.Z.mult(10d), new ColorTexture(new Color(Color.BLUE))));
        scene().add(new TRI(new Point3D(3.1, 3d, -1d), new Point3D(3.1, 1d, -1d), new Point3D(3.1, 3d, 1d), new ColorTexture(new Color(Color.WHITE.getRGB()))));

        scene().add(c1);
        scene().add(c2);
        scene().add(cy1);
        scene().add(cy2);
        scene().add(c3);
        scene().cameraActive(c);
        scene().lumieres()
                .add(
                        new LumierePonctuelle(new Point3D(5d, 20d, 5d), new Color(Color.MAGENTA)));
        scene().lumieres()
                .add(
                        new LumierePonctuelle(new Point3D(20d, 0d, 0d), new Color(Color.CYAN)));
    }

    @Override
    public void finit() {

    }

    @Override
    public void ginit() {

    }
}
