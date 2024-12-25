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

package one.empty3.apps.lasttests;

import one.empty3.library.*;
import one.empty3.library.core.nurbs.CourbeParametriquePolynomialeBezier;
import one.empty3.library.core.nurbs.FctXY;
import one.empty3.library.core.testing.Resolution;
import one.empty3.library.core.testing.TestObjetSub;
import one.empty3.library.core.tribase.Tubulaire3;

import one.empty3.library.Point;
import one.empty3.libs.*;
import java.awt.Dimension;
import java.io.File;

public class TestTubeVisit extends TestObjetSub {
    private static final int NUM_FRAMES_1PTS = 100;
    private static int TOTAL_POINTS = 30;
    private static int ACTUAL_POINTS = 8;
    private final Tubulaire3 tubulaire3refined= new Tubulaire3();
    private int currentPoint = 0;

    public static void main(String[] args) throws Exception {
        TestTubeVisit testTubeVisit = new TestTubeVisit();
        testTubeVisit.setMaxFrames(1);
        testTubeVisit.setResolution(Resolution.HD1080RESOLUTION.x(), Resolution.HD1080RESOLUTION.y());
        testTubeVisit.loop(true);

        new Thread(testTubeVisit).start();
    }

    @Override
    public void ginit() {
        super.ginit();

        z().texture(new ColorTexture(java.awt.Color.BLACK.getRGB()));
        z().setFORCE_POSITIVE_NORMALS(true);
        z().setDisplayType(ZBufferImpl.SURFACE_DISPLAY_TEXT_QUADS );
        CourbeParametriquePolynomialeBezier elem = tubulaire3refined.getSoulCurve().getElem();
        for(int i=0; i<10; i++) {
            elem.getCoefficients().setElem(Point3D.random(1000.0), i);
            currentPoint++;
            setMaxFrames(getMaxFrames()+NUM_FRAMES_1PTS);
        }
        tubulaire3refined.texture(new ImageTexture(new File("resources/sol_sableux.jpg")));
        tubulaire3refined.getDiameterFunction().setElem(new FctXY() {
            @Override
            public double result(double input) {
                return 100.0;
            }
        });
        tubulaire3refined.setIncrU(0.01);
        tubulaire3refined.setIncrV(0.01);

    }

    @Override
    public void finit() throws Exception {
        super.finit();

        double t = 1.0*frame()/getMaxFrames();

        if(t>1.*ACTUAL_POINTS/TOTAL_POINTS*2 && currentPoint<TOTAL_POINTS) {
            /*CourbeParametriquePolynomialeBezier elem = tubulaire3refined.getSoulCurve().getElem();
            elem.getCoefficients().setElem(Point3D.random(1000.0), elem.getCoefficients().getData1d().size());
            elem.getCoefficients().delete(0);
            setMaxFrames(getMaxFrames()+NUM_FRAMES_1PTS);
            currentPoint++;*/
        }

        scene().clear();
        scene().add(tubulaire3refined);
        Point3D t0 = tubulaire3refined.getSoulCurve().getElem().calculerPoint3D(t - 0.001);
        Point3D t1 = tubulaire3refined.getSoulCurve().getElem().calculerPoint3D(t);
        Camera c = new Camera(t1, t1.plus(t1.moins(t0).norme1()), Point3D.Y);
        z().scene().cameraActive(c);
        scene().cameraActive(c);
        z().camera(c);
        camera(c);
    }


}
