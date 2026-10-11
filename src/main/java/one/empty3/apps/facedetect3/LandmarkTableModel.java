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
 *  * Created by Manuel D Dahmen -2026
 *
 *
 */

package one.empty3.apps.facedetect3;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class LandmarkTableModel extends AbstractTableModel {
    private List<LandmarkPoint> landmarkPoints = new ArrayList<>();
    private final String[] columnNames = {"Name", "X (or U)", "Y (or V)"};

    public LandmarkTableModel() {
    }

    public void setLandmarkPoints(List<LandmarkPoint> landmarkPoints) {
        this.landmarkPoints = landmarkPoints != null ? landmarkPoints : new ArrayList<>();
        fireTableDataChanged();
    }

    public List<LandmarkPoint> getLandmarkPoints() {
        return landmarkPoints;
    }

    @Override
    public int getRowCount() {
        return landmarkPoints.size();
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
        LandmarkPoint lp = landmarkPoints.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return lp.getName();
            case 1:
                return lp.getPoint() != null ? lp.getPoint().getX() : null;
            case 2:
                return lp.getPoint() != null ? lp.getPoint().getY() : null;
            default:
                return null;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        LandmarkPoint lp = landmarkPoints.get(rowIndex);
        if (columnIndex == 0) {
            lp.setName((String) aValue);
            fireTableCellUpdated(rowIndex, columnIndex);
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 0;
    }

    public void addLandmarkPoint(LandmarkPoint lp) {
        landmarkPoints.add(lp);
        fireTableRowsInserted(landmarkPoints.size() - 1, landmarkPoints.size() - 1);
    }

    public void removeLandmarkPoint(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < landmarkPoints.size()) {
            landmarkPoints.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);
        }
    }

    public void clear() {
        landmarkPoints.clear();
        fireTableDataChanged();
    }
}
