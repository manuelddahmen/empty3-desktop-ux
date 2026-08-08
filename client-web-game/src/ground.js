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

import * as THREE from 'three';
import { Maps } from './maps.js';

export function createGroundMesh(mapName, resolution = 50) {
    const geometry = new THREE.BufferGeometry();
    const vertices = [];
    const indices = [];

    // 1. Generate grid points
    for (let i = 0; i <= resolution; i++) {
        for (let j = 0; j <= resolution; j++) {
            const u = i / resolution;
            const v = j / resolution;
            const p = Maps.p3(mapName, u, v, 0);
            vertices.push(p.x, p.y, p.z);
        }
    }

    // 2. Generate quad indices (two triangles per quad)
    for (let i = 0; i < resolution; i++) {
        for (let j = 0; j < resolution; j++) {
            const a = i * (resolution + 1) + j;
            const b = a + 1;
            const c = (i + 1) * (resolution + 1) + j;
            const d = c + 1;

            // Two triangles
            indices.push(a, c, b);
            indices.push(b, c, d);
        }
    }

    geometry.setAttribute('position', new THREE.Float32BufferAttribute(vertices, 3));
    geometry.setIndex(indices);
    geometry.computeVertexNormals();

    const material = new THREE.MeshPhongMaterial({ color: 0x888888, side: THREE.DoubleSide, wireframe: true });
    return new THREE.Mesh(geometry, material);
}