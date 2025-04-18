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

/*
 * 2013 Manuel Dahmen
 */
package one.empty3.test.tests.tests2.cubes;

import one.empty3.library.*;
import one.empty3.library.core.testing.jvm.TestObjetSub;

import one.empty3.libs.*;

import java.util.ArrayList;


/*__
 * @author Se7en
 */
public class TestCubes extends TestObjetSub {

    public static void main(String[] args) {
        TestCubes tc = new TestCubes();

        tc.loop(true);

        tc.setMaxFrames(25 * 60);

        new Thread(tc).start();
    }

    @Override
    public void ginit() {
        scene().add(new Cube(50, 10,Color.newCol(1f,0f,1f)));


    }

    private double z(int nof) {
        return 250.0 +  (1.0 * (getMaxFrames()-frame()) / getMaxFrames());
    }

    @Override
    public void testScene() {
        scene().cameras().clear();

        scene().cameraActive(new Camera(
                new Point3D(0d, 0d, z(getMaxFrames())),
                new Point3D(0d, 0d, 1000d),
                Point3D.Y
        ));
        //scene().cameraActive().calculerMatrice(Point3D.Y);
    }

    @Override
    public void finit() {

    }
}

class Cube extends RepresentableConteneur {
    private final ArrayList<Representable> cube = new ArrayList<Representable>();

    public Cube(double dim, int pas, Color c) {
        if (dim < 0)
            return;
        for (double i = -dim / 2; i < dim / 2 + pas; i += pas)
            for (double j = -dim / 2; j < dim / 2 + pas; j += pas)
                for (double k = -dim / 2; k < dim / 2 + pas; k += pas) {
                    if (i + dim / pas < dim / 2 + pas) {
                        cube.add(new LineSegment(
                                        new Point3D(i, j, k),
                                        new Point3D(i + dim / pas, j, k),
                                        new ColorTexture(c)
                                )
                        );
                    }
                    if (j + dim / pas < dim / 2 + pas)
                        cube.add(new LineSegment(
                                        new Point3D(i, j, k),
                                        new Point3D(i, j + dim / pas, k),
                                        new ColorTexture(c)
                                )
                        );
                    if (k + dim / pas < dim / 2 + pas)
                        cube.add(new LineSegment(
                                        new Point3D(i, j, k),
                                        new Point3D(i, j, k + dim / pas),
                                        new ColorTexture(c)
                                )
                        );
                }
    }

    public void deforme(Point3D p) {
    }

    public ArrayList<Representable> getListRepresentable() {
        return cube;
    }

}