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

import java.util.List;

/*__
 * Callbacks of a {@link GameClient}.
 *
 * <p>All of them run on the client's reader thread, never on the Swing or the OpenGL
 * one: an implementation must keep them short and hand anything that touches the UI
 * over to {@code SwingUtilities.invokeLater}. Every method has a default so that a
 * listener only overrides what it cares about.</p>
 *
 * @author Manuel Dahmen dathewolf@gmail.com
 */
public interface GameClientListener {

    /*__ The server accepted the join; map and bonus list are now readable. */
    default void onWelcome(GameClient client) {
    }

    /*__ A new position and score snapshot arrived. Fires on every server tick. */
    default void onState(GameClient client) {
    }

    /*__
     * A bonus left the map, for everybody.
     *
     * @param bonusId  the bonus to remove from the local scene
     * @param playerId who took it; compare with {@link GameClient#getPlayerId()}
     * @param points   what it was worth
     */
    default void onBonusTaken(String bonusId, int playerId, double points) {
    }

    /*__ No bonus is left. @param finalScores every player, as the server saw them */
    default void onGameOver(List<PlayerState> finalScores) {
    }

    /*__ The connection ended. @param reason human readable, may be {@code null} */
    default void onDisconnected(String reason) {
    }

    /*__ The server refused something. */
    default void onServerError(String message) {
    }
}
