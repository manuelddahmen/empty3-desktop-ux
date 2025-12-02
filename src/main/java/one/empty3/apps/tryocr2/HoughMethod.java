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

package one.empty3.apps.tryocr2;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class HoughMethod {
    private Map<HoughLetter, Character> letterMap = new HashMap<>();

    /***
     * a folder that contains subfolders containing letters.
     * Subfolders contain images of letters.with name == char value
     */
    public void train(File rootResourceDirectory) {

    }
    public void test(BufferedImage documentatToParse) {

    }

    public Map<HoughLetter, Character> getLetterMap() {
        return letterMap;
    }

    public void setLetterMap(Map<HoughLetter, Character> letterMap) {
        this.letterMap = letterMap;
    }

}
