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

package one.empty3.feature.Myfacedetect;

import one.empty3.apps.feature.myfacedetect.WeightingFunction;
import one.empty3.feature.PixM;
import one.empty3.apps.feature.app.replace.javax.imageio.ImageIO;
import one.empty3.io.ProcessFile;

import java.awt.*;
import java.io.File;
import java.util.Objects;

public class Face extends ProcessFile {
    private Rectangle rectangleEyeLeft;
    private Rectangle rectangleEyeRight;
    private Rectangle rectangleMouth;

    @Override
    public boolean process(File in, File out) {
        PixM inPixM = PixM.getPixM(Objects.requireNonNull(ImageIO.read(in)), 300);

        inPixM.applyFilter(new WeightingFunction(100, 100));
        return false;
    }
}
