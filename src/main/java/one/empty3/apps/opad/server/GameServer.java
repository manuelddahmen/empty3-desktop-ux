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

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

/*__
 * The Opad game server: one thread accepting connections, one thread per connected
 * player, and one thread broadcasting the world state at a fixed rate.
 *
 * <p>Positions are soft state, pushed on every tick and never acknowledged: a lost
 * update is simply superseded by the next one. Bonuses are hard state, pushed once
 * as an event when the owning {@link ServerGameSession} awards them, so a client
 * never has to diff a bonus list.</p>
 *
 * <p>Embed it with {@link #start()} then {@link #stop()}, or run it standalone with
 * {@link GameServerMain}.</p>
 *
 * @author Manuel Dahmen dathewolf@gmail.com
 */
public class GameServer implements Closeable {

    private static final Logger LOGGER = Logger.getLogger(GameServer.class.getName());

    private final int requestedPort;
    private final int tickMillis;
    private final ServerGameSession session;
    private final List<ClientHandler> clients = new CopyOnWriteArrayList<>();
    private final CountDownLatch terminated = new CountDownLatch(1);

    private ServerSocket serverSocket;
    private volatile boolean running;

    public GameServer(int port, ServerGameSession session) {
        this(port, session, Protocol.DEFAULT_TICK_MILLIS);
    }

    /*__
     * @param port       TCP port to listen on, {@code 0} to let the system choose
     * @param session    the authoritative state this server serves
     * @param tickMillis period of the {@link Protocol#STATE} broadcast
     */
    public GameServer(int port, ServerGameSession session, int tickMillis) {
        this.requestedPort = port;
        this.session = session;
        this.tickMillis = Math.max(10, tickMillis);
    }

    public ServerGameSession getSession() {
        return session;
    }

    public int getTickMillis() {
        return tickMillis;
    }

    /*__ @return the port actually bound, valid once {@link #start()} returned */
    public int getPort() {
        ServerSocket socket = serverSocket;
        return socket != null ? socket.getLocalPort() : requestedPort;
    }

    public boolean isRunning() {
        return running;
    }

    public int getClientCount() {
        return clients.size();
    }

    /*__
     * Binds the port, then starts the accept and tick threads.
     *
     * <p>Binding happens on the calling thread so that a port already in use is
     * reported to the caller instead of being logged from a background thread.</p>
     *
     * @throws IOException if the port cannot be bound
     */
    public synchronized void start() throws IOException {
        if (running) {
            return;
        }
        serverSocket = new ServerSocket(requestedPort);
        running = true;

        Thread acceptThread = new Thread(this::acceptLoop, "OpadGameServer-accept");
        acceptThread.setDaemon(true);
        acceptThread.start();

        Thread tickThread = new Thread(this::tickLoop, "OpadGameServer-tick");
        tickThread.setDaemon(true);
        tickThread.start();

        LOGGER.log(Level.INFO, "Opad server listening on port {0}, map {1}, {2} bonuses",
                new Object[]{getPort(), session.getMapName(), session.getBonusCount()});
    }

    private void acceptLoop() {
        while (running) {
            try {
                Socket socket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(socket, this, session);
                clients.add(handler);
                Thread thread = new Thread(handler, "OpadClient-" + handler.getRemoteAddress());
                thread.setDaemon(true);
                thread.start();
            } catch (IOException ex) {
                if (running) {
                    LOGGER.log(Level.WARNING, "Accept failed", ex);
                }
            }
        }
        terminated.countDown();
    }

    private void tickLoop() {
        while (running) {
            try {
                Thread.sleep(tickMillis);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                return;
            }
            if (!clients.isEmpty()) {
                broadcast(NetMessage.state(session.playerSnapshot(), session.isGameOver()));
            }
        }
    }

    /*__ Sends a message to every connected client. */
    void broadcast(NetMessage message) {
        for (ClientHandler client : clients) {
            client.send(message);
        }
    }

    /*__
     * Removes a client, tells the others, and rearms the map once the last player of
     * a finished game is gone.
     */
    void disconnect(ClientHandler handler) {
        if (!clients.remove(handler)) {
            return;
        }
        handler.close();
        if (handler.getPlayerId() > 0) {
            session.leave(handler.getPlayerId());
        }
        if (session.isEmpty()) {
            if (session.isGameOver()) {
                LOGGER.log(Level.INFO, "Last player left a finished game, generating a new map");
                session.restart();
            }
            return;
        }
        broadcast(NetMessage.state(session.playerSnapshot(), session.isGameOver()));
    }

    /*__ Blocks until the server stops. Used by {@link GameServerMain}. */
    public void awaitTermination() throws InterruptedException {
        terminated.await();
    }

    public synchronized void stop() {
        if (!running) {
            return;
        }
        running = false;
        for (ClientHandler client : clients) {
            client.close();
        }
        clients.clear();
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException ex) {
            LOGGER.log(Level.FINE, "Closing server socket", ex);
        }
        terminated.countDown();
        LOGGER.log(Level.INFO, "Opad server stopped");
    }

    @Override
    public void close() {
        stop();
    }
}
