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

import com.jogamp.newt.event.KeyListener;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLDrawableFactory;
import com.jogamp.opengl.awt.GLCanvas;
import one.empty3.apps.opad.menu.ToggleMenu;

import javax.swing.*;

import one.empty3.library.Point;
import one.empty3.libs.*;

import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DarkFortressGUI extends JFrame {
    private final Class<? extends Drawer> clazz;
    public PositionUpdateImpl positionUpdate;
    public PositionUpdate mover;
    //Plotter3D plotter3D;
    Drawer drawer;
    private Class<? extends Drawer> drawerType;
    String Title;
    private DarkFortressGUIKeyListener gameKeyListener;
    private Game game;
    Plotter3D plotter3D;


    /*public Plotter3D getPlotter3D() {
        return plotter3D;
    }*/

    public DarkFortressGUI(Class<? extends Drawer> clazz) {
        super();
        this.clazz = clazz;
        this.drawerType = clazz;
        Title = "Dark Fortress ";
        setTitle(Title);
        setExtendedState(JFrame.EXIT_ON_CLOSE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void setLevel(Class<Terrain> sol, Player player) {
        try {
            Terrain t = sol.getConstructor().newInstance();
            mover = new PositionUpdateImpl(t, player);
            new Thread(mover).start();
            gameKeyListener = new DarkFortressGUIKeyListener(mover);
            plotter3D = new Plotter3D(mover);
            mover.setPlotter3D(plotter3D);
            new Thread(mover).start();
            new Thread(gameKeyListener).start();
            new Thread(plotter3D).start();


            Logger.getLogger(DarkFortressGUI.class.getName()).log(Level.INFO, drawerType.getSimpleName());

            if (drawerType.equals(JoglDrawer.class)) {
                Title += "with OpenGL bindings";
                drawer = new JoglDrawer(this);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                drawerType = JoglDrawer.class;


            } else if (drawerType.equals(EcDrawer.class)) {
                Title += "with Empty Canvas rendering";
                drawer = new EcDrawer(this);
                drawerType = JoglDrawer.class;
            }


            drawer.setLogic(mover);
            //drawer.setPlotter3D(plotter3D);
            drawer.setToggleMenu(new ToggleMenu());
            drawer.setLevel(sol);

            //addKeyListener(plotter3D);


            if (drawer instanceof JoglDrawer) {
                ((JoglDrawer) drawer).getGlcanvas().display();
                ((JoglDrawer) drawer).getGlcanvas().getAnimator().start();
                mover.setMain(this);
            }

            setMinimumSize(new Dimension(640, 480));
            setFocusable(true);
            addKeyListener(gameKeyListener);
            //addKeyListener(plotter3D);


            setVisible(true);


        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException ex) {
            Logger.getLogger(DarkFortressGUI.class.getName()).log(Level.SEVERE, null, ex);
        }

    }


    public KeyListener getGameKeyListener() {
        return (KeyListener) gameKeyListener;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
