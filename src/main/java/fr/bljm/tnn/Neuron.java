package fr.bljm.tnn;

import java.util.Arrays;
import java.util.Random;

public class Neuron {
    private double value;
    private double weightedSum;
    private double[] weights;
    private double delta;
    private double bias;
    private int index;

    public Neuron(int prevLayerSize, int index) {
        this.index = index;
        weights = new double[prevLayerSize];
        delta = 0;
        value = 0;
        weightedSum = 0;
        bias = (MultiLayerPerzeptron.RANDOM.nextInt(40) * 1.0 - 20) / 10;
        Arrays.setAll(weights, i -> (MultiLayerPerzeptron.RANDOM.nextInt(40) * 1.0 - 20) / 10);
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double[] getWeights() {
        return weights;
    }

    public double getWeight(int k) {
        return weights[k];
    }

    public void addWeight(int k, double w) {
        weights[k] += w;
    }

    public void setWeights(double[] weights) {
        this.weights = weights;
    }

    public double getBias() {
        return bias;
    }

    public void addBias(double bias) {
        this.bias += bias;
    }

    public double getDelta() {
        return delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    public double getWeightedSum() {
        return weightedSum;
    }

    public void setWeightedSum(double weightedSum) {
        this.weightedSum = weightedSum;
    }

    public int getIndex() {
        return index;
    }
}
