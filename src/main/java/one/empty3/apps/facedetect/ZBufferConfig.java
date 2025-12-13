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

package one.empty3.apps.facedetect;

import one.empty3.library.Point3D;
import one.empty3.library.ZBufferImpl;
import one.empty3.library.objloader.E3Model;

public class ZBufferConfig {
    public static int TECHNICAL_MAXIMUM_DIM  = 4000;
    public static void getConfig(ZBufferImpl zBuffer, E3Model model) {
        if(model!=null && zBuffer!=null) {
            int numFaces = ((E3Model) (model))
                    .getObjects().getListRepresentable().size();
            if (numFaces <= 0) {
                numFaces = 1;
            }
            Point3D min = new Point3D(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
            Point3D max = new Point3D(Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE);
            model.getBounds(min, max);
            Point3D diff = max.moins(min);
            double surfaceBoundingCube = Math.sqrt(2 * diff.getX() * diff.getY() + diff.getY() * diff.getZ() + diff.getZ() * diff.getX());
            zBuffer.setIncrementOptimizer(new ZBufferImpl.IncrementOptimizer(ZBufferImpl.IncrementOptimizer.Strategy.ENSURE_MINIMUM_DETAIL, Math.max(1.0/(Math.max(Math.max(zBuffer.la() , zBuffer.ha()), surfaceBoundingCube/(numFaces+1))), 1.0/3000.0)+1.0/TECHNICAL_MAXIMUM_DIM));
        }
    }
}
