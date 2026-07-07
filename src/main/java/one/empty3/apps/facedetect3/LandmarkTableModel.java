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
