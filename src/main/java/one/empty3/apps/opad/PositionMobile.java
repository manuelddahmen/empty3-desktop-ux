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

import one.empty3.library.Camera;
import one.empty3.library.Point3D;

import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/*__
 * Created by manuel on 10-07-17.
 */
public class PositionMobile {
    private static final double SCALE_3D = 0.1;
    protected final ResourceBundle bundle;
    protected final PositionUpdate positionUpdate;
    protected final double positionIncrement;
    protected final double positionIncrement2;
    protected Terrain terrain;
    protected Point3D positionSol;
    protected Point3D angleVisee;

    protected Point3D positionMobile;
    protected Point3D angleVueMobile;
    protected double score;
    protected HashMap<Long, Point3D> trace;


    public PositionMobile(PositionUpdate positionUpdate) {
        super();
        bundle = ResourceBundle.getBundle("one.empty3.apps.opad.Bundle"); // NOI18N
        positionIncrement = Double.parseDouble(bundle.getString("positionIncrement"));
        positionIncrement2 = Double.parseDouble(bundle.getString("positionIncrement2"));
        this.positionUpdate = positionUpdate;
        this.terrain = positionUpdate.getTerrain();
        positionSol = new Point3D(0.5, 0.5, 0.0);
        getPositionSol().setZ(Double.parseDouble(bundle.getString("hauteur")));
        angleVisee = new Point3D(Point3D.O0);
        trace = new HashMap<>();
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }

    public Point3D getPositionSol() {
        return positionSol;
    }

    public void setPositionSol(Point3D positionSol) {
        trace.put(System.nanoTime(), this.positionSol);
        Logger.getAnonymousLogger().log(Level.INFO, "+");
        this.positionSol = positionSol;
    }

    public Point3D getAngleVisee() {
        return angleVisee;
    }

    public void setAngleVisee(Point3D angleVisee) {
        this.angleVisee = angleVisee;
    }

    public Point3D getAngleVueMobile() {
        return angleVueMobile;
    }

    public void setAngleVueMobile(Point3D angleVueMobile) {
        this.angleVueMobile = angleVueMobile;
    }

    public Point3D calcPosition() {
        return getTerrain().p3(getPositionSol());
    }

    public Point3D calcDirection()
    {
        return getTerrain().p3(positionSol.plus(positionUpdate.getVecDir2D()));
    }
    public Point3D calcDirectionPlotY()
    {
        return getTerrain().p3(
                positionSol.plus(
                        new Point3D(
                                Math.cos(getAngleVisee().getZ() * Math.PI * 2),
                                    Math.sin(getAngleVisee().getZ() * Math.PI * 2),
                                    0.0
                        ).norme1().mult(1)));
    }
    public Point3D calcDirectionPlotX()
    {
        return getTerrain().p3(positionSol.plus(
                new Point3D(
                        Math.cos(getAngleVisee().getZ() * Math.PI * 2 + Math.PI/2),
                            Math.sin(getAngleVisee().getZ() * Math.PI * 2 + Math.PI/2),
                            0.0
                ).norme1().mult(1)));
    }
    public Point3D calcDirectionPlotZ()
    {
        return getTerrain().p3(positionSol.plus(
                new Point3D(
                        0., 0., .01
                ))).norme1().mult(1);
    }

    public Point3D calcPosition2D() {
        return getPositionSol();
    }

    public Point3D calcDirection2D() {
        return positionSol.plus(positionUpdate.getVecDir2D());
    }
    public Camera calcCameraMobile()
    {
        final Point3D camera = calcPosition();
        final Point3D lookAt =  getTerrain().p3(calcDirectionPlotY().norme1().mult(SCALE_3D));
        Point3D mult = lookAt.moins(camera).norme1().mult(-positionIncrement);
        return new Camera(camera.moins(mult), lookAt, calcDirectionPlotZ());
    }

    public Camera calcCamera() {
        final Point3D camera = calcPosition();
        final Point3D lookAt = calcDirection();
        Point3D mult = lookAt.moins(camera).norme1().mult(-positionIncrement);
        return new Camera(camera.moins(mult), lookAt, calcDirectionPlotZ());
    }

    public Point3D getPositionMobile() {
        return positionMobile;
    }

    public HashMap<Long, Point3D> getTrace() {
        return trace;
    }
}
