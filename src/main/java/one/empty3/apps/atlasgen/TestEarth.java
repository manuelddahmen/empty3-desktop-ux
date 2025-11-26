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


import one.empty3.csv.CsvReader;

import java.io.File;

/*__
 * Created by manue on 28-04-19.
 */
public class TestEarth extends TestSoS {


    private CsvReader csvReader;

    public static void main(String[] args) {
        TestEarth testEarth = new TestEarth();
        testEarth.setResolution(800, 600);
        testEarth.setQuadrillage(false);
        //testEarth.addAudioFile(new File("res/mp3/Louis Armstrong - What a wonderful world  ( 1967 ).mp3"));
        new Thread(testEarth).start();
    }

    public void ginit() {
        super.ginit();
        setMaxFrames(360 * list.length);
        csvReader = new CsvReader(new File("allCountries/allCountries.txt"),
                "" + '\t', "" + '\n', false);
        ///csvReader.setAction(new DrawPerCountryCsvActionSphere(this));


    }

    public void finit() {
        try {
            super.finit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testScene() {
        //csvReader.process();
        //Logger.getAnonymousLogger().log(Level.INFO, "rendering now!");
    }
}