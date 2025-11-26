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

package one.empty3.apps.gui;

import javax.swing.*;
import one.empty3.library.Point;
import one.empty3.libs.*;
import one.empty3.libs.Color;

import java.awt.*;

/***
 * Created by manue on 26-11-19.
 */

public class Rendu extends JLabel
        implements ListCellRenderer {
    ImageIcon icon;
    ImageIcon selectIcon;
    java.awt.Color selectCouleur = Color.RED;

    public Rendu() {
        //icon = new ImageIcon(getClass().getResource("img1.gif"));
        //selectIcon  = new ImageIcon(getClass().getResource("img2.gif"));
    }

    public Component getListCellRendererComponent(JList list,
                                                  Object value, // valeur à afficher
                                                  int index, // indice d'item
                                                  boolean isSelected, // l'item est-il sélectionné
                                                  boolean cellHasFocus) // La liste a-t-elle le focus
    {
        String s = value.toString();
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(selectCouleur);
            setText(s + "  " + index);
            //setIcon(selectIcon);
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
            setText(s);
            //setIcon(icon);
        }
        setEnabled(list.isEnabled());
        setFont(list.getFont());
        setOpaque(true);
        return this;
    }
}

