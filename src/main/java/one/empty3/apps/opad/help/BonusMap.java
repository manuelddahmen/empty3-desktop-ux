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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package one.empty3.apps.opad.help;

import one.empty3.library.Point;
import one.empty3.libs.*;
import java.awt.Dimension;
import java.util.HashMap;

/*__
 *
 * @author Se7en
 */
public class BonusMap {
    private static final HashMap<Class, Color> maps = new HashMap<>();

    static

    {

        maps.put(Cheval_Licorne.class, new Color(0x000000FF));
        maps.put(Escargot.class, new Color(java.awt.Color.GRAY.getRGB()));
        maps.put(MouvementDirectionnel.class, new Color(java.awt.Color.GREEN.getRGB()));


    }

    public static HashMap<Class, Color> getMap() {
        return maps;
    }

}
