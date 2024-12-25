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
/*
package one.empty3.feature.Masks;

import one.empty3.apps.feature.app.replace.javax.imageio.ImageIO;
import one.empty3.io.ProcessFile;
import one.empty3.tests.humanheadtexturing.TestHumanHeadTexturing;

import one.empty3.library.Point;
import one.empty3.libs.*;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import java.util.Scanner;

public class Mask1 extends ProcessFile {
    private File mask = new File("resources/models/heads/__SERID_0__frame1000005.JPG");
    private File maskCordinates = new File("resources/models/heads/__SERID_0__frame1000005.JPG.cordinates");
    private int listIntPolygonCount;
    private List<Integer> listIntPolygon = new ArrayList<Integer>();

    public Mask1() {
        try {
            BufferedImage maskBrut = ImageIO.read(mask);
            Scanner scanner = null;
            scanner = new Scanner(maskCordinates);
            scanner.useDelimiter("\n");
            scanner.next();

            TestHumanHeadTexturing testHumanHeadTexturing = new TestHumanHeadTexturing();
            testHumanHeadTexturing.ginit();
            Rectangle rectangleFace = testHumanHeadTexturing.getRectangleFace();
            while (scanner.hasNext()) {
                listIntPolygonCount++;
                listIntPolygon.add(Integer.getInteger(scanner.next()));
            }
            // Read .fac face info
            // Map face on mask with coordinate ex.
            // new Dimension( Max(width1,width2), Math.max(height1, height2))

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean process(File in, File out) {
        return false;
    }
}
*/