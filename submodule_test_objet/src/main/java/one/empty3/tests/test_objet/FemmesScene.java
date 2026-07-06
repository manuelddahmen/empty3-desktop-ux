package one.empty3.tests.test_objet;
/*
Explications techniques :
Scene & Representable : On utilise une Scene globale. Chaque femme est groupée dans un RepresentableConteneur pour faciliter leur placement individuel via setOrig.
Morphologie :
La première femme est définie avec une hauteur (height1) de 2.0 et un rayon de cylindre de 0.3 (silhouette élancée).
La deuxième femme a une hauteur de 1.6 et un rayon de 0.4 pour simuler des formes plus généreuses et une taille plus petite.
Couleurs (Texture) : On utilise Color.newCol(r, g, b) conformément aux règles pour définir le roux (orange-rouge) et le brun.
Caméra : Placée à z=6 pour voir les deux personnages, avec Point3D.Y comme vecteur "haut" pour éviter une matrice nulle.
Rendu : La classe TestObjetSub gère automatiquement la création du ZBuffer et l'enregistrement de l'image.
Saisissez un message...
 */
import one.empty3.library.*;
import one.empty3.apps.testobject.TestObjetSub;
import one.empty3.libs.Color;
import one.empty3.libs.Image;
import java.io.File;

public class FemmesScene extends TestObjetSub {

    @Override
    public void ginit() {
        // Création de la scène
        scene = new Scene();

        // --- FEMME 1 : Grande, mince, petits seins, rousse ---
        // Corps (Trunk)
        double height1 = 2.0;
        Cylinder corps1 = new Cylinder(new Point3D(0.0, -height1/2, 0.0), new Point3D(0.0, height1/2, 0.0), 0.3);
        corps1.texture(new ColorTexture(Color.newCol(0.9f, 0.8f, 0.7f))); // Peau claire
        
        // Tête
        Sphere tete1 = new Sphere(new Point3D(0.0, height1/2 + 0.2, 0.0), 0.2);
        tete1.texture(new ColorTexture(Color.newCol(0.9f, 0.8f, 0.7f)));
        
        // Cheveux Roux
        Sphere cheveux1 = new Sphere(new Point3D(0.0, height1/2 + 0.3, 0.0), 0.22);
        cheveux1.texture(new ColorTexture(Color.newCol(0.8f, 0.3f, 0.0f))); // Roux

        // Positionnement de la Femme 1 à gauche
        Representable conteneur1 = new RepresentableConteneur();
        ((RepresentableConteneur)conteneur1).add(corps1);
        ((RepresentableConteneur)conteneur1).add(tete1);
        ((RepresentableConteneur)conteneur1).add(cheveux1);
        conteneur1.setOrig(new Point3D(-1.0, 0.0, 0.0));

        // --- FEMME 2 : Plus petite, plus de formes, brune ---
        double height2 = 1.6;
        // Corps un peu plus large (rayon 0.4 au lieu de 0.3)
        Cylinder corps2 = new Cylinder(new Point3D(0.0, -height2/2, 0.0), new Point3D(0.0, height2/2, 0.0), 0.4);
        corps2.texture(new ColorTexture(Color.newCol(0.85f, 0.75f, 0.65f))); // Peau légèrement différente
        
        // Tête
        Sphere tete2 = new Sphere(new Point3D(0.0, height2/2 + 0.2, 0.0), 0.2);
        tete2.texture(new ColorTexture(Color.newCol(0.85f, 0.75f, 0.65f)));
        
        // Cheveux Bruns
        Sphere cheveux2 = new Sphere(new Point3D(0.0, height2/2 + 0.25, 0.0), 0.22);
        cheveux2.texture(new ColorTexture(Color.newCol(0.2f, 0.1f, 0.05f))); // Brun foncé

        // Positionnement de la Femme 2 à droite
        Representable conteneur2 = new RepresentableConteneur();
        ((RepresentableConteneur)conteneur2).add(corps2);
        ((RepresentableConteneur)conteneur2).add(tete2);
        ((RepresentableConteneur)conteneur2).add(cheveux2);
        conteneur2.setOrig(new Point3D(1.0, -0.2, 0.0)); // Décalée et plus basse car plus petite

        // Ajout à la scène
        scene().add(conteneur1);
        scene().add(conteneur2);

        // Configuration de la Caméra (Règle 8 : Vecteur UP explicite)
        Camera cam = new Camera(new Point3D(0.0, 0.0, 6.0), Point3D.O0, Point3D.Y);
        scene().cameraActive(cam);
    }

    @Override
    public void finit() {
        // Animation optionnelle : légère rotation
        for (Representable r : scene().getObjets().getData1d()) {
            // r.setVectX(...) pour rotation si besoin
        }
    }

    public static void main(String[] args) {
        FemmesScene test = new FemmesScene();
        test.loop(true);
        test.setMaxFrames(1); // Pour une image unique
        test.setPublish(true);
        new Thread(test).start();
    }
}