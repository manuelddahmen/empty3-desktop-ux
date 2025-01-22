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

import one.empty3.matrix.PixM;
import one.empty3.library.Point2D;

public interface IMyDetector {
    public void setPixM(PixM matrice);
    public void execute();
    public int getFaces();
    public int[] getFaceIds();
    public Point2D getEyeLeft();
    public Point2D getEyeRight();
    public Point2D getNose();
    public Point2D getMouthLeft();
    public Point2D getMouthRight();
}
