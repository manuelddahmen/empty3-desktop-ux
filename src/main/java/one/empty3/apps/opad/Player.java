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

/*__
 Global license :

 Microsoft Public Licence

 author Manuel Dahmen _manuel.dahmen@gmx.com_

 ***/


package one.empty3.apps.opad;

import one.empty3.library.Point3D;

import one.empty3.library.Point;
import one.empty3.libs.*;

import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Properties;

/*__
 * Meta Description missing
 * @author Manuel Dahmen dathewolf@gmail.com
 */
public class Player {

    private static Iterable<? extends Object> localPlayers;
    private int playerId;
    private int score;
    private Color color;
    private String name;

    public Player(String name, Color color, int score) {
        this.name = name;
        this.color = color;
        this.score = score;
    }

    public Player(Properties propertiesPlayer) {
        String name = propertiesPlayer.getProperty("name");
        String[] color = propertiesPlayer.getProperty("color").split(",");
        score = Integer.parseInt(propertiesPlayer.getProperty("score"));
        playerId = Integer.parseInt(propertiesPlayer.getProperty("playerId"));

        this.name = name;
        this.color = new Color(new java.awt.Color(Float.parseFloat(color[0]),
                Float.parseFloat(color[1]),
                Float.parseFloat(color[2])).getRGB());
    }

    public static Player getByName(String s) throws IOException {
        Player p;
        ArrayList<Player> localPlayers = Game.getLocalPlayers();
        for (Player player : localPlayers) {
            if (player.name.equals(s)) return player;
        }
        return null;
    }

    public Point3D forceMotrice() {

        // TODO
        return null;
    }

    public double score() {
        return score;
    }

    public String getName() {
        return name;
    }

    public void save() {
        localPlayers = new ArrayList<>();

        ArrayList<Properties> properties = new ArrayList<>();

        String pathname = "gameData/players";

        String filename = pathname + "/" + name;

        File file = new File(filename);
        // New or update
        if (!file.exists() && (file.exists() && check())) {
            try {
                PrintWriter printWriter = new PrintWriter(file);
                printWriter.println("color=" + (float) color.getRed() / 255f + ","
                        + (float) color.getGreen() / 255f + ","
                        + (float) color.getBlue() / 255f);
                printWriter.println("name=" + name);
                printWriter.println("playerId=" + playerId);
                printWriter.println("score=" + score);
                printWriter.println("0");

                printWriter.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }


        // TODO Load player from files in dir gameData

    }

    private boolean check() {
        return true;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setName(String name) {
        this.name = name;
    }

}
