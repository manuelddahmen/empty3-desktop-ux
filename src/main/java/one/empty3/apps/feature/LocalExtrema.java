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


import one.empty3.feature.M3;
import one.empty3.feature.PixM;

import java.util.ArrayList;

public class LocalExtrema extends FilterMatPixM {
    private boolean setMin = false;
    private final int pointsCount;
    private final int neighbourSize;
    protected double sub[];
    private double threshold = 0.7;

    public int getCompNo() {
        return compNo;
    }

    public void setCompNo(int compNo) {
        this.compNo = compNo;
    }

    private int compNo;
    public LocalExtrema() {
        this.neighbourSize = 6;
        pointsCount = 1;
    }
    public LocalExtrema(int width, int height, int neighbourSize, int pointsCount) {
        this.neighbourSize = neighbourSize;
        this.pointsCount = pointsCount;
        //sub = new double[4*lines*columns];
    }

    // Detect regions, vertex(lines or curves), edges
    public ArrayList<AreaDescriptor> searchForFeaturePlaces() {
        ArrayList<AreaDescriptor> areas = new ArrayList<>();
        return areas;

        // Global search AreaDescriptor
        // Edge
        // Vertex
        // Similar colors Region
    }

    @Override
    public one.empty3.feature.M3 filter(one.empty3.feature.M3 original) {
        one.empty3.feature.M3 copy = new one.empty3.feature.M3(original.columns
, original.lines,
                1, 1);
        for (int i = 0; i < original.columns
; i++) {
            for (int j = 0; j < original.lines; j++) {
                //copy.setCompNo(c);
                //boolean isMaximum = true;
                double maxLocal = original.getIntensity(i, j, 0, 0);
                int countOut = 0;
                int countIn = 0;
                if (maxLocal >= threshold) {

                    for (int ii = -neighbourSize / 2; ii <= neighbourSize / 2; ii++) {
                        for (int ij = neighbourSize / 2; ij <= neighbourSize / 2; ij++) {
                            double v = original.getIntensity(i + ii, j + ij, 0, 0);
                            if (v < maxLocal) {
                                countIn++;
                            } else
                                countOut++;
                        }
                    }


                    if (isSetMin() && countIn >= pointsCount && countOut == 0) {
                        copy.setCompNo(0);
                        original.setCompNo(0);
                        copy.set(i, j, 0, 0, original.get(i, j, 0, 0));//1 au lieu value
                        copy.setCompNo(1);
                        original.setCompNo(1);
                        copy.set(i, j, 0, 0, original.get(i, j, 0, 0));//1 au lieu value
                        copy.setCompNo(2);
                        original.setCompNo(2);
                        copy.set(i, j, 0, 0, original.get(i, j, 0, 0));//1 au lieu value
                    } else if (countIn < pointsCount) {
                        copy.setCompNo(0);
                        original.setCompNo(0);
                        copy.set(i, j, 0, 0, original.get(i, j, 0, 0));//1 au lieu value
                        copy.setCompNo(1);
                        original.setCompNo(1);
                        copy.set(i, j, 0, 0, original.get(i, j, 0, 0));//1 au lieu value
                        copy.setCompNo(2);
                        original.setCompNo(2);
                        copy.set(i, j, 0, 0, original.get(i, j, 0, 0));//1 au lieu value
                    }
                }
            }
        }
        return copy;
    }

    private double lambda1dot2div1sum2(one.empty3.feature.M3 original, int compNo, int i, int j) {
        PixM pixM = new PixM(3, 3);
        pixM.setRegionCopy(original, 0, 0, i - 1, j - 1, i + 1, j + 1, pixM, 0, 0);
        return pixM.determinant() / pixM.diagonalSum();
    }

    @Override
    public void element(one.empty3.feature.M3 source, one.empty3.feature.M3 copy, int i, int j, int ii, int ij) {

    }

    @Override
    public one.empty3.feature.M3 norm(one.empty3.feature.M3 m3, M3 copy) {
        return m3.copy();
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public boolean isSetMin() {
        return setMin;
    }

    public void setSetMin(boolean b) {
        this.setMin = b;
    }
}
