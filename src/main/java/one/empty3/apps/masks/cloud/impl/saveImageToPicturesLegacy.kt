/*
 * Copyright (c) 2026.
 *
 *
 *    Copyright 2026 Manuel Dahiel Dahmen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package one.empty3.apps.masks.cloud.impl

import one.empty3.libs.Image
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileOutputStream
import java.util.logging.Logger
import javax.imageio.ImageIO

val requestCode1: Int = 548644884

fun saveImageToPicturesLegacy( bitmap: BufferedImage, filename: String): File? {


    try {
        val directory = File("/tmp")
        val file = File("/tmp",filename)
        if (!directory.exists()) {
            directory.mkdirs() // Create the directory if it doesn't exist
        }
        FileOutputStream(file).use { outputStream ->
            ImageIO.write(bitmap, "png", outputStream)
        }
        ApplicationMeshMasks.getInstance().lastSavedImage = file.absolutePath
        ApplicationMeshMasks.getInstance().setSelectedImage(Image(bitmap))
        return file
    } catch (e: RuntimeException) {
        Logger.getAnonymousLogger().severe("SaveImageLegacy Error writing image: ${e.message}")
        return null
    }
}

