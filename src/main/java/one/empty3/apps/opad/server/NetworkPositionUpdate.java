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
import one.empty3.apps.opad.Player;
import one.empty3.apps.opad.PositionMobile;
import one.empty3.apps.opad.PositionUpdateImpl;
import one.empty3.apps.opad.Sounds;
import one.empty3.apps.opad.TRISphere2;
import one.empty3.apps.opad.Terrain;
import one.empty3.library.Point3D;
import one.empty3.library.Representable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*__
 * The single player game logic, with bonus taking and scoring delegated to the
 * server.
 *
 * <p>Movement stays entirely local: the keyboard drives {@code acc}, {@code dec} and
 * the rotations exactly as before, and the resulting position is pushed to the server
 * a few times a second. Only three things change:</p>
 *
 * <ul>
 *     <li>a local collision no longer removes the bonus, it sends a claim and waits
 *     for {@link Protocol#BONUS_TAKEN}, so two players cannot both score it;</li>
 *     <li>{@link #score()} reports what the server computed;</li>
 *     <li>{@link #estGagnant()} follows the server's end of game.</li>
 * </ul>
 *
 * @author Manuel Dahmen dathewolf@gmail.com
 */
public class NetworkPositionUpdate extends PositionUpdateImpl implements GameClientListener {

    /*__ How long a claim is remembered before being sent again, in milliseconds. */
    private static final long PICK_RETRY_MILLIS = 1_000L;

    /*__ Period of the position updates sent to the server, in milliseconds. */
    private static final long POSITION_PERIOD_MILLIS = 50L;

    /*__ Period of the collision test, matching the single player loop. */
    private static final long COLLISION_PERIOD_MILLIS = 20L;

    private final GameClient client;
    private final SyncedBonus syncedBonus;

    /*__ Bonus id to the time its claim was sent, to avoid flooding the server. */
    private final Map<String, Long> pendingPicks = new ConcurrentHashMap<>();

    private volatile boolean gameOver;

    /*__
     * @param terrain the ground, built from the map the server chose
     * @param player  the local player, for its name and colour
     * @param client  a connected client
     * @param bonus   the container built from {@link GameClient#getInitialBonuses()}
     */
    public NetworkPositionUpdate(Terrain terrain, Player player, GameClient client, SyncedBonus bonus) {
        super(terrain, player);
        this.client = client;
        this.syncedBonus = bonus;
        this.gameOver = client.isGameOver();
        // Replaces the random local bonuses the super constructor just built.
        ennemi(bonus);
        client.addListener(this);
    }

    public GameClient getClient() {
        return client;
    }

    public SyncedBonus getSyncedBonus() {
        return syncedBonus;
    }

    /*__
     * Looks for a bonus within reach and claims it.
     *
     * <p>Unlike the single player version this removes nothing and awards nothing: the
     * bonus stays visible and unscored until the server confirms. A claim is repeated
     * at most once per {@value #PICK_RETRY_MILLIS} ms, which covers a dropped message
     * without turning a player parked on a bonus into a flood.</p>
     */
    @Override
    public synchronized void testCollision(PositionMobile positionMobile) {
        if (gameOver || !client.isRunning()) {
            return;
        }
        Terrain terrain = getTerrain();
        if (terrain == null) {
            return;
        }

        Point3D sol = positionMobile.getPositionSol();
        Point3D position = terrain.p3(sol);
        long now = System.currentTimeMillis();

        for (Representable representable : syncedBonus.snapshot()) {
            if (!(representable instanceof TRISphere2<?> sphere)) {
                continue;
            }
            double radius = sphere.getCircle().getRadius();
            // Computed from the parameter coordinates rather than read from the sphere,
            // whose world centre is only refreshed by the drawing thread.
            Point3D center = terrain.p3(sphere.getCoords());
            if (Point3D.distance(center, position) >= radius) {
                continue;
            }

            String bonusId = syncedBonus.idOf(representable);
            if (bonusId == null) {
                continue;
            }
            Long sentAt = pendingPicks.get(bonusId);
            if (sentAt != null && now - sentAt < PICK_RETRY_MILLIS) {
                continue;
            }
            pendingPicks.put(bonusId, now);
            client.sendPick(bonusId, sol.getX(), sol.getY(), sol.getZ());
        }
    }

    /*__
     * Tests collisions and publishes this player's position, until the connection
     * ends. Replaces the collision-only loop of the single player game.
     */
    @Override
    public void run() {
        long lastSent = 0L;
        while (client.isRunning()) {
            testCollision(getPositionMobile());

            long now = System.currentTimeMillis();
            if (now - lastSent >= POSITION_PERIOD_MILLIS) {
                Point3D sol = getPositionMobile().getPositionSol();
                client.sendPosition(sol.getX(), sol.getY(), sol.getZ(),
                        getPositionMobile().getAngleVisee().getZ());
                lastSent = now;
            }

            try {
                Thread.sleep(COLLISION_PERIOD_MILLIS);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    /*__ @return the score the server keeps for this player */
    @Override
    public int score() {
        return (int) Math.round(client.score());
    }

    /*__ @return {@code true} once the server reports that no bonus is left */
    @Override
    public boolean estGagnant() {
        return gameOver;
    }

    /*__ @return how many bonuses are still on the shared map */
    public int remainingBonusCount() {
        return syncedBonus.remaining();
    }

    // ---- GameClientListener ----

    @Override
    public void onBonusTaken(String bonusId, int playerId, double points) {
        pendingPicks.remove(bonusId);
        if (syncedBonus.removeById(bonusId) && playerId == client.getPlayerId()) {
            Sounds.playSoundBonusHit();
        }
    }

    @Override
    public void onGameOver(List<PlayerState> finalScores) {
        gameOver = true;
    }

    @Override
    public void onDisconnected(String reason) {
        pendingPicks.clear();
    }

    /*__
     * Guards against the container being swapped for a locally generated one: the
     * networked drawers must hand back the synchronised container.
     */
    @Override
    public void ennemi(Bonus e) {
        if (syncedBonus != null && e != syncedBonus) {
            return;
        }
        super.ennemi(e);
    }
}
