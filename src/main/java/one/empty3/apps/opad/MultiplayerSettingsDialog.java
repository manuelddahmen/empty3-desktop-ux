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
        // Run in background to keep UI responsive
        new Thread(() -> {
            int maxRetries = 5;
            int attempt = 0;
            boolean connected = false;
            
            while (attempt < maxRetries && !connected) {
                try {
                    client = new GameClient(ipField.getText(), Integer.parseInt(portField.getText()), nameField.getText(), 0);
                    client.connect(5000); // 5 seconds timeout per attempt
                    
                    connected = true;
                    // Connection successful, switch to EDT to close dialog
                    SwingUtilities.invokeLater(this::dispose);
                    
                } catch (IOException | NumberFormatException ex) {
                    attempt++;
                    if (attempt >= maxRetries) {
                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(this, "Connection failed after " + maxRetries + " attempts: " + ex.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
                        });
                    } else {
                        try {
                            Thread.sleep(attempt * 2000); // Exponential backoff: 2s, 4s, 6s...
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                }
            }
        }).start();
    }
    
    public GameClient getClient() {
        return client;
    }
}
