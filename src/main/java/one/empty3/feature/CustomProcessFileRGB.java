/*
 *
 *  *
 *  *  * Copyright (c) 2025. Manuel Daniel Dahmen
 *  *  *
 *  *  *
 *  *  *    Copyright 2024 Manuel Daniel Dahmen
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

package one.empty3.feature;

import one.empty3.io.ProcessFile;
import one.empty3.library.Lumiere;
import one.empty3.library.StructureMatrix;
import one.empty3.library1.shader.Vec;
import one.empty3.library1.tree.ListInstructions;
import one.empty3.feature.PixM;
import one.empty3.libs.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * $
 * r=0
 * g=1
 * b=0
 */
public class CustomProcessFileRGB extends ProcessFile {
    private static TextDialog textDialog;
    ListInstructions listInstructions;

    public CustomProcessFileRGB() {
        super();
        textDialog = TextDialog.getInstance();
        textDialog.setVisible(true);

    }

    @Override
    public boolean process(final File in, final File out) {
        one.empty3.feature.PixM pixIn;
        if (isImage(in)) {
            BufferedImage readIn = null;
            try {
                readIn = ImageIO.read(in);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            pixIn = one.empty3.feature.PixM.getPixM(new Image(readIn), maxRes);
            one.empty3.feature.PixM pix = pixIn;
            PixM pixOut = pixIn.copy();
            HashMap<String, String> currentVecs = new HashMap<>();
            HashMap<String, Double> currentVars = new HashMap<>();
            HashMap<String, StructureMatrix<Double>> currentVecsComputed = new HashMap<>();

            for (int i = 0; i < pix.getColumns(); i++) {
                for (int j = 0; j < pix.getLines(); j++) {
                    try {
                        double r, g, b;
                        double[] values = pixOut.getValues(i, j);
                        r = values[0];
                        g = values[1];
                        b = values[2];
                        double x = i;
                        double y = j;
                        Vec rgbValOrig = new Vec(r, g, b);

                        listInstructions = new ListInstructions();

                        listInstructions.setCurrentParamsValues(currentVars);
                        listInstructions.setCurrentParamsValuesVec(currentVecs);
                        listInstructions.setCurrentParamsValuesVecComputed(currentVecsComputed);
                        listInstructions.addInstructions(textDialog.getText());
                        currentVars.put("r", r);
                        currentVars.put("g", g);
                        currentVars.put("b", b);
                        currentVars.put("x", (double) i);
                        currentVars.put("y", (double) j);
                        currentVecs.put("rgb", (String.format("(%f,%f,%f)", r, g, b)));


                        listInstructions.runInstructions();

                        r = currentVars.getOrDefault("r", r);// (new StructureMatrix<Double>(0, Double.class).setElem(r))).getElem();
                        g = currentVars.getOrDefault("g", r);// (new StructureMatrix<Double>(0, Double.class).setElem(g))).getElem();
                        b = currentVars.getOrDefault("b", r);// (new StructureMatrix<Double>(0, Double.class).setElem(b))).getElem();
                        x = currentVars.getOrDefault("x", (double) i);// (new StructureMatrix<Double>(0, Double.class).setElem(g))).getElem();
                        y = currentVars.getOrDefault("y", (double) j);// (new StructureMatrix<Double>(0, Double.class).setElem(b))).getElem();
                        pixOut.set(pixOut.index((int) x, (int) y), Lumiere.getInt(r, g, b));
                    } catch (RuntimeException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            assert pixOut != null;
            pixOut.getImage().saveFile( out);
            assert out.exists();
            Logger.getAnonymousLogger().log(Level.INFO, "Image written" + out.getAbsolutePath());
            return true;
        }
        return false;
    }

    public ListInstructions getListInstructions() {
        return listInstructions;
    }

    public void setListInstructions(ListInstructions listInstructions) {
        this.listInstructions = listInstructions;
    }
}
