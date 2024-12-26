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

package one.empty3.apps.gui;

import one.empty3.apps.gui.RPropertyDetailsRow;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

import one.empty3.library.Point;
import one.empty3.libs.*;

import java.awt.*;

/**
 * Created by manue on 27-12-19.
 */
public class RETableRenderer extends DefaultTableCellRenderer {
    String[] columnDescr = {"Nom de la forme 3D\nNom simple",
            "Description\nDescription de la propriété/de l'objet",
            "Entier entre 0 et 2 inclus\nDimension de l'objet: Scalar, Array1D, Array2D",
            "Indice(s) de l'objet dans le tableau\n" +
                    "Dim 0: =0" +
                    "Dim 1: =1 : indice i dans le tableau [1, size]" +
                    "Dim 2: = 2: indice i,j dans le tableau [2, size]"
    };


    private final RPropertyDetailsRow rPropertyRow;

    public RETableRenderer(RPropertyDetailsRow rows) {
        this.rPropertyRow = rows;
    }

    public Component getTableCellRendererComponent(
            JTable table, Object value,
            boolean isSelected, boolean hasFocus,
            int row, int column) {
        JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        // This...
        String pathValue = column < columnDescr.length ? columnDescr[column] : "null" + rPropertyRow.getValueAt(row, column); // Could be value.toString()
        c.setToolTipText(pathValue);
        // ...OR this probably works in your case:
        c.setToolTipText(c.getText());
        return c;
    }
}
