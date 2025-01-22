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

package one.empty3.apps.feature.tryocr;

import one.empty3.apps.feature.Linear;
import one.empty3.feature.PixM;
import one.empty3.apps.feature.app.replace.javax.imageio.ImageIO;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CharacterIsolationMatching extends Thread {
    File directoryIn;
    File fileOut;
    private int sumLinesI;

    public CharacterIsolationMatching(File directoryIn, File fileOut) {
        this.directoryIn = directoryIn;
        this.fileOut = fileOut;
    }

    public void run() {
        final int[] sumColumns = {0};
        final int[] sumLines = {0};

        File directoryOut = this.fileOut.getParentFile();

        PixM result = new PixM(5000, 5000);
        File[] files = directoryIn.listFiles();
        if (files != null) {
            Arrays.stream(Objects.requireNonNull(files)).filter(file -> file.getAbsolutePath().toLowerCase().endsWith(".jpg")).forEach(file1 -> {
                sumLinesI = 40;
                Arrays.stream(Objects.requireNonNull(directoryIn.listFiles())).filter(file -> file.getAbsolutePath().toLowerCase().endsWith(".jpg")).forEach(file2 -> {
                    if (!file1.equals(file2)) {
                        PixM file1m = new PixM(Objects.requireNonNull(ImageIO.read(file1)));
                        PixM file2m = new PixM(Objects.requireNonNull(ImageIO.read(file1)));

                        PixM f1 = new PixM(Math.max(file1m.getColumns(), file2m.getColumns()),
                                Math.max(file1m.getLines(), file2m.getLines()));
                        PixM f2 = new PixM(Math.max(file1m.getColumns(), file2m.getColumns()),
                                Math.max(file1m.getLines(), file2m.getLines()));
                        f1.pasteSubImage(file1m, 0, 0, f1.getColumns(), f1.getLines());
                        f2.pasteSubImage(file2m, 0, 0, f2.getColumns(), f2.getLines());

                        Linear linear = new Linear(f1, f2, new PixM(f1.getColumns(), f2.getLines()));

                        linear.op2d2d(new char[]{'+'}, new int[][]{{0, 1}}, new int[]{2});

                        PixM sum = linear.getImages()[2];

                        linear = new Linear(f1, f2, new PixM(f1.getColumns(), f2.getLines()));

                        linear.op2d2d(new char[]{'-'}, new int[][]{{0, 1}}, new int[]{2});

                        PixM diff = linear.getImages()[2];

                        Logger.getAnonymousLogger().log(Level.INFO, "Logged");

                        result.pasteSubImage(diff, sumColumns[0], sumLines[0], diff.getColumns(), sumLines[0]);
                        sumColumns[0] += diff.getColumns();
                        result.pasteSubImage(sum, sumColumns[0], sumLines[0], sum.getColumns(), sumLines[0]);
                        sumColumns[0] += sum.getColumns();

                        linear = new Linear(f1, f2, new PixM(f1.getColumns(), f2.getLines()));


                        sumLinesI = Math.max(sumLinesI, diff.getLines());
                        sumLinesI = Math.max(sumLinesI, sum.getLines());
                    }
                });
                sumLines[0] += sumLinesI;
            });

            ImageIO.write(result.normalize(0, 1).getImage(), "jpg", this.fileOut);
        }
    }
}
