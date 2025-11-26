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

package one.empty3.apps.gui;

import one.empty3.library.LineSegment;
import one.empty3.library.Point3D;
import one.empty3.library.Representable;

public class PMove {
    private Point3D in;
    private double[] mXyz;
    private Representable representable;
    private int row, col;
    private Point3D pOut;
    private LineSegment[] lsXyz;


    public Point3D getIn() {
        return in;
    }

    public void setIn(Point3D in) {
        this.in = in;
    }

    public double[] getmXyz() {
        return mXyz;
    }

    public void setmXyz(double[] mXyz) {
        this.mXyz = mXyz;
    }

    public Representable getRepresentable() {
        return representable;
    }

    public void setRepresentable(Representable representable) {
        this.representable = representable;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public Point3D getPout() {
        Point3D p = getIn();
        for (int i = 0; i < 3; i++)
            p = p.plus(lsXyz[i].getExtremite().moins(lsXyz[i].getOrigine()).norme1().mult(getmXyz()[i]));

        return p;
    }

    public LineSegment[] getLsXyz() {
        return lsXyz;
    }

    public void setLsXyz(LineSegment[] lsXyz) {
        this.lsXyz = lsXyz;
    }
}