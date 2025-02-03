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
package one.empty3.test.tests.tests2.balleclou;

import one.empty3.library.*;
import one.empty3.library.core.extra.Blob1;
import one.empty3.library.core.testing.TestObjetSub;
import one.empty3.libs.Color;
import one.empty3.libs.Image;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

    /***
     * @author manuel dahmen
     * 
     */
    public class TestBlob extends TestObjetSub {

        private ITexture tc = new ColorTexture(new Color(Color.red));
        private Blob1 blobC;

        public static void main(String[] args) {
            TestBlob th = new TestBlob();
            th.loop(true);
            th.setMaxFrames(1000);
            th.setGenerate(GENERATE_MOVIE | GENERATE_IMAGE | GENERATE_MODEL);
            new Thread(th).start();
        }

        @Override
        public void ginit() {

            z().setDisplayType(ZBufferImpl.SURFACE_DISPLAY_COL_QUADS);
            z().setFORCE_POSITIVE_NORMALS(true);
            try {
                tc = new ImageTexture(new Image(ImageIO.read(new File("resources/img/2018-03-31 11.51_edited.jpg"))));
            } catch (IOException ex) {
                Logger.getLogger(TestBlob.class.getName()).log(Level.SEVERE, null, ex);
            }
            blobC = new Blob1(Point3D.O0, 1.0);
            int m, n;
            m = 5;
            n = 5;
            for (int i = 0; i < m; i++)
                for (int j = 0; j < n; j++) {
                    blobC.addPoint(new Point2D(1.0 * i / m, 1.0 * j / n));
                }
            blobC.texture(tc);
            scene().add(blobC);
            //scene().lumieres().add(new LumierePonctuelle(Point3D.Z, new Color(Color.YELLOW));
            z().backgroundTexture(new ColorTexture(new Color(Color.BLACK)));
            Camera camera;
            camera = new Camera(new Point3D(0d, 0d, -2.0), new Point3D(0d, 0d, 0d));
            scene().cameraActive(camera);
        }

        @Override
        public void testScene() throws Exception {
            blobC.param(1./(frame()+1), blobC.method);
        }


    }


