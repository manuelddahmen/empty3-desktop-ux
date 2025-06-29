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

/*__
 * Importer une autre test: ah si ca pouvait être fait par classes!
 */
package one.empty3.test.tests.tests2.repereAssocieAUneCourbeEX;

import one.empty3.library.Camera;
import one.empty3.library.EOFVideoException;
import one.empty3.library.Point3D;
import one.empty3.library.TextureMov;
import one.empty3.library.core.nurbs.CameraInPath;
import one.empty3.library.core.testing.jvm.TestObjetUx;
import one.empty3.library.core.tribase.TRIEllipsoide;

/*__
 *
 * @author Manuel Dahmen
 */
public class TestCameraEnMouvement extends TestObjetUx {

    TextureMov textureMov;
    private CameraInPath cam;
    private TRIEllipsoide e;

    public static void main(String[] args) {
        TestCameraEnMouvement t = new TestCameraEnMouvement();
        t.setGenerate(GENERATE_IMAGE | GENERATE_MOVIE);
        t.setMaxFrames(30 * 25);
        t.setResx(640);
        t.setResy(480);
        new Thread(t).start();
    }

    @Override
    public void afterRenderFrame() {

    }

    @Override
    public void finit() throws EOFVideoException {
        cam.setT(frame / 25.0 / 8);
        textureMov.nextFrame();
    }

    @Override
    public void ginit() {
        CourbeChoisie cc = new CourbeChoisie(21.0, 11.0, 11.0, 8.0);

        cam = new CameraInPath(cc);

        e = new TRIEllipsoide(Point3D.O0, 20.0, 10.0, 10.0);
        textureMov = new TextureMov("../../../Videos/animal2.mp4");
        textureMov.setTransparent(java.awt.Color.BLACK.getRGB());
        e.texture(textureMov);


        scene().add(e);

        scene().cameraActive(new Camera(new Point3D(30d, 0d, -30d), new Point3D(0d, 0d, 0d)));

        scene().cameraActive(cam);

    }

    @Override
    public void testScene() throws Exception {

    }

    public void afterRender() {

    }
}
