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

/*__
 * One bonus of the shared map.
 *
 * <p>Like {@link PlayerState}, coordinates are in map parameter space; the client
 * rebuilds the sphere with {@code Terrain.p3(..)}. The {@code id} is what clients
 * and server exchange when a bonus is taken, so it must stay stable for the whole
 * session.</p>
 *
 * @author Manuel Dahmen dathewolf@gmail.com
 */
public class BonusState {

    /*__ Session-unique identifier, e.g. {@code "b17"}. */
    public String id;
    /*__ Name of a {@link BonusKind} constant. */
    public String kind;
    public double x;
    public double y;
    public double z;
    /*__ Sphere radius in world units, also the catch distance. */
    public double radius;
    /*__ Points awarded, negative for the penalty bonuses. */
    public double value;
    public boolean taken;
    /*__ Id of the player who took it, {@code 0} while untaken. */
    public int takenBy;

    /*__ Required by Gson. */
    public BonusState() {
    }

    public BonusState(String id, BonusKind kind, double x, double y, double z, double radius) {
        this.id = id;
        this.kind = kind.name();
        this.x = x;
        this.y = y;
        this.z = z;
        this.radius = radius;
        this.value = kind.points();
    }

    public BonusState copy() {
        BonusState c = new BonusState();
        c.id = id;
        c.kind = kind;
        c.x = x;
        c.y = y;
        c.z = z;
        c.radius = radius;
        c.value = value;
        c.taken = taken;
        c.takenBy = takenBy;
        return c;
    }

    @Override
    public String toString() {
        return "BonusState{" + id + " " + kind + " value=" + value + " taken=" + taken + "}";
    }
}
