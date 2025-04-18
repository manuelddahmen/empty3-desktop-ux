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

package one.empty3.test.tests.tests2.position;

import one.empty3.library.Barycentre;
import one.empty3.library.Point3D;
import one.empty3.library.ColorTexture;
import one.empty3.library.core.testing.jvm.TestObjetSub;
import one.empty3.library.core.tribase.TRISphere;

import one.empty3.libs.*;

/*__
 * @author Se7en
 */
public class TestTRISphere extends TestObjetSub {

    public TestTRISphere() {
    }

    public static void main(String[] args) {
        TestTRISphere ts = new TestTRISphere();
        ts.loop(false);
        ts.run();

    }

    @Override
    public void testScene() throws Exception {
        scene().cameraActive().eye().setZ(-10d);

        TRISphere s = new TRISphere(Point3D.O0, 1);
        Barycentre barycentre = new Barycentre();
        barycentre.position = Point3D.Y.mult(5d);

        s.texture(new ColorTexture(new Color(Color.WHITE.getRGB())));
        scene().add(s);

    }

    @Override
    public void finit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void ginit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
