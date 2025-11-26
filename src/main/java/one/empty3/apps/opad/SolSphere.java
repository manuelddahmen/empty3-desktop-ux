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
 * CC Attribution
 *
 * author Manuel Dahmen _manuel.dahmen@gmx.com_
 *
 **
 */
package one.empty3.apps.opad;

import one.empty3.library.Point3D;
import one.empty3.library.Representable;
import one.empty3.library.RepresentableConteneur;
import one.empty3.library.Sphere;
import one.empty3.library.core.nurbs.ParametricSurface;

import java.util.Iterator;

/*__
 *
 * Meta Description missing
 * @author Manuel Dahmen dathewolf@gmail.com
 */
public class SolSphere extends Terrain {

    private double radius = 1.0;

    private Point3D coord(double u, double v) {
        double a = u * 2 * Math.PI;//- Math.PI;
        double b = v * Math.PI;
        Point3D p = new Point3D(Math.sin(a) * Math.sin(b)
                * radius, Math.sin(a) * Math.cos(b) * radius,
                Math.cos(a) * radius);
        return p;

    }

    public SolSphere(/*Game Game*/) {
        setDessineMurs(false);
        //super(Game);
        ps = new Sphere(Point3D.O0, 10.);
        SolPP sol = new SolPP(ps);
        RepresentableConteneur generateWire = sol.generateWire();

        Iterator<Representable> it = generateWire.iterator();

        while (it.hasNext()) {
            add(it.next());
        }
    }

}
