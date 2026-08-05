/*
 *
 *  *
 *  *  * Copyright (c) 2026. Manuel Daniel Dahmen
 *  *  *
 *  *  *
 *  *  *    Copyright 2026 Manuel Daniel Dahmen
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

package one.empty3.apps.opad;

import one.empty3.apps.opad.server.GameClient;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MultiplayerSettingsDialog extends JDialog {
    private JTextField ipField = new JTextField("34.170.12.255", 15);
    private JTextField portField = new JTextField("4712", 5);
    private JTextField nameField = new JTextField("Player1", 15);
    private JButton connectButton = new JButton("Connect");
    private GameClient client;

    public MultiplayerSettingsDialog(Frame parent) {
        super(parent, "Multiplayer Settings", true);
        setLayout(new GridLayout(4, 2));
        add(new JLabel("IP:")); add(ipField);
        add(new JLabel("Port:")); add(portField);
        add(new JLabel("Name:")); add(nameField);
        add(connectButton);

        connectButton.addActionListener(this::onConnect);
        pack();
    }

    private void onConnect(ActionEvent e) {
        try {
            client = new GameClient(ipField.getText(), Integer.parseInt(portField.getText()), nameField.getText(), 0);
            client.connect(36000); // 5 seconds timeout
            
            // If connection is successful, close the dialog and start the game
            dispose();
            
            // How to start the game?
            // Need to pass client to the game.
        } catch (IOException | NumberFormatException ex) {
            Logger.getLogger(MultiplayerSettingsDialog.class.getName()).log(Level.SEVERE, "Connection failed", ex);
            String message = "Connection failed: " + ex.getMessage() + "\n\n" +
                             "If no game server exists, please start a new game server first.";
            JOptionPane.showMessageDialog(this, message, "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public GameClient getClient() {
        return client;
    }
}
