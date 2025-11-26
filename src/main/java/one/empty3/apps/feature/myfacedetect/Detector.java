/*
 *
 *  *
 *  *  * Copyright (c) 2025. Manuel Daniel Dahmen
 *  *  *
 *  *  *
 *  *  *    Copyright 2024 Manuel Daniel Dahmen
 *  *  *
 *  *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *    you may not use this file except in compliance with the License.
 *  *  *    You may obtain a copy of the License at
 *  *  *
 *  *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  *    Unless required by applicable law or agreed to in writing, software
 *  *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *    See the License for the specific language governing permissions and
 *  *  *    limitations under the License.
 *  *
 *  *
 *
 *
 *
 *  * Created by $user $date
 *
 *
 */

package one.empty3.feature.Myfacedetect;

import one.empty3.feature.PixM;
import one.empty3.library.Point2D;

import java.util.ArrayList;
import java.util.List;


public class Detector implements IMyDetector {
    List<one.empty3.feature.Myfacedetect.Face> faces = new ArrayList<>();
    private PixM pixels;


    @Override
    public void execute() {
        if (pixels != null && pixels.getLines() > 0 && pixels.getColumns() > 0) {
            faces = new ArrayList<>();

        }
    }

    public Face getFace(double x, double y, double angleHorizontal,
                        double angleVertical,
                        double wx, double hy) {
        int noseXleft = 0;
        int noseXright = 0;
        int noseYup = 0;
        int noseYdown = 0;
        double incrXup = 1;
        double incrYleft = 1;
        double incrXdown = 1;
        double incrYright = 1;
        int[][] noses = new int[][]{{0, 0, 0}, {0, 1, 0}};
        int[][] eyes = new int[][]{{0, 0, 0}, {0, 1, 0}};
        int[][] face =
                {{2, 2, 2, 2, 2},
                        {2, 0, 0, 0, 2},
                        {2, 1, 0, 1, 2},
                        {2, 0, 1, 0, 2},
                        {0, 0, 0, 0, 0},
                        {0, 0, 8, 0, 0},
                        {0, 0, 0, 0, 0}};
        for (double i = x; i < wx; i++) {
            for (double j = x; j < hy; j++) {

            }
        }
        return null;
    }

    @Override
    public void setPixM(PixM matrice) {
        this.pixels = matrice;
    }

    @Override
    public int getFaces() {
        return 0;
    }

    @Override
    public int[] getFaceIds() {
        return new int[0];
    }

    @Override
    public Point2D getEyeLeft() {
        return null;
    }

    @Override
    public Point2D getEyeRight() {
        return null;
    }

    @Override
    public Point2D getNose() {
        return null;
    }

    @Override
    public Point2D getMouthLeft() {
        return null;
    }

    @Override
    public Point2D getMouthRight() {
        return null;
    }
}
