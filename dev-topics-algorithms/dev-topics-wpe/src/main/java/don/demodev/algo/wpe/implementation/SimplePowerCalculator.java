package don.demodev.algo.wpe.implementation;

import don.demodev.algo.wpe.api.PolynomialEvaluator;
import don.demodev.algo.wpe.api.PowerCalculator;

public class SimplePowerCalculator implements PowerCalculator {

	private static final PolynomialEvaluator evaluator = new PolyEvaluatorImpl();
	private static final double ONE_THIRD = 1.0D / 3.0D;

	@Override
	public void display_TPF(final TurbinePowerFactors tpf) {
		System.out.println("TPF   -> Blade Length: " + tpf.l + "; swept area: " + tpf.a + "; cut-in speed: "
				+ tpf.cutInSpeed + "; cut-out speed: " + tpf.cutOutSpeed);
		System.out.print("         Coef: ");
		for (int i = 0; i < PowerCalculator.TPFNumCoef; i++) {
			if (i > 0) {
				System.out.print(", ");
			}
			System.out.print(tpf.coef[i]);
		}
		System.out.println();
	}

	@Override
	public void display_WF(final WindFactors wf) {
		System.out.println("WF    -> rho: " + wf.rho);
	}

	@Override
	public void display_PPoint(final PowerPoint powerPoint, final double drop) {
		System.out.print("Power -> Pos: " + powerPoint.position + ";  Type: " + powerPoint.generatorType);
		System.out.print(";  Time: " + powerPoint.experimentTime + ";  Speed: " + powerPoint.speed);
		System.out.println(";  delta: " + powerPoint.deltaEnergy + "; drop: ");
	}

//////// --- Power calculations

	/**
	 * See this <a
	 * href="https://www.raeng.org.uk/publications/other/23-wind-turbine"UK
	 * publication</a> and this <a
	 * href="http://www.windandwet.com/windturbine/power_calc/index.php"US
	 * publication</a>
	 */
	public double power_extracted(final double v, final WindFactors wf, final TurbinePowerFactors tpf) {
		if (v < tpf.cutInSpeed || v > tpf.cutOutSpeed) {
			return 0.0;
		}

		double cp = evaluator.polyEval(tpf.coef, v);
		return (double) 0.5 * wf.rho * tpf.a * v * v * v * Math.max(0, Math.min(cp, 0.59)) / 1000000.0;
	}

	/**
	 * v = cube_root(2*p/(rho*cp)), but use Cp == 1; because while we did not
	 * extract all potential energy, we still impedded the wind by rotating the
	 * generator blades.
	 */
	@Override
	public double wind_speed_drop(double pextracted, WindFactors wf) {
		return Math.pow((double) 2.0 * pextracted / wf.rho, ONE_THIRD);
	}

	/**
	 * Calculate the net power generated (using Cp) and the wind speed drop
	 */
	@Override
	public double power_generated(PowerPoint input, WindFactors wf, TurbinePowerFactors tp, PowerPoint output) {
		output.position = input.position;
		output.generatorType = input.generatorType;
		output.experimentTime = input.experimentTime;
		final double v = input.speed;
		final double p = power_extracted(v, wf, tp);
		output.deltaEnergy = p;
		final double drop = wind_speed_drop(p, wf);
		output.speed = v - drop;
		return drop;
	}
}