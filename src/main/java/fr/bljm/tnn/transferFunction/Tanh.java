package fr.bljm.tnn.transferFunction;

import fr.bljm.tnn.TransferFunction;

public class Tanh implements TransferFunction {
    @Override
    public double evaluate(double value) {
        if (value > 3) return 1;
        if (value < -3) return -1;
        return value / (1 + ( value * value / (3 + (value * value / (5 + (value * value / 7))))));
    }

    @Override
    public double evaluateDerivate(double value) {
        double tanh = evaluate(value);
        return 1.0 - tanh * tanh;
    }
}
