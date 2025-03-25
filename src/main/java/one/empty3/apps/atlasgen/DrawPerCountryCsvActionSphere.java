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

package one.empty3.apps.atlasgen;

import one.empty3.csv.CsvAction;
import one.empty3.csv.CsvLine;
import one.empty3.library.ColorTexture;
import one.empty3.library.Point3D;
import one.empty3.library.core.lighting.Colors;
import one.empty3.library.core.nurbs.PcOnPs;
import one.empty3.library.core.nurbs.Point3DS;

import one.empty3.library.Point;
import one.empty3.libs.*;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/*__
 * Created by Manuel Dagmen on 29-06-18.
 */
public class DrawPerCountryCsvActionSphere implements CsvAction {
    int countLine = 0;
    private final TestEarth testEarth;
    private boolean firstPass = false;
    private Map<String, Color> colors = new HashMap<String, Color>();

    private Pixeler pixeler;

    public DrawPerCountryCsvActionSphere(TestEarth testEarth) {
        this.testEarth = testEarth;
    }


    @Override
    public void init() {

    }

    @Override
    public void processLine(CsvLine csvLine) {
        //if (countLine % 1000 == 0) {
        int lattitudeColumn = 4;
        int longitudeColumn = 5;
        int countryCodeColumn = 8;
        String[] lineArray = csvLine.getValue();
        String countryCode = lineArray[countryCodeColumn];
        colors.computeIfAbsent(countryCode, k -> new Colors().random());
        double lng = (Double.parseDouble(lineArray[longitudeColumn]) / 180 + 1) / 2;
        double lat = Double.parseDouble(lineArray[lattitudeColumn]) / 90;
        Point3DS d = new Point3DS(new Point3D(
                lng, lat, 0d));
        d.texture(new ColorTexture(colors.get(countryCode)));
        PcOnPs pcOnPs = new PcOnPs(testEarth.getSphere(), d);
        pcOnPs.texture(new ColorTexture(colors.get(countryCode)));
        pcOnPs.getParameters().setIncrU(1.0);
        pcOnPs.getParameters().setEndU(0.1);
        pcOnPs.setConnected(false);
        testEarth.getZ().draw(pcOnPs);
        if (countLine % 100000 == 0) {
            Logger.getAnonymousLogger().log(Level.INFO, "Lines" + countLine);
            Logger.getAnonymousLogger().log(Level.INFO, ""+d.calculerPoint3D(0.0));
        }
        //}
        countLine++;
    }
}
