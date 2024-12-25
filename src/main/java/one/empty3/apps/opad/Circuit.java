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

/*__
Global license : 

 Microsoft Public Licence

 author Manuel Dahmen _manuel.dahmen@gmx.com_

 Creation time 05-nov.-2014

 ***/


package one.empty3.apps.opad;


import one.empty3.library.ColorTexture;
import one.empty3.library.Point3D;
import one.empty3.library.Representable;
import one.empty3.library.Sphere;
import one.empty3.library.core.tribase.TubulaireN;

import one.empty3.library.Point;
import one.empty3.libs.*;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Iterator;

/*__
 *
 * Meta Description missing
 * @author Manuel Dahmen dathewolf@gmail.com
 */
public class Circuit extends TubulaireN {

    public Circuit(ArrayList<Point3D> listPoint3d) {
        listPoint3d.forEach(this::addPoint);
        this.texture(new ColorTexture( new Color(Color.ORANGE)));
    }

    public Circuit(Bonus bonus) {
        Iterator<Representable> iterator = bonus.getListRepresentable().iterator();
        while (iterator.hasNext()) {
            Representable next = iterator.next();
            if (next != null && next instanceof TRISphere2) {
                addPoint(((Sphere) next).getCircle().getCenter());
            }

            this.texture(new ColorTexture( new Color(Color.ORANGE)));
        }


    }

    public TubulaireN baseCircuitRepresentable() {
        return this;
    }


}
