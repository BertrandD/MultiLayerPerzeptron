package fr.bljm.tnn;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataSet {
    private static final Pattern patternHeaderName = Pattern.compile("^#\\s*([a-zA-Z0-9].*)");
    private static final Pattern patternHeaderInfo = Pattern.compile("^# P=([0-9]+)\\s+N=([0-9]+)\\s+M=([0-9]+)");

    private int N;
    private int M;
    private int P;
    private double[][] inputs;
    private double[][] outputs;
    String name;

    public DataSet() {
    }

    public static DataSet getDataSetFromFile(String fileName) {
        DataSet dataSet = new DataSet();

        dataSet.name = "";
        dataSet.P = 0;
        dataSet.N = 0;
        dataSet.M = 0;

        try {
            String[] lines = Files.readAllLines(new File(fileName).toPath()).toArray(new String[0]);

            // Get ID of training
            Matcher m = patternHeaderName.matcher(lines[0]);
            if (!m.find())
                throw new RuntimeException("First line should start with a # and give the name of the training data");
            dataSet.name = m.group(1);

            // Get P, N and M
            m = patternHeaderInfo.matcher(lines[1]);
            if (!m.find()) throw new RuntimeException("Second line should start with a # and give P, N and M");

            dataSet.P = Integer.valueOf(m.group(1));
            dataSet.N = Integer.valueOf(m.group(2));
            dataSet.M = Integer.valueOf(m.group(3));

            if (dataSet.P > 1000) throw new RuntimeException("Maximum for P is set to 1000 patterns");

            dataSet.inputs = new double[dataSet.P][dataSet.N];
            dataSet.outputs = new double[dataSet.P][dataSet.M];
            Pattern pattern = Pattern.compile("^\\s*((?:\\s*[+-]?\\d*\\.?\\d+){" + dataSet.N + "})\\s+((?:\\s*[+-]?\\d*\\.?\\d+){" + dataSet.M + "})");
            for (int i = 0; i < dataSet.P; i++) {
                String line = lines[i + 2];
                m = pattern.matcher(line);
                if (!m.find()) throw new RuntimeException("Malformed data on line " + (i + 3));

                String[] ins = m.group(1).split("\\s+");
                String[] outs = m.group(2).split("\\s+");
                for (int j = 0; j < dataSet.N; j++) {
                    dataSet.inputs[i][j] = Double.valueOf(ins[j]);
                }
                for (int j = 0; j < dataSet.M; j++) {
                    dataSet.outputs[i][j] = Double.valueOf(outs[j]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        return dataSet;
    }

    public int getN() {
        return N;
    }

    public int getM() {
        return M;
    }

    public int getP() {
        return P;
    }

    public double[][] getInputs() {
        return inputs;
    }

    public double[][] getOutputs() {
        return outputs;
    }

    public String getName() {
        return name;
    }
}
