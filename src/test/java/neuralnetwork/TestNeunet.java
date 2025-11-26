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

//import one.empty3.library.core.math.Matrix;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Random;

@RunWith(JUnit4.class)

public class TestNeunet {
    private static Random random;

/*
    @Test
    public void testCrossEntropy() {
        double[] expectedValues = new double[]{1, 0, 0, 0, 0, 1, 0, 1, 0};
        Matrix expected = new Matrix(3, 3, i -> expectedValues[i]);

        Logger.getAnonymousLogger().log(Level.INFO, "" + expected);

        Matrix actual = new Matrix(3, 3, i -> 2 * i - 4).softmax();

        Logger.getAnonymousLogger().log(Level.INFO, "" + actual);

        Matrix result = LossFunction.crossEntropy(expected, actual);

        Logger.getAnonymousLogger().log(Level.INFO, "" + result);

        actual.forEach(((row, col, index, value) -> {
            double expectedValue = value;
            if (expectedValue >= 0.9) {

            }
        }));

    }

    @Test
    public void testNeuronSoftMax() {
        Neuron n = new Neuron(3);
        n.setW(new double[]{-7, 1, 3});
        double[] doubles = n.softMax(n.getInput(), n.getW());
        for (int i = 0; i < doubles.length; i++) {
            Logger.getAnonymousLogger().log(Level.INFO, "" + doubles[i]);
        }
    }

    @Test
    public void testFunctionAnd() {
        Net<Neuron> net = new Net<>();
        net.setInputLayer(new Layer<Neuron>(2, Neuron.class));
        net.getInputLayer().getNeurons().setElem(new Neuron(2), 0);

        net.getInputLayer().getNeurons().getElem(0).setW(new double[]{0.5, 0.5});

        net.getInputLayer().getNeurons().getElem(0).setActivationMethod(ActivationMethod.MinMax01);

        net.getInputLayer().getNeurons().getElem(0).setInput(new double[]{0., 0.});
        net.computeAll();
        assertEquals(0.0, net.getInputLayer().getNeurons().getElem(0).getOutput());
        net.getInputLayer().getNeurons().getElem(0).setInput(new double[]{0., 1.});
        net.computeAll();
        assertEquals(0.0, net.getInputLayer().getNeurons().getElem(0).getOutput());

        net.getInputLayer().getNeurons().getElem(0).setInput(new double[]{1., 0.});
        net.computeAll();
        assertEquals(0.0, net.getInputLayer().getNeurons().getElem(0).getOutput());

        net.getInputLayer().getNeurons().getElem(0).setInput(new double[]{1., 1.});
        net.computeAll();
        assertEquals(1.0, net.getInputLayer().getNeurons().getElem(0).getOutput());

        net.getInputLayer().getNeurons().getData1d().forEach(n -> Logger.getAnonymousLogger().log(Level.INFO, "" + n.getOutput()));


    }

    @Test
    public void testMatrices() {
        random = new Random();

        int numberOfInputs = 7;
        int numberNeurons = 6;
        int inputSize = 4;
        Matrix inputs = new Matrix(inputSize, numberOfInputs, 1);
        Matrix weights = new Matrix(numberNeurons, inputSize, 1);
        Matrix biases = new Matrix(numberNeurons, 1, 1);

        inputs.modify((row, col, value) -> random.nextDouble());
        weights.modify((row, col, value) -> random.nextGaussian());
        biases.modify((row, col, value) -> random.nextGaussian());

        Matrix result1 = weights.multiply(inputs).modify((row, col, value) -> value + biases.get(row));
        Matrix result2 = weights.multiply(inputs).modify((row, col, value) -> value + biases.get(row))
                .modify((index, value) -> value > 0 ? value : 0.0);

        Logger.getAnonymousLogger().log(Level.INFO, "" + inputs);
        Logger.getAnonymousLogger().log(Level.INFO, "" + weights);
        Logger.getAnonymousLogger().log(Level.INFO, "" + biases);
        Logger.getAnonymousLogger().log(Level.INFO, "" + result1);
        Logger.getAnonymousLogger().log(Level.INFO, "" + result2);

        AtomicReference<Double> softmax = new AtomicReference<>(0.0);
        result2.forEach((index, value) -> {
            softmax.updateAndGet(v -> (double) (v + Math.exp(result2.get(index))));
            //assertTrue(value-r1<Matrix.DOUBLE_MIN);
        });
        result2.modify((index, value) -> Math.exp(value) / softmax.get());
        Logger.getAnonymousLogger().log(Level.INFO, "" + result2);
    }

    @Test
    public void testMatrices1() {
        random = new Random();

        int numberOfInputs = 7;
        int numberNeurons = 6;
        int inputSize = 4;
        Matrix inputs = new Matrix(inputSize, numberOfInputs);
        Matrix weights = new Matrix(numberNeurons, inputSize);
        Matrix biases = new Matrix(numberNeurons, 1);

        inputs.modify((row, col, value) -> random.nextDouble());
        weights.modify((row, col, value) -> random.nextGaussian());
        biases.modify((row, col, value) -> random.nextGaussian());

        Matrix result1 = weights.multiply(inputs).modify((row, col, value) -> value + biases.get(row));
        Matrix result2 = weights.multiply(inputs).modify((row, col, value) -> value + biases.get(row))
                .modify((index, value) -> value > 0 ? value : 0.0);

        Logger.getAnonymousLogger().log(Level.INFO, "" + inputs);
        Logger.getAnonymousLogger().log(Level.INFO, "" + weights);
        Logger.getAnonymousLogger().log(Level.INFO, "" + biases);
        Logger.getAnonymousLogger().log(Level.INFO, "" + result1);
        Logger.getAnonymousLogger().log(Level.INFO, "" + result2);

        AtomicReference<Double> softmax = new AtomicReference<>(0.0);
        result2.forEach((index, value) -> {
            softmax.updateAndGet(v -> (double) (v + Math.exp(result2.get(index))));
            //assertTrue(value-r1<Matrix.DOUBLE_MIN);
        });
        result2.modify((index, value) -> Math.exp(value) / softmax.get());
        Logger.getAnonymousLogger().log(Level.INFO, "" + result2);
    }

*/
}
