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

import one.empty3.apps.gui.RPropertyDetailsRow;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by manue on 17-07-19.
 */
public class History {
    List<one.empty3.apps.gui.RPropertyDetailsRow> history = new ArrayList<>();

    private int current = -1;

    public List<one.empty3.apps.gui.RPropertyDetailsRow> getHistory() {
        return history;
    }

    public void clear() {
        current = -1;
        history.clear();
    }

    public void addToHistory(one.empty3.apps.gui.RPropertyDetailsRow row) {
        current++;
        history.add(current, row);
        if (history.size() >= getCurrent()) {
            for (int i = current + 1; i < history.size(); i++)
                history.remove(i);
        }
    }

    public void setHistory(List<one.empty3.apps.gui.RPropertyDetailsRow> history) {
        this.history = history;
    }


    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }


    public void next() {
        if (current < history.size() - 1)
            current++;
    }

    public void back() {
        if (current > 0)
            current--;
    }

    public Object getCurrentRow() {
        if (current < 0)
            current = 0;
        if (current >= history.size() - 1)
            current = Math.max(history.size() - 1, 0);

        try {
            return history.get(current);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public one.empty3.apps.gui.RPropertyDetailsRow get(int i) {
        if (current < history.size() - 1)
            if (current >= 0) {
                RPropertyDetailsRow rPropertyDetailsRow = history.get(i);
                return rPropertyDetailsRow;

            }
        return null;
    }
}
