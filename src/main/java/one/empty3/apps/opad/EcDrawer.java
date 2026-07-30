/*
 *
 *  *
 *  *  * Copyright (c) 2026. Manuel Daniel Dahmen
 *  *  *
 *  *  *
 *  *  *    Copyright 2026 Manuel Daniel Dahmen
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

import one.empty3.library.*;
import one.empty3.libs.Color;
import one.empty3.libs.Image;

import java.awt.*;
import java.awt.image.BufferedImage;

public class EcDrawer extends Drawer implements Runnable {

    protected DarkFortressGUI component;
    protected Terrain terrain;
    protected Bonus bonus;
    protected ZBuffer z;
    protected int w, h, aw, ah;
    protected Vaisseau vaisseau;
    protected PositionUpdate mover;

    public EcDrawer(DarkFortressGUI darkFortress) {

        super();


        this.component = darkFortress;


        darkFortress.setSize(640, 480);
        new Thread(this).start();

        w = darkFortress.getWidth();
        h = darkFortress.getHeight();

        initZ();

        initFrame(component);



    }
    private void initZ() {
        z = ZBufferFactory.instance(w, h);
        ((ZBufferImpl) z).setIncrementOptimizer(new ZBufferImpl.IncrementOptimizer(ZBufferImpl.IncrementOptimizer.Strategy.ENSURE_MAXIMUM_PERFORMANCE, 1));
        ((ZBufferImpl) z).setDisplayType(ZBufferImpl.SURFACE_DISPLAY_TEXT_QUADS);

    }
    public void resize() {
        initZ();
        w = component.getWidth();
        h = component.getHeight();
        ah = h;
        aw = w;
    }

    @Override

    public void setLogic(PositionUpdate m) {
        this.mover = m;
        vaisseau = new Vaisseau(mover);
        terrain = mover.getTerrain();
        bonus = createBonus(m);
        mover.ennemi(bonus);
    }

    /*__
     * Builds the bonus container for this drawer.
     *
     * <p>Subclasses override it when bonuses come from the server instead of being
     * generated locally. See {@code one.empty3.apps.opad.server.NetworkedEcDrawer}.</p>
     */
    protected Bonus createBonus(PositionUpdate m) {
        return new Bonus();
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
                addExtraObjects(scene);

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
            BufferedImage ri = z.image().getBi();

            Graphics g2 = ((BufferedImage) ri).getGraphics();
            //g2.drawString("Score : " + mover.score(), 0, ri.getHeight() - 40);
            g2.setColor(Color.BLACK);
            g.fillRect(0, 0, ri.getWidth(), ri.getHeight());
            g2.setColor(Color.WHITE);
            g.drawImage(ri, 0, 0, component.getWidth(), component.getHeight(), null);

        }
    }

    /*__
     * Hook for subclasses to put their own objects in the scene being drawn, next to
     * the local player's ship. Called on the drawing thread, once per frame.
     * See {@code one.empty3.apps.opad.server.NetworkedEcDrawer}.
     */
    protected void addExtraObjects(Scene scene) {
    }

    public boolean isLocked() {
        return z.isLocked();
    }

    @Override
    public LineSegment click(Point2D p) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
