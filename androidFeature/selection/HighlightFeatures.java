/*
 * Copyright (c) 2024.
 *
 *
 *  Copyright 2012-2023 Manuel Daniel Dahmen
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 */

package one.empty3.androidFeature.selection;


import java.io.File;
import java.util.List;

import one.empty3.ImageIO;
import one.empty3.io.ProcessFile;
import one.empty3.library.ITexture;
import one.empty3.library.Lumiere;
import one.empty3.feature.PixM;
import one.empty3.library.Point3D;
import one.empty3.library.Scene;
import one.empty3.library.core.nurbs.ParametricCurve;
import one.empty3.libs.Image;

public class HighlightFeatures extends ProcessFile {

    /*
     *
     * @param points Sélection de points
     * @param img Image sur laquelle dessiner
     * @param col Couleur ou texture de dessin
     */
    public void pasteList(List<Point3D> points, one.empty3.feature.PixM img, ITexture col) {

        for (int i = 0; i < points.size(); i++) {

            int x = (int) (double) points.get(i).getX();
            int y = (int) (double) points.get(i).getY();

            int rgb = col.getColorAt(
                    points.get(i).getX() / img.getColumns(),
                    points.get(i).getY() / img.getLines());

            double[] rgbD = Lumiere.getDoubles(rgb);
            for (int i1 = 0; i1 < 3; i1++) {
                img.setCompNo(i1);
                img.set(x, y, rgbD[i1]);
            }

        }

    }

    /*
     *
     * @param points Sélection de points
     * @param img Image sur laquelle dessiner
     * @param objets Objets à dessiner sur l'image (3d-2d)
     */
    public void pasteList(List<Point3D> points, one.empty3.feature.PixM img, Scene objets) {

        for (int i = 0; i < points.size(); i++) {

            int x = (int) (double) points.get(i).getX();
            int y = (int) (double) points.get(i).getY();

            objets.getObjets().getData1d().forEach(representable -> {
                if (representable instanceof ParametricCurve)
                    img.plotCurve((ParametricCurve) representable, representable.texture());///Clip curve for moving, rotating, scaling
                else if (representable instanceof Point3D) {
                    img.setValues((int) (double) ((Point3D) representable).getX(),
                            (int) (double) ((Point3D) representable).getY(),
                            Lumiere.getDoubles(representable.texture().getColorAt(0.5, 0.5)));
                }
            });

        }

    }

    public void openCsv() {

    }

    public void writeCsv(String filename, int xPercent, int yPercent, double... vector) {
    /*
        Misérables oeufs mignolet

        Te voilà jeté en freestyle
        Comme un steak à l 'ail
        J'aime les vaches
        et les viandes
        J'aime les femmes
        Et les comptes
        En banques que
        Je découpe tous
        à la hache
        Ma kekette
        Et mes boulets
        Au bouquet
        Des bollets
        Dans l'bosquet
        Des balais
        Déblayent
        Les lendemains
        De la fête
        A saints de putains
        Qui mont'ses hein
        Dans le purin
        Police politik
        Potiche pissin'
        Ca c'est drôle
        Drole de drame
        Dans l'émoi
        Des piques
        De Véronique
        La putain du diable
        Ses vérolaises
        Ah que
        Hackerdôme
        Dos au mur
        Eh c'est sûr
        Et sertis
        De pistolet
        Un anchois
        En chocolat
    */
    }

    public void closenCsv() {

    }

    @Override
    public boolean process(File in, File out) {
        Image read = ImageIO.read(in);
        PixM pixM = PixM.getPixM(read.getImage());
        File stackItem = getStackItem(1);
        one.empty3.feature.PixM original = new PixM(ImageIO.read(stackItem));
        int cadre = (int) Math.min((pixM.getColumns() + pixM.getLines()) / 2., 10.);
        for (int i = 0; i < pixM.getColumns(); i++)
            for (int j = 0; j < pixM.getLines(); j++) {
                double luminance = pixM.luminance(i, j);
                if (luminance > 0.1) {
                    for (int x = i - cadre; x < i + cadre; x++)
                        for (int y = j - cadre; y < j + cadre; y++) {
                            if ((x == i - cadre) || (y == j - cadre) || x == i + cadre - 1 || y == j + cadre - 1)
                                original.setValues(x, y, 1, 1, 0);
                        }
                }
            }
        original.normalize(0., 1., 0., 1.).getImage2().saveFile(out);

        return true;
    }
}
