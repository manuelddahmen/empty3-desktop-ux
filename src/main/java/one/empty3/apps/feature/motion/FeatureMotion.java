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

package one.empty3.apps.feature.motion;

import one.empty3.apps.feature.FeatureMatch;
import one.empty3.feature.Motion;
import one.empty3.feature.PixM;
import one.empty3.library.Lumiere;

import one.empty3.libs.*;

import java.awt.image.BufferedImage;
import java.util.List;


public class FeatureMotion extends Motion {
    @Override
    public BufferedImage process(PixM frame1, PixM frame2) {
        FeatureMatch featureMatch = new FeatureMatch();

        List<double[]> match = featureMatch.match(frame1, frame2);

        BufferedImage bufferedImage = new BufferedImage(frame1.getColumns(), frame1.getLines(), BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < frame1.getColumns(); i++) {
            for (int j = 0; j < frame1.getLines(); j++) {
                for (int c = 0; c < frame1.getCompCount(); c++) {
                    bufferedImage.setRGB(i, j, Lumiere.getInt(frame1.getValues(i, j)));
                }
            }
        }
        for (int i = 0; i < match.size(); i++) {
            bufferedImage.setRGB((int) match.get(i)[0], (int) match.get(i)[1], Color.WHITE.getRGB());
        }
        return bufferedImage;
    }
}
