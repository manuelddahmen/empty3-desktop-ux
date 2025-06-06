/*
 * Copyright (c) 2024.
 *
 *
 *  Copyright 2023 Manuel Daniel Dahmen
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 */

package one.empty3.androidFeature;

import one.empty3.feature.PixM;
import one.empty3.io.ProcessFile;

import one.empty3.ImageIO;
import one.empty3.libs.Color;
import one.empty3.libs.Image;

import java.io.File;
import java.util.ArrayList;

import java.util.List;

import one.empty3.library.core.lighting.Colors;

/*
 * radial density of region (x, y, r)
 * by mean or mean square or somewhat else.
 */
public class RegionLineCorner extends ProcessFile {
    public final int numLevels = 5;
    private one.empty3.feature.PixM m = null;
    private double[] max;
    private double[] min;

    public class Circle {
        public double x, y, r;
        public double i;

        public Circle(double x, double y, double r) {
            this.x = x;
            this.y = y;
            this.r = r;
            i = 0.0;
        }

        @Override
        public String toString() {
            return "Circle{" +
                    "x=" + x +
                    ", y=" + y +
                    ", r=" + r +
                    ", i=" + i +
                    '}';
        }

        @Override
        public Object clone() {
            Circle c = new Circle(x, y, r);
            c.i = i;
            return c;
        }

        public double dist(Circle c) {
            return Math.sqrt((x - c.x) * (x - c.x) +
                    (y - c.y) * (y - c.y));
        }
    }

    //private final int[][][] levels;
    public void setM(one.empty3.feature.PixM m2) {
        this.m = m2;
    }


    public RegionLineCorner() {


        min = new double[numLevels];
        max = new double[numLevels];

        for (int i = 0; i < numLevels; i++) {
            min[i] = 1.0 * i / numLevels;
            max[i] = 1.0 * (i + 1) / numLevels;
        }


    }

    public void makeHistogram(double r) {

    }

    public Circle getLevel(Circle c) {
        // I mean. Parcourir le cercle
        // mesurer I / numPoints
        // for(int i=Math.sqrt()
        //  return c;
        int count = 0;
        double intensity = 0.0;
        for (double i = c.x - c.r; i <= c.x + c.r; i++) {
            for (double j = c.y - c.r; j <= c.y + c.r; j++) {
                if (Math.sqrt((i - c.x) * (i - c.x) + (j - c.y) * (j - c.y)) <= c.r * c.r
                        && c.x - c.r >= 0 && c.y - c.r >= 0 && c.x + c.r < m.getColumns() && c.y + c.r < m.getLines()) {
                    intensity += m.getIntensity((int) i, (int) j);
                    count++;
                }
            }
        }

        if (count > 0) {
            c.i = intensity / count;
        } else {
            c.i = 0.0;
            c.r = 0;
        }


        return c;
    }

    public double nPoints(int x, int y, int w, int h) {
        return 0.0;
    }

    public one.empty3.feature.PixM reconstruct(List<List<Circle>> circle) {
        one.empty3.feature.PixM rec = m.copy();
        return rec;
    }

    public double distance(Circle c1, Circle c2) {
        // ecart moyen et c r1->r2 sur le chemin
        // entre les deux et c i1-i2 < ecart moyen
        Circle ci = (Circle) c1.clone();
        double incrx = c2.x - c1.x;
        double incry = c2.y - c1.y;
        // parcourir jusqu a c2.
        double iAvg = 0.0;
        while (c2.dist(ci) > 0.0) {
            iAvg += getLevel(ci).i;


            ci.x = ci.x + incrx / 10;
            ci.y = ci.y + incry / 10;
        }
        if (Math.abs(iAvg) / 10 < Math.abs(c1.i - c2.i) * 2)
            return 0.0;
        return 1.0;
    }

    public List<Circle> getPointsOfInterest(double rMin0) {
        ArrayList<Circle> circles;
        circles = new ArrayList<>();

        // Classer les points par intensité et rayon

//        for(double intensity=1.0; intensity>=0.4; intensity-=0.1) {
        for (int i = 0; i < m.getColumns(); i++) {
            for (int j = 0; j < m.getLines(); j++) {
                double rMin = rMin0;
                Circle level = getLevel(new Circle(i, j, rMin));
                level.i = 0;
                getLevel(level);
                //int index = Math.max(((int) (level.i * numLevels)), 0);
                //index = Math.min(numLevels-1, index);
                double iOrigin = getLevel(level).i;
                double maxI = max[1];
                double minI = min[0];

                while (level.i > iOrigin - maxI && level.i < iOrigin + maxI && rMin < Math.max(m.getColumns(), m.getLines())) {

                    rMin *= 1.3;
                    //index = Math.max(((int) (level.i * numLevels)), 0);
                    //index = Math.min(numLevels-1, index);
                    //maxI = max[index];
                    //minI = min[index];
                    getLevel(level);
                }
                level.r = rMin;
                if (level.r >= 1) {
                    circles.add(level);
                }
            }
        }


        return circles;
    }


    public double computeAvg(List<List<Circle>> circles) {
        double error = 0.0;
        for (int i = 0; i < circles.size(); i++) {
            double din = 0.0;
            for (int j = 0; j < circles.get(i).size(); j++) {
                din += distance(circles.get(i).get(j),
                        circles.get(i).
                                get((j + 1) % circles.get(i).size()));
            }
            if (din > 0.0)
                error += 1.0;
        }
        return error;
    }


    public List<List<Circle>> group(List<Circle> circles) {
        List<List<Circle>> out = new ArrayList<>();
        out.add(circles);
        double de1 = computeAvg(out);
        int nit = 0;
        while (de1 > 0.0 && nit < 1000) {
            for (int i = 0; i < out.size(); i++) {

                for (int j = 0; j < out.get(i).size(); j++) {
                    boolean newlist = false;
                    for (int i2 = 0; i2 <= out.size(); i2++) {
                        //move i,j -> i2
                        if (i2 == out.size()) {
                            //move to new list i2
                            out.add(new ArrayList<>());
                            newlist = true;
                        }
                        Circle c = out.get(i).get(j);
                        out.get(i).remove(c);
                        out.get(i2).add(c);
                        //recompute de
                        double de2 = computeAvg(out);
                        //    if de2 < de1
                        //        cancel move 1
                        if (de2 > de1) {
                            // revert
                            out.get(i2).remove(c);
                            out.get(i).add(c);

                        } else {

                        }

                        de1 = computeAvg(out);

                        if (newlist) {
                            break;
                        }
                        //    if de3 < de
                        //        cancel create 1
                    }
                    /// clear empty lists
                }

            }
            nit++;
        }
        return out;

    }

    public boolean process(File in, File out) {
        File directory = new File(out.getParent());
        one.empty3.feature.PixM imageCoutours = PixM.getPixM(ImageIO.read(in), 500.0);
        this.m = imageCoutours;
        Image file = m.getImage();

        int levels = 10;
        double min = 0.0;
        double radiusIncr = 2;
        for (int i = 0; i < levels; i++) {

            Image img = file;
            Image img2 = new Image(img.getWidth(), img.getHeight());
            Image img3 = new Image(img.getWidth(), img.getHeight());
            Image img4 = new Image(img.getWidth(), img.getHeight());

            int finalI = i;
            List<Circle> pointsOfInterest = getPointsOfInterest(levels);

        /*

        pointsOfInterest.stream().forEach(circle -> {
                if (circle.i >= min && circle.r>0) {
                    Graphics graphics = img.getGraphics();
                    graphics.setColor(Color.WHITE);
                    graphics.drawOval((int) (circle.x - circle.r), (int) (circle.y - circle.r), (int) (circle.r * 2), (int) (circle.r * 2));
                    graphics = img2.getGraphics();
                    Color color = new Color((float) circle.i, 0f, (float) (circle.i / circle.r));
                    graphics.setColor(color);
                    graphics.drawOval((int) (circle.x - circle.r), (int) (circle.y - circle.r), (int) (circle.r * 2), (int) (circle.r * 2));
                    img3.setRGB((int) (circle.x), (int) (circle.y), color.getRGB());
                }
            });*/
            // grands;cercles = grandes iles les separer
            // verifier les distances et constantes i
            // petits cercles successifs entoures
            // de grands ou plus grands cercles =
            // coins, corners et possibles features.
           /* pointsOfInterest.sort(new Comparator<Circle>() {
                @Override
                public int compare(Circle o1, Circle o2) {
                    double v = o2.r - o1.r;
                    if(v<0)
                         return -1;
                    if(v>0)
                         return 1;
                    return 0;
                }
            });*/
            // grouper les points par similarites et distances
            List<List<Circle>> circles = group(pointsOfInterest);
            for (List<Circle> lc : circles) {
                Color color = new Colors().random();
                for (Circle c : lc) {

                    img4.setRgb((int) (c.x), (int) (c.y), color.getRGB());
                }
            }
            File fileToWrite = new File(directory.getAbsolutePath()
                    + "level" + finalI + ".jpg");
            File fileToWrite2 = new File(directory.getAbsolutePath()
                    + "level" + finalI + "_NEW.jpg");
            File fileToWrite3 = new File(directory.getAbsolutePath()
                    + "level" + finalI + "_NEW_RGB.jpg");
            //fileToWrite.mkdirs();
            Image.saveFile(img4, "JPEG", out, shouldOverwrite);
            /*
            one.empty3.Image.saveFile(img, "JPEG", fileToWrite);
            one.empty3.Image.saveFile(img, "JPEG", fileToWrite2);
            one.empty3.Image.saveFile(img, "JPEG", fileToWrite3);
*/
        }

        return true;
    }
}
