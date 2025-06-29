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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package one.empty3.library.core.testing.jvm;

import one.empty3.apps.testobject.TestObjet;
import one.empty3.library.core.script.ExtensionFichierIncorrecteException;
import one.empty3.library.core.script.Loader;
import one.empty3.library.core.script.VersionNonSupporteeException;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/*__
 * @author Manuel DAHMEN
 */
public class TestCollection {

    private final ArrayList<TestObjetUx> tests = new ArrayList<TestObjetUx>();
    private boolean dr;

    public void add(final File fichier) {
        TestObjetUx to = new TestObjetSub() {


            @Override
            public void ginit() {
                try {
                    new Loader().load(fichier, scene());
                } catch (VersionNonSupporteeException ex) {
                    Logger.getLogger(TestCollection.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ExtensionFichierIncorrecteException ex) {
                    Logger.getLogger(TestCollection.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        };
        add(to);

    }

    public void add(File[] fichiers) {
        for (File fichier : fichiers) {
            add(fichier);
        }
    }

    public void add(TestObjetUx to) {
        tests.add(to);
    }

    public void displayResult(boolean b) {
        this.dr = b;

    }

    public void run() {
        Iterator<TestObjetUx> it = tests.iterator();
        while (it.hasNext()) {
            TestObjetUx next = it.next();
            next.publishResult(dr);
            next.run();
        }
    }

    public void testCollection() {
        Iterator<TestObjetUx> it = tests.iterator();
        while (it.hasNext()) {
            it.next().run();
        }
    }
}
