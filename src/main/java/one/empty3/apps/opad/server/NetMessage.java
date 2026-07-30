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
 * The single envelope carried by the wire, in either direction.
 *
 * <p>One flat class rather than a hierarchy: Gson then needs no type adapter, an
 * unknown field simply stays {@code null}, and a new message type is one constant
 * plus one factory method. {@link #type} says which fields are meaningful; see
 * {@link Protocol} for the list.</p>
 *
 * @author Manuel Dahmen dathewolf@gmail.com
 */
public class NetMessage {

    /*__ One of the message type constants of {@link Protocol}. */
    public String type;

    /*__ {@link Protocol#VERSION} of the sender, checked on {@link Protocol#JOIN}. */
    public int protocolVersion;

    // ---- join ----
    public String playerName;
    /*__ Requested ship colour {@code 0xRRGGBB}; {@code 0} lets the server pick one. */
    public int colorRgb;

    // ---- welcome ----
    /*__ On {@code welcome} the id given to the receiver; on {@code bonusTaken} the taker. */
    public int playerId;
    /*__ Simple class name of the {@code Terrain} everybody plays on. */
    public String mapName;
    /*__ The full bonus list, sent once. Later changes come as {@code bonusTaken}. */
    public List<BonusState> bonuses;
    /*__ Server broadcast period, so the client can pace its own updates. */
    public int tickMillis;

    // ---- move / pick ----
    public double x;
    public double y;
    public double z;
    public double angleZ;

    // ---- pick / bonusTaken ----
    public String bonusId;
    /*__ Points the bonus was worth. */
    public double points;

    // ---- state / gameOver ----
    public List<PlayerState> players;
    public boolean gameOver;

    // ---- error, and the reason of a rejected pick ----
    public String message;

    /*__ Required by Gson. */
    public NetMessage() {
    }

    private NetMessage(String type) {
        this.type = type;
    }

    public static NetMessage join(String playerName, int colorRgb) {
        NetMessage m = new NetMessage(Protocol.JOIN);
        m.protocolVersion = Protocol.VERSION;
        m.playerName = playerName;
        m.colorRgb = colorRgb;
        return m;
    }

    public static NetMessage welcome(PlayerState me, String mapName, int tickMillis,
                                     List<BonusState> bonuses, List<PlayerState> players,
                                     boolean gameOver) {
        NetMessage m = new NetMessage(Protocol.WELCOME);
        m.protocolVersion = Protocol.VERSION;
        m.playerId = me.id;
        m.playerName = me.name;
        m.colorRgb = me.colorRgb;
        m.x = me.x;
        m.y = me.y;
        m.z = me.z;
        m.mapName = mapName;
        m.tickMillis = tickMillis;
        m.bonuses = bonuses;
        m.players = players;
        m.gameOver = gameOver;
        return m;
    }

    public static NetMessage move(double x, double y, double z, double angleZ) {
        NetMessage m = new NetMessage(Protocol.MOVE);
        m.x = x;
        m.y = y;
        m.z = z;
        m.angleZ = angleZ;
        return m;
    }

    /*__
     * A pick carries the position it was made from: the server validates the claim
     * against it instead of against a {@code move} that may still be in flight.
     */
    public static NetMessage pick(String bonusId, double x, double y, double z) {
        NetMessage m = new NetMessage(Protocol.PICK);
        m.bonusId = bonusId;
        m.x = x;
        m.y = y;
        m.z = z;
        return m;
    }

    public static NetMessage bonusTaken(String bonusId, int playerId, double points) {
        NetMessage m = new NetMessage(Protocol.BONUS_TAKEN);
        m.bonusId = bonusId;
        m.playerId = playerId;
        m.points = points;
        return m;
    }

    public static NetMessage state(List<PlayerState> players, boolean gameOver) {
        NetMessage m = new NetMessage(Protocol.STATE);
        m.players = players;
        m.gameOver = gameOver;
        return m;
    }

    public static NetMessage gameOver(List<PlayerState> finalScores) {
        NetMessage m = new NetMessage(Protocol.GAME_OVER);
        m.players = finalScores;
        m.gameOver = true;
        return m;
    }

    public static NetMessage leave() {
        return new NetMessage(Protocol.LEAVE);
    }

    public static NetMessage error(String message) {
        NetMessage m = new NetMessage(Protocol.ERROR);
        m.message = message;
        return m;
    }

    @Override
    public String toString() {
        return "NetMessage{" + type + " player=" + playerId + " bonus=" + bonusId + "}";
    }
}
