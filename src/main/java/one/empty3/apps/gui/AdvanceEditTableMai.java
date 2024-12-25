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
 * Created by JFormDesigner on Tue Jul 02 12:51:07 CEST 2019
 */

package one.empty3.apps.gui;

import net.miginfocom.swing.MigLayout;
import one.empty3.library.Representable;

import javax.swing.*;
import one.empty3.library.Point;
import one.empty3.libs.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;

/**
 * My class description missing
 *
 * @author Manuel Dahmen dathewolf@gmail.com
 */
public class AdvanceEditTableMai extends JPanel {
    private final Representable representable;

    public AdvanceEditTableMai(Representable representable) {
        this.representable = representable;
        initComponents();
    }

    private void table1MouseClicked(MouseEvent e) {
        if (e.getButton() == 1) {
            // Right Click
            popupMenuOptions.setVisible(true);
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        ResourceBundle bundle = ResourceBundle.getBundle("one.empty3.apps.gui.gui");
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        label1 = new JLabel();
        scrollPane1 = new JScrollPane();
        table1 = new JTable();
        buttonBar = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();
        helpButton = new JButton();
        popupMenuOptions = new JPopupMenu();

        //======== this ========
        setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new MigLayout(
                        "fill,insets dialog,hidemode 3",
                        // columns
                        "[fill]",
                        // rows
                        "[]" +
                                "[]"));

                //---- label1 ----
                label1.setText(bundle.getString("AdvanceEditTable.label1.text"));
                contentPanel.add(label1, "cell 0 0");

                //======== scrollPane1 ========
                {

                    //---- table1 ----
                    table1.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            table1MouseClicked(e);
                        }
                    });
                    scrollPane1.setViewportView(table1);
                }
                contentPanel.add(scrollPane1, "cell 0 1");
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setLayout(new MigLayout(
                        "insets dialog,alignx right",
                        // columns
                        "[button,fill]" +
                                "[button,fill]" +
                                "[button,fill]",
                        // rows
                        null));

                //---- okButton ----
                okButton.setText(bundle.getString("AdvanceEditTable.okButton.text"));
                buttonBar.add(okButton, "cell 0 0");

                //---- cancelButton ----
                cancelButton.setText(bundle.getString("AdvanceEditTable.cancelButton.text"));
                buttonBar.add(cancelButton, "cell 1 0");

                //---- helpButton ----
                helpButton.setText(bundle.getString("AdvanceEditTable.helpButton.text"));
                buttonBar.add(helpButton, "cell 2 0");
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        add(dialogPane, BorderLayout.CENTER);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel label1;
    private JScrollPane scrollPane1;
    private JTable table1;
    private JPanel buttonBar;
    private JButton okButton;
    private JButton cancelButton;
    private JButton helpButton;
    private JPopupMenu popupMenuOptions;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
