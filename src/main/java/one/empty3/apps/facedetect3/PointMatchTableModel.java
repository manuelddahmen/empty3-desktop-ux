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

package one.empty3.apps.facedetect3;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class PointMatchTableModel extends AbstractTableModel {
    private List<PointMatch> pointMatches = new ArrayList<>();
    private final String[] columnNames = {"Left Point", "Right Point", "Name"};

    public PointMatchTableModel() {
        this.pointMatches = new ArrayList<>();
    }

    public PointMatchTableModel(List<PointMatch> pointMatches) {
        this.pointMatches = pointMatches != null ? pointMatches : new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return pointMatches.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        PointMatch pointMatch = pointMatches.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return pointMatch.getLeftPoint();
            case 1:
                return pointMatch.getRightPoint();
            case 2:
                return pointMatch.getName();
            default:
                return null;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        PointMatch pointMatch = pointMatches.get(rowIndex);
        switch (columnIndex) {
            case 0:
                pointMatch.setLeftPoint((one.empty3.library.Point3D) aValue);
                break;
            case 1:
                pointMatch.setRightPoint((one.empty3.library.Point3D) aValue);
                break;
            case 2:
                pointMatch.setName((String) aValue);
                break;
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    public void addPointMatch(PointMatch pointMatch) {
        pointMatches.add(pointMatch);
        fireTableRowsInserted(pointMatches.size() - 1, pointMatches.size() - 1);
    }

    public List<PointMatch> getPointMatches() {
        return pointMatches;
    }

    public void setPointMatches(List<PointMatch> pointMatches) {
        this.pointMatches = pointMatches;
        fireTableDataChanged();
    }

    public void removePointMatch(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < pointMatches.size()) {
            pointMatches.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);
        }
    }

    public void clear() {
        pointMatches.clear();
        fireTableDataChanged();
    }
}