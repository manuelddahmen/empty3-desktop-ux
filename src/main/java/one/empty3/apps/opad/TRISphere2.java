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

import one.empty3.apps.opad.help.BonusClass;
import one.empty3.library.Point3D;
import one.empty3.library.Sphere;
import one.empty3.library.core.tribase.TRISphere;

/*__
 * Created by manuel on 19-05-17.
 */
public class TRISphere2<T extends BonusClass> extends Sphere {

    private Point3D coords;
    private Bonus bonus;
    double u;
    double v;
    BonusClass clazz;
    private BonusClass gameObject;

    public TRISphere2(Bonus bonus, Point3D c, double r) {
        super(c, r);
        setIncrU(0.19);
        setIncrV(0.19);
        setStartU(0.0);
        setStartV(0.0);
        setEndU(1.0);
        setEndV(1.0);
        this.coords = c;
        this.bonus = bonus;
        clazz = new BonusClass();
    }

    public BonusClass getBonusClass() {
        return clazz;
    }

    public void setGameObject(BonusClass gameObject) {
        this.gameObject = gameObject;
    }

    public BonusClass getGameObject() {
        return this.gameObject;
    }

    public Point3D getCoords() {
        return coords;
    }

    public void setCoords(Point3D c) {
        this.coords = c;
    }

}
