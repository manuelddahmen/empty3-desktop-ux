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

import { Protocol } from './protocol.ts';

export class NetworkHandler {
    constructor(url) {
        this.url = url;
        this.socket = null;
        this.onMessage = null;
    }

    connect() {
        return new Promise((resolve, reject) => {
            this.socket = new WebSocket(this.url);
            var msgCount = 0;


            this.socket.onopen = () => {
                console.log("Connected to WebSocket proxy");
                resolve();
            };

            this.socket.onmessage = async (event) => {
                msgCount++;
                const text = await event.data.text();
                console.log(`Msg ${msgCount}:`, text); // See what msg 594 looks like
                // Convert Blob to string
                console.log("Received raw data:", text); // Debug

                const message = Protocol.decode(text);
                if (message && this.onMessage) {
                    this.onMessage(message);


                }
            };

            this.socket.onerror = (error) => {
                console.error("WebSocket error:", error);
                reject(error);
            };

            this.socket.onclose = () => {
                console.log("Disconnected from WebSocket proxy");
            };
        });
    }

    // In client-web-game/src/network.js
    send(message) {
        if (this.socket && this.socket.readyState === WebSocket.OPEN) {
            // Append newline as required by the Java Protocol
            this.socket.send(Protocol.encode(message) + "\n");
        } else {
            console.error("WebSocket not open");
        }
    }
}