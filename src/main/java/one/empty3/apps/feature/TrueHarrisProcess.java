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

import one.empty3.feature.*;


import javax.imageio.ImageIO;

import one.empty3.feature.PixM;
import one.empty3.apps.feature.TrueHarris;
import one.empty3.io.ProcessFile;

import java.io.File;
import java.awt.image.BufferedImage;

public class TrueHarrisProcess extends ProcessFile {

    public boolean process(File in, File out) {

        if (!in.getName().endsWith(".jpg"))

            return false;

        File file = in;

        one.empty3.feature.PixM pix = null;
        BufferedImage img = null;

        try {
            img = ImageIO.read(file);
            pix = one.empty3.feature.PixM.getPixM(img, maxRes);

        } catch (Exception ex) {

            ex.printStackTrace();

            return false;

            // assertTrue(false);


        }

        one.empty3.apps.feature.TrueHarris th = new TrueHarris(pix);
        one.empty3.feature.PixM pixM = new one.empty3.feature.PixM(pix.getImage());
        for (int c = 0; c < 3; c++) {
            th.setCompNo(c);
            pixM.setCompNo(c);
            pix.setCompNo(c);
            for (int i = 0; i < pix.getColumns(); i++)
                for (int j = 0; j < pix.getLines(); j++)
                    pixM.set(i, j, th.filter(i, j));
        }


        PixM normalize = pixM.normalize(0.0, 1.0);


        //


        try {

            ImageIO.write(normalize.getImage(), "JPEG", out);
            return true;
        } catch (Exception ex) {

        }
        return true;
    }

} 
