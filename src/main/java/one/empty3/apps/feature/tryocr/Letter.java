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

package one.empty3.apps.feature.tryocr;

import one.empty3.apps.feature.tryocr.Trait;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Letter {
    char x;
    List<one.empty3.apps.feature.tryocr.Trait> traits = new ArrayList<one.empty3.apps.feature.tryocr.Trait>();

    public Letter(one.empty3.apps.feature.tryocr.Trait... traits) {
        for (one.empty3.apps.feature.tryocr.Trait t :
                traits) {
            this.traits.add(t);
        }
    }

    public List<one.empty3.apps.feature.tryocr.Trait> getTraits() {
        return traits;
    }

    public void setTraits(List<Trait> traits) {
        this.traits = traits;
    }
}
