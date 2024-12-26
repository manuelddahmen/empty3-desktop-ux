/*
 *
 *  * Copyright (c) 2024. Manuel Daniel Dahmen
 *  *
 *  *
 *  *    Copyright 2024 Manuel Daniel Dahmen
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 *
 *
 */

package one.empty3.apps.opad;

import one.empty3.library.Point3D;
import one.empty3.library.core.physics.Level1;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

/*__
 * Created by manuel on 19-05-17.
 */
public class Game {


    private static ArrayList<Player> localPlayers = new ArrayList<>();
    protected String gameName;
    //protected ListSearch<one.empty3.apps.opad.Gamer> players;
    private Player activePlayers;
    private Player currentPlayer;

    public Game() {
    }


    public static ArrayList getLocalPlayers() throws IOException {

        localPlayers = new ArrayList<>();

        ArrayList<Properties> properties = new ArrayList<>();

        String pathname = "gameData/players";
        String[] list = new File(pathname).list();
        assert list != null;
        if (list != null) {
            for (String file : list) {
                properties.add(new Properties());
                Properties propertiesPlayer = properties.get(properties.size() - 1);
                propertiesPlayer.load(new FileReader(pathname + "/" + file));
                Player player = new Player(propertiesPlayer);
                localPlayers.add(player);
            }

        }
        // TODO Load player from files in dir gameData


        return localPlayers;
    }


    public Point3D forceMotrice() {

        return Point3D.O0.mult(getLocalPlayer().score());
    }

    public Player getLocalPlayer() {
        return currentPlayer;
    }

    Level1 getLevel() {
        // TODO
        return null;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}
