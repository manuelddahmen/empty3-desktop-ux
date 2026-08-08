// client-web-game/src/proxy.js
import { WebSocketServer } from 'ws';
import net from 'net';

// Configuration
const WS_PORT = 8080;
const TCP_SERVER_HOST = '34.170.12.255';
const TCP_SERVER_PORT = 4712;

const wss = new WebSocketServer({ port: WS_PORT });

wss.on('connection', (ws) => {
    console.log('Client connected via WebSocket');

    // Create a TCP connection to the game server
    const tcpSocket = new net.Socket();

    tcpSocket.connect(TCP_SERVER_PORT, TCP_SERVER_HOST, () => {
        console.log('Connected to TCP Game Server');
    });

    // Forward WebSocket data to TCP
    ws.on('message', (data) => {
        tcpSocket.write(data);
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