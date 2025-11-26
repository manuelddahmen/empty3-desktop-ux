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

import com.jgoodies.common.collect.ObservableList;

import javax.swing.event.ListDataListener;
import java.util.ArrayList;

/**
 * Created by manue on 01-07-19.
 */
public class MyObservableList<T> extends ArrayList<T> implements ObservableList<T> {
    ArrayList<ListDataListener> listDataListeners = new ArrayList<>();

    @Override
    public int getSize() {
        return size();
    }

    @Override
    public Object getElementAt(int index) {

        return get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {
        listDataListeners.add(l);

    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        listDataListeners.remove(l);

    }

}