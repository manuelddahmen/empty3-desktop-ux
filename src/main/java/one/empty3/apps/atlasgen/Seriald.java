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

import one.empty3.library.Point2D;

import javax.swing.text.NumberFormatter;
import java.io.File;
import java.text.DecimalFormat;

/*__
 * Created by Win on 06-07-18.
 */
public class Seriald {
    public static NumberFormatter numberFormatter;

    static {
        DecimalFormat decimalFormat = new DecimalFormat("0000000");
        numberFormatter
                = new NumberFormatter();
        numberFormatter.setFormat(decimalFormat);


    }

    public static String newSerial() {
        return numberFormatter.getFormat().format((int) (Math.random() * 10000000));
    }

    public static File newOutputFile(String classname) {
        File file = new File(".\\generated_Maps\\" + classname + "\\map-" + Seriald.newSerial() + ".jpg");
        file.mkdirs();
        return file;
    }

    public static String point2DtoString(Point2D p) {
        return "Point2D(" + p.getX() + ", " + p.getY() + " )";
    }

    public static String newOutputFileMulti(String classname, String countryCode) {
        String fileStr = "" + classname + "\\" + countryCode + "\\map-" + Seriald.newSerial() + ".jpg";
        File file = new File(fileStr);
        file.mkdirs();
        return fileStr
                ;
    }
}
