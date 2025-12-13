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

package one.empty3.apps.sculpt;

import one.empty3.library.Config;
import one.empty3.libs.Image;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.UUID;

public class UX {
    protected String defaultFilePath = new Config().getMap().getOrDefault("defaultForHeightMapDesiger", "./imageMap-"+ (new Date().getTime())+"-"
            +"-"+ UUID.randomUUID()+"-.jpg");
    protected File imageMap = new File(defaultFilePath);


    /**
     * Retrieves the image map file used within the class.
     *
     * @return the File object representing the image map.
     */
    public File getImageMap() {
        return imageMap;
    }

    /**
     * Updates the image map file used in the class with the specified file.
     *
     * @param imageMap the File object representing the new image map to be used
     */
    public void setImageMap(File imageMap) {
        this.imageMap = imageMap;
    }

    /**
     * Retrieves an image object created using the specified image map.
     *
     * @return an Image object initialized with the imageMap data
     */
    public Image getImage() {
        return new Image(imageMap);
    }

    /**
     * Sets the provided image by writing its contents to the default file path
     * specified in the class configuration.
     *
     * @param image the Image object to be saved to the specified file path
     * @throws RuntimeException if the file cannot be created or written to
     */
    public void setImage(Image image) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(defaultFilePath);
            image.toOutputStream(fileOutputStream);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
