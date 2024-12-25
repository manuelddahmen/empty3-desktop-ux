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
import one.empty3.library.core.nurbs.ParametricSurface;

/*__
 * Created by Win on 12-09-18.
 *
 * <a href="https://www.mathcurve.com/courbes3d/topographic/ecoulement.shtml">Lignes d'écoulement</a>
 *
 * ->     ->        ->
 * P  = A*v     + B*v    + C (à intervzlles dt constants
 *  N       N-1      N-1
 *
 *  ->
 *  v(u,v) <- Input (keyboard)
 *  ???
 */
public class LigneEcoulement implements Runnable {
    private final PositionMobile positionMobile;
    private ParametricSurface ps;
    public static final double g = 9.8;
    private boolean running = true;

    public LigneEcoulement(PositionMobile positionMobile, ParametricSurface parametricSurface)
    {
        this.ps = parametricSurface;
        this.positionMobile = positionMobile;
        new Thread(this).start();
    }

    public void setSurface(ParametricSurface ps)
    {
        this.ps = ps;
    }


    @Override
    public void run() {
        while (isRunning()) {
            Point3D position = positionMobile.calcPosition();
            Point3D deplacement = positionMobile.calcDirection();


        }
    }

    public boolean isRunning() {
        return running;
    }

    public ParametricSurface getPs() {
        return ps;
    }

    public static double getG() {
        return g;
    }
}
