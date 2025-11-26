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

package one.empty3.apps.feature;

import one.empty3.feature.*;


import one.empty3.apps.feature.FTPProcessFiles;

import java.io.File;

public class Run extends Thread {
    static String[] args = new String[]{"originalHarris_PasteBlank"};

    @Override
    public void run() {
        String settingsPropertiesPath = "sets/";
        for (int i = 0; i < args.length; i++) {

            File file = new File(settingsPropertiesPath + File.separator + (args[i]));
            if (file.exists()) {
                FTPProcessFiles.settingsPropertiesPath = file.getAbsolutePath();
                FTPProcessFiles.loadArgsProps(FTPProcessFiles.settingsPropertiesPath);
                FTPProcessFiles.defaultProcess(file);
            }
        }
    }

    public static void main(String[] args0) {
        if (args0.length > 0)
            args = args0;
        new Run().start();
    }
}
