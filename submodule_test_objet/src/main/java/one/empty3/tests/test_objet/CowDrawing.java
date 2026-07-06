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

package one.empty3.tests.test_objet;

import one.empty3.apps.testobject.TestObjetSub;
import one.empty3.library.Camera;
import one.empty3.library.objloader.E3Model;
import one.empty3.libs.Color;
import one.empty3.library.Point3D;
import one.empty3.library.Scene;
import one.empty3.library.ZBufferImpl;
import one.empty3.libs.Image;

import java.io.*;
import java.nio.charset.Charset;


public class CowDrawing extends TestObjetSub {
int FPS = 25;
double DURATION_SECONDS = 20.0;

    private static E3Model cowModel;

    @Override
    public void finit() throws Exception {
        // 5. Animation de la rotation (Rule 2 & 5)
        // Calcul de l'angle en fonction de l'image actuelle (frame)
        double totalFrames = (double) (DURATION_SECONDS * FPS);
        double angle = 2.0 * Math.PI * (double) frame / totalFrames;

        // Rotation autour de l'axe Y : mise à jour des vecteurs d'orientation
        double cosA = Math.cos(angle);
        double sinA = Math.sin(angle);

        // Modification des axes de la sphère pour la faire tourner sur elle-même
        cowModel.setVectX(new Point3D(cosA, 0.0, -sinA));
        cowModel.setVectY(Point3D.Y);
        cowModel.setVectZ(new Point3D(sinA, 0.0, cosA));
        cowModel.setOrig(new Point3D(0.0, 0.0, 0.0)); // Centre de rotation
    }


    @Override
    public void ginit() {
        // Ceci évite les matrices de caméra dégénérées.
        camera(new Camera(new Point3D(0.0, 0.0, 5.0), Point3D.O0, Point3D.Y));
        scene().cameraActive(camera());
        String cowObjString = """
# Minimal OBJ file for a cube to demonstrate E3Model.
# Replace this with actual cow OBJ data.
v 0.0 0.0 0.0
v 1.0 0.0 0.0
v 1.0 1.0 0.0
v 0.0 1.0 0.0
v 0.0 0.0 1.0
v 1.0 0.0 1.0
v 1.0 1.0 1.0
v 0.0 1.0 1.0
f 1 2 3 4
f 5 6 7 8
f 1 2 6 5
f 2 3 7 6
f 3 4 8 7
f 4 1 5 8
"""; // Votre texte OBJ pour la vache ici
        StringReader sr = new StringReader(new String(cowObjString.getBytes(Charset.availableCharsets().get("UTF-8"))));
        cowModel = new E3Model(new BufferedReader(sr), false, "");

        // Déplacez et redimensionnez la vache si nécessaire
        // Exemple: définir la position d'origine (centre de la vache)
        cowModel.setOrig(new Point3D(0.0, -0.5, 0.0)); // Déplace la vache légèrement vers le bas

        // Exemple: définir la couleur de la vache
        cowModel.texture(new one.empty3.library.TextureCol(Color.newCol(0.5f, 0.3f, 0.1f))); // Couleur marron clair

        // Ajoute le modèle de vache à la scène
        scene.add(cowModel);
        // --- Fin du code spécifique à la vache ---

        // Configure le ZBuffer pour le rendu de l'image
        int width = 800;
        int height = 600;
        ZBufferImpl zBuffer = new ZBufferImpl(width, height);
        zBuffer.scene(scene());
        zBuffer.camera(camera());
    }

    public static void main(String[] args) {
        new Thread(new CowDrawing()).start();
    }
}