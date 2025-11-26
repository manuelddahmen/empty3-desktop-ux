/*
 *
 *  *
 *  *  * Copyright (c) 2025. Manuel Daniel Dahmen
 *  *  *
 *  *  *
 *  *  *    Copyright 2024 Manuel Daniel Dahmen
 *  *  *
 *  *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *    you may not use this file except in compliance with the License.
 *  *  *    You may obtain a copy of the License at
 *  *  *
 *  *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  *    Unless required by applicable law or agreed to in writing, software
 *  *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *    See the License for the specific language governing permissions and
 *  *  *    limitations under the License.
 *  *
 *  *
 *
 *
 *
 *  * Created by $user $date
 *
 *
 */

package one.empty3.apps.atlasgen;

import one.empty3.csv.CsvAction;
import one.empty3.csv.CsvLine;
import one.empty3.library.core.lighting.Colors;

import one.empty3.library.Point;
import one.empty3.libs.*;

import java.awt.Dimension;
import java.util.HashMap;

/*__
 * Created by Manuel Dahmen on 30-06-18.
 */
public class FirstPassColoredMapsCsvAction implements CsvAction {
    private Pixeler pixeler;
    private HashMap<String, Color> colors = new HashMap<>();

    @Override
    public void init() {
        this.pixeler = pixeler;
    }

    @Override
    public void processLine(CsvLine csvLine) {
        int lattitudeColumn = 4;
        int longitudeColumn = 5;
        int countryCodeColumn = 8;
        String[] lineArray = csvLine.getValue();
        String countryCode = lineArray[17];
        /*for(int i=0; i<lineArray.length; i++)
            if(lineArray[i]!=null)
            Logger.getAnonymousLogger().log(Level.INFO, ""+i+"  "+lineArray[i]);
*/
        if (colors.get(countryCode) == null) {
            colors.put(countryCode, new Colors().random());
        }
        pixeler.pixelize(
                (int) ((Double.parseDouble(lineArray[longitudeColumn]) / 180 + 1) / 2 * pixeler.getImage().getWidth()),
                (int) ((-Double.parseDouble(lineArray[lattitudeColumn]) / 90 + 1) / 2 * pixeler.getImage().getHeight()),
                colors.get(countryCode)
        );
    }
}
