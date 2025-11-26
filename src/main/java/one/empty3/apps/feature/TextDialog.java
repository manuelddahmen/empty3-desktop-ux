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

package one.empty3.apps.feature;

import one.empty3.feature.*;


import javax.swing.*;

import one.empty3.library.Point;
import one.empty3.libs.*;

import java.awt.Dimension;

public class TextDialog extends JFrame {
    private final JTextArea textArea = new JTextArea(80, 25);
    private static TextDialog instance = null;

    public TextDialog() {
        setContentPane(textArea);
        setMinimumSize(new Dimension(1024, 768));
        //textArea.setText(customProcessFileRGB);
        if (instance == null)
            instance = this;
    }


    public static TextDialog getInstance() {
        if (instance == null)
            return new TextDialog();
        return instance;
    }

    public String getText() {
        return textArea.getText();
    }

    public void setText(String text) {
        this.textArea.setText(text);
    }
}
