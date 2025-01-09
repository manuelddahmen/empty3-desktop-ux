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

package one.empty3.test.library.lang;

import org.junit.jupiter.api.Test;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParseCodeTest {
    @Test
    public void testMain() {
        ParseCode.main(null);
    }

    @Test
    public void testComment() {
        ParseCode parseCode = new ParseCode();
        parseCode.setBrut("/* */ \n//\n /* */");
        parseCode.parseTokensToTree();
        Logger.getAnonymousLogger().log(Level.INFO, parseCode.tree.toString());

        assertEquals(parseCode.uncommented, (""));
    }

    @Test
    public void testCompilerSimple1() {
        ParseCode parseCode = new ParseCode();
        parseCode.setBrut("/* Entry point */\nEntryPoint() {return 1;}");
        parseCode.parseTokensToTree();
        assertEquals(parseCode.uncommented, ("EntryPoint() {return 1;}"));
    }
}