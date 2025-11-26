/*
 *
 *  *
 *  *  * Copyright (c) 2025. Manuel Daniel Dahmen
 *  *  *
 *  *  *
 *  *  *    Copyright 2024 Manuel Daniel Dahmen
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

package one.empty3.apps.opad;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*__
 * Created by manuel on 19-05-17.
 */
public class Server {
    //protected ListSearch<Game> games;
    private static java.sql.Connection con;
    @DatabaseField
    protected URL url;


    public static void fetchPlayers()
            throws SQLException {

        Statement stmt = null;
        String query = "select * from game";
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String gameName = rs.getString("gameName");
                int playerID = rs.getInt("playerID");
                int playerUID = rs.getInt("playerUID");


            }
        } catch (SQLException e) {
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    public static void fetchPlayersByGame(Game game)
            throws SQLException {

        PreparedStatement pstmt = null;
        String query = "select * from game where game.gameName=?";
        pstmt.setNString(0, game.gameName);
        try {
            pstmt = con.prepareCall(query);
            ResultSet rs = pstmt.executeQuery(query);
            while (rs.next()) {
                String gameName = rs.getString("gameName");
                int playerID = rs.getInt("playerID");
                int playerUID = rs.getInt("playerUID");


            }
        } catch (SQLException e) {
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }
}
