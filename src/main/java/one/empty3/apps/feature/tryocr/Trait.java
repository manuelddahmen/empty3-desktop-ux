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

import one.empty3.apps.feature.tryocr.TraitsShape;
import one.empty3.apps.feature.tryocr.TraitsX;
import one.empty3.apps.feature.tryocr.TraitsY;

public class Trait {
    private TraitsY valueFrom1;
    private TraitsX valueFrom2;

    private TraitsY valueTo1;

    private TraitsX valueTo2;
    private TraitsShape curveFrom;
    private TraitsShape curveTo;

    public Trait(TraitsY valueFrom1, TraitsX valueFrom2, TraitsY valueTo1, TraitsX valueTo2,
                 TraitsShape curveFrom, TraitsShape curveTo) {
        this.valueFrom1 = valueFrom1;
        this.valueFrom2 = valueFrom2;
        this.valueTo1 = valueTo1;
        this.valueTo2 = valueTo2;
        this.curveFrom = curveFrom;
        this.curveTo = curveTo;
    }

    public TraitsY getValueFrom1() {
        return valueFrom1;
    }

    public void setValueFrom1(TraitsY valueFrom1) {
        this.valueFrom1 = valueFrom1;
    }

    public TraitsY getValueTo1() {
        return valueTo1;
    }

    public void setValueTo1(TraitsY valueTo1) {
        this.valueTo1 = valueTo1;
    }

    public TraitsX getValueFrom2() {
        return valueFrom2;
    }

    public void setValueFrom2(TraitsX valueFrom2) {
        this.valueFrom2 = valueFrom2;
    }

    public TraitsX getValueTo2() {
        return valueTo2;
    }

    public void setValueTo2(TraitsX valueTo2) {
        this.valueTo2 = valueTo2;
    }

    public TraitsShape getCurveFrom() {
        return curveFrom;
    }

    public void setCurveFrom(TraitsShape curveFrom) {
        this.curveFrom = curveFrom;
    }

    public TraitsShape getCurveTo() {
        return curveTo;
    }

    public void setCurveTo(TraitsShape curveTo) {
        this.curveTo = curveTo;
    }
}
