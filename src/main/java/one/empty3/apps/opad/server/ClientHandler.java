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
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/*__
 * One connected player, seen from the server: a reader loop on its own thread and a
 * synchronised writer the tick thread and the other handlers also write to.
 *
 * @author Manuel Dahmen dathewolf@gmail.com
 */
public class ClientHandler implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(ClientHandler.class.getName());

    private final AtomicLong incomingMessages = new AtomicLong(0);
    private final AtomicLong outgoingMessages = new AtomicLong(0);
    private final ScheduledExecutorService reporter = Executors.newSingleThreadScheduledExecutor();

    private final Socket socket;
    private final GameServer server;
    private final ServerGameSession session;
    private final BufferedReader in;
    private final PrintWriter out;
    private final Object writeLock = new Object();

    private volatile int playerId = -1;
    private volatile boolean closed;

    ClientHandler(Socket socket, GameServer server, ServerGameSession session) throws IOException {
        this.socket = socket;
        this.server = server;
        this.session = session;
        socket.setTcpNoDelay(true);
        socket.setSoTimeout(Protocol.SOCKET_TIMEOUT_MILLIS);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), false);
        
        reporter.scheduleAtFixedRate(() -> {
            LOGGER.log(Level.INFO, "Client {0} stats: In={1}, Out={2}", 
                new Object[]{getRemoteAddress(), incomingMessages.getAndSet(0), outgoingMessages.getAndSet(0)});
        }, 10, 10, TimeUnit.SECONDS);
    }

    /*__ @return the id assigned on join, or {@code -1} while the join is pending */
    public int getPlayerId() {
        return playerId;
    }

    public String getRemoteAddress() {
        return String.valueOf(socket.getRemoteSocketAddress());
    }

    @Override
    public void run() {
        try {
            String line;
            while (!closed && (line = in.readLine()) != null) {
                incomingMessages.incrementAndGet();
                NetMessage message = Protocol.decode(line);
                if (message == null) {
                    send(NetMessage.error("Malformed message"));
                    continue;
                }
                if (!handle(message)) {
                    break;
                }
            }
        } catch (SocketTimeoutException ex) {
            LOGGER.log(Level.INFO, "Client {0} timed out", getRemoteAddress());
        } catch (IOException ex) {
            if (!closed) {
                LOGGER.log(Level.FINE, "Client " + getRemoteAddress() + " dropped", ex);
            }
        } finally {
            server.disconnect(this);
        }
    }

    /*__
     * @return {@code false} to end the connection
     */
    private boolean handle(NetMessage message) {
        switch (message.type) {
            case Protocol.JOIN -> {
                return handleJoin(message);
            }
            case Protocol.MOVE -> {
                if (playerId > 0) {
                    session.updatePosition(playerId, message.x, message.y, 0.0, message.angleZ);
                }
                return true;
            }
            case Protocol.PICK -> {
                handlePick(message);
                return true;
            }
            case Protocol.LEAVE -> {
                return false;
            }
            default -> {
                send(NetMessage.error("Unsupported message type: " + message.type));
                return true;
            }
        }
    }

    private boolean handleJoin(NetMessage message) {
        if (playerId > 0) {
            send(NetMessage.error("Already joined as #" + playerId));
            return true;
        }
        if (message.protocolVersion != Protocol.VERSION) {
            send(NetMessage.error("Protocol version mismatch: server speaks "
                    + Protocol.VERSION + ", client speaks " + message.protocolVersion));
            return false;
        }

        PlayerState me = session.join(message.playerName, message.colorRgb);
        playerId = me.id;

        send(NetMessage.welcome(me, session.getMapName(), server.getTickMillis(),
                session.bonusSnapshot(), session.playerSnapshot(), session.isGameOver()));

        // Lets everybody see the newcomer without waiting for the next tick.
        server.broadcast(NetMessage.state(session.playerSnapshot(), session.isGameOver()));
        if (session.isGameOver()) {
            send(NetMessage.gameOver(session.playerSnapshot()));
        }
        return true;
    }

    private void handlePick(NetMessage message) {
        if (playerId <= 0) {
            send(NetMessage.error("Join before picking"));
            return;
        }
        ServerGameSession.PickResult result =
                session.pick(playerId, message.bonusId, message.x, message.y, message.z);
        if (!result.accepted()) {
            LOGGER.log(Level.FINE, "Pick refused: {0}", result.reason());
            return;
        }

        NetMessage bonusTaken = NetMessage.bonusTaken(result.bonusId(), result.playerId(), result.points());
        LOGGER.log(Level.INFO, "Broadcasting bonus taken: {0}", bonusTaken);
        server.broadcast(bonusTaken);
        
        if (result.gameOver() && session.claimGameOverAnnouncement()) {
            LOGGER.log(Level.INFO, "Game over, {0} bonuses taken", session.getBonusCount());
            server.broadcast(NetMessage.gameOver(session.playerSnapshot()));
        }
    }

    /*__
     * Writes one message. Callers come from several threads, hence the lock; a failed
     * write closes the connection rather than being retried.
     */
    void send(NetMessage message) {
        if (closed) {
            return;
        }
        outgoingMessages.incrementAndGet();
        String line = Protocol.encode(message);
        synchronized (writeLock) {
            out.print(line);
            out.print('\n');
            out.flush();
        }
        if (out.checkError()) {
            LOGGER.log(Level.FINE, "Write failed to {0}, closing", getRemoteAddress());
            server.disconnect(this);
        }
    }

    /*__ Closes the socket; the reader loop ends on the resulting read failure. */
    void close() {
        closed = true;
        reporter.shutdownNow();
        try {
            socket.close();
        } catch (IOException ex) {
            LOGGER.log(Level.FINEST, "Closing socket", ex);
        }
    }
}
