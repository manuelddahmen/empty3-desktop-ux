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

package one.empty3.test.tests.tests2.ver;

import one.empty3.apps.feature.app.replace.javax.imageio.ImageIO;
import one.empty3.library.*;
import one.empty3.library.core.testing.jvm.TestObjetSub;
import one.empty3.library.core.tribase.TubulaireN;

import one.empty3.libs.*;

import java.io.File;
import java.util.HashMap;

/*__
 * @author Se7en
 */
public class Serpentins extends TestObjetSub {

    public int MAXFRAMES = 2000;
    public int Ns = 4;
    public int N = 7;
    public int Ncolors = 6;
    private Point3D[][] s = new Point3D[Ns][N];
    private Point3D[][] v = new Point3D[Ns][N];
    private double V = 0.08;
    private double D = 1.0;
    private double TUBE_RAYON = 0.1;
    private HashMap<Point2D, Color> map = new HashMap<Point2D, Color>();
    private TubulaireN[] tubulaireN = new TubulaireN[Ns];
    private ITexture[] itext = new ITexture[Ns];

    public static void main(String[] args) {
        Serpentins th = new Serpentins();

        th.loop(true);

        th.setGenerate(GENERATE_IMAGE | GENERATE_MOVIE);

        th.run();
    }

    @Override
    public void ginit() {
        for (int i = 0; i < Ns; i++) {
            itext[i] = new TextureImg(new Image(ImageIO.read(new File("c:\\Emptycanvas\\textures\\photo" + (i + 1) + ".jpg"))));
        }
        LumierePonctuelle lumierePonctuelle = new LumierePonctuelle(Point3D.X,new Color(Color.RED));
        lumierePonctuelle.setR0(1);

        scene().lumieres().add(lumierePonctuelle);

        lumierePonctuelle = new LumierePonctuelle(Point3D.Y, new Color(Color.GREEN));
        lumierePonctuelle.setR0(1);

        scene().lumieres().add(lumierePonctuelle);

        s = new Point3D[Ns][N];
        v = new Point3D[Ns][N];
        for (int j = 0; j < Ns; j++) {
            for (int i = 0; i < N; i++) {

                s[j][i] = rand(-D, D);

                v[j][i] = rand(-V, V);

            }
        }
        scene().lumieres().add(new LumierePonctuelle(Point3D.O0, new Color(Color.WHITE.getRGB())));

        Camera camera;
        camera = new Camera(new Point3D(0d, 0d, -3d),
                new Point3D(0d, 0d, 0d));

        scene().cameraActive(camera);

    }

    public Point3D rand(double limitMinus, double limitPlus) {
        Double[] d = new Double[3];
        for (int i = 0; i < 3; i++) {
            d[i] = (limitPlus - limitMinus) * Math.random() + limitMinus;
        }
        return new Point3D(d, null);
    }

    public void bounce(int i, int j) {
        s[i][j] = s[i][j].plus(v[i][j]);

        if (s[i][j].getX() > D && v[i][j].getX() > 0) {
            v[i][j].setX(-v[i][j].getX());
        }
        if (s[i][j].getX() < -D && v[i][j].getX() < 0) {
            v[i][j].setX(-v[i][j].getX());
        }
        if (s[i][j].getY() > D && v[i][j].getY() > 0) {
            v[i][j].setY(-v[i][j].getY());
        }
        if (s[i][j].getY() < -D && v[i][j].getY() < 0) {
            v[i][j].setY(-v[i][j].getY());
        }
        if (s[i][j].getZ() > D && v[i][j].getZ() > 0) {
            v[i][j].setZ(-v[i][j].getZ());
        }
        if (s[i][j].getZ() < -D && v[i][j].getZ() < 0) {
            v[i][j].setZ(-v[i][j].getZ());
        }
    }

    @Override
    public void testScene() throws Exception {
        for (TubulaireN ti : tubulaireN)
            scene().remove(ti);

        for (int i = 0; i < s.length; i++) {
            for (int j = 0; j < s[i].length; j++) {
                bounce(i, j);
            }
        }
        for (int i = 0; i < Ns; i++) {
            tubulaireN[i] = new TubulaireN();
            tubulaireN[i].diam((float) TUBE_RAYON);
            tubulaireN[i].texture(itext[i]);
            tubulaireN[i].nbrAnneaux(40);
            tubulaireN[i].nbrRotations(30);
            scene().add(tubulaireN[i]);
        }

        int i = 0;
        for (Point3D[] pp : s) {
            for (Point3D p : pp) {
                tubulaireN[i].addPoint(p);
            }
            i++;
        }

    }

    private Matrix33 matrix1(double a, double b) {
        return Matrix33.rot(a, b);
    }

    @Override
    public void finit() {

    }
}
