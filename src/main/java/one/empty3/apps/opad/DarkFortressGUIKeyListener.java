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


import com.jogamp.newt.event.KeyEvent;
import one.empty3.apps.opad.menu.ToggleMenu;

import java.awt.event.KeyListener;
import java.util.logging.Level;
import java.util.logging.Logger;



/*__
 * Created by manuel on 07-06-17.
 */

public class DarkFortressGUIKeyListener implements KeyListener, Runnable {
    private final PositionUpdate mover;
    protected boolean release_up = true;
    protected boolean release_down = true;
    protected boolean release_left = true;
    protected boolean release_right = true;
    protected boolean release_space = true;
    protected boolean ctrl = false;
    ToggleMenu toggleMenu;

    public DarkFortressGUIKeyListener(PositionUpdate mover) {
        this.mover = mover;
        toggleMenu = new ToggleMenu();
    }

    private void cont(long timeKeyPress) {
        timeKeyPress = Math.abs(timeKeyPress);
        //Logger.getAnonymousLogger().log(Level.INFO, "cont"+timeKeyPress);
        //Logger.getAnonymousLogger().log(Level.INFO, "ctrl"+ctrl+"releaseUp"+release_up);

        if (!ctrl) {
            if (!release_up) {
                Logger.getAnonymousLogger().log(Level.INFO, "Acc");
                mover.acc(timeKeyPress);
            }
            if (!release_down) {
                mover.dec(timeKeyPress);
                Logger.getAnonymousLogger().log(Level.INFO, "Dec");
            }
            if (!release_left) {
                mover.rotationGauche(timeKeyPress);
                Logger.getAnonymousLogger().log(Level.INFO, "Left");
            }
            if (!release_right) {
                mover.rotationDroite(timeKeyPress);
                Logger.getAnonymousLogger().log(Level.INFO, "Right");
            }
            if (!release_space) {
                mover.moveUp(timeKeyPress);
            }

        }
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

    @Override
    public void keyTyped(java.awt.event.KeyEvent e) {

    }

    @Override
    public void keyPressed(java.awt.event.KeyEvent e) {
        Logger.getAnonymousLogger().log(Level.INFO, "Key Pressed");
        if (e.getKeyChar() == 't')
            toggleMenu.setDisplayMenu(!toggleMenu.isDisplayMenu());
        if (e.getKeyChar() == '+')
            toggleMenu.setIndex(toggleMenu.getIndex() + 1);
        if (e.getKeyChar() == '-')
            toggleMenu.setIndex(toggleMenu.getIndex() - 1);
        if (e.getKeyChar() == '\n')
            toggleMenu.setOption(toggleMenu.getIndex(), !toggleMenu.getOption(toggleMenu.getIndex()));
        if (e.getKeyCode() == KeyEvent.VK_CONTROL
                && mover.state() == mover.STATE_GAME_IN_PROGRESS()) {
            ctrl = true;
        }
        if (e.getExtendedKeyCode() == KeyEvent.VK_Z
                && mover.state() == mover.STATE_GAME_IN_PROGRESS()) {
            Logger.getAnonymousLogger().log(Level.INFO, "UPPRESSED");
            release_up = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE
                && mover.state() == mover.STATE_GAME_IN_PROGRESS()) {
            release_space = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_S
                && mover.state() == mover.STATE_GAME_IN_PROGRESS()) {
            release_down = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_Q
                && mover.state() == mover.STATE_GAME_IN_PROGRESS()) {
            release_left = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_D
                && mover.state() == mover.STATE_GAME_IN_PROGRESS()) {
            release_right = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }

    }

    @Override
    public void keyReleased(java.awt.event.KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_CONTROL
                && mover.state() == mover.STATE_GAME_IN_PROGRESS()) {
            ctrl = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_Z
                && mover.state() == mover.STATE_GAME_IN_PROGRESS()) {
            release_up = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_S
                && mover.state() == mover.STATE_GAME_IN_PROGRESS()) {
            release_down = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_Q
                && mover.state() == mover.STATE_GAME_IN_PROGRESS()) {
            release_left = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_D
                && mover.state() == mover.STATE_GAME_IN_PROGRESS()) {
            release_right = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE
                && mover.state() == mover.STATE_GAME_IN_PROGRESS()) {
            release_space = true;
        }

    }
}
