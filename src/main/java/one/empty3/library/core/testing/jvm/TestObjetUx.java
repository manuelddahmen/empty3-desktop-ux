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

/*
 *

/*
 * 2013-2020 Manuel Dahmen
 */
package one.empty3.library.core.testing.jvm;

import com.formdev.flatlaf.FlatDarkLaf;
import one.empty3.apps.testobject.IShowTestResult;
import one.empty3.apps.testobject.TestObjet;
import one.empty3.library.core.testing2.ShowTestResult;
import org.jcodec.api.awt.AWTSequenceEncoder;
import org.jcodec.common.io.FileChannelWrapper;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.io.SeekableByteChannel;
import org.jcodec.common.model.Rational;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.logging.Logger;


/**
 * The TestObjetUx class provides functionality for testing objects in the library.
 *
 * @author Manuel DAHMEN
 * Created: 15-04-2014
 * Updated: 04-02-2024
 */
public abstract class TestObjetUx extends TestObjet {
    protected FileChannelWrapper out;
    HomeEncoder encoder;
    GraphicalUserInterface gui = new GraphicalUserInterface();
    protected static Logger logger = Logger.getLogger(TestObjetUx.class.getName());


    {
        strClass = one.empty3.library.core.testing2.ShowTestResult.class;
        FlatDarkLaf.setup();
    }
    /**
     * Publishes the test results if the publish flag is set to true.
     * <p>
     * This method creates a new thread to publish the test results by calling the {@link one.empty3.library.core.testing2.ShowTestResult} class,
     * passing in the necessary parameters. The publishing process includes setting the image container, the test object, and starting the thread for execution.
     * </p>
     * <p>
     * This method does not return any value.
     * </p>
     * <p>
     * <b>Note:</b> The publish flag needs to be set to true in order for the test results to be published.
     * </p>
     */
    public void publishResult() {
        if (publish) {

            str =(IShowTestResult)( new ShowTestResult((ri)));
            str.setImageContainer(biic);
            ((ShowTestResult)str).setTestObjet(this);
            new Thread(((ShowTestResult)str)).start();
        }
    }

    public void startNewMovie() {
        idxFilm++;
        avif = new File(this.dir.getAbsolutePath() + File.separator
                + sousdossier + this.getClass().getName() + "__" + filmName + idxFilm + ".mpg");

        out = null;
        try {
            encoder = new HomeEncoder(avif);
        } catch (IOException ioException) {
            ioException.printStackTrace();
            NIOUtils.closeQuietly(out);
        }
        aviOpen = true;
    }

    public void setDimension(Resolution resolution) {
        this.dimension = new one.empty3.apps.testobject.Resolution(resolution.x, resolution.y);

    }

    public static class HomeEncoder {
        private AWTSequenceEncoder encoder;
        private boolean initialized = false;
        public HomeEncoder(File avif) throws IOException {
            SeekableByteChannel out = null;
            try {
                out = NIOUtils.writableFileChannel(avif.getAbsolutePath());
                // for Android use: AndroidSequenceEncoder
                 this.encoder = new AWTSequenceEncoder(out, Rational.R(25, 1));
                 initialized = true;
                // Finalize the encoding, i.e. clear the buffers, write the header, etc.
            } catch (RuntimeException ex){
                initialized = false;
                NIOUtils.closeQuietly(out);
            }
        }

        public void encodeImage(BufferedImage bi) throws IOException {
            if(initialized) {
                encoder.encodeImage(bi);
            }
        }

        public void finish() throws IOException {
            if(initialized)
                encoder.finish();
        }

    }
    public abstract void afterRenderFrame();

    public abstract void finit() throws Exception;

    public abstract void ginit();

    public abstract void afterRender();

    public abstract void testScene() throws Exception;
}