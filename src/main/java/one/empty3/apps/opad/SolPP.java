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

/*__
 * *
 * Global license :  *
 * Microsoft Public Licence
 *
 * author Manuel Dahmen _manuel.dahmen@gmx.com_
 *
 Creation time 05-nov.-2014
 *
 **
 */
package one.empty3.apps.opad;

import one.empty3.library.ColorTexture;
import one.empty3.library.LineSegment;
import one.empty3.library.Point3D;
import one.empty3.library.RepresentableConteneur;
import one.empty3.library.core.nurbs.ParametricSurface;

import one.empty3.library.Point;
import one.empty3.libs.*;
import java.awt.Dimension;

/*__
 *
 * Meta Description missing
 * @author Manuel Dahmen dathewolf@gmail.com
 */
public class SolPP {

    public double increment = 0.01;
    private ParametricSurface sol;

    public SolPP() {
    }

    public ParametricSurface getObject() {
        return sol;
    }

    public SolPP(ParametricSurface ps) {
        this.sol = ps;
    }

    public RepresentableConteneur generateWire() {
        RepresentableConteneur rc = new RepresentableConteneur();


        for (double i = 0; i < 1; i += increment) {
            for (double j = 0; j < 1; j += increment) {
                Point3D p1 = sol.calculerPoint3D(j, i);
                Point3D p2 = sol.calculerPoint3D(j + increment, i);

                rc.add(new LineSegment(p1, p2, new ColorTexture( new Color(Color.WHITE))));

                p1 = sol.calculerPoint3D(j, i);
                p2 = sol.calculerPoint3D(j, i + increment);

                rc.add(new LineSegment(p1, p2, new ColorTexture( new Color(Color.WHITE))));
            }
        }


        return rc;
    }
}
