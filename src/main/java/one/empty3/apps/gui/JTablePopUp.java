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

import one.empty3.apps.gui.ObjectDetailDescription;
import one.empty3.apps.gui.RPropertyDetailsRow;

import javax.swing.*;

import one.empty3.libs.*;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by manue on 07-09-19.
 */
public class JTablePopUp extends JTable {

    public String getToolTipText(MouseEvent e) {
        String tip = null;
        java.awt.Point p = e.getPoint();
        int rowIndex = rowAtPoint(p);
        int colIndex = columnAtPoint(p);

        try {
            tip = getValueAt(rowIndex, 5).toString();
            ObjectDetailDescription objectDetailDescription = ((RPropertyDetailsRow) this.getModel()).objectDetailDescriptions.get(rowIndex);
            tip += objectDetailDescription.toString();
        } catch (RuntimeException e1) {

        }

        return tip;
    }

    /**
     * 
     */
    @Override
    public JPopupMenu getComponentPopupMenu() {
        Point p = getMousePosition();
        // mouse over table and valid row
        if (p != null && rowAtPoint(p) >= 0) {
            this.changeSelection(rowAtPoint(p), 0, false, false);
            // condition for showing popup triggered by mouse
            return super.getComponentPopupMenu();
        }
        return super.getComponentPopupMenu();
    }
}
