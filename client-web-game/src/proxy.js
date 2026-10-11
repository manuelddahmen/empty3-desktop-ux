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
 *  * Created by Manuel D Dahmen -2026
 *
 *
 */

// client-web-game/src/proxy.js
import { WebSocketServer } from 'ws';
import net from 'net';

// Configuration
const WS_PORT = process.env.PORT || 8080;
const TCP_SERVER_HOST = '34.170.12.255';
const TCP_SERVER_PORT = 4712;

const wss = new WebSocketServer({ port: WS_PORT});

wss.on('connection', (ws) => {
    console.log('Client connected via WebSocket');

    const tcpSocket = new net.Socket();

    // ADD THIS LOGGING
    tcpSocket.on('error', (err) => {
        console.error('TCP Connection Error (GKE server unreachable?):', err);
        ws.close();
    });

    tcpSocket.connect(TCP_SERVER_PORT, TCP_SERVER_HOST, () => {
        console.log('Successfully connected to TCP Game Server at ' + TCP_SERVER_HOST);
    });
    // Forward WebSocket data to TCP
    ws.on('message', (data) => {
        // Ensure the data ends with a newline character
        const messageString = data.toString().trim() + "\n";
        console.log('Sending to TCP:', messageString);
        tcpSocket.write(messageString);
    });

    // Forward TCP data to WebSocket
    tcpSocket.on('data', (data) => {
        ws.send(data);
    });

    // Handle closures
    ws.on('close', () => {
        console.log('WebSocket client disconnected');
        tcpSocket.destroy();
    });

    tcpSocket.on('close', () => {
        console.log('TCP Game Server disconnected');
        ws.close();
    });

    tcpSocket.on('error', (err) => {
        console.error('TCP error:', err);
        ws.close();
    });
});

console.log(`WebSocket Proxy server listening on port ${WS_PORT}`);
