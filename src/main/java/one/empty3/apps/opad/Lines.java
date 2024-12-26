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

package one.empty3.apps.opad;

import one.empty3.library.LineSegment;
import one.empty3.library.Point3D;
import one.empty3.library.core.nurbs.ParametricCurve;

public class Lines extends ParametricCurve {
    private final Path path;

    public Lines(Path path) {
        this.path = path;
    }

    public double remainder(double t, double n) {
        double sign = Math.signum(t * n);
        if (sign != 0) {
            double N = Math.abs(n);
            double T = Math.abs(t);
            double r = (T / N - (int) (T * N)) / ((int) N);
            return r;
        }
        return n == 0 ? Double.NaN : 0;
    }

    @Override
    public Point3D calculerPoint3D(double t) {
        try {
            int segm = (int) (t * path.size());

            if (path.size() >= 2) {
                LineSegment segmentDroite = new LineSegment(path.get(segm), path.get(segm + 1));

                double v = remainder(t * path.size(), segm);

                return segmentDroite.calculerPoint3D(v);
            } else
                return Point3D.O0;
        } catch (Exception ex) {
            return Point3D.O0;
        }
    }

    @Override
    public Point3D calculerVitesse3D(double t) {
        return null;
    }
}
