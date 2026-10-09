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
 *  * Created by Manuel D Dahmen -2026
 *
 *
 */

package one.empty3.testagentcode_jvm;

import one.empty3.library.*;
import one.empty3.apps.testobject.TestObjetSub;
import one.empty3.libs.Color;

public class GreenCubeAnimation extends TestObjetSub {

    private Representable cube;
    double cubeSize = 0.5;

    @Override
    public void ginit() {
        // Initialisation de la scène
        scene = new Scene();


        // Positionnement de la caméra (vue de face)
        Camera camera1 = new Camera(new Point3D(0d, 0d, -4* cubeSize), Point3D.O0, Point3D.Y);
        scene.cameraActive(camera1);
        camera(camera1);
        camera1.angleXY(z().getDimx(), z().getDimy(), Math.PI / 6, Axis.Y);
    }

    @Override
    public void finit() {
        scene().clear();
        // Calcul de la progression du film (de 0.0 à 1.0)
        double progress = (double) frame / getMaxFrames();
        setGenerate(GENERATE_IMAGE|GENERATE_SAVE_IMAGE|GENERATE_MOVIE);
        // Animation : de gauche (x = -2.5) à droite (x = 2.5)
        double startX = -2.5*cubeSize;
        double endX = 2.5*cubeSize;
        double currentX = startX + (endX - startX) * progress;
        // Création du cube vert
        // Correspond au quart d'une hauteur d'écran de 2.0 unités
        Point3D base = new Point3D(-cubeSize, -cubeSize, -cubeSize).plus(new Point3D(currentX, 0d, 0d));
        Point3D x = base.plus(new Point3D(2*cubeSize, 0.0,0.0));
        Point3D y = base.plus(new Point3D(0.0, 2*cubeSize,0.0));
        Point3D z = base.plus(new Point3D(0.0, 0.0,2*cubeSize));
        cube = new Parallelepiped(base, x, y, z, new ColorTexture(Color.newCol(0f, 1f, 0f)));

        // Ajout du cube à la scène
        scene().add(cube);

        // Mise à jour de la position du cube
        // On définit la position du centre du cube
        //cube.setOrig();
    }

    public static void main(String[] args) {
        GreenCubeAnimation animation = new GreenCubeAnimation();
        // Configuration de la résolution et du nombre d'images
        animation.setResolution(800, 600);
        animation.setMaxFrames(100);

        new Thread(animation).start();
    }
}