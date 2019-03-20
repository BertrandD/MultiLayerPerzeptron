package fr.bljm.tnn;

import java.util.stream.IntStream;

public class Layer {
    private Neuron[] neurons;
    private Layer previous;
    private int k;
    private TransferFunction transferFunction;
    private double learningRate;

    public Layer(int k, int prevLayerSize, Layer previous, TransferFunction transferFunction) {
        this.k = k;
        this.previous = previous;
        this.transferFunction = transferFunction;
        this.learningRate = 0.1;
        neurons = new Neuron[k];
        IntStream.range(0, k).forEach(i -> neurons[i] = new Neuron(prevLayerSize, i));
    }

    public int getSize() {
        return k;
    }

    public Neuron[] getNeurons() {
        return neurons;
    }

    public Neuron getNeuron(int k) {
        return neurons[k];
    }

    public Layer getPrevious() {
        return previous;
    }

    public TransferFunction getTransferFunction() {
        return transferFunction;
    }

    public double getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }
}
