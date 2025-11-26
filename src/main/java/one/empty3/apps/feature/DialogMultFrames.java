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
 * Created by JFormDesigner on Fri Apr 07 14:28:25 CEST 2023
 */

package one.empty3.apps.feature;


import javax.swing.event.*;

import net.miginfocom.swing.MigLayout;
import one.empty3.feature.process.InProcessCode;
import one.empty3.io.ObjectWithProperties;
import one.empty3.io.ProcessFile;

import javax.swing.*;

import one.empty3.library.Point;
import one.empty3.libs.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import java.util.ResourceBundle;
import java.util.function.Consumer;

/**
 * My class description missing
 *
 * @author Manuel Dahmen dathewolf@gmail.com
 */
public class DialogMultFrames extends JDialog {
    private final ClassSchemaBuilder owner;
    private ProcessFile classInstance;
    private ClassSchemaBuilder.DiagramElement current = null;
    List<ClassSchemaBuilder.DiagramElement> diagramElements;
    ClassSchemaBuilder.ClassElement currentElement = null;
    private TreeDiagram treeDiagram = null;
    private String currentPropertyName;
    private ObjectWithProperties.ClassTypes classType;
    private InProcessCode code;


    public DialogMultFrames(Window owner) {
        super(owner);
        this.owner = (ClassSchemaBuilder) owner;
        current = ((ClassSchemaBuilder) owner).getSelectedElement();

        initComponents();

        updateForms();
        initComboBox();
    }

    private void updateForms() {

        if (current == null) {
            dispose();
            this.classInstance = null;
        } else {
            this.classInstance = ((ClassSchemaBuilder.ClassElement) current).getInstance();
        }
        ProcessFile pf = classInstance;
        if (pf == null) {
            System.err.println("DialogMultFrames: pf==null ");
            dispose();
            setVisible(false);
            return;
        }
        pack();

    }

    public void initComboBox() {

        if (classInstance != null) {
            List<String> model = new ArrayList<>(classInstance.getProperties()
                    .getPropertyList());

            DefaultComboBoxModel defaultComboBoxModel = new DefaultComboBoxModel();

            defaultComboBoxModel.addAll(model);

            getComboBox1().setModel(defaultComboBoxModel);

            getComboBox1().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    updateData(e);
                }
            });
        }
        pack();
    }

    private void updateData(ActionEvent e) {
        updateDataField();
    }

    private void updateDataField() {
        Object o = getComboBox1().getSelectedItem();
        if (o instanceof String) {
            String s = (String) o;
            ProcessFile instance = ((ClassSchemaBuilder.ClassElement) current).getInstance();
            ObjectWithProperties.ClassTypes types =
                    ((ClassSchemaBuilder.ClassElement) current).getInstance()
                            .getProperties().getPropertyType(s);
            Object value = instance.getProperties().getProperty(s);
            textFieldName.setText(s);
            textFieldClassname.setText(instance.getClass().getCanonicalName());
            textFieldValue.setText(String.valueOf(value));
            currentPropertyName = s;
            code = classInstance.getCode();
            this.classType = types;
        } else {
            System.err.println("Selected item is null or no string :" + o);
        }
        String property = (String) (comboBox1.getSelectedItem());

        pack();
    }

    private void applyChanges(ActionEvent e) {
        if (classInstance != null && getClassType() != null) {
            JTextField textFieldValue1 = getTextFieldValue();
            String text = textFieldValue1.getText();

            if (text != null && text.length() > 0) {

                Object o = text;

                switch (classType) {
                    case AtomicInt:
                        o = Integer.parseInt(text);
                        break;
                    case AtomicBoolean:
                        o = Boolean.parseBoolean(text);
                        break;

                    case String:
                        o = text;
                        break;
                    case AtomicChar:
                        Character c = text.charAt(0);
                        o = c;
                        break;

                    case AtomicDouble:
                        o = Double.parseDouble(text);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + classType);
                }
                classInstance.getProperties()
                        .updateProperty(currentPropertyName, o);
            }
        }

        pack();
    }

    private ObjectWithProperties.ClassTypes getClassType() {
        return classType;
    }

    public JPanel getDialogPane() {
        return dialogPane;
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }

    public JComboBox getComboBox1() {
        return comboBox1;
    }

    public JButton getButtonUpdate() {
        return buttonUpdate;
    }

    public JButton getButtonApply() {
        return buttonApply;
    }

    public JTextField getTextFieldValue() {
        return textFieldValue;
    }

    public JPanel getButtonBar() {
        return buttonBar;
    }

    public JButton getOkButton() {
        return okButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public JLabel getLabelName() {
        return labelName;
    }

    public JTextField getTextFieldName() {
        return textFieldName;
    }

    public JLabel getLabelVakue() {
        return labelVakue;
    }

    public JLabel getLabelClassProperties() {
        return labelClassProperties;
    }

    public JLabel getLabelClassname() {
        return labelClassname;
    }

    public JTextField getTextFieldClassname() {
        return textFieldClassname;
    }

    private void comboBox1(ActionEvent e) {
        updateForms();
    }

    public JScrollPane getScrollPane1() {
        return scrollPane1;
    }

    public JTextArea getTextAreaCodeEditorSimple() {
        return textAreaCodeEditorSimple;
    }

    private void textAreaCodeEditorSimpleCaretUpdate(CaretEvent e) {
        code = classInstance.getCode();
        classInstance.getProperties().getPropertyList().forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                String codeNew = "\n" + s + "=" + classInstance.getProperties().getProperty(s) + "\n";
                if (textAreaCodeEditorSimple.getText().contains(s)) {
                    String text = textAreaCodeEditorSimple.getText();
                    int start = text.indexOf(s);
                    int end = text.indexOf(s, start);
                    String textMod = text.substring(0, start)
                            + codeNew + text.substring(end, -1);
                    textAreaCodeEditorSimple.setText(textMod);
                } else {
                    textAreaCodeEditorSimple.setText(textAreaCodeEditorSimple.getText()
                            + codeNew);
                }
                code.newVersion(textAreaCodeEditorSimple.getText());

            }
        });

    }


    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel labelClassProperties;
    private JComboBox comboBox1;
    private JScrollPane scrollPane1;
    private JTextArea textAreaCodeEditorSimple;
    private JLabel labelName;
    private JTextField textFieldName;
    private JButton buttonUpdate;
    private JLabel labelVakue;
    private JTextField textFieldValue;
    private JButton buttonApply;
    private JLabel labelClassname;
    private JTextField textFieldClassname;
    private JPanel buttonBar;
    private JButton okButton;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        ResourceBundle bundle = ResourceBundle.getBundle("one.empty3.library.core.testing.Bundle");
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        labelClassProperties = new JLabel();
        comboBox1 = new JComboBox();
        scrollPane1 = new JScrollPane();
        textAreaCodeEditorSimple = new JTextArea();
        labelName = new JLabel();
        textFieldName = new JTextField();
        buttonUpdate = new JButton();
        labelVakue = new JLabel();
        textFieldValue = new JTextField();
        buttonApply = new JButton();
        labelClassname = new JLabel();
        textFieldClassname = new JTextField();
        buttonBar = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();

        //======== this ========
        setAlwaysOnTop(true);
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new MigLayout(
                    "fill,insets dialog,hidemode 3",
                    // columns
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
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

                //---- labelClassProperties ----
                labelClassProperties.setText(bundle.getString("DialogMultFrames.labelClassProperties.text"));
                contentPanel.add(labelClassProperties, "cell 0 0");

                //---- comboBox1 ----
                comboBox1.addActionListener(e -> comboBox1(e));
                contentPanel.add(comboBox1, "cell 2 0");

                //======== scrollPane1 ========
                {

                    //---- textAreaCodeEditorSimple ----
                    textAreaCodeEditorSimple.addCaretListener(e -> textAreaCodeEditorSimpleCaretUpdate(e));
                    scrollPane1.setViewportView(textAreaCodeEditorSimple);
                }
                contentPanel.add(scrollPane1, "cell 3 0 2 8,grow");

                //---- labelName ----
                labelName.setText(bundle.getString("DialogMultFrames.labelName.text"));
                contentPanel.add(labelName, "cell 0 1");
                contentPanel.add(textFieldName, "cell 1 1");

                //---- buttonUpdate ----
                buttonUpdate.setText(bundle.getString("DialogMultFrames.buttonUpdate.text"));
                buttonUpdate.addActionListener(e -> updateData(e));
                contentPanel.add(buttonUpdate, "cell 2 1");

                //---- labelVakue ----
                labelVakue.setText(bundle.getString("DialogMultFrames.labelVakue.text"));
                contentPanel.add(labelVakue, "cell 0 2");
                contentPanel.add(textFieldValue, "cell 1 2");

                //---- buttonApply ----
                buttonApply.setText(bundle.getString("DialogMultFrames.buttonApply.text"));
                buttonApply.addActionListener(e -> applyChanges(e));
                contentPanel.add(buttonApply, "cell 2 2");

                //---- labelClassname ----
                labelClassname.setText(bundle.getString("DialogMultFrames.labelClassname.text"));
                contentPanel.add(labelClassname, "cell 0 3");
                contentPanel.add(textFieldClassname, "cell 1 3 2 1");
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
                okButton.setText(bundle.getString("DialogMultFrames.okButton.text"));
                buttonBar.add(okButton, "cell 0 0");

                //---- cancelButton ----
                cancelButton.setText(bundle.getString("DialogMultFrames.cancelButton.text"));
                buttonBar.add(cancelButton, "cell 1 0");
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }
}
