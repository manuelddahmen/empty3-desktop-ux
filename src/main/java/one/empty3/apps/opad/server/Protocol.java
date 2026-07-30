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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

/*__
 * Wire format of the Opad multiplayer protocol: one JSON {@link NetMessage} per
 * line, UTF-8, over a plain TCP socket.
 *
 * <p>Newline delimiting is safe because Gson never emits a raw line break outside
 * of pretty printing, and escapes the ones a player may have typed in their
 * name.</p>
 *
 * <h2>Client to server</h2>
 * <ul>
 *     <li>{@link #JOIN} &mdash; first message of a connection, answered by
 *     {@link #WELCOME} or {@link #ERROR}.</li>
 *     <li>{@link #MOVE} &mdash; the sender's position, sent a few times a second.</li>
 *     <li>{@link #PICK} &mdash; a claim on a bonus. Only the server decides.</li>
 *     <li>{@link #LEAVE} &mdash; a clean goodbye; dropping the socket works too.</li>
 * </ul>
 *
 * <h2>Server to client</h2>
 * <ul>
 *     <li>{@link #WELCOME} &mdash; the assigned id, the map and the whole bonus list.</li>
 *     <li>{@link #STATE} &mdash; every player's position and score, on every tick.</li>
 *     <li>{@link #BONUS_TAKEN} &mdash; a bonus is gone, for everybody.</li>
 *     <li>{@link #GAME_OVER} &mdash; no bonus is left; carries the final scores.</li>
 *     <li>{@link #ERROR} &mdash; a rejected join or an unusable message.</li>
 * </ul>
 *
 * @author Manuel Dahmen dathewolf@gmail.com
 */
public final class Protocol {

    /*__ Default TCP port of the game server. */
    public static final int DEFAULT_PORT = 4712;

    /*__ Bumped whenever the meaning of a field changes; mismatches are refused. */
    public static final int VERSION = 1;

    /*__ Default period of the {@link #STATE} broadcast, in milliseconds. */
    public static final int DEFAULT_TICK_MILLIS = 50;

    /*__ Idle time after which a peer is considered gone, in milliseconds. */
    public static final int SOCKET_TIMEOUT_MILLIS = 30_000;

    // Client to server.
    public static final String JOIN = "join";
    public static final String MOVE = "move";
    public static final String PICK = "pick";
    public static final String LEAVE = "leave";

    // Server to client.
    public static final String WELCOME = "welcome";
    public static final String STATE = "state";
    public static final String BONUS_TAKEN = "bonusTaken";
    public static final String GAME_OVER = "gameOver";
    public static final String ERROR = "error";

    private static final Gson GSON = new GsonBuilder().create();

    private Protocol() {
    }

    /*__
     * @return {@code m} as a single JSON line, without the line terminator
     */
    public static String encode(NetMessage m) {
        return GSON.toJson(m);
    }

    /*__
     * @param line one JSON line, as produced by {@link #encode(NetMessage)}
     * @return the decoded message, or {@code null} if the line is blank, malformed
     * or carries no {@link NetMessage#type}
     */
    public static NetMessage decode(String line) {
        if (line == null || line.isBlank()) {
            return null;
        }
        try {
            NetMessage m = GSON.fromJson(line, NetMessage.class);
            return m != null && m.type != null ? m : null;
        } catch (JsonSyntaxException ex) {
            return null;
        }
    }
}
