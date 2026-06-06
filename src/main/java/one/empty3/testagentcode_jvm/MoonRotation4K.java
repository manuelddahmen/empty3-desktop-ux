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

import one.empty3.library.*;
import one.empty3.apps.testobject.TestObjetSub;
import one.empty3.libs.Image;
import java.io.File;

/**
 * Animation d'une sphère Moon en 4K pendant 20 secondes.
 */
public class MoonRotation4K extends TestObjetSub {
    private Sphere moon;
    private static final int FPS = 25;
    private static final int DURATION_SECONDS = 20;

    @Override
    public void ginit() {
        frame = 0;
        // 1. Création de la scène
        scene = new Scene();


        // 2. Création de la sphère (Centre 0,0,0, Rayon 1.0)
        moon = new Sphere(Point3D.O0, 1.0);

        // 3. Application de la texture Moon (Rule 4 & 11)
        try {
            File textureFile = new File("d:\\current\\moon.tif");
            if (textureFile.exists()) {
                Image img = new Image(textureFile);
                ImageTexture texture = new ImageTexture(img);
                moon.texture(texture);
            } else {
                // Fallback couleur si le fichier est absent
                moon.texture(new ColorTexture(one.empty3.libs.Color.newCol(0.8f, 0.4f, 0.2f)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Ajout de la sphère à la scène
        scene.add(moon);

        // 4. Configuration de la caméra (Rule 7 & 8: Vecteur UP explicite pour éviter matrice nulle)
        // Positionnée à z=3 pour voir la sphère de rayon 1.0
        Camera camera = new Camera(new Point3D(0.0, 0.0, 5.0), Point3D.O0, Point3D.Y);
        camera.angleXr(camera.angleX(), ((double)z().la/ z().ha)) {
        scene.cameraActive(camera);
    }

    @Override
    public void finit() {
        // 5. Animation de la rotation (Rule 2 & 5)
        // Calcul de l'angle en fonction de l'image actuelle (frame)
        double totalFrames = (double) (DURATION_SECONDS * FPS);
        double angle = 2.0 * Math.PI * (double) frame / totalFrames;

        // Rotation autour de l'axe Y : mise à jour des vecteurs d'orientation
        double cosA = Math.cos(angle);
        double sinA = Math.sin(angle);

        // Modification des axes de la sphère pour la faire tourner sur elle-même
        moon.setVectX(new Point3D(cosA, 0.0, -sinA));
        moon.setVectY(Point3D.Y);
        moon.setVectZ(new Point3D(sinA, 0.0, cosA));
        moon.setOrig(new Point3D(0.0, 0.0, 0.0)); // Centre de rotation
    }

    public static void main(String[] args) {
        MoonRotation4K animation = new MoonRotation4K();
        animation.setGenerate(GENERATE_IMAGE|GENERATE_MOVIE|GENERATE_SAVE_IMAGE);
        // Configuration du rendu
        animation.setResx(3840); // 4K UHD
        animation.setResy(2160);
        animation.setFps(FPS);
        animation.setPublish(false);
        // Nombre total d'images (20s * 25fps = 500 frames)
        animation.setMaxFrames(DURATION_SECONDS * FPS);

        // Lancement du processus de rendu
        Thread thread = new Thread(animation);
        thread.start();
    }
}