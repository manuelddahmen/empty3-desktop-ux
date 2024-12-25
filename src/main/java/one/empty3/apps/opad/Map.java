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

 CC Attribution

 author Manuel Dahmen _manuel.dahmen@gmx.com_

 ***/


package one.empty3.apps.opad;

import one.empty3.library.Point3D;

import one.empty3.library.Point;
import one.empty3.libs.*;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

/*__
 *
 * Meta Description missing
 * @author Manuel Dahmen dathewolf@gmail.com
 */
public abstract class Map {
    public final int CARD_TYPE_COLORS = 0;
    public final int CARD_TYPE_POLYGONS = 1;
    protected int width;

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    protected int height;

    public abstract Point3D getGamePosition();

    public abstract void setGamePosition(Point3D coordonnees);

    public abstract BufferedImage genererImage();

    public abstract void initCard(int width, int height, Color[][] objet);

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
