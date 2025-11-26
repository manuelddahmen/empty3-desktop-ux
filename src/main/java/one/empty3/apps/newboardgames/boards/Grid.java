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

package one.empty3.apps.newboardgames.boards; /// *
// *
// *  * Copyright (c) 2024. Manuel Daniel Dahmen
// *  *
// *  *
// *  *    Copyright 2024 Manuel Daniel Dahmen
// *  *
// *  *    Licensed under the Apache License, Version 2.0 (the "License");
// *  *    you may not use this file except in compliance with the License.
// *  *    You may obtain a copy of the License at
// *  *
// *  *        http://www.apache.org/licenses/LICENSE-2.0
// *  *
// *  *    Unless required by applicable law or agreed to in writing, software
// *  *    distributed under the License is distributed on an "AS IS" BASIS,
// *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *  *    See the License for the specific language governing permissions and
// *  *    limitations under the License.
// *
// *
// */
//
//package one.empty3.apps.newboardgames.boards;
//
//import one.empty3.apps.newboardgames.Board;
//import one.empty3.apps.newboardgames.Cell;
//import one.empty3.apps.newboardgames.Character;
//import one.empty3.library.Camera;
//import one.empty3.library.Point2D;
//import one.empty3.library.Point3D;
//import one.empty3.library.core.math.Matrix;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//
//public class Grid extends Board {
//
//
//    private Matrix matrix = null;
//
//    public Grid(int dimX, int dimY, int heightView, int medianDistView) {
//
//        this.dimX = dimX;
//        this.dimY = dimY;
//        this.heightView = heightView;
//        this.medianDistView = medianDistView;
//
//        Matrix matrix = new Matrix(dimX, dimY, heightView);
//    }
//
//    @Override
//    public Camera camera() {
//        if (camera == null) {
//            camera = new Camera(Point3D.Y.mult(-medianDistView), Point3D.O0, Point3D.Z);
//        }
//        return camera;
//    }
//
//    @Override
//    public Point2D getSize2D() {
//        return new one.empty3.library.Point2D((double) dimX, (double) dimY);
//    }
//
//    @Override
//    public Point3D getSize3D() {
//        return new Point3D((double) dimX, (double) dimY, (double) heightView);
//    }
//
//    @Override
//    protected List<Character> getCharacters() {
//        return new ArrayList<>();
//    }
//}
