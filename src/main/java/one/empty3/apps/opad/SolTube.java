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

package one.empty3.apps.opad;

import one.empty3.library.Point3D;
import one.empty3.library.Representable;
import one.empty3.library.RepresentableConteneur;
import one.empty3.library.core.nurbs.CourbeParametriquePolynomialeBezier;
import one.empty3.library.core.tribase.TubulaireN2;

import java.util.Iterator;

/*__
 * Created by manuel on 25-05-17.
 */
public class SolTube extends Terrain  {
    public SolTube(/*Game Game*/) {
        setDessineMurs(false);
        TubulaireN2 n2 = new TubulaireN2();
        CourbeParametriquePolynomialeBezier courbeParametriquePolynomialeBezier;
        courbeParametriquePolynomialeBezier = new CourbeParametriquePolynomialeBezier();
        courbeParametriquePolynomialeBezier.getCoefficients().add(Point3D.O0);
        courbeParametriquePolynomialeBezier.getCoefficients().add(Point3D.X);
        courbeParametriquePolynomialeBezier.getCoefficients().add(Point3D.Y);
        courbeParametriquePolynomialeBezier.getCoefficients().add(Point3D.Z);
        n2.getSoulCurve().setElem(courbeParametriquePolynomialeBezier);
        n2.nbrAnneaux(40);
        n2.nbrRotations(20);
        //n2.diam(1);

        //n2.generate();

        ps = n2;

        SolPP sol = new SolPP(ps);
        RepresentableConteneur generateWire = sol.generateWire();

        Iterator<Representable> it = generateWire.iterator();

        while (it.hasNext()) {
            add(it.next());
        }
    }
}
