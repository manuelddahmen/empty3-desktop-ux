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

import one.empty3.apps.opad.menu.ToggleMenu;
import one.empty3.library.LineSegment;
import one.empty3.library.Point2D;

import one.empty3.library.Point;
import one.empty3.libs.*;

import java.awt.Frame;
import java.util.logging.Logger;

public abstract class Drawer {


    protected ToggleMenu toggleMenu = new ToggleMenu();
    //protected Plotter3D plotter3D;
    protected Class level;

    public abstract void setLogic(PositionUpdate l);

    public void initFrame(Frame component) {
        //    component.setVisible(true);

    }

    /*__
     *
     * @param p Point 2D in the window (mouse cordinates)
     * @return Segment Near Far direction of click
     */
    public abstract LineSegment click(Point2D p);

    public void setToggleMenu(ToggleMenu toggleMenu) {
        this.toggleMenu = toggleMenu;
    }

    /*public void setPlotter3D(Plotter3D plotter3D) {
        this.plotter3D  = plotter3D;
    }*/

    public Class getLevel() {
        return level;
    }

    public void setLevel(Class level) {
        this.level = level;
        Logger.getAnonymousLogger().info("Level: " + level.getCanonicalName());

    }

    public void update() {

    }
}
