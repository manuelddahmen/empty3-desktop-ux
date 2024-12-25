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

import one.empty3.library.Point3D;
import one.empty3.library.Representable;
import one.empty3.library.RepresentableConteneur;
import one.empty3.library.core.nurbs.ParametricSurface;

import java.util.Iterator;

@SuppressWarnings("serial")
public class SolRelief extends Terrain {

    public SolRelief(/*Game Game*/) {

        setDessineMurs(false);

        //super(Game);
        ps = new ParametricSurface() {
            @Override
            public Point3D calculerPoint3D(double u, double v) {
                double T = 6.;
                double hauteurVague = 0.002;
                return new Point3D(u, hauteurVague*Math.sin(2 * Math.PI * u*T )
                        * Math.cos(2 * Math.PI * v*T), v);
            }

            @Override
            public Point3D calculerVitesse3D(double u, double v) {
                Point3D pU = calculerPoint3D(u + 0.001, v).moins(calculerPoint3D(u, v));
                Point3D pV = calculerPoint3D(u, v + 0.001).moins(calculerPoint3D(u, v));
                return pV.plus(pU).norme1();
            }
        };
        SolPP sol = new SolPP(ps);
        RepresentableConteneur generateWire = sol.generateWire();

        Iterator<Representable> it = generateWire.iterator();

        while (it.hasNext()) {
            add(it.next());
        }
    }


}
