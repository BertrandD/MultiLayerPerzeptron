package fr.bljm.tnn;

import java.util.Random;
import java.util.stream.IntStream;

public class MultiLayerPerzeptron {
    public static long SEED = Double.doubleToLongBits(Math.random());
//    private static long SEED = 4598381177097956938L;

    public static Random RANDOM = new Random(SEED);

    private Layer[] layers;

    public MultiLayerPerzeptron(int[] layers, TransferFunction[] transferFunctions) {
        if (layers.length != transferFunctions.length)
            throw new RuntimeException("size of transferFunctions & layers array should be equal");
        if (layers.length > 4)
            throw new RuntimeException("Maximum of 4 layers (2 hidden layers) allowed");

        System.out.println("Generating MultiLayerPerzeptron with seed " + SEED);

        this.layers = new Layer[layers.length];
        this.layers[0] = new Layer(layers[0], 0, null, transferFunctions[0]);
        for (int i = 1; i < layers.length; i++) {
            if (layers[i] > 1000)
                throw new RuntimeException("Max size of a layer is 1000");

            this.layers[i] = new Layer(layers[i], layers[i - 1], this.layers[i - 1], transferFunctions[i]);
        }
    }

    private Layer getInputLayer() {
        return layers[0];
    }

    private Layer getOutputLayer() {
        return layers[layers.length - 1];
    }

    public double[] getOutput() {
        int M = getOutputLayer().getSize();
        double[] output = new double[M];
        for (int i = 0; i < M; i++) {
            output[i] = getOutputLayer().getNeuron(i).getValue();
        }
        return output;
    }

    public void execute(double[] input) {
        if (input.length != getInputLayer().getSize())
            throw new RuntimeException("input array does not have same length as init");

        Neuron[] neurons = getInputLayer().getNeurons();
        for (Neuron neuron : neurons) {
            neuron.setValue(input[neuron.getIndex()]);
        }

        for (Layer layer : layers) {
            Layer prevLayer = layer.getPrevious();
            if (prevLayer == null) continue;
            for (Neuron neuron : layer.getNeurons()) {
                double weightedSum = 0.0;
                for (int k = 0; k < prevLayer.getSize(); k++) {
                    weightedSum += prevLayer.getNeuron(k).getValue() * neuron.getWeight(k);
                }
                weightedSum += neuron.getBias();
                neuron.setWeightedSum(weightedSum);
                neuron.setValue(layer.getTransferFunction().evaluate(weightedSum));
            }
        }
    }

    public double backPropagateError(double[] input, double[] teacherOutput) {
        int M = getOutputLayer().getSize();
        if (teacherOutput.length != M) {
            throw new RuntimeException("teacher output array does not have same length as init");
        }
        // We pass the data in the network to get an output
        execute(input);
        double error, globalError = 0;
        //System.out.println("Got " + Arrays.toString(output) + " ~ Wanted " + Arrays.toString(teacherOutput));

        for (Neuron neuron : getOutputLayer().getNeurons()) {
            error = teacherOutput[neuron.getIndex()] - neuron.getValue();
            globalError += Math.abs(error);
            neuron.setDelta(error * getOutputLayer().getTransferFunction().evaluateDerivate(neuron.getWeightedSum()));
        }

        for (int h = layers.length - 2; h > 0; h--) {
            Layer layer_h = layers[h];
            Neuron[] neurons = layer_h.getNeurons();
            for (Neuron neuron_h : neurons) {
                error = 0.0;
                for (Neuron neuron_k : layers[h + 1].getNeurons()) {
                    error += neuron_k.getDelta() * neuron_k.getWeight(neuron_h.getIndex());
                }
                neuron_h.setDelta(error * layer_h.getTransferFunction().evaluateDerivate(neuron_h.getWeightedSum()));
            }
        }

        for (int k = 1; k < layers.length; k++) {
            Layer layer = layers[k];
            Layer prevLayer = layer.getPrevious();
            for (Neuron neuron_i : layer.getNeurons()) {
                Neuron[] neurons = prevLayer.getNeurons();
                for (Neuron neuron_j : neurons) {
                    double d = layer.getLearningRate() * neuron_i.getDelta() * neuron_j.getValue();
                    neuron_i.addWeight(neuron_j.getIndex(), d);
                }
                neuron_i.addBias(layer.getLearningRate() * neuron_i.getDelta());
            }
        }

        return globalError;
    }

    public Layer[] getLayers() {
        return layers;
    }
}
