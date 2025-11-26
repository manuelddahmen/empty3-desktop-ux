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

package one.empty3.apps.newboardgames; import one.empty3.libs.*;

import java.awt.Dimension;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//public class JoglDrawerBoardGame extends JoglDrawer {
//
//    private Board board;
//    private Camera camera;
//
//    public JoglDrawerBoardGame(WindowDrawing darkFortressGUI) {
//        super(darkFortressGUI);
//    }
//
//    public void setCamera(Camera camera) {
//        this.camera = camera;
//    }
//    public void setBoard(Board board) {
//        this.board = board;
//        this.camera = board.camera();
//    }
//    @Override
//    public void display(GLAutoDrawable gLDrawable) {
//        if (!wasAnimating) {
//            animator.start();
//            wasAnimating = true;
//        }
//
//        millis = System.currentTimeMillis();
//        Logger.getAnonymousLogger().log(Level.INFO, "FPS " + 1000/((millis - millis0)));
//        millis0 = millis;
//
//        try {
//            if (glu == null )
//                glu = GLU.createGLU();
//
//            gl = gLDrawable.getGL().getGL2();
//            //glu = GLU.createGLU();
//        } catch (Exception e) {
//            try {
//                Thread.sleep(200);
//            } catch (InterruptedException ex) {
//                ex.printStackTrace();
//            }
//            e.printStackTrace();
//            return;
//        }
//        // Change to projection matrix.
//        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
//
//        glu.gluPerspective(60, 1.33, 1.0, 100.0);
//        gl.glLoadIdentity();
//
//        if(board!=null && board.camera!=null) {
//            //Logger.getAnonymousLogger().log(Level.INFO, "setCamera");
//            //camera = board.camera();
//        } else if(board==null)
//            Logger.getAnonymousLogger().log(Level.INFO, "board==null");
//
//
//        if(camera!=null) {
//            Point3D pos = camera.getEye();
//            Point3D dir = camera.getLookat().moins(pos).norme1();
//            diff = dir.moins(pos).norme1();
//            Point3D up = camera.getVerticale();
//
//
//            Point3D posCam = pos;//.moins(dir.norme1());
//            Point3D vertical = camera.getVerticale().norme1();
//            Point3D vert2 = vertical.prodVect(dir).mult(-1);
//
//            posCam = posCam.plus(camera.getLookat().moins(posCam).mult(-0.05));
//
//            up = dir.prodVect(up.prodVect(dir));
//
//            glu.gluLookAt(posCam.get(0), posCam.get(1), posCam.get(2),
//                    dir.get(0), dir.get(1), dir.get(2),
//                    up.get(0), up.get(1), up.get(2));
//
//            if (toggleMenu.isDisplayScore())
//                draw("Score :  " + 100, Color.WHITE, glu, gl);
//            if (toggleMenu.isDisplayEnergy())
//                draw("Life :  " + 100, new Dimension(30, 10), Color.GREEN, glu, gl);
//
//
//            drawToggleMenu(glu, gl);
//
//            drawTrajectory(getPlotter3D(), glu, gl);
//
//            Sphere sphere = new Sphere(new Axe(Point3D.Y.mult(10), Point3D.Y.mult(-10)), 10);
//            sphere.texture(new ColorTexture(Color.blue));
//            sphere.setIncrU(0.1);
//            sphere.setIncrV(0.1);
//
//            draw(sphere, glu, gl);
//
//            for(int i=0; i<board.getSize2D().getX(); i++)
//                for(int j=0; j<board.getSize2D().getY(); j++)
//                    draw((RepresentableConteneur) board.cellAt(i, j), glu, gl);
//        } else {
//            Logger.getAnonymousLogger().log(Level.INFO, "Camera == null");
//        }
//
//
//    }
//
//}
