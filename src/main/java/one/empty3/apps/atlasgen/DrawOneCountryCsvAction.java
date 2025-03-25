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
import one.empty3.library.ITexture;
import one.empty3.library.Point2D;
import one.empty3.library.core.lighting.Colors;

import one.empty3.library.Point;
import one.empty3.libs.*;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

/*__
 * Created by Manuel Dagmen on 29-06-18.
 */
public class DrawOneCountryCsvAction implements CsvAction {
    private final ITexture tex;
    private SetMinMax.MyDim dim;
    private String country;
    private boolean firstPass = false;
    private Map<String, Color> colors = new HashMap<String, Color>();

    private Pixeler pixeler;

    public DrawOneCountryCsvAction(Pixeler pixeler,
                                   SetMinMax.MyDim dim,
                                   ITexture tex) {
        this.pixeler = pixeler;
        this.dim = dim;
        this.country = dim.getCountryCode();
        this.tex = tex;

    }

    @Override
    public void init() {

    }

    @Override
    public void processLine(CsvLine csvLine) {
        int latitudeColumn = 4;
        int longitudeColumn = 5;
        int countryCodeColumn = 8;
        String[] lineArray = csvLine.getValue();
        String countryCode = lineArray[countryCodeColumn];
        if (country.equals(countryCode)) {
            colors.computeIfAbsent(countryCode, k -> new Colors().random());
            double latitude = Double.parseDouble(lineArray[longitudeColumn]);
            double longitude = -Double.parseDouble(lineArray[latitudeColumn]);
            Dimension dimension = new Dimension(
                    pixeler.getImage().getWidth(),
                    pixeler.getImage().getHeight());
            Point2D p = dim.convert(dimension, longitude, latitude);

            Point2D pR = dim.getRatios(p);
            /*
            Logger.getAnonymousLogger().log(Level.INFO, "p" + Seriald.point2DtoString(p));
            Logger.getAnonymousLogger().log(Level.INFO, "pR" + Seriald.point2DtoString(pR));
            */
            pixeler.pixelize((int) p.x, (int) p.y,
                    new Color(tex.getColorAt(pR.x, pR.y)));

        }
    }
}
