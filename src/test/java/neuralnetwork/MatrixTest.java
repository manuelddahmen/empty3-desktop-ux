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

package one.empty3.test.neuralnetwork;
/*
import one.empty3.library.core.math.Matrix;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)

public class MatrixTest {
    @Test
    public void toStringTest() {
        Matrix m = new Matrix(3, 4, i -> i * 2);

        String text = m.toString();

        String[] rows = text.split("\n");

        double[] expected = new double[12];
        for (int i = 0; i < expected.length; i++) {
            expected[i] = i * 2;
        }
        int index = 0;
        for (String row : rows) {
            String[] values = row.split("\t");
            for (String value : values) {
                Logger.getAnonymousLogger().log(Level.INFO, value);
                double parsedValue = Double.parseDouble(value);
                index++;
            }
        }
    }

    public void testMultiplyDouble() {
        Matrix m = new Matrix(3, 4, i -> 0.5 * (i - 6));
        double x = 0.5;
//        Matrix expected = new Matrix(3,4, i-> 0.5*i)
    }

    @Test
    public void testEquals() {
        Matrix m1 = new Matrix(3, 4, i -> 0.5 * (i - 6));
        Matrix m2 = new Matrix(3, 4, i -> 0.5 * (i - 6));
        Matrix m3 = new Matrix(3, 4, i -> 0.5 * (i - 6.2));

        assertTrue(m1.equals(m2));
        assertFalse(m2.equals(m3));
    }

}
*/