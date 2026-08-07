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

import { NetworkHandler } from './network.js';
import { GameScene } from './game.js';
import { Protocol } from './protocol.ts';

const network = new NetworkHandler('ws://localhost:8080');
const scene = new GameScene(network);

network.onMessage = (message) => {
    if (message.type === Protocol.WELCOME) {
        document.getElementById('menu').style.display = 'none';
        document.getElementById('hud').style.display = 'block';
    }
    scene.updateState(message);
};

const handleJoin = (isNew) => {
    const playerName = document.getElementById('playerName').value || "WebPlayer";
    const roomId = document.getElementById('roomId').value;
    const token = document.getElementById('authToken').value;
    const mapName = document.getElementById('levelSelect').value;
    
    network.connect().then(() => {
        network.send({
            type: Protocol.JOIN,
            protocolVersion: 1,
            playerName: playerName,
            token: token,
            mapName: mapName,
            colorRgb: 0
        });
    });
};

document.getElementById('btnNew').addEventListener('click', () => handleJoin(true));
document.getElementById('btnJoin').addEventListener('click', () => handleJoin(false));