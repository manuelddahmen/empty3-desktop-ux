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

import one.empty3.library.Point2D;
import one.empty3.library.Point3D;
import one.empty3.library.RepresentableConteneur;
import one.empty3.library.core.nurbs.ParametricSurface;

/*__
 *
 * Meta Description missing
 * @author Manuel Dahmen dathewolf@gmail.com
 */
public abstract class Terrain extends RepresentableConteneur {
    protected ParametricSurface ps;
    private Point2D direction;
    private Player player;
    private Game game;
    private double calibrage;
    private boolean isDessineMurs = true;
    private double incrCalcNormale = 0.0001;

    public boolean isDessineMurs() {
        return isDessineMurs;
    }

    public void setDessineMurs(boolean b) {
        //isDessineMurs = b;
    }

    public Terrain() {
    }


    public Point3D calcNormale(double u, double v) {
        Point3D v1 = ps.calculerPoint3D(u + getaDouble(), v).moins(ps.calculerPoint3D(u, v));
        Point3D v2 = ps.calculerPoint3D(u, v + getaDouble()).moins(ps.calculerPoint3D(u, v));

        return v1.prodVect(v2);
    }

    private double getaDouble() {
        return incrCalcNormale;
    }

    /*__
     *
     * @param u X-coordonnée [0-1] sur la surface
     * @param v Y-coordonnée [0-1] sur la surface
     * @return
     */
    public Point3D calcCposition(double u, double v) {
        return ps.calculerPoint3D(u, v);
    }


    public Point3D hauteur(double x, double y, double z) {
        return ps.calculerPoint3D(x, y).plus(calcNormale(x, y).norme1().mult(z));

    }

    public Point3D calculerLigneEcoulement(Point3D p3) {
        Point3D P = Point3D.O0;

        Point3D N = calcNormale(p3.get(0), p3.get(1));
        Point3D g = calcCposition(p3.get(0), p3.get(1)).moins(calcCposition(0.5, 0.5));
        Point3D forceMotrice = getGame().getLocalPlayer().forceMotrice();

        Point3D toZero = Point3D.INFINI;
        while (toZero.norme() > 0.0001) {
            toZero = N.prodVect(forceMotrice.moins(g).mult(calibrage));

        }
        return P;
    }

    public Point3D p3(Point3D point3D) {
        return hauteur(point3D.getX(), point3D.getY(), point3D.getZ());
    }

    public Game getGame() {
        return game;
    }
}
