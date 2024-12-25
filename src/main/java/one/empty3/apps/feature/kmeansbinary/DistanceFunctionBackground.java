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

package one.empty3.apps.feature.kmeansbinary;

import one.empty3.apps.feature.kmeansbinary.Distance;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DistanceFunctionBackground extends Distance {
    public DistanceFunctionBackground() {
        super();
    }

    @Override
    public double distance(double[] point1, double[] point2) {
        double sumColor = 1.0;
        double sumSpace = 0.01;
        if (point1.length < 5) {
            Logger.getAnonymousLogger().log(Level.INFO, "kmeans distance error point1" + point1.length);
            System.exit(-1);
        }
        if (point2.length < 5) {
            Logger.getAnonymousLogger().log(Level.INFO, "kmeans distance error point2" + point2.length);

            System.exit(-1);

        }
        double k = 0.05;

        for (int i = 2; i < 5; i++) {
            sumColor += ((point1[i] - point2[i]) * (point1[i] - point2[i]));
        }
        sumSpace = 0.0;
        double comp;
        for (int i = 0; i < 3; i++) {
            comp = Math.exp(-(point1[i] - point2[i]) * (point1[i] - point2[i]));
            //double comp = (point1[i + 2] - point2[i + 2]) * (point1[i + 2] - point2[i + 2]);
            sumSpace += comp;
        }
        sumColor = 1.0;// / Math.sqrt(sumSpace);
        return Math.sqrt(sumSpace + sumColor);
    }

}
