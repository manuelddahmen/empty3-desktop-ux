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

package one.empty3.apps.feature.app.replace.javax.imageio;

//import android.graphics.BufferedImage;
//import android.graphics.BufferedImageFactory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Deprecated
public class ImageIO {
    public static BufferedImage read(File file) {
        try {
            javax.imageio.ImageIO.read(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            if (!file.isDirectory()) {
                if (file.exists()) {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    BufferedImage bufferedImage2 = javax.imageio.ImageIO.read(fileInputStream);
                    fileInputStream.close();
                    fileInputStream = null;
                    return bufferedImage2;
                } else {
                    Logger.getAnonymousLogger().log(Level.SEVERE, "File can't read (ImageIO.read) File can't exist");
                }
            }
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static boolean write(BufferedImage imageOut, String jpg, File out) {

        try {
            javax.imageio.ImageIO.write(imageOut, jpg, out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
        /*
        try {
            if (!ClassSchemaBuilder.getParentFile(out).exists()) {
                ClassSchemaBuilder.getParentFile(out).mkdirs();
            }

            if (!out.exists()) {
                //out.createNewFile();
            }

            FileOutputStream fileOutputStream = new FileOutputStream(out);
            // ???

            javax.imageio.new one.empty3.libs.Image(imageOut).saveFile( fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            fileOutputStream = null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;*/
    }
    /*
    public static BufferedImage read(File file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedImage BufferedImage = BufferedImageFactory.decodeStream(fileInputStream);
            fileInputStream.close();
            return BufferedImage;
        
        return null;
    }

    public static boolean write(BufferedImage imageOut, String jpg, File out) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(out);
        imageOut.compress(BufferedImage.CompressFormat.JPEG, 10, fileOutputStream);
        fileOutputStream.close();
        return false;
    }*/
}
