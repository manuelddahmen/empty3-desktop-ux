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

package one.empty3.test.tests;

import one.empty3.library.Camera;
import one.empty3.library.LumierePonctuelle;
import one.empty3.library.Point3D;
import one.empty3.library.ZBufferImpl;
import one.empty3.library.core.testing.jvm.TestObjet;
import one.empty3.library.core.testing.jvm.TestObjetSub;
import one.empty3.library.core.tribase.Tubulaire3;

import one.empty3.libs.*;

public class TestHumainMarche2 extends TestObjetSub {
    public void tubeAddPoint(Tubulaire3 tube, Point3D p) {
        tube.getSoulCurve().getElem().getCoefficients().getData1d().add(p);
    }

    public void ginit() {
        setMaxFrames(180);
        z.setDisplayType(ZBufferImpl.DISPLAY_ALL);
        scene().lumieres().add(new LumierePonctuelle(new Point3D(10., 10., 2.), new Color(Color.BLUE)));
    }

    public void finit() {
        scene().cameras().clear();
        scene().getObjets().getData1d().clear();
        Camera c = new Camera(Point3D.Z.mult(-80.), Point3D.O0, Point3D.Y);
        scene().cameras().add(c);
        c.declareProperties();
        scene().cameraActive(c);

        Voiture voiture = new Voiture();


        HumainMarche humainMarche = new HumainMarche();

        scene().add(humainMarche);
        scene().add(voiture);

        humainMarche.setT(frame()/25.);
        humainMarche.init();
    }

    public static void main(String[] args) {
        TestHumainMarche2 testHumain = new TestHumainMarche2();
        testHumain.setPublish(true);
        testHumain.setDimension(TestObjet.HD1080);
        testHumain.setGenerate(testHumain.getGenerate()|GENERATE_MODEL);
        new Thread(testHumain).start();
    }

}

