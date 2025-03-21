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

package one.empty3.apps.feature;

import one.empty3.feature.*;


import one.empty3.apps.feature.FilterMatPixM;
import one.empty3.feature.FilterPixM;



import one.empty3.feature.M3;

public class GradientFilter extends FilterMatPixM {

    protected int columnsIn = 2;
    protected int linesIn = 2;
    protected int columns;
    protected int lines;
    // Local minima; maxima
    private double[][][][] gNormalize;
    int incrOK = 0;
    private double[][][] mean;


    public GradientFilter(int imageColumns, int imageLines) {
        this.columns = imageColumns;
        this.lines = imageLines;
        initGNormalise();
    }


    @Override
    public one.empty3.feature.M3 filter(one.empty3.feature.M3 source) {
        initGNormalise();
        one.empty3.feature.M3 copy = source.copy();
        for (int j = 0; j < copy.lines; j++) {
            for (int i = 0; i < copy.columns
                    ; i++) {
                for (int ii = 0; ii < copy.columns; ii++) {
                    for (int ij = 0; ij < copy.lines; ij++) {
                        for (int c = 0; c < copy.getCompCount(); c++) {


                            copy.setCompNo(c);
                            source.setCompNo(c);
                            element(source, copy, i, j, ii, ij);
                        }

                    }
                }
            }
        }
        return norm(copy, copy.copy());
    }

    /*
     * Computes gradient element p(x,y)
     * @param source 2x2 inner image matrix
     * @param copy 2x2 inner image gradient matrix
     * @param i x cordinates of p(x, y)
     * @param j y cordinates of p(x, y)
     * @param ii (0, 1) gradX, gradY
     * @param ij (0, 1) difference, angle (delta phase)
     *
     */
    @Override
    public void element(one.empty3.feature.M3 source, one.empty3.feature.M3 copy, int i, int j, int ii, int ij) {
        double d = 1.0;
        double v = source.get(i, j, ii, ij);
        if (ii == 0 && ij == 0) {
            d = (-source.get(i - 1, j, ii, ij) + v) * columns;
        }
        if (ii == 0 && ij == 1) {
            d = Math.atan(((-source.get(i, j - 1, ii, ij) + v) /
                    (-source.get(i - 1, j, ii, ij) + v)) * columns);
        }
        if (ii == 1 && ij == 0) {
            d = (-source.get(i, j - 1, ii, ij) + v) * lines;
        }
        if (ii == 1 && ij == 1) {
            d = Math.atan(1 / (
                    (source.get(i, j + 1, ii, ij) - v) /
                            (source.get(i + 1, j, ii, ij) - v)) * lines);
        }
        if (ii >= 0 && ii < 2 && ij >= 0 && ij < 2)
            copy.set(i, j, ii, ij, d);
        else
            System.exit(-3);
        incrOK++;

        if (v < gNormalize[source.getCompNo()][ii][ij][0])
            gNormalize[source.getCompNo()][ii][ij][0] = v;
        if (v > gNormalize[source.getCompNo()][ii][ij][1])
            gNormalize[source.getCompNo()][ii][ij][1] = v;
        mean[source.getCompNo()][ii][ij] += v;
    }


    /*
     * Norme linéaire
     * Autre exemple : histogramme de valeurs = échelle pondérée
     *
     * @param image M3 image array
     * @return copy norm
     */

    public one.empty3.feature.M3 norm(one.empty3.feature.M3 image, one.empty3.feature.M3 copy) {
        for (int i = 0; i < image.columns
                ; i++)
            for (int j = 0; j < image.lines; j++) {
                for (int ii = 0; ii < image.columns; ii++)
                    for (int ij = 0; ij < image.lines; ij++) {
                        for (int c = 0; c < image.getCompCount(); c++) {
                            image.setCompNo(c);
                            copy.setCompNo(c);
                            double v = image.get(i, j, ii, ij);
                            v = (v - gNormalize[c][ii][ij][0]) /
                                    (gNormalize[c][ii][ij][1] - gNormalize[c][ii][ij][0]);
                            if (Double.isInfinite(v) || Double.isNaN(v)) {
                                v = 1.0;
                            }
                            copy.set(i, j, ii, ij, v);
                        }
                    }
            }
        return copy;
    }

    public one.empty3.feature.M3 mean(one.empty3.feature.M3 image, M3 copy) {
        double[] sum = new double[copy.getCompCount()];
        for (int i = 0; i < image.columns
                ; i++)
            for (int j = 0; j < image.lines; j++) {
                for (int ii = 0; ii < image.columns; ii++)
                    for (int ij = 0; ij < image.lines; ij++) {
                        for (int c = 0; c < image.getCompCount(); c++) {
                            image.setCompNo(c);
                            copy.setCompNo(c);
                            double v = image.get(i, j, ii, ij);
                            v = ((v - mean[c][ii][ij]) * (v - mean[c][ii][ij]));
                            if (Double.isInfinite(v) || Double.isNaN(v)) {
                                v = 1.0;
                            }
                            sum[c] += v;
                            copy.set(i, j, ii, ij, v);
                        }

                    }
            }
        for (int i = 0; i < image.columns; i++)
            for (int j = 0; j < image.lines; j++) {
                for (int ii = 0; ii < image.columns; ii++)
                    for (int ij = 0; ij < image.lines; ij++) {
                        for (int c = 0; c < image.getCompCount(); c++) {
                            copy.setCompNo(c);
                            double v = copy.get(i, j, ii, ij);
                            copy.set(i, j, ii, ij, v / sum[c]);
                        }

                    }
            }

        return copy;
    }

    public void initGNormalise() {
        gNormalize = new double[4][columnsIn][linesIn][2];
        for (int c = 0; c < 4; c++) {
            for (int ii = 0; ii < columnsIn; ii++)
                for (int ij = 0; ij < linesIn; ij++) {

                    gNormalize[c][ii][ij][0] = Double.MAX_VALUE;
                    gNormalize[c][ii][ij][1] = -Double.MAX_VALUE;
                }

        }
        mean = new double[4][columnsIn][linesIn];
        for (int c = 0; c < 4; c++) {
            for (int ii = 0; ii < columnsIn; ii++)
                for (int ij = 0; ij < linesIn; ij++) {
                    mean[c][ii][ij] = 0;
                }

        }
    }
}
