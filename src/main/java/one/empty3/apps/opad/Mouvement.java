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

import one.empty3.library.Point3D;
import one.empty3.library.Representable;
import one.empty3.library.core.nurbs.ParametricCurve;

public abstract class Mouvement extends Thread {
    protected Representable representable;
    private Timer timer;
    private Thread thread;

    public Mouvement(Representable representable, long timeNano, ParametricCurve parametricCurve) {
        this.representable = representable;
        move(timeNano, parametricCurve);
    }

    public void threadMove() {

    }

    public void run() {
        timer = new Timer();
        if (thread != null) {

            thread.start();


        }
    }

    public void move(long timeNano, ParametricCurve parametricCurve) {
        thread = new Thread() {
            public void run() {
                startMoveAction();

                while (timeNano <= timer.timeEllapsed) {
                    double moveTimeRatio = 1.0 * timeNano / timer.timeEllapsed;
                    setPosition(parametricCurve.calculerPoint3D(moveTimeRatio));
                    action(representable, moveTimeRatio);
                }

                endMoveAction();
            }
        };
    }

    protected abstract void action(Representable representable, double moveTimeRatio);

    protected abstract void setPosition(Point3D position);

    protected abstract void startMoveAction();

    protected abstract void endMoveAction();
}
