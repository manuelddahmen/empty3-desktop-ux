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

package one.empty3;


import one.empty3.libs.commons.IImageMp;
import org.jetbrains.annotations.NotNull;

import java.io.File;

import one.empty3.libs.Image;

public class ImageIO {

    public static @NotNull Image read(@NotNull File in) {
        Image i = (Image) Image.getFromFile(in);
        i.loadFile(in);
        return i;
    }


    public static void write(@NotNull Image bitmap, String jpg, File out) {
        if (!out.getParentFile().exists())
            out.getParentFile().mkdirs();

        new Image(bitmap).saveFile(out);
    }

    public static void write(@NotNull Image bitmap, String jpg, File out, boolean shouldOverwrite) {
        if (!out.getParentFile().exists()) {
            out.getParentFile().mkdirs();
            new Image(bitmap).saveFile(out);
        }
    }
}
