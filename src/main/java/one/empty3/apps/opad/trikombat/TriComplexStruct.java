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

package one.empty3.apps.opad.trikombat;

import one.empty3.library.Point3D;
import one.empty3.library.core.tribase.ApproximationFonction1D;
import one.empty3.library.core.tribase.ApproximationFonction2D;
import one.empty3.library.core.tribase.Tubulaire;

/*__
 * Created by manuel on 03-09-16.
 */
public class TriComplexStruct {
    private Tubulaire tube;
    private ApproximationFonction1D diam;
    private ApproximationFonction1D nbSpires;


    /*__
     * Paramètre global, statique
     */
    private Point3D vitesseGlobale;
    /*__
     * Vitesse dynamique, propriétés de la fonction à déterminer
     */
    private ApproximationFonction2D vitesseDansLePlanNormaleAuTube;


}