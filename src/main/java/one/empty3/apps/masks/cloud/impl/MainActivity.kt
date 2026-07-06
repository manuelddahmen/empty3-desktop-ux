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

import java.nio.charset.Charset
import one.empty3.library.objloader.E3Model
import one.empty3.libs.Image
import java.awt.image.BufferedImage
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileReader
import java.io.FileOutputStream // Added for saveImageToPicturesLegacy
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Base64
import java.util.Date
import java.util.logging.Level
import java.util.logging.Logger
import javax.imageio.ImageIO

public open class MainActivity {
    private var Log: Logger

    private var service: Boolean = true
    val CHANNEL = "one.empty3.apps.masks/renderImages1"
    lateinit var landmarks: Array<Array<Array<String>>>
    val TAG: String = "one.empty3.apps.masks.MainActivity"
    public val SAVED_IMAGE_REQUEST_CODE: Int = 46868

    init {
        Log = Logger.getLogger(this.javaClass.canonicalName)
    }

    fun saveImageToPicturesLegacy(bitmap: BufferedImage, filename: String): Boolean {
        try {
            val directory = File("/tmp")
            if (!directory.exists()) {
                directory.mkdirs() // Create the directory if it doesn't exist
            }
            var file = File(
                directory,
                filename + directory.listFiles().size + "_${'$'}{System.currentTimeMillis()}" + ".jpg"
            )
            Logger.getAnonymousLogger().info("saveImageToPicturesLegacy: ${'$'}{file.absolutePath}")
            if (file.exists()) {
                // Decide on a strategy: overwrite, new name, or return false
                // For now, let's try a slightly different name to avoid collision if called rapidly
                file = File(
                    directory,
                    filename + directory.listFiles().size + "_${'$'}{System.currentTimeMillis()}_new" + ".jpg"
                )
                if (file.exists()) return false // If still exists, then skip.
            }
            FileOutputStream(file).use { outputStream ->
                ImageIO.write(bitmap, "jpg", outputStream)
                outputStream.flush()
            }
            // ApplicationMeshMasks.getInstance().lastSavedImage = file.absolutePath
            // ApplicationMeshMasks.getInstance().setSelectedImage(Image(bitmap)) // This might be redundant or specific to another flow
            return true
        } catch (e: Exception) { // Catch generic Exception as file operations can throw various issues
            Log.severe( "Error writing image: ${'$'}{e.message}")
            return false
        }
    }


    var processImage: Image? = null
    var filename: String? = null

    fun renderImages1_1(
        image1Bytes: ByteArray?, model: ByteArray?, modelPath: ByteArray?, image3Bytes: ByteArray?,
        text1: ByteArray?, text2: ByteArray?, text3: ByteArray?,
        algorithm: Int, hdTextures: Boolean, isBezier: Boolean,
        settings: Map<String, *>?,
        userId: String? = "default"
    ): ByteArray? {
        try {

            Log.info("MainActivity: Rendering images for userId: $userId")
            var bitmapImage1: Image? = null
            var bitmapImage3: Image? = null
            var textContent1: String = ""
            var textContent2: String = ""
            var textContent3: String = ""
            if (image1Bytes != null) {
                bitmapImage1 = Image(
                    decodeByteArray(
                        image1Bytes,
                        0,
                        image1Bytes.size
                    )
                )
            } else
                bitmapImage1 = Image(200, 200)
            if (image3Bytes != null) {
                try {
                    bitmapImage3 = Image(
                        decodeByteArray(
                            image3Bytes,
                            0,
                            image3Bytes.size
                        )
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    bitmapImage3 = null
                }
            } else
                bitmapImage3 = Image(200, 200)
            if (text1 != null) {
                textContent1 = text1.toString(Charset.defaultCharset())
                Log.info("Attempting to read text1: $textContent1")
            }
            if (text2 != null) {
                textContent2 = text2.toString(Charset.defaultCharset())
                Log.info("Attempting to read text2: $textContent2")
            }
            if (text3 != null) {
                textContent3 = text3.toString(Charset.defaultCharset())
                Log.info("Attempting to read text3: $textContent3")

            }
            Log.severe("text1: " + text1.toString())
            Log.severe( "text1content " + textContent1)
            Log.severe( "text2: " + text2.toString())
            Log.severe( "text2content " + textContent2)
            Log.severe( "text3: " + text3.toString())
            Log.severe( "text3content " + textContent3)
            val e3Model: E3Model
            ///val bufferedReader = BufferedReader(StringReader(model.toString()))
            if (model != null)
                Log.severe( "model: " + model.size)
            else
                Log.severe( "model is null")
            if (modelPath != null)
                Log.severe(
                    "modelPath.exists?: " + File(modelPath.toString(Charset.defaultCharset())).exists()
                )
            else
                Log.severe( "modelPath is null")
            if (model != null && model.size > 0) {
                val tmpDir = File("/tmp")
                if (!tmpDir.exists()) tmpDir.mkdirs()
                val file = File.createTempFile("output", ".obj", tmpDir)
                file.deleteOnExit()
                file.writeBytes(model)
                val bufferedReader = BufferedReader(FileReader(file))
                Log.info( "MainActivity: Created temp model file from bytes: ${file.absolutePath}")
                e3Model = E3Model(bufferedReader, true, file.absolutePath)
            } else if (modelPath != null && modelPath.size > 0) {
                val modelPath1 = String(modelPath, Charset.defaultCharset())
                Log.info("MainActivity: Trying to load model from path: $modelPath1")
                val modelFile = File(modelPath1)
                if (modelFile.exists()) {
                    val bufferedReader = BufferedReader(FileReader(modelFile))
                    e3Model = E3Model(bufferedReader, true, modelPath1)
                } else {
                    throw RuntimeException("Model path does not exist on server: $modelPath1")
                }
            } else {
                throw RuntimeException("No model data (model) and no valid model path (modelPath) provided in request")
            }

            Log.severe( "bitmapImage1: " + (bitmapImage1 != null))
            Log.severe( "model: " + (e3Model.listRepresentable.size))
            Log.severe( "modelPath: " + (modelPath != null))
            Log.severe( "bitmapImage3: " + (bitmapImage3 != null))
            Log.severe( "text1: " + (text1 != null && text1.size > 0))
            Log.severe( "text2: " + (text2 != null && text2.size > 0))
            Log.severe( "text3: " + (text3 != null && text3.size > 0))
            Log.severe( "hdTextures: " + (hdTextures != null))
            Log.severe( "algorithm: " + (algorithm != null && algorithm >= 0))
            Log.severe( "isBezier: " + (isBezier))
            Log.severe( "settings: " + (settings != null))
            
            Log.severe( "process image with ProcessData")
            processImage = null

            val runZBuffer =
                RunZBuffer(
                    bitmapImage1,
                    e3Model,
                    bitmapImage3,
                    textContent1,
                    textContent2,
                    textContent3,
                    hdTextures,
                    algorithm,
                    isBezier,
                    service, settings
                )


            Log.severe( "process image with RunZBuffer")
            //val render = Thread({
            runZBuffer.mActivity = this
            val useMaxRes = settings?.get("useMaxRes")?.toString()?.toBoolean() ?: false
            val maxResWidth = settings?.get("maxResWidth")?.toString()?.toIntOrNull() ?: 1000
            val maxResHeight = settings?.get("maxResHeight")?.toString()?.toIntOrNull() ?: 1000

            //if(useMaxRes) {
            //   processImage = runZBuffer.processImage(maxResWidth, maxResHeight)
            //} else {
            processImage = runZBuffer.processImage()

            //}
            if (processImage == null)
                throw RuntimeException("Rendering process returned null image")
            
            Log.severe( "result: " + (processImage!!.bi != null))

            if (processImage != null && processImage!!.bi != null) {
                val df: DateFormat = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
                val date: String = df.format(Date())
                val filename =
                    "autosave_" + date + "_" + processImage!!.width + "x" + processImage!!.height + ".png"
            }

            val byteArrayOutputStream = ByteArrayOutputStream(10)
            ImageIO.write(processImage!!.bi, "png", byteArrayOutputStream)


            return byteArrayOutputStream.toByteArray()

        } catch (e: Throwable) {
            Log.severe("Error in renderImages1_1: ${e.message}")
            e.printStackTrace()
            throw e
        }
        throw RuntimeException("Unknown error in renderImages1_1: model or image might be missing")
    }


    fun writeMediaToGoogleStorage(content:ByteArray? , filename: String) {

        if (content == null) {
            return
        }

    }

    private fun decodeByteArray(image1Bytes: ByteArray, i: Int, size: Int) : BufferedImage{
        val read = ImageIO.read(ByteArrayInputStream(image1Bytes))
        return read

    }



}


