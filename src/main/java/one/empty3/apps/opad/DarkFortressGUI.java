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

import com.jogamp.newt.event.KeyListener;
import one.empty3.apps.opad.menu.ToggleMenu;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DarkFortressGUI extends JFrame {
    private final Class<? extends Drawer> clazz;
    public PositionUpdateImpl positionUpdate;
    public PositionUpdate mover;
    //Plotter3D plotter3D;
    protected Drawer drawer;
    private Class<? extends Drawer> drawerType;
    protected String Title;
    private DarkFortressGUIKeyListener gameKeyListener;
    private Game game;
    Plotter3D plotter3D;


    public DarkFortressGUI(Class<? extends Drawer> clazz) {
        super();
        this.clazz = clazz;
        this.drawerType = clazz;
        Title = "Dark Fortress ";
        setTitle(Title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void setLevel(Class<Terrain> sol, Player player) {
        // Sets level; initializes and starts game components
        try {
            Terrain t = sol.getConstructor().newInstance();
            mover = createMover(t, player);
            gameKeyListener = new DarkFortressGUIKeyListener(mover);
            plotter3D = new Plotter3D(mover);
            mover.setPlotter3D(plotter3D);

            new Thread(mover, "PositionUpdate").start();
            new Thread(gameKeyListener, "DarkFortressGUIKeyListener").start();
            new Thread(plotter3D, "Plotter3D").start();

            Logger.getLogger(DarkFortressGUI.class.getName()).log(Level.INFO, drawerType.getSimpleName());

            // Selects rendering backend; configures title and drawer
            drawer = createDrawer();
            if (drawer == null) {
                Logger.getLogger(DarkFortressGUI.class.getName()).log(Level.SEVERE,
                        "No drawer available for {0}", drawerType);
                return;
            }

            setTitle(Title);

            drawer.setLogic(mover);
            drawer.setToggleMenu(new ToggleMenu());
            drawer.setLevel(sol);

            setMinimumSize(new Dimension(640, 480));
            setFocusable(true);
            addKeyListener(gameKeyListener);

            if (drawer instanceof JoglDrawer) {
                mover.setMain(this);
            }

            setLocationRelativeTo(null);
            setVisible(true);

            if (drawer instanceof JoglDrawer joglDrawer) {
                SwingUtilities.invokeLater(() -> {
                    joglDrawer.getGlcanvas().requestFocusInWindow();
                    if (!joglDrawer.getAnimator().isStarted()) {
                        joglDrawer.getAnimator().start();
                    }
                });
            }
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException ex) {
            Logger.getLogger(DarkFortressGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    /*__
     * Creates the game logic that drives this window.
     *
     * <p>Subclasses override it to plug in another mover, for instance the
     * server-synchronised {@code one.empty3.apps.opad.server.NetworkPositionUpdate}.</p>
     */
    protected PositionUpdate createMover(Terrain t, Player player) {
        return new PositionUpdateImpl(t, player);
    }

    /*__
     * Creates the rendering backend for {@link #getDrawerType()} and appends its name
     * to the window title.
     *
     * @return the drawer, or {@code null} if the requested type is not supported
     */
    protected Drawer createDrawer() {
        if (drawerType.equals(JoglDrawer.class)) {
            Title += "with OpenGL bindings";
            return new JoglDrawer(this);
        }
        if (drawerType.equals(EcDrawer.class)) {
            Title += "with Empty Canvas rendering";
            return new EcDrawer(this);
        }
        return null;
    }

    /*__ @return the rendering backend asked for at construction time */
    protected Class<? extends Drawer> getDrawerType() {
        return drawerType;
    }

    public Drawer getDrawer() {
        return drawer;
    }

    public KeyListener getGameKeyListener() {
        return (KeyListener) gameKeyListener;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
