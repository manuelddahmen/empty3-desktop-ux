import * as THREE from 'three';
import { createGroundMesh } from './ground.js';

export class GameScene {
    constructor(network) {
        this.network = network; // Store reference to network
        this.playerU = 0.5;    // Initial (u, v)
        this.playerV = 0.5;
        this.playerAngle = 0;
        this.speed = 0.01;
        this.turnSpeed = 0.1;

        // Add these to GameScene constructor:
        this.localPlayerId = -1; // Track who I am

        this.keys = {};
        window.addEventListener('keydown', (e) => this.keys[e.key] = true);
        window.addEventListener('keyup', (e) => this.keys[e.key] = false);

        this.scene = new THREE.Scene();
        this.camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 1000);
        this.renderer = new THREE.WebGLRenderer();

        this.renderer.setSize(window.innerWidth, window.innerHeight);
        document.body.appendChild(this.renderer.domElement);

        this.camera.position.set(0, 2, 2); // Closer
        this.camera.lookAt(0, 0, 0);

        // Basic light
        const light = new THREE.DirectionalLight(0xffffff, 1);
        light.position.set(1, 1, 1);
        this.scene.add(light);
        this.scene.add(new THREE.AmbientLight(0x404040)); // Soft white light

        // Containers for entities
        this.players = new Map(); // id -> THREE.Mesh
        this.bonuses = new Map(); // id -> THREE.Mesh
        // Example for SolPlan: f(u,v) = (u-0.5, 0, v-0.5)
// Define the map's ground function (this must be updated based on the loaded map)
// Example: f(u,v) = (u-0.5, 0, v-0.5)
        const ground = createGroundMesh(this.calculerPoint3D );
        this.scene.add(ground);

        this.animate();
    }
            // Helper to transform server coords
// Helper to transform server coords
    transformCoords(raw) {
        // Assuming server raw.x is 'u' and raw.z is 'v'
        // If raw coordinates are already in 3D, this function may need adjustment
        return this.calculerPoint3D(raw.x, raw.z);
    }


    // Inside your animate/update loop
    updateMovement() {
        let moved = false;
        if (this.keys['ArrowUp']) {
            this.playerU += Math.cos(this.playerAngle) * this.speed;
            this.playerV += Math.sin(this.playerAngle) * this.speed;
            moved = true;
        }
        if (this.keys['ArrowLeft']) {
            this.playerAngle += this.turnSpeed;
            moved = true;
        }
        if (this.keys['ArrowRight']) {
            this.playerAngle -= this.turnSpeed;
            moved = true;
        }

        if (moved) {
            this.sendMyPosition(this.playerU, this.playerV, this.playerAngle);
        }
    }

// Update the animate loop:
    // Update the animate loop:
    animate() {
        requestAnimationFrame(() => this.animate());

        // Update movement inputs
        this.updateMovement();

        // Camera tracking
        if (this.localPlayerId !== -1) {
            const playerMesh = this.players.get(this.localPlayerId);
            if (playerMesh) {
                // Inside your animate() method in GameScene class

                // Get player position and current angle
                const pos = playerMesh.position;
                const angle = this.playerAngle; // Ensure this is the angle in radians

                // 1. Calculate camera position behind the player
                // Based on: pos.x - cos(angle)*dist, pos.y - sin(angle)*dist
                const dist = 0.5; // Adjust this distance for "rear" view
                const camX = pos.x - Math.cos(angle) * dist;
                const camY = pos.z - Math.sin(angle) * dist; // Note: In 3D (x,z), so mapping y-plane to z
                const camZ = 0.2; // Fixed height as requested

                // 2. Set camera position
                this.camera.position.set(camX, camZ, camY);

                // 3. Look at player's position
                this.camera.lookAt(pos);
            }
        } else {
            console.error("Unexpected state:", message);
        }

        this.renderer.render(this.scene, this.camera);
    }

    updateState(message) {
        if (message.type === 'welcome') {
            this.localPlayerId = message.playerId; // Assume welcome message has playerId
            console.log("WELCOME:", message);
            // Initialize bonuses
            if (message.bonuses) {
                message.bonuses.forEach(b => this.createBonus(b));
            }
        } else if (message.type === 'state') {
            // Update player positions
            if (message.players) {
                message.players.forEach(p => this.updatePlayer(p));
            }
        }
    }


// The map-specific ground function
    calculerPoint3D(u, v) {
        // Ported from SolPlan.java
        return { x: u - 0.5, y: 0, z: v - 0.5 };
    }

// Transform (u, v) to world 3D position for Three.js
    worldPosition(u, v) {
        return this.calculerPoint3D(u, v);
    }

// 1. Rendering Bonuses:
    createBonus(bonus) {
        const pos = this.worldPosition(bonus.x, bonus.z);
        console.log("Creating bonus at:", pos); // LOG

        const geometry = new THREE.SphereGeometry(0.5);
        const material = new THREE.MeshBasicMaterial({ color: 0xffff00 });
        const mesh = new THREE.Mesh(geometry, material);
        mesh.position.set(pos.x, pos.y, pos.z);
        this.scene.add(mesh);
        this.bonuses.set(bonus.id, mesh);
        console.log("Bonus added to scene. Current scene children:", this.scene.children.length); // LOG
    }

    updatePlayer(player) {
        // 1. Transform (u, v) -> (x, z) world position
        const pos = this.worldPosition(player.x, player.z);

        let mesh = this.players.get(player.id);
        if (!mesh) {
            // Create player mesh
            // In updatePlayer
            const geometry = new THREE.BoxGeometry(0.5, 1.0, 0.5); // Changed 0.1, 0.2, 0.1 -> 0.5, 1.0, 0.5
            const material = new THREE.MeshBasicMaterial({ color: 0x00ff00 });
            mesh = new THREE.Mesh(geometry, material);
            this.scene.add(mesh);
            this.players.set(player.id, mesh);
        }

        // 2. Update position
        mesh.position.set(pos.x, pos.y, pos.z);

        // 3. Update orientation
        // Assuming angleZ is in radians and maps to rotation around the Y-axis
        mesh.rotation.y = player.angleZ;
    }


    sendMyPosition(u, v, angleZ) {
        // Keep (u, v) as raw input, and angleZ as raw orientation
        this.network.send({
            type: 'move',
            x: u,      // u
            y: v,      // v
            z: 0,      // Likely placeholder if map is plane
            angleZ: angleZ
        });
    }
}