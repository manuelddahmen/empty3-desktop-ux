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

import junit.framework.TestCase;
import one.empty3.growth.graphics.Rotation2;
import one.empty3.library.Point3D;
import one.empty3.growth.graphics.Rotation;

public class TestRotation extends TestCase {


    public void testRotationIdent1() {
        Point3D x = Rotation.rotate(Point3D.O0, Point3D.X,
                2 * Math.PI, Point3D.Y);
        Point3D y = Point3D.Y;

        assertEqualsPoint3D(x, y, 0.1);

    }

    public void testRotationIdent2() {
        Point3D x = Rotation.rotate(Point3D.O0, Point3D.X,
                2 * Math.PI, Point3D.X);
        Point3D y = Point3D.X;

        assertEqualsPoint3D(x, y, 0.1);

    }

    public void testRotationIdent3() {
        Point3D x = Rotation.rotate(Point3D.O0, Point3D.X,
                2 * Math.PI, Point3D.Z);
        Point3D y = Point3D.Z;

        assertEqualsPoint3D(x, y, 0.1);

    }

    public void testRotation90() {
        Point3D x = Rotation.rotate(Point3D.O0, Point3D.X,
                Math.PI, Point3D.Z);
        Point3D y = Point3D.Z.mult(-1.);

        assertEqualsPoint3D(x, y, 0.1);


    }

    public void testRotationNonO() {
        Point3D x = Rotation.rotate(Point3D.X, new Point3D(10., 0., 0.),
                Math.PI, new Point3D(3., 5., 5.));
        Point3D y = new Point3D(3., -5., -5.);

        assertEqualsPoint3D(x, y, 0.1);

    }

    public void testRotation180() {
        Point3D x = Rotation.rotate(new Point3D(11., 0., 0.), new Point3D(10., 0., 0.),
                Math.PI, new Point3D(3., 5., 0.));
        Point3D y = new Point3D(3., -5., 0.);

        assertEqualsPoint3D(x, y, 0.1);

    }

    public void testRotation30deg() {
        Point3D x = new Point3D(3., 5., 5.);
        Point3D y = x;

        for (int i = 0; i < 12 * 2; i++) {
            x = Rotation.rotate(Point3D.X, new Point3D(10., 0., 0.),
                    Math.PI / 6, x);
        }


        assertEqualsPoint3D(x, y, 0.1);

    }

    public void testRotation0degRandomPoint() {
        Point3D x = Point3D.random(10.);

        Point3D y = Rotation.rotate(Point3D.X, new Point3D(10., 0., 0.),
                0., x);

        assertEqualsPoint3D(x, y, 0.1);

    }

    public void testRotation0degRandomAxe() {
        Point3D a = Point3D.random(10.);
        Point3D b = Point3D.random(10.);

        Point3D y = Rotation.rotate(a, b,
                0., Point3D.X);

        assertEqualsPoint3D(Point3D.X, y, 0.1);

    }

    public void testRotation360degRandomAxe() {
        Point3D a = Point3D.random(10.);
        Point3D b = Point3D.random(10.);

        Point3D y = Rotation.rotate(a, b,
                2 * Math.PI, Point3D.X);

        assertEqualsPoint3D(Point3D.X, y, 0.1);

    }

    public void assertEqualsPoint3D(Point3D x, Point3D y, double delta) {
        for (int i = 0; i < 3; i++) {
            TestCase.assertEquals(y.get(i), x.get(i), delta);
        }
    }

    public void testRotationMethode2() {
        Rotation2 rotation2 = new Rotation2();

        Point3D intersection = rotation2.projection(Point3D.X, Point3D.Y, new Point3D(6., 5., 6.));
        assertEqualsPoint3D(intersection, new Point3D(1., 5., 0.), 0.001);

    }
}
