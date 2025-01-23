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

public class LocalPattern extends FilterMatPixM {
    private one.empty3.feature.M3 sr;
    public static String formulaXvLogical = "count(x==v)==columns()*lines()/2";

    /*
   
        Ligne 1 searchPattern value = x [0,1] 
           - 1 : ignore 
        Ligne 2 replacePattern value x' [0,1] 
        comp4 => rgb' = rgb *(1-opacity) si x ! =-1
       
    */
    public LocalPattern(one.empty3.feature.M3 searchReplace) {
        this.sr = searchReplace;

    }

    /*
     */
    public one.empty3.feature.M3 filter(

            one.empty3.feature.M3 original
    ) {
        one.empty3.feature.M3 copy = new one.empty3.feature.M3(original.columns, original.lines, 1, 1);

        for (int i = 0; i < original.columns; i++) {

            for (int j = 0; j < original.lines; j++) {

                //copy.setCompNo(c);

                //boolean isMaximum = true;
                double v = 0.;
                double maxLocal = original.getIntensity(i, j, 0, 0);
//v = maxLocal;
                int countOut = 0;

                int countIn = 0;

                for (int s = 0; s < sr.columns; s++) {

                    for (int ii = -sr.columns / 2; ii <= sr.columns; ii++) {

                        for (int ij = -sr.lines / 2; ij <= sr.lines / 2; ij++) {

                            v = original.get(i + ii, j + ij, 0, 0);

                            if (sr.get(ii + sr.columns
                                    / 2, ij + sr.lines / 2, 0, s)
                                    == v) {

                                countIn++;

                            }

                        }

                    }


                    if (countIn == sr.lines * sr.columns
                            / 2.0) {

                        copy.setCompNo(0);

                        copy.set(i, j, 0, 0, 1.0);//1 au lieu value

                        copy.setCompNo(1);

                        copy.set(i, j, 0, 0, 1.0);//1 au lieu value

                        copy.setCompNo(2);

                        copy.set(i, j, 0, 0, 1.0);//1 au lieu value
                    }
                }

            }

        }

        return copy;


    }


    @Override
    public void element(one.empty3.feature.M3 source, one.empty3.feature.M3 copy, int i, int j, int ii, int ij) {

    }

    @Override
    public one.empty3.feature.M3 norm(one.empty3.feature.M3 m3, M3 copy) {
        return m3.copy();
    }
} 
