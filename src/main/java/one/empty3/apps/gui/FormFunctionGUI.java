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

package one.empty3.apps.gui;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

import one.empty3.library.Point;
import one.empty3.libs.*;

import java.awt.*;
import java.util.ResourceBundle;
/*
 * Created by JFormDesigner on Thu Jun 27 11:40:57 CEST 2019
 */


/**
 * My class description missing
 *
 * @author Manuel Dahmen dathewolf@gmail.com
 */
public class FormFunctionGUI {

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        ResourceBundle bundle = ResourceBundle.getBundle("gui");
        FormFunctionGUI = new JFrame();

        //======== FormFunctionGUI ========
        {
            FormFunctionGUI.setTitle(bundle.getString("FormFunctionGUI.title"));
            Container FormFunctionGUIContentPane = FormFunctionGUI.getContentPane();
            FormFunctionGUIContentPane.setLayout(new MigLayout(
                    "hidemode 3",
                    // columns
                    "[fill]" +
                            "[fill]",
                    // rows
                    "[]" +
                            "[]" +
                            "[]"));
            FormFunctionGUI.pack();
            FormFunctionGUI.setLocationRelativeTo(FormFunctionGUI.getOwner());
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JFrame FormFunctionGUI;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
