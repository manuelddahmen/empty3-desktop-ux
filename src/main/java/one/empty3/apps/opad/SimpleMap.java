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

/*__
Global license : 

 CC Attribution

 author Manuel Dahmen _manuel.dahmen@gmx.com_

 ***/


package one.empty3.apps.opad;

import one.empty3.library.*;
import one.empty3.library.Polygon;

import one.empty3.library.Point;
import one.empty3.libs.*;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

/*__
 *
 * Meta Description missing
 * @author Manuel Dahmen dathewolf@gmail.com
 */
public class SimpleMap extends Map {
    private Point3D gamePosition;
    private final Camera camera;
    private final ArrayList<Polygon> polygons;
    private ZBuffer z;


    public ArrayList<Polygon> getPolygons() {
        return polygons;
    }

    public SimpleMap() {
        polygons = new ArrayList<Polygon>();
        camera = new Camera(new Point3D(0.5d, 1d, 0.5), new Point3D(0.5, 0d, 0.5));
    }

    public Camera camera() {
        return camera;
    }

    public void prepareImage() {
        z = ZBufferFactory.instance(width, height);
        z.scene(new Scene());
        Iterator<Polygon> itP;
        itP = polygons.iterator();
        while (itP.hasNext())
            z.scene().add(itP.next());
        z.scene().add(gamePosition);
    }

    @Override
    public Point3D getGamePosition() {
        return gamePosition;
    }

    @Override
    public void setGamePosition(Point3D coordonnees) {
        this.gamePosition = coordonnees;
    }

    @Override
    public BufferedImage genererImage() {
        prepareImage();
        return z.image();
    }

    @Override
    public void initCard(int width, int height, Color[][] objet) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
