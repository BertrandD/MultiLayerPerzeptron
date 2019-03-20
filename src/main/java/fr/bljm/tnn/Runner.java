package fr.bljm.tnn;

import fr.bljm.tnn.transferFunction.Identity;
import fr.bljm.tnn.transferFunction.Tanh;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;

public class Runner {

    public static void main(String[] args) {


        String dataFile = "training.dat";
        String testFile = "test.dat";

        for (String arg : args) {
            String[] a = arg.split("=");
            if (a[0].equals("-data")) {
                dataFile = a[1];
            } else if (a[0].equals("-test")) {
                testFile = a[1];
            } else {
                System.out.println("Invalid arg " + a[0]);
                System.out.println("Valid args : -data=DATAFILE - test=TESTFILE");
                System.exit(1);
            }
        }

        // ###############
        // Read dataset
        // ###############

        System.out.println("Reading training data from file " + dataFile);
        DataSet trainingDataSet = DataSet.getDataSetFromFile(dataFile);

        System.out.println("Reading testing data from file " + testFile);
        DataSet testingDataSet = DataSet.getDataSetFromFile(testFile);

        System.out.println("Initialize the neural network : N=" + trainingDataSet.getN() + " M=" + trainingDataSet.getM());
        MultiLayerPerzeptron multiLayerPerzeptron = new MultiLayerPerzeptron(
                new int[]{
                        trainingDataSet.getN(), // input layer
                        5,
                        trainingDataSet.getM()// output layer
                },
                new TransferFunction[]{
                        new Identity(),
                        new Tanh(),
                        new Tanh()
                });

        // ###############
        // Train perzeptron
        // ###############

        // Some parameters for the training

        int trainingStepsCount = 1000;

        multiLayerPerzeptron.getLayers()[1].setLearningRate(0.1);
        multiLayerPerzeptron.getLayers()[2].setLearningRate(0.01);

        System.out.println("Train network with dataset: " + trainingDataSet.getName());

        StringBuilder learningCurve = new StringBuilder();

        learningCurve.append("# Epoch").append("\t").append("Error").append("\n");
        double errorMean = 0;

        for (int j = 0; j < trainingStepsCount; j++) {
            errorMean = 0;

            for (int i = 0; i < trainingDataSet.getP(); i++) {
                double error = multiLayerPerzeptron.backPropagateError(trainingDataSet.getInputs()[i], trainingDataSet.getOutputs()[i]);
                errorMean += error;
            }
            errorMean /= trainingDataSet.getP();
            learningCurve.append("  ").append(j).append("\t").append(errorMean).append("\n");
        }

        System.out.println("Finished training after " + trainingStepsCount + " steps an error mean of " + errorMean);

        System.out.println("Output learning curve in file learning.curve");

        try (PrintWriter out = new PrintWriter("learning.curve")) {
            out.println(learningCurve.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("Test network with testing dataSet : " + trainingDataSet.getName());

        errorMean = 0;

        for (int i = 0; i < testingDataSet.getP(); i++) {
            multiLayerPerzeptron.execute(testingDataSet.getInputs()[i]);
            double[] output = multiLayerPerzeptron.getOutput();

            System.out.println("Got " + Arrays.toString(output) + " - Wanted " + Arrays.toString(testingDataSet.getOutputs()[i]));

            double error = 0;
            for (int j = 0; j < trainingDataSet.getM(); j++) {
                error += output[j] - testingDataSet.getOutputs()[i][j];
            }
            errorMean += error;
        }
        errorMean /= testingDataSet.getP();

        System.out.println("Mean error on testing data: " + errorMean);

    }
}
