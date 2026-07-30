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

import one.empty3.apps.opad.Bonus;
import one.empty3.apps.opad.TRISphere2;
import one.empty3.apps.opad.help.BonusClass;
import one.empty3.library.ColorTexture;
import one.empty3.library.Point3D;
import one.empty3.library.Representable;
import one.empty3.libs.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*__
 * The bonus container of a multiplayer game: the same spheres the single player
 * {@link Bonus} draws, but laid out by the server and addressable by id.
 *
 * <p>Two indexes are kept next to the {@code RepresentableConteneur}: id to sphere,
 * so a {@link Protocol#BONUS_TAKEN} can remove the right one, and sphere to id, so
 * a local collision can name what it just touched. The second one is an identity map
 * on purpose: two spheres of the same kind sitting at the same spot must still be
 * told apart.</p>
 *
 * @author Manuel Dahmen dathewolf@gmail.com
 */
public class SyncedBonus extends Bonus {

    private final Map<String, Representable> byId = new ConcurrentHashMap<>();
    private final Map<Representable, String> idOf =
            Collections.synchronizedMap(new IdentityHashMap<>());

    /*__
     * @param states the server's bonus list; already taken entries are skipped, so a
     *               client joining a game in progress starts with the right map
     */
    public SyncedBonus(List<BonusState> states) {
        super(false);
        if (states != null) {
            for (BonusState state : states) {
                if (!state.taken) {
                    addBonus(state);
                }
            }
        }
    }

    private void addBonus(BonusState state) {
        BonusKind kind = BonusKind.of(state.kind);
        TRISphere2<BonusClass> sphere = new TRISphere2<>(this,
                new Point3D(state.x, state.y, state.z), state.radius);
        sphere.texture(new ColorTexture(new Color(kind.colorRgb())));

        BonusClass gameObject = new BonusClass();
        gameObject.setValue(state.value);
        sphere.setGameObject(gameObject);

        byId.put(state.id, sphere);
        idOf.put(sphere, state.id);
        add(sphere);
    }

    /*__
     * @return the server id of a sphere of this container, or {@code null} if it is
     * not one of ours or has already been removed
     */
    public String idOf(Representable representable) {
        return idOf.get(representable);
    }

    public boolean contains(String bonusId) {
        return byId.containsKey(bonusId);
    }

    /*__
     * Takes a bonus out of the scene, on the server's order.
     *
     * @return {@code false} if it was already gone, which happens when the same
     * removal is applied twice
     */
    public boolean removeById(String bonusId) {
        Representable representable = byId.remove(bonusId);
        if (representable == null) {
            return false;
        }
        idOf.remove(representable);
        removeBonus(representable);
        return true;
    }

    /*__ @return how many bonuses are still on the map */
    public int remaining() {
        return byId.size();
    }

    /*__
     * @return the spheres still on the map, as a list safe to iterate while the
     * network thread removes taken ones
     */
    public List<Representable> snapshot() {
        return new ArrayList<>(byId.values());
    }
}
