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

/*
 * Created by JFormDesigner on Thu Dec 28 17:19:26 CET 2023
 */

package one.empty3.apps.vecmesh;

import one.empty3.library.Point;
import one.empty3.libs.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import net.miginfocom.swing.*;

/**
 * @author Manuel Dahmen dathewolf@gmail.com
 */
public class DimensionZBuffer extends JDialog {
    private int resX;
    private int resY;

    public DimensionZBuffer(Window owner) {
        super(owner);
        initComponents();
    }

    private void ok(ActionEvent e) {
        ((VecMeshEditorGui) (getOwner())).setResX(Integer.parseInt(this.textFieldX.getText()));
        ((VecMeshEditorGui) (getOwner())).setResY(Integer.parseInt(this.textFieldY.getText()));
        this.dispose();
    }

    private void cancel(ActionEvent e) {
        this.dispose();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        ResourceBundle bundle = ResourceBundle.getBundle("one.empty3.library.core.testing.Bundle");
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        label1 = new JLabel();
        textFieldX = new JTextField();
        label2 = new JLabel();
        textFieldY = new JTextField();
        buttonBar = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new MigLayout(
                    "insets dialog,hidemode 3",
                    // columns
                    "[fill]" +
                    "[fill]" +
                    "[fill]",
                    // rows
                    "[]" +
                    "[]" +
                    "[]"));

                //---- label1 ----
                label1.setText(bundle.getString("Dimension.label1.text"));
                contentPanel.add(label1, "cell 0 0 2 1");

                //---- textFieldX ----
                textFieldX.setText(bundle.getString("Dimension.textFieldX.text"));
                contentPanel.add(textFieldX, "cell 0 0 2 1");

                //---- label2 ----
                label2.setText(bundle.getString("Dimension.label2.text"));
                contentPanel.add(label2, "cell 0 1");

                //---- textFieldY ----
                textFieldY.setText(bundle.getString("Dimension.textFieldY.text"));
                contentPanel.add(textFieldY, "cell 1 1");
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setLayout(new MigLayout(
                    "insets dialog,alignx right",
                    // columns
                    "[button,fill]" +
                    "[button,fill]",
                    // rows
                    null));

                //---- okButton ----
                okButton.setText(bundle.getString("Dimension.okButton.text"));
                okButton.addActionListener(e -> ok(e));
                buttonBar.add(okButton, "cell 0 0");

                //---- cancelButton ----
                cancelButton.setText(bundle.getString("Dimension.cancelButton.text"));
                cancelButton.addActionListener(e -> cancel(e));
                buttonBar.add(cancelButton, "cell 1 0");
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel label1;
    private JTextField textFieldX;
    private JLabel label2;
    private JTextField textFieldY;
    private JPanel buttonBar;
    private JButton okButton;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
