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
import one.empty3.library.core.lighting.Colors;
import one.empty3.io.ProcessFile;

import javax.imageio.ImageIO;

import one.empty3.libs.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import java.util.function.Predicate;

public class ExtractIntensityInfo extends
        ProcessFile {
    int colorsLevels = 15;


    public ExtractIntensityInfo() {
    }


    @Override
    public boolean process(File in, File out) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(in);
        } catch (Exception rx) {
        }
        PixM pix = PixM.getPixM(img, -1);


        PixM pixMOriginal = pix;

        final BufferedImage[] img3 = new BufferedImage[]{pix.getImage()};


        one.empty3.apps.feature.GradientFilter gradientMask = new GradientFilter(pixMOriginal.getColumns()
                , pixMOriginal.getLines());
        one.empty3.feature.M3 imgForGrad = new one.empty3.feature.M3(pixMOriginal,
                2, 2);
        M3 filter = gradientMask.filter(imgForGrad);
        PixM[][] imagesMatrix = filter.getImagesMatrix();//.normalize(0, 1);


//                    image1 = null;

        // Zero. +++Zero orientation variation.
        one.empty3.apps.feature.Linear linear = new Linear(imagesMatrix[1][0], imagesMatrix[0][0],
                new PixM(pixMOriginal.getColumns()
                        , pixMOriginal.getLines()));
        linear.op2d2d(new char[]{'*'}, new int[][]{{1, 0}}, new int[]{2});
        PixM smoothedGrad = linear.getImages()[2];


        double min = 0.3;
        double rMin = 2.0;
        // varier rMin et min

        PixM pix2 = smoothedGrad.copy();

        one.empty3.apps.feature.Histogram2 histogram = new one.empty3.apps.feature.Histogram2(15);
        histogram.setM(pix2);
        List<one.empty3.apps.feature.Histogram2.Circle> pointsOfInterest = histogram.getPointsOfInterest(rMin);


        double isumtot[] = new double[]{0, 0};
        double[] iSum = {0.0, 0.0, 1.0};
        pointsOfInterest.stream().filter(new Predicate<one.empty3.apps.feature.Histogram2.Circle>() {
            @Override
            public boolean test(one.empty3.apps.feature.Histogram2.Circle circle) {
                iSum[0] += circle.i;
                iSum[1] = Math.min(circle.i, iSum[1]);
                iSum[2] = Math.max(circle.i, iSum[2]);

                return true;
            }
        }).forEach(circle -> {
            circle.i = (circle.i - iSum[1]) / (iSum[2] - iSum[1]);
            isumtot[0] += circle.i;
            isumtot[1] += circle.r;
        });
        Color[] colors = new Color[colorsLevels];
        for (int i = 0; i < colors.length; i++)
            colors[i] = new Colors().random();

        double finalMin = min;
        pointsOfInterest.stream().filter(new Predicate<one.empty3.apps.feature.Histogram2.Circle>() {
            @Override
            public boolean test(Histogram2.Circle circle) {
                return circle.i > isumtot[0] / pix.getColumns()
                        / pix.getLines();
            }
        }).forEach(circle -> {
            //Logger.getAnonymousLogger().log(Level.INFO, circle.toString());
            pix2.setCompNo(0);

            pix2.set((int) circle.x, (int) circle.y, 1.0);
            pix2.setCompNo(2);
            pix2.set((int) circle.x, (int) circle.y, circle.r);
            Color color = colors[(int) ((circle.i - iSum[1]) / (iSum[2] - iSum[1])
                    * colorsLevels - 1)];
                  /*  Graphics graphics = img3[0].getGraphics();
                    graphics.setColor(color);
                    graphics.drawRect((int) (circle.x-10), (int) (circle.y-10), (int) (10), (int) (10));
*/
        });


        pix2.normalize(0.0, 1.0);

        //


        try {
            pix.getImage2().saveFile( out);
        } catch (Exception ex) {
        }


        return true;


    }

}
