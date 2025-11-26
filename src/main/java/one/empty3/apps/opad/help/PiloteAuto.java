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

package one.empty3.apps.opad.help;

import one.empty3.apps.opad.Timer;
import one.empty3.library.Point3D;
//import one.empty3.library.core.gdximports.gdx_BSplineCurve;

/*__
 *
 * @author Se7en
 */
public class PiloteAuto extends BonusClass {
    public Timer t;
    //  gdx_BSplineCurve bspline;
    private final double duration;
    private final double timeStart;

    public PiloteAuto(double timeStart, double duration) {
        super();

        this.timeStart = timeStart;
        this.duration = duration;

    }

    public void createBSpline(Point3D[] points) {
    }

    public double timeStart() {
        return timeStart;
    }

    public double duration() {
        return duration;
    }

    public double tempsEcoule() {
        return System.nanoTime() - timeStart();
    }

    public boolean begins() {
        return System.nanoTime() > timeStart();
    }

    public boolean ends() {
        return System.nanoTime() > timeStart() + duration();
    }

    //public Point3D getDirectionAtTimeT(double t) {
    //    return bspline.calculerPoint3D(t);
    //}
}
