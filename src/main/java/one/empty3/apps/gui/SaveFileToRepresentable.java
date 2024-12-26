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

/*
 * Created by JFormDesigner on Wed Sep 18 13:19:59 CEST 2019
 */

package one.empty3.apps.gui;

import net.miginfocom.swing.MigLayout;
import one.empty3.library.Representable;

import javax.swing.*;

import one.empty3.library.Point;
import one.empty3.libs.*;

import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.ResourceBundle;

/**
 * My class description missing
 *
 * @author Manuel Dahmen dathewolf@gmail.com
 */
public class SaveFileToRepresentable extends JDialog {
    private Representable r;

    public SaveFileToRepresentable(Window owner) {
        super(owner);
        initComponents();
    }

    public void init(Representable r, Object fileS, String property, int dim, int row, int col) {
        this.r = r;
        if (fileS instanceof File) {
        } else if (fileS instanceof List) {
            List<File> files = ((List<File>) fileS);

        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        ResourceBundle bundle = ResourceBundle.getBundle("one.empty3.apps.gui.gui");
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        buttonBar = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();

        //======== this ========
        Container contentPane = getContentPane();
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
                                "[fill]",
                        // rows
                        "[]" +
                                "[]" +
                                "[]"));
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
                okButton.setText(bundle.getString("SaveFileToRepresentable.okButton.text"));
                buttonBar.add(okButton, "cell 0 0");

                //---- cancelButton ----
                cancelButton.setText(bundle.getString("SaveFileToRepresentable.cancelButton.text"));
                buttonBar.add(cancelButton, "cell 1 0");
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JPanel buttonBar;
    private JButton okButton;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
