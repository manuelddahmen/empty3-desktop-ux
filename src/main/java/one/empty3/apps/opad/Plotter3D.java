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

package one.empty3.apps.opad;


import one.empty3.apps.opad.menu.ToggleMenu;
import one.empty3.library.*;

import one.empty3.library.Point;
import one.empty3.libs.*;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


/*__
 * Created by manuel on 07-06-17.
 */

public class Plotter3D implements KeyListener, Runnable {
    private final ResourceBundle bundle;

    public double rotationYParNano;
    public double rotationXParNano;
    protected boolean release_up = true;
    protected boolean release_down = true;
    protected boolean release_left = true;
    protected boolean release_right = true;
    DistanceCount distanceCount;
    ToggleMenu toggleMenu;
    private PositionUpdate positionUpdate;
    private boolean active;
    private Point2D impact;
    private boolean droite = false;
    private boolean gauche = false;
    private boolean haut = false;
    private boolean bas = false;
    private long timeEllapsed = 0;
    private boolean activeShift = false;

    public Plotter3D(PositionUpdate positionUpdate) {

        this.positionUpdate = positionUpdate;
        distanceCount = new DistanceCount();
        bundle = ResourceBundle.getBundle("one.empty3.apps.opad/Bundle"); // NOI18N
        rotationYParNano = Double.parseDouble(bundle.getString("rotationYPerNanos"));
        rotationXParNano = Double.parseDouble(bundle.getString("rotationXPerNanos"));
    }

    @Override
    public void run() {
        long timeBefore = System.nanoTime();
        long timeAfter = timeBefore;
        while (true) {
            timeBefore = System.nanoTime();


            cont(timeAfter - timeBefore);


            timeAfter = System.nanoTime();

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public Point3D getImpact() {

        return distanceCount.getDistance();
    }

    public Point2D getImpactY() {
        return distanceCount.dirY;
    }

    public Terrain getTerrain() {
        return positionUpdate.getTerrain();
    }

    public Collection<Representable> getScene() {
        return getTerrain().getListRepresentable();
    }

    public boolean isActive() {
        return active;
    }

    public Point2D getPosition() {
        return positionUpdate.getPositionMobile().getPositionSol().to2DwoZ();
    }

    public boolean plot(Point3D position, Point3D deplacement, Cube plottee) {
        Point3D point3D = getTerrain().hauteur(position.getX() + deplacement.getX(), position.getY() + deplacement.getY(), position.getZ());
        plottee.setPosition(point3D);
        getScene().add(plottee);
        return true;
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
            active = true;
            distanceCount = new DistanceCount();
        }
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            activeShift = true;
            distanceCount = new DistanceCount();
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (isActive()) {

                distanceCount.countPressed();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            plot(distanceCount.dir.plus(getTerrain().calcCposition(getPosition().getX(), getPosition().getY())),
                    distanceCount.dir.plus(getTerrain().calcCposition(getPosition().getX(), getPosition().getY()))
                    , new Cube(0.1, Point3D.O0, new ColorTexture(new Color(Color.WHITE))));
        }
        if (e.getKeyCode() == KeyEvent.VK_Z) {
            release_up = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_S) {
            release_down = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_Q) {
            release_left = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            release_right = false;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
            active = false;
            distanceCount = new DistanceCount();
        }
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            activeShift = false;
            distanceCount = new DistanceCount();
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            distanceCount.countRelease();
        }
        if (e.getKeyCode() == KeyEvent.VK_Z) {
            release_up = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_S) {
            release_down = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_Q) {
            release_left = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            release_right = true;
        }
    }

    private void cont(long timeKeyPress) {
        if (isActive()) {
            Logger.getAnonymousLogger().log(Level.INFO, "Plotter is active");
            if (!release_up) {
                distanceCount.rotationHaut(timeKeyPress);
                //Logger.getAnonymousLogger().log(Level.INFO, "Acc");
            }
            if (!release_down) {
                distanceCount.rotationBas(timeKeyPress);
                //Logger.getAnonymousLogger().log(Level.INFO, "Dec");
            }
            if (!release_left) {
                distanceCount.rotationGauche(timeKeyPress);
                //Logger.getAnonymousLogger().log(Level.INFO, "Left");
            }
            if (!release_right) {
                distanceCount.rotationDroite(timeKeyPress);
                //Logger.getAnonymousLogger().log(Level.INFO, "Right");
            }
        }
    }


    class DistanceCount {
        ResourceBundle bundle = ResourceBundle.getBundle("one.empty3.apps.opad.Bundle");
        private double distance = 1;
        private Point3D dir;
        private long timeMiliisStart;
        private long timeElapsedMiliis;
        private boolean distanceNull;
        private boolean isReleased = true;
        private double acc = Double.parseDouble(bundle.getString("plotter.speed"));
        private double angle = 0;
        private double angleY = Math.PI / 2;
        private Point2D dirY;

        public DistanceCount() {
            this.dir = positionUpdate.calcDirection();
            if (dir == null) {
                dir = Point3D.O0;
            }
            angle = positionUpdate.getAngle();
        }

        public void countPressed() {
            if (isReleased) {
                timeMiliisStart = System.currentTimeMillis();
                timeElapsedMiliis = 0;
                acc = Double.parseDouble(bundle.getString("plotter.speed"));
            } else {
                distance += distance * timeElapsedMiliis * acc;
                timeElapsedMiliis = System.currentTimeMillis() - timeMiliisStart;

            }
        }

        public void countRelease() {
            timeElapsedMiliis = System.currentTimeMillis() - timeMiliisStart;
            isReleased = true;
            Bullet bullet = new Bullet(positionUpdate.calcCposition(), positionUpdate.calcDirection(), 0.0001, null, System.nanoTime());
            getTerrain().add(bullet);
        }

        public Point3D getDistance() {
            return dir.mult(distance);
        }

        public void setDir(Point3D dir) {
            this.dir = dir;
        }


        public void rotationGauche(long timeMillis) {
            angle -= Math.PI * 2 * rotationXParNano * timeMillis;
            dir
                    = new Point3D(
                    Math.sin(angle) * Math.cos(angleY),
                    Math.cos(angle) * Math.cos(angleY),
                    dir.getZ() + angle);
            positionUpdate.getPositionMobile().setAngleVueMobile(dir);
            Logger.getAnonymousLogger().log(Level.INFO, "RG");
        }

        public void rotationDroite(long timeMillis) {
            angle += Math.PI * 2 * rotationXParNano * timeMillis;
            dir
                    = new Point3D(
                    Math.sin(angle) * Math.cos(angleY),
                    Math.cos(angle) * Math.cos(angleY),
                    dir.getZ() + angle);
            positionUpdate.getPositionMobile().setAngleVueMobile(dir);
            Logger.getAnonymousLogger().log(Level.INFO, "RD");

        }

        public void rotationHaut(long timeMillis) {
            angleY -= Math.PI * 2 * rotationYParNano * timeMillis;
            dir
                    = new Point3D(
                    Math.sin(angle) * Math.cos(angleY),
                    Math.cos(angle) * Math.cos(angleY),
                    Math.sin(angleY));
            positionUpdate.getPositionMobile().setAngleVueMobile(dir);
            Logger.getAnonymousLogger().log(Level.INFO, "RH");
        }

        public void rotationBas(long timeMillis) {
            angleY += Math.PI * 2 * rotationYParNano * timeMillis;
            dir = new Point3D(
                    Math.sin(angle) * Math.cos(angleY),
                    Math.cos(angle) * Math.cos(angleY),
                    Math.sin(angleY));

            positionUpdate.getPositionMobile().setAngleVueMobile(dir);
            Logger.getAnonymousLogger().log(Level.INFO, "RB");
        }

        public Point2D getDirY() {
            return dirY;
        }
    }
}

