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

import java.io.IOException;
import java.util.Arrays;

/*__
 * Runs a headless Opad game server.
 *
 * <pre>
 * java one.empty3.apps.opad.server.GameServerMain \
 *      [--port 4712] [--map SolPlan] [--seed 42] [--tick 50] [--tolerance 0.05]
 * </pre>
 *
 * <p>No graphics context is created, so this runs on a machine without a display.</p>
 *
 * @author Manuel Dahmen dathewolf@gmail.com
 */
public final class GameServerMain {

    private GameServerMain() {
    }

    public static void main(String[] args) {
        int port = Protocol.DEFAULT_PORT;
        String map = MapCatalog.defaultMap();
        long seed = 20260730L;
        int tick = Protocol.DEFAULT_TICK_MILLIS;
        double tolerance = ServerGameSession.DEFAULT_PICK_TOLERANCE;

        try {
            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "--port" -> port = Integer.parseInt(args[++i]);
                    case "--map" -> map = args[++i];
                    case "--seed" -> seed = Long.parseLong(args[++i]);
                    case "--tick" -> tick = Integer.parseInt(args[++i]);
                    case "--tolerance" -> tolerance = Double.parseDouble(args[++i]);
                    case "--help", "-h" -> {
                        usage();
                        return;
                    }
                    default -> {
                        System.err.println("Unknown argument: " + args[i]);
                        usage();
                        System.exit(2);
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException ex) {
            System.err.println("Bad arguments: " + Arrays.toString(args));
            usage();
            System.exit(2);
            return;
        }

        if (!MapCatalog.isKnown(map)) {
            System.err.println("Unknown map '" + map + "'. Known maps: "
                    + String.join(", ", MapCatalog.MAPS));
            System.exit(2);
            return;
        }

        ServerGameSession session = new ServerGameSession(map, seed, tolerance);
        GameServer server = new GameServer(port, session, tick);
        Runtime.getRuntime().addShutdownHook(new Thread(server::stop, "OpadGameServer-shutdown"));

        try {
            server.start();
        } catch (IOException ex) {
            System.err.println("Cannot listen on port " + port + ": " + ex.getMessage());
            System.exit(1);
            return;
        }

        System.out.println("Opad server ready on port " + server.getPort()
                + " - map " + map + " - " + session.getBonusCount() + " bonuses");
        System.out.println("Press Ctrl+C to stop.");

        try {
            server.awaitTermination();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            server.stop();
        }
    }

    private static void usage() {
        System.out.println("""
                Usage: GameServerMain [options]
                  --port <n>        TCP port to listen on (default 4712, 0 for any free port)
                  --map <name>      map every client plays on (default SolPlan)
                  --seed <n>        seed of the bonus layout (default 20260730)
                  --tick <ms>       state broadcast period (default 50)
                  --tolerance <d>   how far a player may claim a bonus from, in map
                                    parameter space (default 0.05)
                  --help            this message
                """);
        System.out.println("Maps: " + String.join(", ", MapCatalog.MAPS));
    }
}
