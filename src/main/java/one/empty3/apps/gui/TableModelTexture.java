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

import one.empty3.apps.gui.MyObservableList;
import one.empty3.library.ITexture;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by manue on 25-07-19.
 */
public class TableModelTexture implements TableModel {
    TableModelListener listener;
    private int rows;

    public ArrayList<ModelLine> getLines() {
        return lines;
    }

    /**
     * Created by manue on 25-07-19.
     */
    static class ModelLine {
        private File file;
        private ITexture iTexture;
        private Class clazz;

        public ModelLine(File file, ITexture iTexture, Class clazz) {
            this.file = file;
            this.iTexture = iTexture;
            this.clazz = clazz;
        }

        public File getFile() {
            return file;
        }

        public void setFile(File file) {
            this.file = file;
        }

        public ITexture getiTexture() {
            return iTexture;
        }

        public void setiTexture(ITexture iTexture) {
            this.iTexture = iTexture;
        }

        public Class getClazz() {
            return clazz;
        }

        public void setClazz(Class clazz) {
            this.clazz = clazz;
        }

        public void set(int columnIndex, Object aValue) {
            if (columnIndex == 0) {
                if (aValue instanceof File) {
                    this.file = (File) aValue;
                } else if (aValue instanceof ITexture)
                    this.iTexture = (ITexture) aValue;
            } else if (columnIndex == 1) {
                if (aValue instanceof Class) {
                    this.clazz = (Class) aValue;
                }
            }
        }

        public Object get(int columnIndex) {
            return columnIndex == 0 ? (getFile() == null ? getiTexture() : getFile()) : getClazz();

        }

    }

    private ArrayList<ModelLine> lines = new ArrayList<>();
    private final MyObservableList<File> listFile = new MyObservableList<>();
    private final MyObservableList<ITexture> listText = new MyObservableList<>();

    public MyObservableList<File> getListFile() {
        return listFile;
    }

    public MyObservableList<ITexture> getListText() {
        return listText;
    }

    public TableModelTexture() {
        initTable();
    }

    public void initTable() {
//        lines.clear();
//        index = 0;
//        listText.forEach(iTexture -> {
//            lines.add(new ModelLine(null, iTexture, ITexture.class));
//            rows++;
//            setValueAt(iTexture, index, 0);
//            setValueAt(iTexture!=null?iTexture.getClass():null, index, 1);
//            index++;
//        });
//        listFile.forEach(file -> {
//            lines.add(new ModelLine(file, null, File.class));
//            setValueAt(file, index, 0);
//            setValueAt(file!=null?file.getClass():null, index, 1);
//            rows++;
//            index++;
//        });
        Logger.getAnonymousLogger().log(Level.INFO, "Lines count =  " + getRowCount());
        if (lines.size() > 0) {
            //      listener.tableChanged(new TableModelEvent(this, 0, lines.size(), 0, 2));
        }
    }

    @Override
    public int getRowCount() {
        return lines.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnIndex == 0 ? "Texture" : "Text type";
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnIndex == 0 ? Object.class : Class.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return lines.get(rowIndex).get(columnIndex);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        lines.get(rowIndex).set(columnIndex, aValue);
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        this.listener = l;
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        this.listener = null;
    }
}
