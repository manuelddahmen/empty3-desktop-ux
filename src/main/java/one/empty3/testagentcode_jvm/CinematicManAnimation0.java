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

package one.empty3.testagentcode_jvm;

import one.empty3.apps.testobject.TestObjetSub;
import one.empty3.library.*;
import one.empty3.libs.Color;

import java.util.ArrayList;
import java.util.List;

public class CinematicManAnimation0 extends TestObjetSub {
    // Body parts
    private Sphere head;
    private Box torso;
    private Cylinder leftArm, rightArm;
    private Cylinder leftLeg, rightLeg;    // Environmental elements
    private List<Box> buildings = new ArrayList<>();
    private Box bridgeRoad;
    private List<Cylinder> bridgeArches = new ArrayList<>();
    private Box water;
    private int frameCount = 0;

    @Override
    public void ginit() {
        scene = new Scene();
        // 1. Generate City Background (Phase 1)
        // High-rise buildings on both sides of the street
        for (int i = 0; i < 8; i++) {
            double zOffset = -6.0 + i * 2.0;
            // Left side buildings
            Box bLeft = new Box(0.6, 2.5 + (i % 3) * 0.8, 0.6);
            bLeft.setOrig(new Point3D(-1.5, 0.0, zOffset));
            bLeft.texture(new ColorTexture(Color.newCol(0.15f, 0.15f, 0.18f)));
            buildings.add(bLeft);
            // Right side buildings
            Box bRight = new Box(0.6, 2.0 + (i % 2) * 1.2, 0.6);
            bRight.setOrig(new Point3D(1.5, 0.0, zOffset));
            bRight.texture(new ColorTexture(Color.newCol(0.12f, 0.12f, 0.15f)));
            buildings.add(bRight);
        }
        // 2. Generate the Bridge (Phase 2)
        // Road spans from Z = 2.0 to Z = 7.0 at height Y = 0.5
        bridgeRoad = new Box(1.0, 0.1, 5.0);
        bridgeRoad.setOrig(new Point3D(-0.5, 0.5, 2.0));
        bridgeRoad.texture(new ColorTexture(Color.newCol(0.3f, 0.3f, 0.35f)));
        // Bridge pillars and railings
        for (double z = 2.0; z <= 7.0; z += 1.0) {
            Cylinder leftPillar = new Cylinder(new Point3D(-0.5, -2.0, z), new Point3D(-0.5, 0.5, z), 0.08);
            leftPillar.texture(new ColorTexture(Color.newCol(0.4f, 0.4f, 0.4f)));
            bridgeArches.add(leftPillar);
            Cylinder rightPillar = new Cylinder(new Point3D(0.5, -2.0, z), new Point3D(0.5, 0.5, z), 0.08);
            rightPillar.texture(new ColorTexture(Color.newCol(0.4f, 0.4f, 0.4f)));
            bridgeArches.add(rightPillar);
        }
        // 3. Generate the Water body below (Phase 3)
        // Positioned at Y = -2.0
        water = new Box(15.0, 0.1, 15.0);
        water.setOrig(new Point3D(-7.5, -2.0, 3.0));
        water.texture(new ColorTexture(Color.newCol(0.0f, 0.3f, 0.7f)));
        // 4. Character skin/clothing colors
        Color skinColor = Color.newCol(0.95f, 0.80f, 0.69f);
        Color shirtColor = Color.newCol(0.85f, 0.20f, 0.20f);
        // Bright red shirt for visibility
        Color pantsColor = Color.newCol(0.10f, 0.10f, 0.15f);
        head = new Sphere(Point3D.O0, 0.15);
        head.texture(new ColorTexture(skinColor));
        torso = new Box(0.3, 0.5, 0.16);
        torso.texture(new ColorTexture(shirtColor));
        leftArm = new Cylinder(Point3D.O0, new Point3D(0.0, -0.45, 0.0), 0.04);
        leftArm.texture(new ColorTexture(skinColor));
        rightArm = new Cylinder(Point3D.O0, new Point3D(0.0, -0.45, 0.0), 0.04);
        rightArm.texture(new ColorTexture(skinColor));
        leftLeg = new Cylinder(Point3D.O0, new Point3D(0.0, -0.65, 0.0), 0.05);
        leftLeg.texture(new ColorTexture(pantsColor));
        rightLeg = new Cylinder(Point3D.O0, new Point3D(0.0, -0.65, 0.0), 0.05);
        rightLeg.texture(new ColorTexture(pantsColor));
    }

    @Override
    public void finit() throws Exception {
        super.finit();
        scene.clear();
        // Put static environment in scene
        for (Box b : buildings) scene.add(b);
        scene.add(bridgeRoad);
        for (Cylinder c : bridgeArches) scene.add(c);
        scene.add(water);
        // Put character parts
        scene.add(head);
        scene.add(torso);
        scene.add(leftArm);
        scene.add(rightArm);
        scene.add(leftLeg);
        scene.add(rightLeg);
        // Movement Phase Calculations
        double yPos = 0.6;
        // Normal surface elevation (height = 0.6 on ground)
        double zPos = -6.0;
        double swingAngle = 0.0;
        double torsoLean = 0.0;
        double diveRotation = 0.0;
        if (frameCount <= 100) {
            // PHASE 1: Walk in the city
            double p = frameCount / 100.0;
            zPos = -5.0 + p * 7.0;
            // Moves from Z = -5.0 to Z = 2.0 (Entering the bridge)
            yPos = 0.6;
            swingAngle = Math.sin(frameCount * 0.15) * 0.35;
            // Gentle walk swing
            torsoLean = 0.05;
            // Minimal body lean
        } else if (frameCount <= 200) {
            // PHASE 2: Run on the bridge
            double p = (frameCount - 100) / 100.0;
            zPos = 2.0 + p * 5.0;
            // Runs from Z = 2.0 to Z = 7.0 (reaching the bridge edge)
            // Vertical running bounce
            yPos = 0.6 + Math.abs(Math.sin(frameCount * 0.35)) * 0.08;
            swingAngle = Math.sin(frameCount * 0.35) * 0.7;
            // Large athletic stride
            torsoLean = 0.25;
            // Forward athletic body lean
        } else {
            // PHASE 3: Dive off the bridge into water
            double p = (frameCount - 200) / 100.0;
            zPos = 7.0 + p * 2.0;
            // Continues forward trajectory
            // Quadratic fall (gravity plunge)
            // Starts at Y = 0.6, peak jump at Y = 1.0, plunges down to Y = -2.5 (underwater)
            yPos = 0.6 + 2.0 * p - 5.1 * p * p;
            // Dive physics: body rotates into head-first plunge
            diveRotation = p * Math.PI * 0.7;
            // Smoothly rotate ~120 degrees forward
            swingAngle = 0.1;
            // Legs straighten together, arms extend forward
        }        // Apply spatial properties to character limbs using axis transforms (Rule 5)
        Point3D torsoOrig = new Point3D(0.0, yPos, zPos);
        torso.setOrig(torsoOrig);
        rotateX(torso, torsoLean + diveRotation);
        // Head shifts according to dive rotation
        Point3D headOffset = new Point3D(0.0, 0.45, 0.0);
        head.setOrig(torsoOrig.plus(rotateVectorX(headOffset, torsoLean + diveRotation)));
        rotateX(head, torsoLean + diveRotation);
        // Left Leg
        Point3D leftHipOffset = new Point3D(-0.1, 0.0, 0.0);
        leftLeg.setOrig(torsoOrig.plus(rotateVectorX(leftHipOffset, torsoLean + diveRotation)));
        rotateX(leftLeg, swingAngle + diveRotation);
        // Right Leg
        Point3D rightHipOffset = new Point3D(0.1, 0.0, 0.0);
        rightLeg.setOrig(torsoOrig.plus(rotateVectorX(rightHipOffset, torsoLean + diveRotation)));
        rotateX(rightLeg, -swingAngle + diveRotation);
        // Left Arm (swings opposite to left leg)
        Point3D leftShoulderOffset = new Point3D(-0.2, 0.4, 0.0);
        leftArm.setOrig(torsoOrig.plus(rotateVectorX(leftShoulderOffset, torsoLean + diveRotation)));
        if (frameCount > 200) {
            // Reach arms forward/upward during a dive
            rotateX(leftArm, -Math.PI * 0.8 + diveRotation);
        } else {
            rotateX(leftArm, -swingAngle + torsoLean);
        }
        // Right Arm
        Point3D rightShoulderOffset = new Point3D(0.2, 0.4, 0.0);
        rightArm.setOrig(torsoOrig.plus(rotateVectorX(rightShoulderOffset, torsoLean + diveRotation)));
        if (frameCount > 200) {
            rotateX(rightArm, -Math.PI * 0.8 + diveRotation);
        } else {
            rotateX(rightArm, swingAngle + torsoLean);
        }
        // --- CAMERA SETUP (Rule 7 & 8) ---
        // Dynamically follows the action
        Point3D cameraPos;
        Point3D lookAtTarget = new Point3D(0.0, yPos, zPos);
        if (frameCount <= 100) {
            // Camera tracks the walk from a side angle
            cameraPos = new Point3D(2.5, 1.8, zPos + 2.0);
        } else if (frameCount <= 200) {
            // Camera pans ahead of the runner to show the upcoming bridge
            cameraPos = new Point3D(2.8, 1.5, zPos + 1.5);
        } else {
            // Camera stays static on the edge of the bridge to capture the dramatic plunge
            cameraPos = new Point3D(3.0, 1.0, 6.0);
        }
        Camera camera = new Camera(cameraPos, lookAtTarget, Point3D.Y);
        scene.cameraActive(camera);
        frameCount++;
    }

    // Helper method to set local coordinate rotation matrix on X-Axis (Rule 5)
    private void rotateX(Representable obj, double theta) {
        obj.setVectX(Point3D.X);
        obj.setVectY(new Point3D(0.0, Math.cos(theta), Math.sin(theta)));
        obj.setVectZ(new Point3D(0.0, -Math.sin(theta), Math.cos(theta)));
    }

    // Rotates a local displacement vector around the X-axis for joint linking
    private Point3D rotateVectorX(Point3D vec, double theta) {
        double y = vec.getY() * Math.cos(theta) - vec.getZ() * Math.sin(theta);
        double z = vec.getY() * Math.sin(theta) + vec.getZ() * Math.cos(theta);
        return new Point3D(vec.getX(), y, z);
    }

    public static void main(String[] args) {
        CinematicManAnimation0 cinematic = new CinematicManAnimation0();
        cinematic.loop(true);
        cinematic.setMaxFrames(300);
        // 300 frames of cinematic sequence
        new Thread(cinematic).start();
    }
}