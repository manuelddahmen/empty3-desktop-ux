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

import one.empty3.library.MoveeObject;
import one.empty3.library.Point3D;
import one.empty3.library.Representable;
import one.empty3.library.Scene;
import one.empty3.library.stl_loader.IncorrectFormatException;
import one.empty3.library.stl_loader.ParsingErrorException;
import one.empty3.library.stl_loader.StlFile;

import java.io.FileNotFoundException;
import java.util.ResourceBundle;

/*__
 * Created by manuel on 29-06-17.
 */
public class Bullet extends Representable implements MoveeObject {
    private Point3D origin;
    private Point3D direction;
    private double speed;
    private Representable shape;
    private long nanoTime;

    public Bullet(Point3D origin, Point3D direction, double speed, Representable shape, long nanoTime) {
        this();
        this.origin = origin;
        this.direction = direction;
        this.speed = speed;
        this.shape = shape;
        this.nanoTime = nanoTime;

    }

    public Bullet() {
        ResourceBundle bundle = ResourceBundle.getBundle("Bundle");
        String bundleString = bundle.getString("resource3D.stl.Bullet1");
        StlFile stlFile = new StlFile();
        try {
            Scene load = stlFile.load(bundleString);
            this.setShape(load.getObjets().getElem(0));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IncorrectFormatException e) {
            e.printStackTrace();
        } catch (ParsingErrorException e) {
            e.printStackTrace();
        }


    }

    public long getNanoTime() {
        return nanoTime;
    }

    public void setNanoTime() {
        this.nanoTime = System.nanoTime();
    }

    public Point3D getOrigin() {
        return origin;
    }

    public void setOrigin(Point3D origin) {
        this.origin = origin;
    }

    public Point3D getDirection() {
        return direction;
    }

    public void setDirection(Point3D direction) {
        this.direction = direction;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public Representable getShape() {
        return shape;
    }

    public void setShape(Representable shape) {
        this.shape = shape;
    }

    public Point3D getPosition() {
        return origin.plus(direction.norme1().mult(speed));
    }

    @Override
    public void setPositionAtTime(Point3D point3D, long l) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Point3D getCurrentPosition() {
        return getPosition();
    }
}
