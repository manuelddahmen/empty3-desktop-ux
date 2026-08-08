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

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/*__
 * The game's connection to a {@link GameServer}.
 *
 * <p>{@link #connect(long)} blocks until the server has answered the join, because
 * the caller needs the map name and the bonus list before it can build a scene. Once
 * connected a single reader thread keeps the local mirror up to date; everything the
 * game reads from here ({@link #remotePlayers()}, {@link #score()},
 * {@link #isGameOver()}) is safe to read from the OpenGL or Swing thread.</p>
 *
 * <p>Nothing is predicted locally: a bonus disappears when the server says so, not
 * when the local collision test fires. That is what keeps two players from both
 * scoring the same bonus.</p>
 *
 * @author Manuel Dahmen dathewolf@gmail.com
 */
public class GameClient implements Closeable {

    private static final Logger LOGGER = Logger.getLogger(GameClient.class.getName());

    private final String host;
    private final int port;
    private final String playerName;
    private final int requestedColorRgb;
    private final String requestedMapName;

    private final Map<Integer, PlayerState> players = new ConcurrentHashMap<>();
    private final List<GameClientListener> listeners = new CopyOnWriteArrayList<>();
    private final CountDownLatch welcomed = new CountDownLatch(1);

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private final Object writeLock = new Object();

    private volatile int playerId = -1;
    private volatile String mapName;
    private volatile int tickMillis = Protocol.DEFAULT_TICK_MILLIS;
    private volatile List<BonusState> initialBonuses = List.of();
    private volatile boolean gameOver;
    private volatile List<PlayerState> finalScores = List.of();
    private volatile boolean running;
    private volatile String joinError;

    /*__
     * @param playerName        name shown to the other players
     * @param requestedColorRgb wanted ship colour {@code 0xRRGGBB}, {@code 0} to let
     *                          the server pick a distinct one
     */
    public GameClient(String host, int port, String playerName, int requestedColorRgb) {
        this(host, port, playerName, requestedColorRgb, MapCatalog.defaultMap());
    }

    /*__
     * @param playerName        name shown to the other players
     * @param requestedColorRgb wanted ship colour {@code 0xRRGGBB}, {@code 0} to let
     *                          the server pick a distinct one
     * @param mapName           map to join or create (one of {@link MapCatalog#MAPS});
     *                          blank or unknown falls back to {@link MapCatalog#defaultMap()}
     */
    public GameClient(String host, int port, String playerName, int requestedColorRgb,
                      String mapName) {
        this.host = host;
        this.port = port;
        this.playerName = playerName;
        this.requestedColorRgb = requestedColorRgb;
        this.requestedMapName = MapCatalog.isKnown(mapName) ? mapName : MapCatalog.defaultMap();
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getPlayerName() {
        return playerName;
    }

    /*__ @return this client's server-assigned id, {@code -1} before the welcome */
    public int getPlayerId() {
        return playerId;
    }

    /*__ @return the map name requested at construction time */
    public String getRequestedMapName() {
        return requestedMapName;
    }

    /*__ @return the map confirmed by the server welcome, or {@code null} before join */
    public String getMapName() {
        return mapName;
    }

    public int getTickMillis() {
        return tickMillis;
    }

    /*__ @return the bonus list as it was on join, taken ones included */
    public List<BonusState> getInitialBonuses() {
        return initialBonuses;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    /*__ @return the final score board, empty until the game is over */
    public List<PlayerState> getFinalScores() {
        return finalScores;
    }

    public void addListener(GameClientListener listener) {
        listeners.add(listener);
    }

    public void removeListener(GameClientListener listener) {
        listeners.remove(listener);
    }

    /*__ @return this client's score as the server computed it */
    public double score() {
        PlayerState me = players.get(playerId);
        return me != null ? me.score : 0;
    }

    /*__ @return every player, this one included */
    public Collection<PlayerState> allPlayers() {
        return Collections.unmodifiableCollection(players.values());
    }

    /*__
     * @return the other players, for rendering; a fresh list, so the caller can
     * iterate it while the reader thread keeps updating the mirror
     */
    public List<PlayerState> remotePlayers() {
        List<PlayerState> others = new ArrayList<>(Math.max(0, players.size() - 1));
        int me = playerId;
        for (PlayerState player : players.values()) {
            if (player.id != me) {
                others.add(player);
            }
        }
        return others;
    }

    /*__
     * @return the score board, best first
     */
    public List<PlayerState> scoreBoard() {
        List<PlayerState> board = gameOver && !finalScores.isEmpty()
                ? new ArrayList<>(finalScores)
                : new ArrayList<>(players.values());
        board.sort((a, b) -> Double.compare(b.score, a.score));
        return board;
    }

    /*__
     * Opens the connection, sends the join and waits for the server's welcome.
     *
     * @param timeoutMillis how long to wait for the welcome
     * @throws IOException if the socket cannot be opened, the server refuses the
     *                     join, or no welcome arrives in time
     */
    public void connect(long timeoutMillis) throws IOException {
        socket = new Socket();
        socket.connect(new InetSocketAddress(host, port), (int) Math.max(1, timeoutMillis));
        socket.setTcpNoDelay(true);
        socket.setSoTimeout(Protocol.SOCKET_TIMEOUT_MILLIS);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), false);
        running = true;

        Thread reader = new Thread(this::readLoop, "OpadGameClient-reader");
        reader.setDaemon(true);
        reader.start();

        send(NetMessage.join(playerName, requestedColorRgb, requestedMapName));

        boolean welcome;
        try {
            welcome = welcomed.await(timeoutMillis, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            close();
            throw new IOException("Interrupted while joining " + host + ":" + port, ex);
        }
        if (!welcome) {
            String reason = joinError != null ? joinError : "no answer from " + host + ":" + port;
            close();
            throw new IOException("Cannot join the game: " + reason);
        }

        if (joinError != null) {
            String reason = joinError;
            close();
            throw new IOException("Cannot join the game: " + reason);
        }

        if (playerId <= 0) {
            close();
            throw new IOException("Cannot join the game: server did not assign a player id");
        }

        if (!MapCatalog.isKnown(mapName)) {
            String receivedMapName = mapName;
            close();
            throw new IOException("Cannot join the game: server sent unknown map: " + receivedMapName);
        }
    }



    private void readLoop() {
        String reason = "connection closed";
        try {
            String line;
            while (running && (line = in.readLine()) != null) {
                NetMessage message = Protocol.decode(line);
                if (message != null) {
                    dispatch(message);
                }
            }
        } catch (SocketTimeoutException ex) {
            reason = "server stopped answering";
        } catch (IOException ex) {
            reason = ex.getMessage() != null ? ex.getMessage() : ex.toString();
            LOGGER.log(Level.FINE, "Reader loop ended", ex);
        } finally {
            boolean wasRunning = running;
            running = false;
            // Unblocks connect(..) when the server hangs up before welcoming us.
            joinError = reason;
            welcomed.countDown();
            if (wasRunning) {
                for (GameClientListener listener : listeners) {
                    listener.onDisconnected(reason);
                }
            }
        }
    }

    private void dispatch(NetMessage message) {
        switch (message.type) {
            case Protocol.WELCOME -> {
                playerId = message.playerId;
                mapName = message.mapName;
                tickMillis = message.tickMillis > 0 ? message.tickMillis : Protocol.DEFAULT_TICK_MILLIS;
                initialBonuses = message.bonuses != null ? List.copyOf(message.bonuses) : List.of();
                replacePlayers(message.players);
                gameOver = message.gameOver;
                welcomed.countDown();
                for (GameClientListener listener : listeners) {
                    listener.onWelcome(this);
                }
            }
            case Protocol.STATE -> {
                replacePlayers(message.players);
                gameOver = message.gameOver;
                for (GameClientListener listener : listeners) {
                    listener.onState(this);
                }
            }
            case Protocol.BONUS_TAKEN -> {
                for (GameClientListener listener : listeners) {
                    listener.onBonusTaken(message.bonusId, message.playerId, message.points);
                }
            }
            case Protocol.GAME_OVER -> {
                gameOver = true;
                if (message.players != null) {
                    finalScores = List.copyOf(message.players);
                    replacePlayers(message.players);
                }
                for (GameClientListener listener : listeners) {
                    listener.onGameOver(finalScores);
                }
            }
            case Protocol.ERROR -> {
                joinError = message.message;
                LOGGER.log(Level.WARNING, "Server error: {0}", message.message);
                for (GameClientListener listener : listeners) {
                    listener.onServerError(message.message);
                }
            }
            default -> LOGGER.log(Level.FINE, "Ignoring message type {0}", message.type);
        }
    }

    private void replacePlayers(List<PlayerState> snapshot) {
        if (snapshot == null) {
            return;
        }
        for (PlayerState player : snapshot) {
            players.put(player.id, player);
        }
        // Drops the ones the server no longer reports, so a leaver stops being drawn.
        players.keySet().removeIf(id -> {
            for (PlayerState player : snapshot) {
                if (player.id == id) {
                    return false;
                }
            }
            return true;
        });
    }

    /*__ Tells the server where this player is. Cheap, meant to be called on a timer. */
    public void sendPosition(double x, double y, double z, double angleZ) {
        send(NetMessage.move(x, y, z, angleZ));
    }

    /*__
     * Claims a bonus. Nothing is removed locally: the server answers with
     * {@link Protocol#BONUS_TAKEN} if the claim wins.
     */
    public void sendPick(String bonusId, double x, double y, double z) {
        send(NetMessage.pick(playerId, bonusId, x, y, z));
    }

    private void send(NetMessage message) {
        if (!running || out == null) {
            return;
        }
        String line = Protocol.encode(message);
        synchronized (writeLock) {
            out.print(line);
            out.print('\n');
            out.flush();
        }
        if (out.checkError()) {
            LOGGER.log(Level.FINE, "Write to server failed");
            running = false;
        }
    }

    @Override
    public void close() {
        if (running) {
            send(NetMessage.leave());
        }
        running = false;
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ex) {
            LOGGER.log(Level.FINEST, "Closing client socket", ex);
        }
    }
}
