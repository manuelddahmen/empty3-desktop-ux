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

package one.empty3.apps.feature.gui;

import org.apache.commons.net.ftp.FTPFile;

import java.awt.image.BufferedImage;
import java.io.File;

public class FDEntity {
    public enum type {
        FTPFile, FTPFolder, File, Folder, BufferedImage
    }

    public BufferedImage load(FTPFile ftpFile) {
        return null;
    }

    public BufferedImage load(File file) {
        return null;
    }

    public BufferedImage load(BufferedImage originalImage) {
        return null;
    }

}
