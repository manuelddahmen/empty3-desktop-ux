package one.empty3.apps.newboardgames; ///*
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
//import com.jogamp.opengl.GLAutoDrawable;
//import com.jogamp.opengl.GLDrawable;
//import com.jogamp.opengl.awt.GLCanvas;
//import com.jogamp.opengl.util.Animator;
//import one.empty3.apps.newboardgames.boards.Grid;
//import one.empty3.apps.opad.DarkFortressGUI;
//import one.empty3.apps.opad.Drawer;
//import one.empty3.apps.opad.JoglDrawer;
//import one.empty3.library.Camera;
//
//import javax.swing.*;
//import one.empty3.library.Point;
import one.empty3.libs.*;
import java.awt.Dimension;
//
//public class WindowDrawing extends DarkFortressGUI {
//
//    private GLCanvas canvas;
//    private JFrame frame;
//    JoglDrawerBoardGame joglDrawerBoardGame;
//    GLCanvasBoardGame glCanvasBoardGame;
//    JPanel jPanel;
//    public WindowDrawing() {
//        super(JoglDrawerBoardGame.class);
//
//        joglDrawerBoardGame = new JoglDrawerBoardGame(this);
//        glCanvasBoardGame = new GLCanvasBoardGame(this);
//        joglDrawerBoardGame.setBoard(new Grid(10, 10, 5, 8));
//        glCanvasBoardGame.setVisible(true);
//        glCanvasBoardGame.setMinimumSize(new Dimension(800, 600));
//
//
//
//        jPanel = new JPanel();
//        jPanel.setMinimumSize(new Dimension(800, 600));
//        jPanel.add(glCanvasBoardGame);
//
//
//
//
//        add(jPanel);
//        setVisible(true);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        Animator animator = new Animator();
//        glCanvasBoardGame.setAnimator(animator);
//        glCanvasBoardGame.getAnimator().start();
//
//
//        setMinimumSize(new Dimension(800, 600));
//    }
//    public static void main(String [] args) {
//        WindowDrawing windowDrawing = new WindowDrawing();
//
//    }
//
//    public void setCamera(Camera camera) {
//        joglDrawerBoardGame.setCamera(camera);
//    }
//}
