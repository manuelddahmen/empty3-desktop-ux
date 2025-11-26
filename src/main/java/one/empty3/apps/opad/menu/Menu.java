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

package one.empty3.apps.opad.menu;

import java.util.ArrayList;
import java.util.List;


/*__
 * Created by manuel on 21-05-17.
 */
public class Menu {
    private MenuNavigation menuNavigation;
    public List<MenuItem> items = new ArrayList<MenuItem>();

    public List<MenuItem> getItems() {
        return items;
    }

    public void setItems(List<MenuItem> items) {
        this.items = items;
    }

    public MenuNavigation getMenuNavigation() {
        return menuNavigation;
    }

    public void setMenuNavigation(MenuNavigation menuNavigation) {
        this.menuNavigation = menuNavigation;
    }

    public Menu() {
        menuNavigation = new MenuNavigation();
    }
}
