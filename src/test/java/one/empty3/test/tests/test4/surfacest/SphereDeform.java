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

package one.empty3.test.tests.test4.surfacest;

import one.empty3.library.Point3D;
import one.empty3.library.Sphere;

public class SphereDeform extends Sphere
        implements IParameterPS {
    public void initTDep(double tStart, double tEnd) {

    }

    @Override
    public Point3D getUV(double u, double v, double t) {
        return null;
    }

    @Override
    public Point3D calculerPoint3D(double u, double v) {
        return super.calculerPoint3D(u, v);
    }
}
