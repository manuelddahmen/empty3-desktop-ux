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

package one.empty3.apps.opad.server;

import one.empty3.libs.Color;

/*__
 * One player as seen by everybody else.
 *
 * <p>Positions are expressed in the map parameter space used by
 * {@code PositionMobile.getPositionSol()}: {@code x} and {@code y} in
 * {@code [0, 1]} and {@code z} the height above the surface. The receiving client
 * turns them into world coordinates with {@code Terrain.p3(..)}, so the same state
 * renders correctly on every kind of ground.</p>
 *
 * @author Manuel Dahmen dathewolf@gmail.com
 */
public class PlayerState {

    /*__ Server assigned identifier, unique for the lifetime of the server. */
    public int id;
    public String name;
    /*__ Ship colour, packed as {@code 0xRRGGBB}. */
    public int colorRgb = Color.newCol(0, 0.5f, 0).getRGB();
    public double x = 0.5;
    public double y = 0.5;
    public double z;
    /*__ Heading, in turns, as stored in {@code getAngleVisee().getZ()}. */
    public double angleZ;
    public double score;
    public boolean connected = true;

    /*__ Required by Gson. */
    public PlayerState() {
    }

    public PlayerState(int id, String name, int colorRgb, double z) {
        this.id = id;
        this.name = name;
        this.colorRgb = colorRgb;
        this.z = z;
    }

    /*__
     * @return a defensive copy, so that a snapshot handed to the network threads is
     * not mutated while it is being serialised.
     */
    public PlayerState copy() {
        PlayerState c = new PlayerState(id, name, colorRgb, z);
        c.x = x;
        c.y = y;
        c.angleZ = angleZ;
        c.score = score;
        c.connected = connected;
        return c;
    }

    @Override
    public String toString() {
        return "PlayerState{" + id + " " + name + " score=" + score
                + " at (" + x + ", " + y + ", " + z + ")}";
    }
}
