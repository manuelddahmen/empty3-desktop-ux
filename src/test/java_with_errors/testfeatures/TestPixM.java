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

package testfeatures;

import one.empty3.feature.PixM;
import one.empty3.feature.WriteFile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestPixM {
    @Test
    public void testPixMblack() {
        PixM p = new PixM(500, 500);
        WriteFile.writeNext(p.getImage(), "black 500x500");
        PixM p2 = PixM.getPixM(new PixM(1000, 1000).getImage(), 500.0);
        WriteFile.writeNext(p.getImage(), "black 500x500 resized from 1000x1000");
        assertTrue(p.equals(p2));
        PixM p3 = PixM.getPixM(p.getImage(), 500.);
        assertTrue(p.equals(p3));
    }

    @Test
    public void testPrimaryColors() {
        PixM p = new PixM(500, 500);
        colorsRegion(p, 0, 0, 250, 250, new double[]{1.0, 1.0, 1.0});
        PixM p2 = new PixM(1000, 1000);
        colorsRegion(p2, 0, 0, 500, 500, new double[]{1.0, 1.0, 1.0});
        p2 = PixM.getPixM(p2.getImage(), 500.0);
        assertTrue(p.equals(p2));

    }

    public void colorsRegion(PixM p, int x, int y, int w, int h, double[] comps) {
        for (int i = x; i < x + w; i++)
            for (int j = y; j < y + h; j++)
                for (int c = 0; c < comps.length; c++) {
                    p.setCompNo(c);
                    p.set(i, j, comps[c]);
                }
    }
}
