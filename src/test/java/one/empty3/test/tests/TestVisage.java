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

package one.empty3.test.tests;

import one.empty3.library.Point3D;
import one.empty3.library.core.testing.jvm.TestObjetSub;

import java.awt.image.BufferedImage;

public class TestVisage extends TestObjetSub {
    BufferedImage image = null;
    @Override
    public void ginit() {
        super.ginit();
        for (int i = -image.getWidth(); i < image.getWidth()*2; i++) {
            for (int j = -image.getHeight(); j < image.getHeight()*2; j++) {
                for (int k = -image.getHeight()+1 ; k<image.getWidth()*2; k++) {
                    for (int l = 0; l<360; l++) {
                        Point3D a = new Point3D(0.0+i, 0.0+j, 0.0+0);
                        Point3D c = new Point3D(0.0+j, 0.0+k, 0.0+0);
                        //Point3D b =   c.moins(a).rotate(a, a, Point3D.X.mult(2 * Math.PI * l / 360));
                        //Point3D d =   c.moins(a).rotate(a, a, Point3D.X.mult(2 * Math.PI * l / 360));
                    }
                }

            }

        }
    }
}
