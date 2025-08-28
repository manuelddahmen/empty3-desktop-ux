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

import one.empty3.library.*;

import one.empty3.library.Point;
import one.empty3.libs.*;
import one.empty3.libs.Color;
import one.empty3.libs.Image;

import java.awt.*;
import java.awt.image.BufferedImage;

public class EcDrawer extends Drawer implements Runnable {

    protected DarkFortressGUI component;
    private Terrain terrain;
    private Bonus bonus;
    protected ZBuffer z;
    protected int w, h, aw, ah;
    private Vaisseau vaisseau;
    private PositionUpdate mover;

    public EcDrawer(DarkFortressGUI darkFortress) {

        super();


        this.component = darkFortress;

        z = new ZBufferImpl(640, 480);
        ((ZBufferImpl) z).setIncrementOptimizer(new ZBufferImpl.IncrementOptimizer(ZBufferImpl.IncrementOptimizer.Strategy.NONE, 0.1));
        ((ZBufferImpl) z).setDisplayType(ZBufferImpl.SURFACE_DISPLAY_COL_QUADS);
        darkFortress.setSize(640, 480);
        new Thread(this).start();

        initFrame(component);

    }

    public void resize() {
        z = ZBufferFactory.instance(w, h);
        z.couleurDeFond(new ColorTexture( new Color(Color.black)));
        ((ZBufferImpl) z).setIncrementOptimizer(new ZBufferImpl.IncrementOptimizer(ZBufferImpl.IncrementOptimizer.Strategy.NONE, 0.1));
        ((ZBufferImpl) z).setDisplayType(ZBufferImpl.SURFACE_DISPLAY_COL_QUADS);
        ah = h;
        aw = w;
    }

    @Override

    public void setLogic(PositionUpdate m) {
        this.mover = m;
        vaisseau = new Vaisseau(mover);
        terrain = mover.getTerrain();
        bonus = new Bonus();
        mover.ennemi(bonus);
    }

    @Override
    public void run() {

        while (true) {
            dessiner();

            w = component.getWidth();
            h = component.getHeight();

            if (ah != h || aw != w) {
                resize();
            }
            /*try {
             Thread.sleep(10);
             } catch (InterruptedException e) {
             e.printStackTrace();
             }*/
        }
    }


    public void dessiner() {
        Graphics g = component.getGraphics();

        //z.couleurDeFond(new TColor(java.awt.Color.BLACK));
        if (g != null && component.getWidth() > 0 && component.getHeight() > 0) {

            Scene scene = new Scene();

            if (mover != null) {
                //scene.add(mover.getCircuit());
                //scene.add(terrain);
                scene.add(bonus);
                scene.add(vaisseau.getObject());

                if (toggleMenu.isDisplayBonus()) {
                    bonus.getListRepresentable().forEach(representable -> {
                        Point3D center = ((TRISphere2) representable).getCoords();
                        ((TRISphere2) representable).getCircle().getAxis().getElem().setCenter(terrain.p3(center));
                        scene.add(representable);
                    });
                }
                Camera camera;
                if (mover.getPlotter3D() != null && mover.getPlotter3D().isActive())
                    camera = mover.getPositionMobile().calcCameraMobile();
                else
                    camera = mover.getPositionMobile().calcCamera();

                Point3D pos = camera.getEye();
                Point3D dir = camera.getLookat().moins(pos).norme1();
                Point3D up = camera.getVerticale();


                Point3D posCam = pos;//.moins(dir.norme1());

                posCam = posCam.plus(camera.getLookat().moins(posCam).mult(-0.05));

                scene.cameraActive(new Camera(posCam, pos.plus(dir), up));
                scene.cameraActive().declareProperties();
            }
            try {
                z.idzpp();
                z.scene(scene);
                z.draw(scene);
            } catch (Exception ex) {
                System.err.println(ex);
            }
            Image ri = z.image();

            Graphics g2 = ((BufferedImage)ri).getGraphics();
            g2.setColor(Color.WHITE);
            //g2.drawString("Score : " + mover.score(), 0, ri.getHeight() - 40);

            g.drawImage(ri, 0, 0, component.getWidth(), component.getHeight(), null);

        }
    }

    public boolean isLocked() {
        return z.isLocked();
    }

    @Override
    public LineSegment click(Point2D p) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
