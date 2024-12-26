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

package one.empty3.feature.Myfacedetect;

import one.empty3.apps.feature.FilterPixM;
import one.empty3.feature.PixM;

public class WeightingFunction extends FilterPixM {
    private static final double MIN_START = 0.2;

    public WeightingFunction(int l, int c) {
        super(l, c);
        generatePixels();
    }

    @Override
    public double filter(double x, double y) {
        double ratioY = y / getLines();
        for (int c = 0; c < 3; c++) {
            switch (c) {
                case 0:
                case 1:
                case 2:
                    //                   set((int) x, (int) y, ratioY <= MIN_START ? ratioY / MIN_START : (ratioY < 1 - MIN_START ? 1.0 :
                    //
                    return ratioY <= MIN_START ? ratioY / MIN_START : (ratioY < 1 - MIN_START ? 1.0 : ratioY * (1 - MIN_START));
            }
        }

        return Double.NaN;
    }

    public void generatePixels() {
        for (int j = 0; j < lines; j++) {
            double percentsY = 1.0 * j / getLines();
            for (int i = 0; i < columns; i++) {
                for (int c = 0; c < 3; c++) {
                    setCompNo(c);
                    switch (c) {
                        case 0:
                        case 1:
                        case 2:
                            set(i, j, percentsY <= MIN_START ? percentsY / MIN_START : (percentsY < 1 - MIN_START ? 1.0 :
                                    percentsY * (1 - MIN_START)));
                    }
                }
            }
        }
    }
}
