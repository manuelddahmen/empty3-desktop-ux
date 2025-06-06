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

package one.empty3.apps.vecmesh;

import javax.swing.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Output {
    private static JLabel getText;

    public static void println(String string) {
        Logger.getAnonymousLogger().log(Level.INFO, string);
        if (getText != null)
            getText.setText(string == null ? "" : (string.length() <= 20 ? string : string.substring(0, 20)) +
                    string.length());
    }

    public static JLabel getGetText() {
        return getText;
    }

    public static void setGetText(JLabel getText) {
        Output.getText = getText;
    }
}
