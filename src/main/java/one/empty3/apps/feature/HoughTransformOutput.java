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


import one.empty3.apps.feature.CircleHit;

import javax.imageio.ImageIO;

import one.empty3.library.Lumiere;
import one.empty3.library.Point;
import one.empty3.libs.*;
import one.empty3.libs.Color;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class HoughTransformOutput {

    private static String path = System.getProperty("user.dir");
    static int drawnCircles = 1;
    static int imgWidth;
    static int imgHeight;

    public static void writeImage(int[][] imgArray, File outFile) throws Exception {
        BufferedImage img = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < imgWidth; i++) {
            for (int j = 0; j < imgHeight; j++) {
                img.setRGB(i, j, new Color(Lumiere.getIntFromInts(255, 255, imgArray[i][j])).getRGB());
            }
        }
        new one.empty3.libs.Image(img).saveFile( outFile);
    }

    public static void writeImage(double[][] imgArray, File outFile) throws Exception {
        BufferedImage img = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_BYTE_GRAY);
        for (int i = 0; i < imgWidth; i++) {
            for (int j = 0; j < imgHeight; j++) {
                img.setRGB(i, j, (int) imgArray[i][j]);
            }
        }
        new one.empty3.libs.Image(img).saveFile( outFile);
    }

    public static void writeImage(BufferedImage image, File outFile) throws Exception {
        new one.empty3.libs.Image(image).saveFile(outFile);
    }

    public static void writeImage(double[][] sobelArray, File outFile, int threshold) throws Exception {
        BufferedImage img = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_BYTE_GRAY);
        for (int i = 0; i < imgWidth; i++) {
            for (int j = 0; j < imgHeight; j++) {
                if (sobelArray[i][j] > threshold) {
                    img.setRGB(i, j, Color.WHITE.getRGB());
                }
            }
        }
        new one.empty3.libs.Image(img).saveFile(outFile);
    }

    public static void superimposeCircles(List<one.empty3.apps.feature.CircleHit> hits, BufferedImage in, File out) {
        Graphics2D g = in.createGraphics();
        g.setColor(Color.RED);
        for (int circles = 0; circles < drawnCircles; circles++) {
            one.empty3.apps.feature.CircleHit toDraw = hits.get(circles);
            double a = toDraw.x - toDraw.r * Math.cos(0 * Math.PI / 180);
            double b = toDraw.y - toDraw.r * Math.sin(90 * Math.PI / 180);
            g.drawOval((int) a, (int) b, 2 * toDraw.r, 2 * toDraw.r);
            if (circles < 10) {
                System.out.printf("Circle #%d\n", circles);
                System.out.printf("X: %d\n", toDraw.x);
                System.out.printf("Y: %d\n", toDraw.y);
                System.out.printf("R: %d\n", toDraw.r);
            }
        }
        try {
            new one.empty3.libs.Image(in).saveFile( out);

        } catch (Exception ex) {
        }

    }

    public static void superimposeCircles(List<one.empty3.apps.feature.CircleHit> hits, double[][] sobelTotal, File out) throws Exception {
        BufferedImage totalCircles = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);

        BufferedImage total = changeBrightness(0.5f, scaledSobelResult(sobelTotal));
        totalCircles.getGraphics().drawImage(total, 0, 0, null);
        Graphics2D g = totalCircles.createGraphics();
        g.setColor(Color.RED);
        for (int circles = 0; circles < drawnCircles; circles++) {
            one.empty3.apps.feature.CircleHit toDraw = hits.get(circles);
            double a = toDraw.x - toDraw.r * Math.cos(0 * Math.PI / 180);
            double b = toDraw.y - toDraw.r * Math.sin(90 * Math.PI / 180);
            g.drawOval((int) a, (int) b, 2 * toDraw.r, 2 * toDraw.r);
            if (circles < 10) {
                System.out.printf("Circle #%d\n", circles);
                System.out.printf("X: %d\n", toDraw.x);
                System.out.printf("Y: %d\n", toDraw.y);
                System.out.printf("R: %d\n", toDraw.r);
            }
        }

        new one.empty3.libs.Image(totalCircles).saveFile( out);
    }

    public static BufferedImage scaledSobelResult(double[][] sobelTotal) {
        BufferedImage total = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_BYTE_GRAY);
        double max = 0;
        for (int i = 0; i < imgWidth; i++) {
            for (int j = 0; j < imgHeight; j++) {
                if (sobelTotal[i][j] > max) {
                    max = sobelTotal[i][j];
                }
            }
        }
        for (int i = 0; i < imgWidth; i++) {
            for (int j = 0; j < imgHeight; j++) {
                //maps every pixel to a grayscale value between 0 and 255 from between 0 and the max value in sobelTotal
                int rgb = new Color(Lumiere.getIntFromInts((int) map(sobelTotal[i][j], 0, max, 0, 255),
                        (int) map(sobelTotal[i][j], 0, max, 0, 255),
                        (int) map(sobelTotal[i][j], 0, max, 0, 255))).getRGB();
                total.setRGB(i, j, rgb);
            }
        }
        return changeBrightness(20.0f, total);
    }

    //maps the given value between startCoord1 and endCoord1 to a value between startCoord2 and endCoord2
    public static double map(double valueCoord1,
                             double startCoord1, double endCoord1,
                             double startCoord2, double endCoord2) {


        double ratio = (endCoord2 - startCoord2) / (endCoord1 - startCoord1);
        return ratio * (valueCoord1 - startCoord1) + startCoord2;
    }


    //changes the brightness of an image by the factor given
    private static BufferedImage changeBrightness(float brightenFactor, BufferedImage image) {
        RescaleOp op = new RescaleOp(brightenFactor, 0, null);
        image = op.filter(image, image);
        return image;
    }
}