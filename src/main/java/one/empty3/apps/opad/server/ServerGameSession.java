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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/*__
 * The authoritative state of one multiplayer game: the map, the bonuses and the
 * players.
 *
 * <p>Every method that reads or writes that state is {@code synchronized} on the
 * session, which is what makes a bonus impossible to take twice: two clients
 * claiming the same bonus in the same millisecond are serialised here, and only the
 * first one gets it.</p>
 *
 * <p>The class is deliberately free of any dependency on the rendering side: it
 * works in map parameter space only, so it can run headless.</p>
 *
 * @author Manuel Dahmen dathewolf@gmail.com
 */
public class ServerGameSession {

    /*__
     * How far, in map parameter space, a player may be from a bonus and still be
     * believed. The server has no {@code Terrain} to measure real distances with, so
     * this is a sanity check against a client claiming a bonus from the other end of
     * the map, not an exact collision test.
     */
    public static final double DEFAULT_PICK_TOLERANCE = 0.05;

    /*__ Ship colours handed out in turn to players that do not ask for one. */
    private static final int[] PALETTE = {
            0xFFD700, 0x00E5FF, 0xFF4081, 0x76FF03,
            0xFF6D00, 0xB388FF, 0x00BFA5, 0xFFFFFF};

    private static final Logger LOGGER = Logger.getLogger(ServerGameSession.class.getName());

    private final String mapName;
    private final double pickTolerance;
    private final Map<String, BonusState> bonuses = new LinkedHashMap<>();
    private final Map<Integer, PlayerState> players = new LinkedHashMap<>();

    private long seed;
    private int nextPlayerId = 1;
    private int remaining;
    private boolean gameOver;
    private boolean gameOverAnnounced;

    /*__
     * @param mapName one of {@link MapCatalog#MAPS}
     * @param seed    seed of the bonus layout; the same seed gives the same map
     */
    public ServerGameSession(String mapName, long seed) {
        this(mapName, seed, DEFAULT_PICK_TOLERANCE);
    }

    public ServerGameSession(String mapName, long seed, double pickTolerance) {
        if (!MapCatalog.isKnown(mapName)) {
            throw new IllegalArgumentException("Unknown map: " + mapName);
        }
        this.mapName = mapName;
        this.seed = seed;
        this.pickTolerance = pickTolerance;
        generateBonuses();
    }

    public String getMapName() {
        return mapName;
    }

    public synchronized long getSeed() {
        return seed;
    }

    /*__
     * Lays out the bonuses of every {@link BonusKind}, in the counts and with the
     * point values of {@code Bundle.properties}, at random positions of the unit
     * square. Mirrors what {@code Bonus} does locally in the single player game.
     */
    private synchronized void generateBonuses() {
        bonuses.clear();
        Random random = new Random(seed);
        double radius = BonusKind.scale();
        int index = 0;
        for (BonusKind kind : BonusKind.values()) {
            int count = kind.count();
            for (int i = 0; i < count; i++) {
                String id = "b" + index++;
                bonuses.put(id, new BonusState(id, kind,
                        random.nextDouble(), random.nextDouble(), BonusKind.HEIGHT, radius));
            }
        }
        remaining = bonuses.size();
        gameOver = remaining == 0;
        gameOverAnnounced = false;
        LOGGER.log(Level.INFO, "Map {0} generated with {1} bonuses (seed {2})",
                new Object[]{mapName, remaining, seed});
    }

    /*__
     * Starts a fresh game on the same map with a new bonus layout, and resets every
     * score. Called when the last player leaves, so that a long lived server does
     * not hand an already finished game to the next arrivals.
     */
    public synchronized void restart() {
        seed = seed * 6364136223846793005L + 1442695040888963407L;
        generateBonuses();
        for (PlayerState player : players.values()) {
            player.score = 0;
        }
    }

    /*__
     * Registers a new player.
     *
     * @param name     display name; a default is used when blank
     * @param colorRgb requested ship colour, or {@code 0} to be given one
     * @return the freshly created state, already part of the session
     */
    public synchronized PlayerState join(String name, int colorRgb) {
        int id = nextPlayerId++;
        String displayName = name == null || name.isBlank() ? "Player " + id : name.trim();
        int rgb = colorRgb != 0 ? colorRgb & 0xFFFFFF : PALETTE[(id - 1) % PALETTE.length];
        PlayerState player = new PlayerState(id, displayName, rgb, BonusKind.HEIGHT);
        players.put(id, player);
        LOGGER.log(Level.INFO, "Player {0} joined as #{1}", new Object[]{displayName, id});
        return player;
    }

    /*__ Forgets a player. Their bonuses stay taken, their score disappears. */
    public synchronized void leave(int playerId) {
        PlayerState removed = players.remove(playerId);
        if (removed != null) {
            LOGGER.log(Level.INFO, "Player {0} (#{1}) left", new Object[]{removed.name, playerId});
        }
    }

    public synchronized boolean isEmpty() {
        return players.isEmpty();
    }

    public synchronized void updatePosition(int playerId, double x, double y, double z, double angleZ) {
        PlayerState player = players.get(playerId);
        if (player == null) {
            return;
        }
        player.x = x;
        player.y = y;
        player.z = z;
        player.angleZ = angleZ;
    }

    /*__
     * Arbitrates a claim on a bonus.
     *
     * <p>The claim carries the position it was made from, which is applied to the
     * player before the distance check: a {@link Protocol#MOVE} still travelling
     * must not cost a player the bonus they are standing on.</p>
     *
     * @return the outcome, never {@code null}; a rejection explains itself in
     * {@link PickResult#reason()}
     */
    public synchronized PickResult pick(int playerId, String bonusId, double x, double y, double z) {
        PlayerState player = players.get(playerId);
        if (player == null) {
            return PickResult.rejected("unknown player #" + playerId);
        }
        player.x = x;
        player.y = y;
        player.z = z;

        BonusState bonus = bonuses.get(bonusId);
        if (bonus == null) {
            return PickResult.rejected("unknown bonus " + bonusId);
        }
        if (bonus.taken) {
            return PickResult.rejected("bonus " + bonusId + " already taken by #" + bonus.takenBy);
        }
        double distance = Math.hypot(player.x - bonus.x, player.y - bonus.y);
        if (distance > pickTolerance) {
            return PickResult.rejected("player #" + playerId + " is " + distance
                    + " away from bonus " + bonusId + " (tolerance " + pickTolerance + ")");
        }

        bonus.taken = true;
        bonus.takenBy = playerId;
        player.score += bonus.value;
        remaining--;
        if (remaining <= 0) {
            gameOver = true;
        }
        return new PickResult(true, bonus.id, playerId, bonus.value, gameOver, null);
    }

    /*__ {@code true} once every bonus has been taken. */
    public synchronized boolean isGameOver() {
        return gameOver;
    }

    /*__
     * @return {@code true} the first time it is called after the game ended, so that
     * the {@link Protocol#GAME_OVER} broadcast happens exactly once
     */
    public synchronized boolean claimGameOverAnnouncement() {
        if (gameOver && !gameOverAnnounced) {
            gameOverAnnounced = true;
            return true;
        }
        return false;
    }

    public synchronized int getRemainingBonusCount() {
        return remaining;
    }

    public synchronized int getBonusCount() {
        return bonuses.size();
    }

    /*__
     * @return a copy of the whole bonus list, taken ones included, for a joining
     * client
     */
    public synchronized List<BonusState> bonusSnapshot() {
        List<BonusState> snapshot = new ArrayList<>(bonuses.size());
        for (BonusState bonus : bonuses.values()) {
            snapshot.add(bonus.copy());
        }
        return snapshot;
    }

    /*__ @return a copy of every player, for a broadcast or a score board */
    public synchronized List<PlayerState> playerSnapshot() {
        List<PlayerState> snapshot = new ArrayList<>(players.size());
        for (PlayerState player : players.values()) {
            snapshot.add(player.copy());
        }
        return snapshot;
    }

    /*__
     * Outcome of a {@link ServerGameSession#pick} arbitration.
     *
     * @param accepted whether the bonus was awarded
     * @param bonusId  the bonus that was claimed
     * @param playerId the claiming player
     * @param points   points awarded, meaningful when {@code accepted}
     * @param gameOver whether that pick emptied the map
     * @param reason   why the claim was refused, {@code null} when accepted
     */
    public record PickResult(boolean accepted, String bonusId, int playerId,
                             double points, boolean gameOver, String reason) {

        static PickResult rejected(String reason) {
            return new PickResult(false, null, 0, 0, false, reason);
        }
    }

    /*__
     * @return the shared {@code Bundle} the bonus layout is read from, exposed so
     * that a caller can log or display the map settings
     */
    public static ResourceBundle bundle() {
        return ResourceBundle.getBundle("one.empty3.apps.opad.Bundle");
    }
}
