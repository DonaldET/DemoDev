package don.demodev.algo.wpe.api;

public interface PolynomialEvaluator {

/**
 * Evaluate a polynomial from coefficients; y = coef[n - 1] * x^(n - 1) + . . .
 * + coef[1] * x + coef[0].
 *
 * Parameters:
 * coef: double array with polynommial coefficients, lowest power first.
 * ncoef: number of coefficients (polynomial power minus one.)
 * X: a double value at which we evaluate the polynomial.
 */
public abstract double polyEval(double coef[], double x);
}
