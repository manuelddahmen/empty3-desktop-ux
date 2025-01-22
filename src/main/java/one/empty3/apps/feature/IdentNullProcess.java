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
import one.empty3.io.ObjectWithProperties;
import one.empty3.io.ProcessFile;
import one.empty3.libs.Image;

import java.io.File;
import java.io.IOException;

public class IdentNullProcess extends ProcessFile {


    public IdentNullProcess() {
        super();
        getProperties().addProperty("luminanceFactor", ObjectWithProperties.ClassTypes.AtomicDouble, 1.0);
        getProperties().addProperty("redFactor", ObjectWithProperties.ClassTypes.AtomicDouble, 1.0);
        getProperties().addProperty("blueFactor", ObjectWithProperties.ClassTypes.AtomicDouble, 1.0);
        getProperties().addProperty("greenFactor", ObjectWithProperties.ClassTypes.AtomicDouble, 1.0);
    }

    @Override
    public boolean process(File in, File out) {

        PixM pixM = null;
        pixM = PixM.getPixM(in, maxRes);

        double l = (double) getProperties().getProperty("luminanceFactor");
        double r = (double) getProperties().getProperty("redFactor");
        double g = (double) getProperties().getProperty("blueFactor");
        double b = (double) getProperties().getProperty("greenFactor");


        for (int i = 0; i < pixM.getColumns(); i++) {
            for (int j = 0; j < pixM.getLines(); j++) {
                pixM.set(pixM.index(i,j), pixM.getInt(i,j));
            }
        }

        Image image = pixM.getImage2();
        image.saveFile(out);
        addSource(out);
        return true;

    }

}
