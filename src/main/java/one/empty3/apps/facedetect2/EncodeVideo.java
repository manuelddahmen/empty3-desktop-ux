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

package one.empty3.apps.facedetect2;

import one.empty3.apps.facedetect.DistanceProxLinear2;
import one.empty3.apps.feature.app.replace.javax.imageio.ImageIO;
import one.empty3.library.Config;
import one.empty3.library.P;
import one.empty3.library.Point3D;
import one.empty3.library.core.testing.jvm.Resolution;
import one.empty3.libs.Image;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EncodeVideo {
    private String subDirectory;
    private String suffix;
    private BufferedImage bufferedImage1;
    private BufferedImage bufferedImage2;
    protected File[] filesOut;
    private HashMap<String, Point3D> a;
    private HashMap<String, Point3D> b;

    public EncodeVideo(String subDirectory, String suffix) throws IOException {
        this.subDirectory = subDirectory + suffix;
        this.suffix = suffix;
        init();
    }

    public void init() throws IOException {
        Config config = new Config();
        String folderWorkingDirectoryFaceDetectApp = config.getMap().get("folderWorkingDirectoryFaceDetectApp");


        File fileOut = new File(folderWorkingDirectoryFaceDetectApp + File.separator + subDirectory + File.separator + "out");
        File fileIn1 = new File(folderWorkingDirectoryFaceDetectApp + File.separator + subDirectory + File.separator + "in1");
        File fileIn2 = new File(folderWorkingDirectoryFaceDetectApp + File.separator + subDirectory + File.separator + "in2");

        if (!fileOut.exists() || !fileIn1.exists() || fileIn2.exists()) {
            fileIn1.mkdirs();
            fileIn2.mkdirs();
            fileOut.mkdirs();
        }
        System.out.println("dir out  : " + fileOut.getAbsolutePath());
        System.out.println("dir in movie 1  : " + fileIn1.getAbsolutePath());
        System.out.println("dir in movie 2  : " + fileIn2.getAbsolutePath());

        if (fileIn1.exists() && fileIn2.isDirectory() && fileOut.isDirectory()) {
            File[] files1 = fileIn1.listFiles();
            File[] files2 = fileIn2.listFiles();
            File[] filesIn1 = fileIn1.listFiles();
            File[] filesIn2 = fileIn2.listFiles();
            boolean cont;
            if (fileIn1.length() > 1) {
                cont = true;
            } else {
                cont = false;
            }
            boolean boolean2jpg = false, boolean2txt = false;
            boolean boolean1jpg = false, boolean1txt = false;
            File fImage1 = null;
            File fImage2 = null;
            File txtIn1 = null;
            File txtIn2 = null;
            int i2 = 0;
            for (int i1 = 0; i1 < files2.length; i1++) {
                File fTest = null;
                while (!boolean1jpg || !boolean1txt || !boolean2jpg || !boolean2txt) {
                    if (!boolean1jpg) {
                        fTest = nextOfX(filesIn1, i1, ".jpg");
                        if (fTest != null) {
                            fImage1 = fTest;
                            boolean1jpg = true;
                            if (boolean1txt)
                                i2++;
                        }
                    }
                    if (!boolean1txt) {
                        fTest = nextOfX(filesIn1, i1, ".txt");
                        if (fTest != null) {
                            txtIn1 = fTest;
                            boolean1txt = true;

                        }
                    }
                    if (!boolean2jpg)
                        fTest = nextOfX(filesIn2, i1, ".jpg");
                    if (fTest != null) {
                        fImage2 = fTest;
                        boolean2jpg = true;
                    }
                    if (!boolean2txt) {
                        fTest = nextOfX(filesIn2, i1, ".txt");
                        if (fTest != null) {
                            txtIn2 = fTest;
                            boolean2txt = true;

                        }
                    }
                    fTest = null;
                }
                boolean2jpg = false;
                boolean2txt = false;
                boolean1jpg = false;
                boolean1txt = false;

                File file1 = (cont && files2.length < i1) ? files2[i1] : files2[files2.length - 1];
                if (files2[i1].isFile()) {
                    File fOut = new File(fileOut.getAbsolutePath() + File.separator + fImage2.getName());
                    try {
                        bufferedImage1 = javax.imageio.ImageIO.read(fImage1);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        bufferedImage2 = javax.imageio.ImageIO.read(fImage2);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    a = new HashMap<String, Point3D>();
                    b = new HashMap<String, Point3D>();

                    System.out.println("Frame                  #" + i1);
                    System.out.println("Image movie to insert  :" + fImage1.getAbsolutePath() + " exists ?" + fImage1.exists());
                    System.out.println("Image movie            :" + fImage2.getAbsolutePath() + " exists ?" + fImage2.exists());
                    System.out.println("Image text 1           :" + txtIn1.getAbsolutePath() + " exists ?" + txtIn1.exists());
                    System.out.println("Image text 2           :" + txtIn2.getAbsolutePath() + " exists ?" + txtIn2.exists());
                    System.out.println("Image out              :" + fOut.getAbsolutePath() + " exists ?" + fOut.exists());

                    // réduction de la taille pour vitesse
                    //try {
                    //    bufferedImage2 = resizeImage(bufferedImage2, 256, 256);
                    //} catch (IOException e) {
                    //    throw new RuntimeException(e);
                    //}


                    a = initX(txtIn1, a, bufferedImage1);
                    b = initX(txtIn2, b, bufferedImage2);


                    bufferedImage1 = resizeImage(bufferedImage1, 150, a);
                    bufferedImage2 = resizeImage(bufferedImage2, 150, b);

                    TextureMorphMoveImageAimagesB texture = new TextureMorphMoveImageAimagesB(bufferedImage1, bufferedImage2, DistanceProxLinear2.class, new Resolution(bufferedImage2.getWidth(), bufferedImage2.getWidth()),
                            a.values().stream().toList(), b.values().stream().toList());

                    FrameEncoding frameEncoding = new FrameEncoding(this, bufferedImage1, bufferedImage2, a, b, fOut, texture);
                    BufferedImage bufferedImage = frameEncoding.execute();
                    ImageIO.write(bufferedImage, "jpg", fOut);
                }
            }
        }
    }

    private File nextOfX(File[] filesX, int indexX, String extension) {
        while (indexX < filesX.length) {
            File fileX = filesX[indexX];
            if ((fileX != null) && (fileX.getAbsolutePath().endsWith(extension)))
                return fileX;
            indexX++;
        }
        return null;
    }


    public HashMap<String, Point3D> initX(@NotNull File txtX, @NotNull HashMap<String, Point3D> xx, @NotNull BufferedImage refSize) {
        Scanner bufferedReader = null;
        try {
            bufferedReader = new Scanner(new FileReader(txtX));
            String line = "";
            while (bufferedReader.hasNextLine()) {
                line = bufferedReader.nextLine();
                Point3D point = new Point3D();
                String landmarkType;
                double x;
                double y;
                if (!line.isEmpty()) {
                    if (Character.isLetter(line.charAt(0))) {
                        landmarkType = line;
                        // X
                        line = bufferedReader.nextLine();
                        x = Double.parseDouble(line);
                        // Y
                        line = bufferedReader.nextLine();
                        y = Double.parseDouble(line);
                        // Blank line
                        line = bufferedReader.nextLine();

                        xx.put(landmarkType, new Point3D(x / refSize.getWidth(), y / refSize.getHeight(), 0.0));
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Logger.getAnonymousLogger().log(Level.INFO, "Loaded {0} points in image", xx.size());
        bufferedReader.close();


        return xx;
    }

    public static void main(String[] args) {
        String suffix = "_1";
        String fileMp4originalName = "20240724_101828.zip";
        if (args.length >= 1) {
            fileMp4originalName = args[0];
        }
        try {
            EncodeVideo encodeVideo = new EncodeVideo(fileMp4originalName, suffix);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    BufferedImage resizeImage(BufferedImage originalImage, int maxRes, HashMap<String, Point3D> a) throws IOException {
        a.forEach(new BiConsumer<String, Point3D>() {
            @Override
            public void accept(String s, Point3D p) {
                a.put(s, p.multDot(P.n((double) maxRes / originalImage.getWidth(),
                        (double) maxRes / originalImage.getHeight(), 0.0)));
            }
        });
        double targetWidth = 1.0 * maxRes / originalImage.getWidth();
        double targetHeight = 1.0 * maxRes / originalImage.getHeight();
        BufferedImage resizedImage = new BufferedImage((int) (originalImage.getWidth() * targetWidth), (int) (originalImage.getHeight() * targetHeight), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage((Image) originalImage, 0,
                0, (int) targetWidth, (int) targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }
}
