package fr.bljm.tnn;

public interface TransferFunction {
    double evaluate(double value);
    double evaluateDerivate(double value);
}
