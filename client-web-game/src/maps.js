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

export const Maps = {
    calculerPoint3D(mapName, u, v) {
        // Normalize u, v to -0.5 to 0.5
        const nu = u - 0.5;
        const nv = v - 0.5;
        
        switch (mapName) {
            case 'SolPlan':
                return new THREE.Vector3(nu, 0, nv);
            case 'SolRelief':
                // T = 6 from plan
                const y = 0.2 * Math.sin(2 * Math.PI * 6 * nu) * Math.cos(2 * Math.PI * 6 * nv);
                return new THREE.Vector3(nu, y, nv);
            case 'SolSphere':
                // Approximation: radius 10
                const radius = 10;
                // Simple mapping: u, v -> angles
                const theta = u * 2 * Math.PI;
                const phi = v * Math.PI;
                return new THREE.Vector3(
                    radius * Math.sin(phi) * Math.cos(theta),
                    radius * Math.cos(phi),
                    radius * Math.sin(phi) * Math.sin(theta)
                );
            default:
                console.warn(`Unknown map: ${mapName}, defaulting to SolPlan`);
                return new THREE.Vector3(nu, 0, nv);
        }
    },

    p3(mapName, x, y, z) {
        // Surface point
        const surfacePoint = this.calculerPoint3D(mapName, x, y);
        // Note: For full normal-aware height, we'd need to compute the normal.
        // For now, assume vertical offset for SolPlan/SolRelief as a first approximation.
        return surfacePoint.add(new THREE.Vector3(0, z, 0));
    }
};
