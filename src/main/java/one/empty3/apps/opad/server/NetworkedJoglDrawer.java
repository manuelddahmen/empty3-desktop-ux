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

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import one.empty3.apps.opad.*;
import one.empty3.library.*;

import java.util.List;

public class NetworkedJoglDrawer extends JoglDrawer {
    private GameClient client;

    public NetworkedJoglDrawer(DarkFortressGUI darkFortressGUI, GameClient client) {
        super(darkFortressGUI);
        this.client = client;
    }

    @Override
    protected Bonus createBonus(PositionUpdate m) {
        return new SyncedBonus(client.getInitialBonuses());
    }

    @Override
    protected void drawExtraObjects(GLU glu, GL2 gl) {
        List<PlayerState> remotePlayers = client.remotePlayers();
        for (PlayerState playerState : remotePlayers) {
            Cube remotePlayerCube = new Cube();
            remotePlayerCube.setOrig(new Point3D(playerState.x, playerState.y, playerState.z));
            // Draw this cube using JOGL's draw method defined in JoglDrawer
            draw(remotePlayerCube, glu, gl);
        }
    }
}
