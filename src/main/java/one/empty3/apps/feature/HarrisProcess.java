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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class HarrisProcess extends ProcessFile {
    public boolean process(File in, File out) {
        try {
            BufferedImage img = ImageIO.read(in);
            PixM m2 = PixM.getPixM(img, maxRes);
            HarrisToPointInterest h = new HarrisToPointInterest(2, 2);

            m2.applyFilter(h);

            new one.empty3.libs.Image(m2.normalize(0.0, 1.0).getImage()).saveFile(out);

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }
}
