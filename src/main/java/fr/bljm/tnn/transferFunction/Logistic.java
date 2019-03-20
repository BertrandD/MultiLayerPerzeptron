package fr.bljm.tnn.transferFunction;

import fr.bljm.tnn.TransferFunction;

public class Logistic implements TransferFunction {
    @Override
    public double evaluate(double value) {
        return 1 / (1 + Math.exp(-value));
    }

    @Override
    public double evaluateDerivate(double value) {
        double log = evaluate(value);
        return log * (1 - log);
    }
}
