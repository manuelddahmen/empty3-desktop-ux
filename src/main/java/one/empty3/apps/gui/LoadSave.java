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
 * Created by JFormDesigner on Sun Aug 11 14:34:54 CEST 2019
 */

package one.empty3.apps.gui;

import net.miginfocom.swing.MigLayout;
import one.empty3.apps.gui.DataModel;
import one.empty3.apps.gui.Main;
import one.empty3.library.Scene;
import one.empty3.library.core.export.ObjExport;
import one.empty3.library.core.export.STLExport;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * My class description missing
 *
 * @author Manuel Dahmen dathewolf@gmail.com
 */
public class LoadSave extends JPanel {
    private Main main;
    private DataModel dataModel;
    private File currentDirectory = new File("./");

    public LoadSave() {
        initComponents();
    }

    public DataModel getDataModel() {
        return dataModel;
    }

    private void buttonNewActionPerformed(ActionEvent e) {
        try {
            main.getDataModel().save(null);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        main.setDataModel(new DataModel());
        reinit(main.getDataModel().getScene());
    }

    public void reinit(Scene scene) {
        main.getREditor().history.clear();
        main.getREditor().init(scene);
    }


    private void buttonLoadActionPerformed(ActionEvent e) {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setCurrentDirectory(currentDirectory);
        jFileChooser.showDialog(this, "Load");
        File selectedFile = jFileChooser.getSelectedFile();
        if (selectedFile != null) {
            Logger.getAnonymousLogger().log(Level.INFO, "Try to save back");
            try {
                main.getDataModel().save(null);
                Logger.getAnonymousLogger().log(Level.INFO, "Save back");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            Logger.getAnonymousLogger().log(Level.INFO, "Load file" + selectedFile.toString());

            main.setDataModel(new DataModel(selectedFile));
            currentDirectory = jFileChooser.getCurrentDirectory();
            reinit(main.getDataModel().getScene());

        }
    }

    private void buttonSaveActionPerformed(ActionEvent e) {
        try {
            main.getDataModel().save(null);
            reinit(main.getDataModel().getScene());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void buttonEstlActionPerformed(ActionEvent e) {
        try {
            STLExport.save(new File(main.getDataModel().getNewStlFile()), getDataModel().getScene(), false);
            reinit(main.getDataModel().getScene());
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

    private void buttonEobjActionPerformed(ActionEvent e) {
        // TODO FIND CLASS
        ObjExport objExport = new ObjExport();
    }

    private void button2ActionPerformed(ActionEvent e) {
        // TODO add your code here
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        buttonNew = new JButton();
        scrollPane1 = new JScrollPane();
        tableLatest = new JTable();
        buttonLoad = new JButton();
        button3 = new JButton();
        button4 = new JButton();
        buttonEstl = new JButton();
        buttonEobj = new JButton();

        //======== this ========
        setLayout(new MigLayout(
                "hidemode 3",
                // columns
                "[fill]" +
                        "[fill]",
                // rows
                "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]"));

        //---- buttonNew ----
        buttonNew.setText("new");
        buttonNew.addActionListener(e -> {
            buttonNewActionPerformed(e);
            buttonNewActionPerformed(e);
        });
        add(buttonNew, "cell 0 0");

        //======== scrollPane1 ========
        {

            //---- tableLatest ----
            tableLatest.setModel(new DefaultTableModel(
                    new Object[][]{
                            {"Latest"},
                    },
                    new String[]{
                            null
                    }
            ) {
                Class<?>[] columnTypes = new Class<?>[]{
                        String.class
                };

                @Override
                public Class<?> getColumnClass(int columnIndex) {
                    return columnTypes[columnIndex];
                }
            });
            {
                TableColumnModel cm = tableLatest.getColumnModel();
                cm.getColumn(0).setCellEditor(new DefaultCellEditor(
                        new JComboBox(new DefaultComboBoxModel(new String[]{
                                "Latest"
                        }))));
            }
            scrollPane1.setViewportView(tableLatest);
        }
        add(scrollPane1, "cell 1 0 1 7");

        //---- buttonLoad ----
        buttonLoad.setText("load");
        buttonLoad.addActionListener(e -> buttonLoadActionPerformed(e));
        add(buttonLoad, "cell 0 1");

        //---- button3 ----
        button3.setText("save");
        button3.addActionListener(e -> buttonSaveActionPerformed(e));
        add(button3, "cell 0 2");

        //---- button4 ----
        button4.setText("save as");
        add(button4, "cell 0 3");

        //---- buttonEstl ----
        buttonEstl.setText("Export STL");
        buttonEstl.addActionListener(e -> {
            buttonEstlActionPerformed(e);
            buttonEstlActionPerformed(e);
        });
        add(buttonEstl, "cell 0 4");

        //---- buttonEobj ----
        buttonEobj.setText("Export OBJ");
        buttonEobj.addActionListener(e -> buttonEobjActionPerformed(e));
        add(buttonEobj, "cell 0 5");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    public void setMain(Main main) {
        this.main = main;
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JButton buttonNew;
    private JScrollPane scrollPane1;
    private JTable tableLatest;
    private JButton buttonLoad;
    private JButton button3;
    private JButton button4;
    private JButton buttonEstl;
    private JButton buttonEobj;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
