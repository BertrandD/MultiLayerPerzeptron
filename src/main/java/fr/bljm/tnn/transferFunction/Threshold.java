package fr.bljm.tnn.transferFunction;

import fr.bljm.tnn.TransferFunction;

public class Threshold implements TransferFunction {
    @Override
    public double evaluate(double value) {
        return value > 0 ? 1 : 0;
    }

    @Override
    public double evaluateDerivate(double value) {
        return 1;
    }
}
