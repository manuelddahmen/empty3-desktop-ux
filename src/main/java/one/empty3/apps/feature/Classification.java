/*
 *
 *  * Copyright (c) 2024. Manuel Daniel Dahmen
 *  *
 *  *
 *  *    Copyright 2024 Manuel Daniel Dahmen
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 *
 *
 */

package one.empty3.apps.feature;


import one.empty3.feature.PixM;
import one.empty3.io.ProcessFile;
import one.empty3.library.Lumiere;
import one.empty3.library.core.lighting.Colors;

import javax.imageio.ImageIO;

import one.empty3.libs.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

public class Classification extends ProcessFile {
    Random random = new Random();
    private BufferedImage imageOut;
    private int SIZE = 5;
    private double ratio = 0.3;
    private double threshold = 0.3;


    @Override
    public boolean process(final File in, final File out) {
        if (!in.getName().endsWith(".jpg"))
            return false;
        PixM selectPointColorMassAglo = null;
        BufferedImage read = null;
        try {
            read = ImageIO.read(in);
            selectPointColorMassAglo = PixM.getPixM(read, maxRes);
        } catch (Exception ex) {
        }

        try {
            imageOut = ImageIO.read(in);
        } catch (Exception ex) {
        }

        assert selectPointColorMassAglo != null;
        SelectPointColorMassAglo selectPointColorMassAglo1 = new SelectPointColorMassAglo(read);
        int color = Color.WHITE.getRGB();
        for (int i = 0; i < selectPointColorMassAglo1.getColumns(); i += 1)
            for (int j = 0; j < selectPointColorMassAglo1.getLines(); j += 1) {
                selectPointColorMassAglo1.setTmpColor(new Colors().random());
                double v = selectPointColorMassAglo1.averageCountMeanOf(i, j, SIZE, SIZE, threshold);
                if (v > ratio) {
                    imageOut.setRGB(i, j, color);/*selectPointColorMassAglo.getChosenColor().getRGB()*/
                } else {
                    double[] doubles = Lumiere.getDoubles(read.getRGB(i, j));
                    /*for(int c=0; c<3; c++)
                        doubles[c] = doubles[c]/3;
*/
                    imageOut.setRGB(i, j, Lumiere.getInt(doubles));
                }
            }

        try {
            new one.empty3.libs.Image(imageOut).saveFile( out);
        } catch (Exception ex) {
            return false;
        }

        return true;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }
}
