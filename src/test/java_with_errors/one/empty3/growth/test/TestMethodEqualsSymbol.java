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

package one.empty3.growth.test;

import junit.framework.TestCase;
import one.empty3.growth.Symbol;
import one.empty3.growth.SymbolSequence;

public class TestMethodEqualsSymbol extends TestCase {
    public void testEmpty() {
        SymbolSequence a = new SymbolSequence();
        SymbolSequence b = new SymbolSequence();

        assertTrue(a.equals(b));
    }

    public void testEmpty2() {
        assertFalse(new SymbolSequence().equals(new SymbolSequence(new Symbol('a'))));
    }

    public void test1symbolDiff() {
        SymbolSequence a = new SymbolSequence(new Symbol('a'));
        SymbolSequence b = new SymbolSequence(new Symbol('b'));

        assertFalse(a.equals(b));
    }

    public void test1symbolId() {
        SymbolSequence a = new SymbolSequence(new Symbol('a'));
        SymbolSequence b = new SymbolSequence(new Symbol('a'));

        assertTrue(a.equals(b));
    }
}
