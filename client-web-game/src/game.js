import * as THREE from 'three';
import { createGroundMesh } from './ground.js';
import { Maps } from './maps.js';
import { Protocol } from './protocol.ts';

export class GameScene {
    constructor(network) {
        this.network = network;
        this.mapName = 'SolPlan';
        this.playerU = 0.5;
        this.playerV = 0.5;
        this.playerAngle = 0; // Turns
        this.speed = 0.005;
        this.turnSpeed = 0.01;
        
        this.localPlayerId = -1;
        this.tickMillis = 50; // Default
        this.lastMoveTime = 0;
        
        this.keys = {};
        window.addEventListener('keydown', (e) => this.keys[e.key] = true);
        window.addEventListener('keyup', (e) => this.keys[e.key] = false);

        this.scene = new THREE.Scene();
        this.camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.001, 1000);
        this.renderer = new THREE.WebGLRenderer({ antialias: true });

        this.renderer.setSize(window.innerWidth, window.innerHeight);
        document.body.appendChild(this.renderer.domElement);

        const light = new THREE.DirectionalLight(0xffffff, 1);
        light.position.set(1, 1, 1);
        this.scene.add(light);
        this.scene.add(new THREE.AmbientLight(0x404040));

        this.players = new Map();
        this.bonuses = new Map();
        this.pendingPicks = new Set();
        this.ground = null;

        this.rebuildGround();
        this.animate();
    }

    rebuildGround() {
        if (this.ground) this.scene.remove(this.ground);
        const calculator = (u, v) => Maps.calculerPoint3D(this.mapName, u, v);
        this.ground = createGroundMesh(calculator);
        this.scene.add(this.ground);
    }

    updateMovement() {
        let moved = false;
        const radAngle = this.playerAngle * 2 * Math.PI;

        if (this.keys['ArrowUp']) {
            this.playerU += Math.cos(radAngle) * this.speed;
            this.playerV += Math.sin(radAngle) * this.speed;
            moved = true;
        }
        if (this.keys['ArrowDown']) {
            this.playerU -= Math.cos(radAngle) * this.speed;
            this.playerV -= Math.sin(radAngle) * this.speed;
            moved = true;
        }
        if (this.keys['ArrowLeft']) { this.playerAngle -= this.turnSpeed; moved = true; }
        if (this.keys['ArrowRight']) { this.playerAngle += this.turnSpeed; moved = true; }

        this.playerU = Math.max(0, Math.min(1, this.playerU));
        this.playerV = Math.max(0, Math.min(1, this.playerV));

        const now = Date.now();
        if (moved && (now - this.lastMoveTime > this.tickMillis)) {
            this.sendMyPosition(this.playerU, this.playerV, this.playerAngle);
            this.lastMoveTime = now;
        }
    }

    posCamera() {
        const p3 = Maps.p3(this.mapName, this.playerU, this.playerV, 0.05);
        const radAngle = this.playerAngle * 2 * Math.PI;
        const dist = 0.2;
        const camU = this.playerU - Math.cos(radAngle) * dist;
        const camV = this.playerV - Math.sin(radAngle) * dist;
        const camP3 = Maps.p3(this.mapName, camU, camV, 0.1);
        
        this.camera.position.set(camP3.x, camP3.y, camP3.z);
        this.camera.lookAt(p3.x, p3.y, p3.z);
    }

    checkBonusCollisions() {
        this.bonuses.forEach((mesh, id) => {
            const dist = mesh.position.distanceTo(Maps.p3(this.mapName, this.playerU, this.playerV, 0));
            // Increased threshold to 0.08 to be more generous than server's 0.05
            if (dist < 0.08 && !this.pendingPicks.has(id)) {
                console.log("Collision detected with bonus:", id, "dist:", dist);
                const message = { type: Protocol.PICK, playerId: this.localPlayerId, bonusId: id, x: this.playerU, y: this.playerV, z: 0 };
                console.log("Sending PICK message:", message);
                this.pendingPicks.add(id);
                this.network.send(message);
            }
        });
        /*for (let i = 0; i < this.pendingPicks.size; i++) {
            this.bonuses.delete(this.pendingPicks[i]);
        }
        this.pendingPicks.clear();*/
    }

    animate() {
        requestAnimationFrame(() => this.animate());
        this.updateMovement();
        this.checkBonusCollisions();
        this.posCamera();
        this.renderer.render(this.scene, this.camera);
    }

    updateState(message) {
        console.log("Received message:", message);
        const statusEl = document.getElementById('status');
        const scoreboardEl = document.getElementById('scoreboard');
        
        switch(message.type) {
            case 'welcome':
                this.localPlayerId = message.playerId;
                this.tickMillis = message.tickMillis || 50;
                statusEl.innerText = `Map: ${message.mapName}`;
                
                if (message.mapName && message.mapName !== this.mapName) {
                    this.mapName = message.mapName;
                    this.rebuildGround();
                }
                if (message.bonuses) {
                    message.bonuses.forEach(b => this.createBonus(b));
                }
                break;
            case 'state':
                if (message.players) {
                    const activeIds = new Set();
                    let scoreboardText = 'Players:<br>';
                    message.players.forEach(p => {
                        this.updatePlayer(p);
                        activeIds.add(p.id);
                        scoreboardText += `${p.playerName || p.id}: ${p.points || 0}<br>`;
                    });
                    scoreboardEl.innerHTML = scoreboardText;
                    
                    // Remove disconnected
                    this.players.forEach((mesh, id) => {
                        if (!activeIds.has(id)) {
                            this.scene.remove(mesh);
                            this.players.delete(id);
                        }
                    });
                }
                break;
            case 'pick':
            case 'bonusTaken':
                console.log('picked');
                const mesh = this.bonuses.get(message.bonusId);
                this.pendingPicks.delete(message.bonusId); // Clear from throttle
                if (mesh) {
                    this.scene.remove(mesh);
                    this.bonuses.delete(message.bonusId);
                }
                break;
            case 'gameOver':
                document.getElementById('game-over').style.display = 'block';
                break;
        }
    }

    createBonus(bonus) {
        const pos = Maps.p3(this.mapName, bonus.x, bonus.y, 0); // Use y instead of z
        const geometry = new THREE.SphereGeometry(0.005);
        const material = new THREE.MeshBasicMaterial({ color: 0xffff00 });
        const mesh = new THREE.Mesh(geometry, material);
        mesh.position.set(pos.x, pos.y, pos.z);
        this.scene.add(mesh);
        this.bonuses.set(bonus.id, mesh);
    }

    updatePlayer(player) {
        const pos = Maps.p3(this.mapName, player.x, player.y, 0.006);
        let mesh = this.players.get(player.id);
        if (!mesh) {
            const geometry = new THREE.BoxGeometry(0.006, 0.006, 0.006);
            const material = new THREE.MeshBasicMaterial({ color: player.id === this.localPlayerId ? 0x00ff00 : 0xff0000 });
            mesh = new THREE.Mesh(geometry, material);
            this.scene.add(mesh);
            this.players.set(player.id, mesh);
        }
        mesh.position.set(pos.x, pos.y, pos.z);
        mesh.rotation.y = player.angleZ * 2 * Math.PI;
    }

    sendMyPosition(u, v, angleZ) {
        this.network.send({
            type: Protocol.MOVE,
            x: u,
            y: v,
            z: 0,
            angleZ: angleZ
        });
    }
}