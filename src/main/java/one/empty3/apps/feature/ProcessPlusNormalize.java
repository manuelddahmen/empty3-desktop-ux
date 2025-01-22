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


import one.empty3.matrix.PixM;
import one.empty3.apps.feature.app.replace.javax.imageio.ImageIO;
import one.empty3.io.ProcessNFiles;

import java.awt.Dimension;
import java.io.File;
import java.util.Objects;

public class ProcessPlusNormalize extends ProcessNFiles {


    @Override
    public boolean processFiles(File out, File... ins) {
        int n = ins.length;
        PixM[] arr = new PixM[n];
        Dimension max = new Dimension();
        for (int i = 0; i < ins.length; i++) {
            arr = new PixM[]{new PixM(Objects.requireNonNull(ImageIO.read(ins[i])))};
            if (arr[i].getColumns()
                    > max.width) {
                max.setSize(arr[i].getColumns()
                        , max.height);
            }
            if (arr[i].getLines() > max.height) {
                max.setSize(max.width, arr[i].getLines());
            }
        }
        PixM pixM = new PixM(max.width, max.height);


        for (int i = 0; i < pixM.getColumns()
                ; i++) {
            for (int j = 0; j < pixM.getLines(); j++) {
                for (int a = 0; a < 2; a++) {
                    double ri = 1.0 * i * arr[a].getColumns()
                            / pixM.getColumns();
                    double rj = 1.0 * i * arr[a].getLines() / pixM.getLines();

                    pixM.setP(i, j, pixM.getP(i, j).plus(arr[a].getP(i, j)));
                }
            }
        }
        return ImageIO.write(pixM.getImage(), "jpg", out);
    }
}
