package don.demodev.algo.wpe.implementation;

import don.demodev.algo.wpe.api.PolynomialEvaluator;

public class PolyEvaluatorImpl implements PolynomialEvaluator {
	@Override
	public double polyEval(final double coef[], final double x) {
		final int ncoef = coef.length;
		double yval = 0.0;
		if (ncoef > 0) {
			for (int i = ncoef - 1; i >= 0; i--) {
				yval = x * yval + coef[i];
			}
		}
		return yval;
	}
}