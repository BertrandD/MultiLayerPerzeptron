package fr.bljm.tnn;

public class Perzeptron {

    private int N;
    private int M;
    private double[][] weights;
    private double[] biasWeights;

    public Perzeptron(int n, int m) {
        if (n > 101) throw new RuntimeException("N should be less than 101");
        if (m > 30) throw new RuntimeException("M should be less than 30");

        N = n;
        M = m;

        weights = new double[M][N];
        biasWeights = new double[M];

        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                weights[i][j] = Math.random() - 0.5;
            }
            biasWeights[i] = Math.random() - 0.5;
        }
    }

    public void setWeight(int m, int n, double w) {
        weights[m][n] = w;
    }

    public void setBiasWeight(int m, double w) {
        biasWeights[m] = w;
    }

    public int[] execute(int[] input) {
        if (input.length != N) throw new RuntimeException("input array does not have same length as init");

        int[] output = new int[M];

        for (int i = 0; i < M; i++) {
            double weightedSum = 0.0;
            for (int j = 0; j < N; j++) {
                weightedSum += input[j] * weights[i][j];
            }
            weightedSum += 1 * biasWeights[i]; // We add bias
            output[i] = (weightedSum > 0 ? 1 : 0); // transfer function = threshold
        }

        return output;
    }

    public double train(int[] input, int[] teacherOutput) {
        if (teacherOutput.length != M) {
            throw new RuntimeException("teacher output array does not have same length as init");
        }
        // We pass the data in the network to get an output
        int[] output = execute(input);
        double error, delta;
        double learningRate = 0.1;
        //System.out.println("Got " + Arrays.toString(output) + " ~ Wanted " + Arrays.toString(teacherOutput));

        for (int i = 0; i < M; i++) {
            // We calculate the difference between the current output and the teacher output
            error = teacherOutput[i] - output[i];

            // update the weights for each input-neuron
            for (int j = 0; j < N; j++) {
                delta = learningRate * error * input[j];
                weights[i][j] += delta;
            }

            // update the bias weights for each neuron
            biasWeights[i] += learningRate * error;
        }

        // compute total of errors to stop if necessary
        error = 0;

        for (int i = 0; i < M; i++) {
            error += Math.abs(output[i] - teacherOutput[i]);
        }

        return error;
    }

    public double[][] getWeights() {
        return weights;
    }

    public double[] getBiasWeights() {
        return biasWeights;
    }
}
