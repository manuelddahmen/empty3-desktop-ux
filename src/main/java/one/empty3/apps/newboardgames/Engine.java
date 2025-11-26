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

package one.empty3.apps.newboardgames; /// *
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
//package one.empty3.apps.newboardgames;
//
//import one.empty3.apps.newboardgames.boards.Grid;
//import one.empty3.library.Camera;
//import one.empty3.library.Point3D;
//import one.empty3.library.Representable;
//
//public class Engine implements Runnable {
//    private final WindowDrawing window;
//    private Board board;
//    private boolean isRunning;
//    private Level level;
//
//    public Engine(WindowDrawing windowDrawing) {
//        this.window = windowDrawing;
//    }
//    @Override
//    public void run() {
//        while (isRunning()) {
//            updateCameraPosition(board, level);
//            setCameraOn(window, board.camera());
//            draw(board);
//        }
//    }
//
//    private void setCameraOn(WindowDrawing window, Camera camera) {
//        if(camera==null) {
//            board.camera = new Camera(board.cellAt(0, 0).rotation.getElem().getCentreRot().data0d.plus(Point3D.Z.mult(board.heightView)),
//                    board.cellAt(board.dimX, board.dimY).rotation.getElem().getCentreRot().data0d.plus(Point3D.Z.mult(board.heightView))
//
//                    );
//            camera = camera;
//        }
//        window.setCamera(camera);
//    }
//
//    private void updateCameraPosition(Board board, Level level) {
//
//    }
//
//    private void draw(Board board) {
//        for(int i=0; i<board.getSize2D().getX(); i++)
//            for(int j=0; j<board.getSize2D().getY(); j++) {
//                drawCell(board.cellAt(i, j));
//            }
//    }
//
//    private void drawCell(Representable cellAt) {
//    }
//
//    private boolean isRunning() {
//        return isRunning;
//    }
//
//    public void loadLevel(String pathname) {
//        level = new Level("");
//        board = new Grid(10, 10, 5, 7);
//    }
//
//}
