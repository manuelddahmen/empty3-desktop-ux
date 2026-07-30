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

/*__
 * Multiplayer support for the Opad game.
 *
 * <p>The package turns the single player game of {@code one.empty3.apps.opad} into a
 * client/server one. It is split in three parts:</p>
 *
 * <ul>
 *     <li><b>Protocol</b> &mdash; {@link one.empty3.apps.opad.server.Protocol},
 *     {@link one.empty3.apps.opad.server.NetMessage},
 *     {@link one.empty3.apps.opad.server.PlayerState} and
 *     {@link one.empty3.apps.opad.server.BonusState}. Newline delimited JSON over
 *     a plain TCP socket.</li>
 *
 *     <li><b>Server</b> &mdash; {@link one.empty3.apps.opad.server.GameServer} plus
 *     {@link one.empty3.apps.opad.server.ServerGameSession}, which owns the
 *     authoritative state: the map, the bonus list and every player's position and
 *     score. Launch it standalone with
 *     {@link one.empty3.apps.opad.server.GameServerMain}.</li>
 *
 *     <li><b>Client</b> &mdash; {@link one.empty3.apps.opad.server.GameClient} feeding
 *     {@link one.empty3.apps.opad.server.SyncedBonus} and
 *     {@link one.empty3.apps.opad.server.NetworkPositionUpdate}, rendered by
 *     {@link one.empty3.apps.opad.server.NetworkedJoglDrawer} /
 *     {@link one.empty3.apps.opad.server.NetworkedEcDrawer}. The lobby is
 *     {@link one.empty3.apps.opad.server.PanelGraphicsServer}, the multiplayer
 *     counterpart of {@code PanelGraphics}.</li>
 * </ul>
 *
 * <p>In this first version players do not interact with each other: they share the
 * map and the bonus list, they see each other move, but they cannot collide, shoot
 * or block one another. The server decides who takes a bonus first, removes it for
 * everybody, and ends the game when no bonus is left.</p>
 *
 * <p>The protocol is unauthenticated and unencrypted: run the server on a trusted
 * network only.</p>
 *
 * @author Manuel Dahmen dathewolf@gmail.com
 */
package one.empty3.apps.opad.server;
