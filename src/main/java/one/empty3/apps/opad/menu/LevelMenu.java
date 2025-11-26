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

import java.util.logging.Level;
import java.util.logging.Logger;

/*__
 * Created by manuel on 21-05-17.
 */
public class LevelMenu {
    private final String[] level = new String[]{
            "SolPlan",
            "SolRelief",
            "SolReliefMouvant",
            "SolSphere",
            "SolTube"};
    private int index = 0;
    private int maxIndex = level.length;

    public String[] getLevel() {
        return level;
    }

    public void setLevel(String[] level) {
        level = level;
    }

    public Class loadClass() {
        String package1 = "one.empty3.apps.opad.";
        String solClass = package1 + (getLevel()[index]);
        try {
            return Class.forName(solClass);
        } catch (ClassNotFoundException e) {
            System.err.println("ClassNotFoundException - Error: " + solClass);
            System.exit(1);
        } catch (NoClassDefFoundError e) {
            System.err.println("NoClassDefFoundError - Error: " + solClass);
            System.exit(1);
        }
        return null;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index % maxIndex;
        if (index < 0)
            this.index = maxIndex - 1;
        Logger.getAnonymousLogger().log(Level.INFO, "" + index);
    }

}
