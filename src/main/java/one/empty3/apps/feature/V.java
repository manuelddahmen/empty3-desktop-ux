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

import one.empty3.feature.*;

import one.empty3.feature.M;

public class V extends one.empty3.feature.M {
    public V(int l, int c) {
        super(l, c);
    }

    public V(int lc) {
        super(lc, 1);


    }

    public one.empty3.feature.M outerProduct(V vec1, V vec2) {
        one.empty3.feature.M m1 = new M(vec2.getColumns()
, vec1.getColumns()
);
        for (int m = 0; m < vec1.getColumns()
; m++) { // line incr

            for (int n = 0; n < vec2.getColumns()
; n++) {
                m1.set(m, n, vec1.get(1, m) * vec2.get(1, n));
            }

        }
        return m1;
    }
}
