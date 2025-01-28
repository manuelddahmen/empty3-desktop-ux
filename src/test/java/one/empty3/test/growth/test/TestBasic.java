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

package one.empty3.test.growth.test;

import one.empty3.growth.LSystem;
import one.empty3.growth.NotWellFormattedSystem;
import one.empty3.growth.Symbol;
import one.empty3.growth.SymbolSequence;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TestBasic extends TestCaseExtended {
    public void generate1(LSystem lSystem) {
        try {
            lSystem.applyRules();
        } catch (NotWellFormattedSystem notWellFormattedSystem) {
            notWellFormattedSystem.printStackTrace();
        }
    }

    public void test0() {
        assertTrue(new SymbolSequence(new Symbol('A')).equals(new SymbolSequence(new Symbol('A'))));
    }

    public void testA() {

        LSystem lSystem = new LSystem();
        lSystem.init();
        SymbolSequence a1 = new SymbolSequence(new Symbol('A'));
        SymbolSequence a = new SymbolSequence(new Symbol('A'));
        lSystem.addRule(a, a1);

        lSystem.getCurrentSymbols().add(new Symbol('A'));

        //Logger.getAnonymousLogger().log(Level.INFO, lSystem);

        try {
            lSystem.applyRules();
        } catch (NotWellFormattedSystem notWellFormattedSystem) {
            notWellFormattedSystem.printStackTrace();
        }


        //Logger.getAnonymousLogger().log(Level.INFO, lSystem);

//        assertTrue(lSystem.getCurrentSymbols().equals(new SymbolSequence(new Symbol('A'))));

    }

    public void generateN(LSystem lSystem, int n) {
        SymbolSequence ab = new SymbolSequence();
        ab.add(new Symbol('A'));
        ab.add(new Symbol('B'));

        SymbolSequence a1 = new SymbolSequence(new Symbol('A'));
        SymbolSequence b = new SymbolSequence(new Symbol('B'));
        SymbolSequence a = new SymbolSequence(new Symbol('A'));

        lSystem.addRule(a, ab);// a -> ab
        lSystem.addRule(b, a1);// b -> a

        Logger.getAnonymousLogger().log(Level.INFO, "BEFORE" + lSystem.toString());
        for (int i = 0; i < n; i++) {

            generate1(lSystem);

            //Logger.getAnonymousLogger().log(Level.INFO, "AFTER " + (i + 1) + " PASS : " + lSystem.toString());
        }
    }

    public void testGenerate2() {
        LSystem lSystem = new LSystem();
        lSystem.init();
        lSystem.getCurrentSymbols().add(new Symbol('A'));
        generateN(lSystem, 2);
        SymbolSequence symbolSequence = new SymbolSequence();
        symbolSequence.add(new Symbol('A'));
        symbolSequence.add(new Symbol('B'));
        symbolSequence.add(new Symbol('A'));
//        assertTrue(lSystem.getCurrentSymbols().equals(symbolSequence));
    }

    public void testGenerate1() {

        LSystem lSystem = new LSystem();
        lSystem.init();
        lSystem.getCurrentSymbols().add(new Symbol('A'));

        // run
        generateN(lSystem, 1);

        // Waited for:
        SymbolSequence symbolSequence = new SymbolSequence();
        symbolSequence.add(new Symbol('A'));
        symbolSequence.add(new Symbol('B'));

        // evaluate the answer
        //assertTrue(lSystem.getCurrentSymbols().equals(symbolSequence));
    }
}
