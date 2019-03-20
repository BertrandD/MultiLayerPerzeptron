package fr.bljm.tnn.transferFunction;

import fr.bljm.tnn.TransferFunction;

public class Identity implements TransferFunction {
    @Override
    public double evaluate(double value) {
        return value;
    }

    @Override
    public double evaluateDerivate(double value) {
        return 1;
    }
}
