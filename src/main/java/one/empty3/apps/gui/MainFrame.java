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

package one.empty3.apps.gui;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import one.empty3.library.Point;
import one.empty3.libs.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
/*
 * Created by JFormDesigner on Thu Jun 20 16:49:24 CEST 2019
 */


/**
 * My class description missing
 *
 * @author Manuel Dahmen dathewolf@gmail.com
 */
public class MainFrame extends JFrame {
    public MainFrame() {
        initComponents();
    }

    private void buttonObjectsWindowsActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        ResourceBundle bundle = ResourceBundle.getBundle("gui");
        panel1 = new JPanel();
        scrollPane2 = new JScrollPane();
        list1 = new JList();
        button3 = new JButton();
        button1 = new JButton();
        button2 = new JButton();
        scrollPane1 = new JScrollPane();
        textArea1 = new JTextArea();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new MigLayout(
                "fill,hidemode 3",
                // columns
                "[fill]" +
                        "[fill]",
                // rows
                "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]"));

        //======== panel1 ========
        {
            panel1.setLayout(new MigLayout(
                    "hidemode 3",
                    // columns
                    "[fill]" +
                            "[fill]",
                    // rows
                    "[]" +
                            "[]" +
                            "[]"));
        }
        contentPane.add(panel1, "cell 0 0 2 3");

        //======== scrollPane2 ========
        {
            scrollPane2.setViewportView(list1);
        }
        contentPane.add(scrollPane2, "cell 0 3");

        //---- button3 ----
        button3.setText(bundle.getString("button3.text"));
        contentPane.add(button3, "cell 1 3");

        //---- button1 ----
        button1.setText(bundle.getString("button1.text"));
        contentPane.add(button1, "cell 1 3");

        //---- button2 ----
        button2.setText(bundle.getString("button2.text"));
        contentPane.add(button2, "cell 1 3");

        //======== scrollPane1 ========
        {
            scrollPane1.setViewportView(textArea1);
        }
        contentPane.add(scrollPane1, "cell 0 4");
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    private void initComponentsI18n() {
        // JFormDesigner - Component i18n initialization - DO NOT MODIFY  //GEN-BEGIN:initI18n
        // JFormDesigner - End of component i18n initialization  //GEN-END:initI18n
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel panel1;
    private JScrollPane scrollPane2;
    private JList list1;
    private JButton button3;
    private JButton button1;
    private JButton button2;
    private JScrollPane scrollPane1;
    private JTextArea textArea1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
