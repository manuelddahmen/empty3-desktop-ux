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


import one.empty3.library.Point3D
import one.empty3.library.objloader.E3Model
import one.empty3.libs.Image
import java.io.File


public class ApplicationMeshMasks() {
    lateinit var lastSavedImage: String
    private val TAG: String = this.javaClass.canonicalName?.toString() ?: "Application"
    lateinit var image1: Image
    lateinit var model: E3Model
    lateinit var image3: Image
    lateinit var txt1: Map<String, Point3D>
    lateinit var txt2: Map<String, Point3D>
    lateinit var txt3: Map<String, Point3D>
    var hd_textures: Boolean = false
    var algorithm: Int = 0
    var isBezier: Boolean = false
    private var selectedImage1: Image? = null
    lateinit var activity1: MainActivity

    public fun notify(pathToFile: File) {

    }

    public fun getSelectedImage(): Image? {
        return if (selectedImage1 != null)
            selectedImage1
        else
            null
    }

    public fun setSelectedImage(image: Image) {
        selectedImage1 = image
    }



    companion object {
        private var instance: ApplicationMeshMasks? = null

        @Synchronized
        fun getInstance(): ApplicationMeshMasks {
            return instance ?: ApplicationMeshMasks().also {
                instance = it
            }
        }
    }

}