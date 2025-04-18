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

package one.empty3.test.tests.tests2.blob;


import one.empty3.library.*;
import one.empty3.library.core.extra.Blob1;
import one.empty3.library.core.testing.jvm.TestObjetSub;
import one.empty3.libs.Color;


/*__
 * @author Se7en
 */
public class TestBlob2 extends TestObjetSub {

    public int MAXFRAMES = 2000;
    public int N = 2;
    private ColorTexture tc = new ColorTexture(new Color(Color.red));
    private Blob1 ballec;
    private Point3D[] s;
    private Point3D[] v;
    private double V = 0.03;
    private double D = 1;

    public static void main(String[] args) {
        TestBlob2 th = new TestBlob2();

        th.loop(true);

        th.setResx(1920);

        th.setResy(1080);

        th.MAXFRAMES = 4000;

        new Thread(th).start();
    }

    @Override
    public void ginit() {
        z().setFORCE_POSITIVE_NORMALS(true);
        z().setDisplayType(Representable.DISPLAY_ALL);

        LumierePonctuelle lumierePonctuelle = new LumierePonctuelle(Point3D.X,new Color(Color.RED));
        lumierePonctuelle.setR0(1);

        scene().lumieres().add(lumierePonctuelle);

        lumierePonctuelle = new LumierePonctuelle(Point3D.Y, new Color(Color.BLUE));
        lumierePonctuelle.setR0(1);

        scene().lumieres().add(lumierePonctuelle);

        s = new Point3D[N];
        v = new Point3D[N];
        for (int i = 0; i < N; i++) {
            s[i] = new Point3D(Point3D.O0);

            s[i].texture(new ColorTexture(new Color(Color.GREEN)));

            v[i] = new Point3D(Math.random() * (V / 2 - V), Math.random() * (V / 2 - V), Math.random() * (V / 2 - V));

        }
        tc = new ColorTexture(new Color(Color.YELLOW));


        ballec = new Blob1(Point3D.O0, 2.0);


        ballec.texture(tc);

        scene().add(ballec);

        scene().lumieres().add(new LumierePonctuelle(Point3D.O0, new Color(Color.BLUE)));

        Camera camera;
        camera = new Camera(new Point3D(0d, 0d, 4d),
                new Point3D(0d, 0d, 0d), Point3D.Y);

        scene().cameraActive(camera);

    }

    public void bounce(int i) {
        s[i] = s[i].plus(v[i]);


        if (s[i].getX() > D && v[i].getX() > 0) {
            v[i].setX(-v[i].getX());
        }
        if (s[i].getX() < -D && v[i].getX() < 0) {
            v[i].setX(-v[i].getX());
        }
        if (s[i].getY() > D && v[i].getY() > 0) {
            v[i].setY(-v[i].getY());
        }
        if (s[i].getY() < -D && v[i].getY() < 0) {
            v[i].setY(-v[i].getY());
        }
        if (s[i].getZ() > D && v[i].getZ() > 0) {
            v[i].setZ(-v[i].getZ());
        }
        if (s[i].getZ() < -D && v[i].getZ() < 0) {
            v[i].setZ(-v[i].getZ());
        }
    }

    @Override
    public void finit() {
        for (int i = 0; i < s.length; i++) {
            bounce(i);
        }

        ballec.points().clear();
        double totalA = 0;
        double totalB = 0;

        for (int j = 0; j < s.length; j++) {
            if (s[j].getX() < 0) {
                s[j].setX(s[j].getX() + D);
            }
            if (s[j].getY() < 0) {
                s[j].setY(s[j].getY() + D);
            }
            if (s[j].getX() > D) {
                s[j].setX(s[j].getX() - D);
            }
            if (s[j].getY() > D) {
                s[j].setY(s[j].getY() - D);
            }

            totalA += s[j].getX();
            totalB += s[j].getY();

            ballec.addPoint(new Point2D(s[j].getX(), s[j].getY()));

        }


    }

}
