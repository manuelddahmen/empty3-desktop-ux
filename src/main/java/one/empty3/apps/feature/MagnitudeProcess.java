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


import one.empty3.feature.M3;
import one.empty3.feature.PixM;
import one.empty3.io.ProcessFile;

import javax.imageio.ImageIO;
import java.io.File;

/*
 * Magnitude² filter
 */
public class MagnitudeProcess extends ProcessFile {

    public boolean process(File in, File out) {

        if (!in.getName().endsWith(".jpg"))
            return false;
        File file = in;
        PixM pix;
        try {
            pix = PixM.getPixM(ImageIO.read(file), maxRes);
            one.empty3.apps.feature.GradientFilter gf = new GradientFilter(pix.getColumns(),
                    pix.getLines());
            PixM[][] imagesMatrix = gf.filter(
                    new M3(
                            pix, 2, 2)
            ).getImagesMatrix();
            one.empty3.apps.feature.Linear linearProd1 = new one.empty3.apps.feature.Linear(imagesMatrix[0][0], imagesMatrix[0][0],
                    new PixM(pix.getColumns(), pix.getLines()));
            linearProd1.op2d2d(new char[]{'*'}, new int[][]{{1, 0}}, new int[]{2});
            one.empty3.apps.feature.Linear linearProd2 = new one.empty3.apps.feature.Linear(imagesMatrix[0][1], imagesMatrix[0][1],
                    new PixM(pix.getColumns(), pix.getLines()));
            linearProd2.op2d2d(new char[]{'*'}, new int[][]{{1, 0}}, new int[]{2});
            one.empty3.apps.feature.Linear res = new Linear(linearProd1.getImages()[2], linearProd2.getImages()[2],
                    new PixM(pix.getColumns(), pix.getLines()));
            res.op2d2d(new char[]{'+'}, new int[][]{{1, 0}}, new int[]{2});
            new one.empty3.libs.Image(res.getImages()[2].normalize(0.0, 1.0).getImage()).saveFile( out);

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
