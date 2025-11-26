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

package one.empty3.apps.feature.facemorph;

import one.empty3.apps.feature.myfacedetect.WeightingFunction;
import one.empty3.feature.PixM;
import one.empty3.apps.feature.app.replace.javax.imageio.ImageIO;
import one.empty3.io.ProcessFile;

import java.io.File;
import java.util.Objects;

public class RunFeatures extends ProcessFile {
    WeightingFunction function;

    @Override
    public boolean process(File in, File out) {
        PixM imageIn = PixM.getPixM(Objects.requireNonNull(ImageIO.read(in)), maxRes);

        double ratioFace = 1;
        double ratioXy = (1.0) * imageIn.getColumns() / imageIn.getLines();

        int filterMaxRes = 10;
        function = new WeightingFunction(filterMaxRes, (int) (filterMaxRes * ratioXy));

        PixM imageOut = imageIn.applyFilter(function);

        imageOut.normalize(0., 1.).getImage().saveFile( out);
        return true;
    }
}
