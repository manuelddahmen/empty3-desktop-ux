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

import one.empty3.library.Representable;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by manue on 19-11-19.
 */
public class ListModelSelection extends DefaultListModel<Representable> {
    public ListModelSelection(ArrayList<Representable> selectionIn) {
        this.representables = selectionIn;
    }

    List<Representable> representables = new ArrayList<>();

    @Override
    public int getSize() {
        return representables.size();
    }

    @Override
    public Representable getElementAt(int index) {
        return representables.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {
        super.addListDataListener(l);
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        super.removeListDataListener(l);

    }

    public void add(Representable representable) {
        representables.add(representable);
    }

    /**
     *
     */
    @Override
    public void addElement(Representable o) {
        super.addElement(o);
        add(o);

    }
}
