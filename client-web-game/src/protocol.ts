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

// Mirror of Protocol.java for the web client
export const VERSION = 1;
export interface NetMessage {
    type: string;
    protocolVersion?: number;
    playerName?: string;
    colorRgb?: number;
    token?: string;
    mapName?: string; // Add mapName
    playerId?: number;
    tickMillis?: number;
    bonuses?: any[];
    players?: any[];
    gameOver?: boolean;
    message?: string;
    bonusId?: string;
    points?: number;
    x?: number;
    y?: number;
    z?: number;
    angleZ?: number;
}

export const Protocol = {
    JOIN: "join",
    MOVE: "move",
    PICK: "pick",
    LEAVE: "leave",
    WELCOME: "welcome",
    STATE: "state",
    BONUS_TAKEN: "bonusTaken",
    GAME_OVER: "gameOver",
    ERROR: "error",

    decode(data: string) {
        const lines = data.split('\n').filter(line => line.trim().length > 0);
        return lines.map(line => {
            try {
                return JSON.parse(line);
            } catch (e) {
                console.error("Failed to decode message", e, line);
                return null;
            }
        }).filter(msg => msg !== null);
    },

    encode(message: any) {
        return JSON.stringify(message);
    }
};