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
 * *
 * Global license :  *
 * CC Attribution
 *
 * author Manuel Dahmen _manuel.dahmen@gmx.com_
 *
 **
 */
package one.empty3.apps.opad;

import one.empty3.library.ColorTexture;
import one.empty3.library.Cube;

import one.empty3.library.Point;
import one.empty3.libs.*;

import java.awt.Dimension;
import java.util.ResourceBundle;

/*__
 *
 * Meta Description missing
 * @author Manuel Dahmen dathewolf@gmail.com
 */
public class Vaisseau {
    private final ResourceBundle bundle;
    private final double mlc;

    {
        bundle = ResourceBundle.getBundle("one.empty3.apps.opad.Bundle"); // NOI18N
        mlc = Double.parseDouble(bundle.getString("persoCube.mlc"));
    }

    private final PositionUpdate gm;

    public Vaisseau(PositionUpdate gm) {
        this.gm = gm;
    }

    public Cube getObject() {
        Cube cube = new Cube(mlc, gm.getPositionMobile().calcPosition(), new ColorTexture(new Color(Color.YELLOW)));
        cube.generate();
        return cube;
    }
}
