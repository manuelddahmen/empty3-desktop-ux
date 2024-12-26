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

package one.empty3.apps.feature.gui;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.io.File;
import java.util.*;
import java.util.function.Predicate;

public class FileSystemViewModel implements TreeModel {
    TreeMap<String, File> fileTreeMap;
    private final File file;

    public FileSystemViewModel(File file) {
        this.file = file;
    }

    @Override
    public Object getRoot() {
        return file;
    }

    @Override
    public Object getChild(Object parent, int index) {
        return fileTreeMap.get(parent).list()[index];
    }

    @Override
    public int getChildCount(Object parent) {
        return fileTreeMap.get((String) parent).list().length;
    }

    @Override
    public boolean isLeaf(Object node) {
        return fileTreeMap.get((String) node).list().length == 0;
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        fileTreeMap.put(path.toString(), (File) newValue);
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        File file = fileTreeMap.get(parent);
        if (file.isDirectory() && Arrays.stream(file.list()).anyMatch(s -> s.equals(child))) {
            Iterator<String> iterator = Arrays.stream(file.list()).iterator();
            int index = 0;
            while (iterator.hasNext()) {
                String next = iterator.next();
                if (child.equals(next)) {
                    return index;
                }
                index++;
            }
        }
        return -1;
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {

    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {

    }
}
