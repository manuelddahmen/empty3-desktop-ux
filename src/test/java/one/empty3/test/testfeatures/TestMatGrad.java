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

package one.empty3.test.testfeatures;


import one.empty3.apps.feature.*;
import one.empty3.feature.M3;
import one.empty3.feature.WriteFile;
import one.empty3.libs.Image;
import one.empty3.feature.PixM;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.logging.Logger;

public class TestMatGrad {
    static Logger logger;

    static {
        logger

                = Logger.getLogger(TestMatGrad.class.getName());

    }

    @Test
    public void testMatGradAndDotProduct() {
        for (String fileStr : new File("resources").list()) {
            logger.info("start with " + fileStr);
            if (!fileStr.endsWith(".jpg"))
                continue;
            File file = new File("resources/" + fileStr);
            one.empty3.feature.PixM pixMOriginal = null;
            try {
                pixMOriginal = PixM.getPixM((Image)(Image.getFromFile(file.getAbsoluteFile())), 500.0);
            } catch (Exception ex) {
                ex.printStackTrace();
                continue;
                // assertTrue(false);

            }
            logger.info("file loaded");
            GradientFilter gradientMask = new GradientFilter(pixMOriginal.getColumns(), pixMOriginal.getLines());
            M3 imgForGrad = new M3(pixMOriginal, 2, 2);
            M3 filter = gradientMask.filter(imgForGrad);
            one.empty3.feature.PixM[][] imagesMatrix = filter.getImagesMatrix();//.normalize(0, 1);
            logger.info("gradient computed");

//                    image1 = null;

            // Zero. +++Zero orientation variation.
            Linear linear = new Linear(imagesMatrix[1][0], imagesMatrix[0][0],
                    new one.empty3.feature.PixM(pixMOriginal.getColumns(), pixMOriginal.getLines()));
            linear.op2d2d(new char[]{'*'}, new int[][]{{1, 0}}, new int[]{2});
            one.empty3.feature.PixM smoothedGrad = linear.getImages()[2].normalize(0., 1.);
            logger.info("dot outter product");
            one.empty3.feature.PixM pext = pixMOriginal;
            LocalExtrema le =
                    new LocalExtrema(imagesMatrix[1][0].getColumns(),
                            imagesMatrix[1][0].getLines(),
                            3, 0);
            one.empty3.feature.PixM plext = le.filter(new M3(pext,
                    1, 1)
            ).getImagesMatrix()[0][0].normalize(0., 1.);
            logger.info("local maximum");


            pext = pixMOriginal;
            LocalExtrema le2 =
                    new LocalExtrema(imagesMatrix[1][0].getColumns(),
                            imagesMatrix[1][0].getLines(),
                            5, 0);
            one.empty3.feature.PixM plext2 = le2.filter(new M3(pext,
                    1, 1)
            ).getImagesMatrix()[0][0].normalize(0., 1.);
            logger.info("local maximum 5x5");


            LocalExtrema le3 =
                    new LocalExtrema(imagesMatrix[1][0].getColumns(),
                            imagesMatrix[1][0].getLines(),
                            19, 3);
            one.empty3.feature.PixM plext3 = le3.filter(new M3(pext,
                    1, 1)
            ).getImagesMatrix()[0][0].normalize(0., 1.);
            logger.info("local maximum 20x20");


            AfterGradientBeforeExtremum a
                    = new AfterGradientBeforeExtremum(3);
            M3 anglesTangente = a.filter(new M3(

                    new one.empty3.feature.PixM[][]
                            {{
                                    pext, imagesMatrix[0][0], imagesMatrix[1][0]
                            }}
            ));
            logger.info("angles tangentes");
            one.empty3.feature.PixM pix = smoothedGrad;
            IntuitiveRadialGradient i
                    = new IntuitiveRadialGradient(pix);
            i.setMax(2., 5., 2, 4);
            PixM rad = i.filter(pix);
            logger.info("radial orientation");
            WriteFile.writeNext("reduite" + file.getName(), new Image(pixMOriginal.normalize(0., 1.).getImage()));
            WriteFile.writeNext("gradient gx" + file.getName(), new Image(imagesMatrix[0][0].normalize(0., 1.).getImage()));
            WriteFile.writeNext("gradient gy" + file.getName(), new Image(imagesMatrix[1][0].normalize(0., 1.).getImage()));
            WriteFile.writeNext("gradient phase x" + file.getName(), new Image(imagesMatrix[0][1].normalize(0., 1.).getImage()));
            WriteFile.writeNext("gradient phase y" + file.getName(), new Image(imagesMatrix[1][1].normalize(0., 1.).getImage()));
            WriteFile.writeNext("gradients dot" + file.getName(), new Image(smoothedGrad.normalize(0., 1.).getImage()));
            WriteFile.writeNext("extrema 3x3" + file.getName(), new Image(plext.normalize(0., 1.).getImage()));
            WriteFile.writeNext("angles" + file.getName(), new Image(anglesTangente.getImagesMatrix()[0][0].normalize(0., 1.).getImage()));
            WriteFile.writeNext("radial grad" + file.getName(), new Image(rad.normalize(0., 1.).getImage()));
            WriteFile.writeNext("extrema 5x5" + file.getName(), new Image(plext2.normalize(0., 1.).getImage()));
            WriteFile.writeNext("extrema 19x19" + file.getName(), new Image(plext3.normalize(0., 1.).getImage()));
            System.gc();
        }

    }
}