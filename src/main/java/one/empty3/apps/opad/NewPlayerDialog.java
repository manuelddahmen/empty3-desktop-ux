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

package one.empty3.apps.opad;

import javax.swing.*;
import one.empty3.library.Point;
import one.empty3.libs.*;

import java.awt.*;
import java.awt.Color;

/*__
 * Created by Win on 26-10-18.
 */
public class NewPlayerDialog extends JDialog {
    public String playerName;

    public NewPlayerDialog(Frame owner, boolean modal) {
        super(owner, modal);



        FlowLayout flowLayout = new FlowLayout();

        flowLayout.setHgap(3);

        setLayout(flowLayout);
        JLabel jLabel = new JLabel("Player name");
        jLabel.setSize(100,20);
        add(jLabel);
        JTextField jTextFieldNewPlayer = new JTextField("");
        jTextFieldNewPlayer.setSize(100,20);
        add(jTextFieldNewPlayer);

        JButton ok = new JButton("Ok");
        ok.setSize(100,20);

        add(ok);

        ok.addActionListener(e -> {
            playerName = jTextFieldNewPlayer.getText();

            new Player(playerName, new one.empty3.libs.Color(Color.cyan.getRGB()), 100).save();
        });

        pack();
    }



}
