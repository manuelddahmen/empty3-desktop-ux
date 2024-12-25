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

/*__
Global license : 

 CC Attribution

 author Manuel Dahmen _manuel.dahmen@gmx.com_

 ***/


package one.empty3.apps.opad;

/*__
 *
 * Meta Description missing
 * @author Manuel Dahmen dathewolf@gmail.com
 */
public class Timer {
    double timeEllapsed = 0.0;
    long timeEllapsedSystem = 0;

    public Timer() {
    }

    public void init() {
        timeEllapsedSystem = System.nanoTime();
        timeEllapsed = 0.0;
    }

    public double getTimeEllapsed() {
        long timeInter = System.nanoTime();

        timeEllapsed = (timeInter - timeEllapsedSystem) / 10000.0;

        return timeEllapsed;
    }

    public void reset() {
        init();
    }

}
