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

import one.empty3.apps.opad.Terrain;

/*__
 * The maps a multiplayer session can be played on: the same list the single player
 * level menu offers, addressed by name instead of by index.
 *
 * <p>A name travels over the wire in {@link NetMessage#mapName}, so that a joining
 * client loads the ground the server already chose. Unlike
 * {@code one.empty3.apps.opad.menu.LevelMenu#loadClass()} this resolver reports a
 * failure to its caller instead of calling {@code System.exit}, which matters when
 * the failure comes from a remote peer.</p>
 *
 * @author Manuel Dahmen dathewolf@gmail.com
 */
public final class MapCatalog {

    /*__ Simple class names of the playable {@code Terrain} implementations. */
    public static final String[] MAPS = {
            "SolPlan",
            "SolRelief",
            "SolReliefMouvant",
            "SolSphere",
            "SolTube"};

    private static final String PACKAGE = "one.empty3.apps.opad.";

    private MapCatalog() {
    }

    public static String defaultMap() {
        return MAPS[0];
    }

    public static boolean isKnown(String mapName) {
        if (mapName == null) {
            return false;
        }
        for (String map : MAPS) {
            if (map.equals(mapName)) {
                return true;
            }
        }
        return false;
    }

    /*__
     * @param mapName a simple class name taken from {@link #MAPS}
     * @return the {@code Terrain} subclass to instantiate
     * @throws IllegalArgumentException if the name is not a known map, or names a
     *                                  class that is missing or is not a {@code Terrain}
     */
    @SuppressWarnings("unchecked")
    public static Class<Terrain> resolve(String mapName) {
        if (!isKnown(mapName)) {
            throw new IllegalArgumentException("Unknown map: " + mapName);
        }
        String className = PACKAGE + mapName;
        try {
            Class<?> clazz = Class.forName(className);
            if (!Terrain.class.isAssignableFrom(clazz)) {
                throw new IllegalArgumentException(className + " is not a Terrain");
            }
            return (Class<Terrain>) clazz;
        } catch (ClassNotFoundException | NoClassDefFoundError ex) {
            throw new IllegalArgumentException("Cannot load map class " + className, ex);
        }
    }
}
