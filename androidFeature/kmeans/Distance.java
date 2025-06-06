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

package one.empty3.androidFeature.kmeans;

/*
 * Programmed by Shephalika Shekhar
 * class containing methods to calculate distance between two points with features
 * based on distance metric
 */
public class Distance {
    public static float[] r;

    public Distance(float x, float y) {
        float rx = 1f / (float) x;
        float ry = 1f / (float) y;
        r = new float[]{1f / 10f * x, 1f / 10f * y, 1f, 1f, 1f};
    }

    public static double eucledianDistance(double[] point1, double[] point2) {
        double sumColor = 0.01;
        double sumSpace = 0.01;
        if (point1.length < 5) {
            System.out.println("kmeans distance error point1" + point1.length);
            System.exit(-1);
        }
        if (point2.length < 5) {
            System.out.println("kmeans distance error point2" + point2.length);

            System.exit(-1);

        }
        double k = 0.05;
        /*for (int i = 0; i < 2; i++) {
            double comp = Math.exp(-(point1[i] - point2[i]) * (point1[i] - point2[i]));
            if (Double.isNaN(comp)
                    || Double.isInfinite(comp))
                comp = 0;
            sumSpace += comp;
        }

        for (int i = 2; i < 5; i++) {
            sumColor += ((point1[i] - point2[i]) * (point1[i] - point2[i]));
        }*/
        sumSpace = 0.0;
        for (int i = 0; i < 3; i++) {
            //double comp = Math.exp(-(point1[i] - point2[i]) * (point1[i] - point2[i]));
            double comp = (point1[i + 2] - point2[i + 2]) * (point1[i + 2] - point2[i + 2]);
            sumSpace += comp;
        }
        return Math.sqrt(sumSpace /** sumColor*/);
    }

    public static double manhattanDistance(double[] point1, double[] point2) {
        double sum = 0.0;
        for (int i = 2; i < 5; i++) {
            sum += (Math.abs(point1[i] - point2[i]));//* r[i] * r[i];
        }
        return sum;
    }


}
