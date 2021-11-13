package don.demodev.algo.wpe;

import don.demodev.algo.wpe.api.PowerCalculator;
import don.demodev.algo.wpe.api.PowerCalculator.PowerPoint;
import don.demodev.algo.wpe.api.PowerCalculator.TurbinePowerFactors;
import don.demodev.algo.wpe.api.PowerCalculator.WindFactors;
import don.demodev.algo.wpe.implementation.PolyEvaluatorImpl;
import don.demodev.algo.wpe.implementation.SimplePowerCalculator;

/**
 * <pre>
 * <code>
 * ============================================================================
 * Name       : WPESimulation.java
 * Version    : 0.1.0
 * Copyright  : (c) 2021
 * Description: Simulate wind electric power generation - alpha Java version
 * ============================================================================
 * </code>
 * </pre>
 * 
 * @author Donald Trummell
 */
public class WPESimulation {
	/* The number of sequential generators */
	private static final long INLINE_GENERATORS = 10;
	/* Setup some random wind */
	private static final double winds[] = { 8.0, 11.0, 5.0, 24.0, 30.0, 7.0, 3.0, 26.0, 2.0, 9.0, 4.0, 9.0, 17.0, 20.0,
			29.0, 18.0, 7.0, 13.0, 20.0, 25.0, 22.0, 18.0, 21.0, 13.0, 3.0, 6.0, 20.0, 22.0, 26.0, 6.0, 29.0, 23.0, 6.0,
			16.0 };
	/* The number of random wind speeds */
	private static final long RANDOM_WIND_COUNT = winds.length;
	/* Number of repetitions */
	private static final long REPEATS = 25000;

	private static final PowerCalculator pc = new SimplePowerCalculator();

	/**
	 * Calculate power generated in a wind channel of N generators; perform the
	 * calculation for multiple random wind speeds; repeat sufficient CPU is used to
	 * get a good timing.
	 */
	public static void main(String[] args) {
		System.out.println("*** Simulate Wind Power Generation (Java alpha version) ***");

		test_time();
		test_PolyEval();

		//
		// Simulation Process is:
		// 1) Organize statistics
		// 2) Create input structure
		// 3) Create output structure
		// 4) Calculate power generation, input -> output
		// 5) Update statistics
		// 6) Swap input and output
		// 7) Repeat from (4) N times (N is number of generators in a wind channel
		//

		TurbinePowerFactors tpf = new TurbinePowerFactors();
		tpf.l = 52.0; // m
		tpf.a = Math.PI * tpf.l * tpf.l; // m^2
		tpf.coef = new double[PowerCalculator.TPFNumCoef];
		populate_test_coefficient(tpf);
		tpf.cutInSpeed = 2; // m/s
		tpf.cutOutSpeed = 27; // m/s
		pc.display_TPF(tpf);

		WindFactors wf = new WindFactors();
		wf.rho = 1.23; // kg/m3
		pc.display_WF(wf);

		/* The input-output energy generation points */
		PowerPoint p1 = new PowerPoint();
		PowerPoint p2 = new PowerPoint();

		long count = 0;
		double sumPower = 0.0;
		double sumDrop = 0.0;
		long start = System.currentTimeMillis();
		for (int rep = 0; rep < REPEATS; rep++) {
			for (int wind = 0; wind < RANDOM_WIND_COUNT; wind++) {
				p1.position = 3344;
				p1.generatorType = 0;
				p1.experimentTime = 0;
				p1.speed = winds[wind];
				p1.deltaEnergy = 0.0;

				PowerPoint pre = p1;
				PowerPoint post = p2;

				// display_PPoint(pre, 0);
				pre.experimentTime++;

				double drop = 0.0;
				for (long generator = 0; generator < INLINE_GENERATORS; generator++) {
					count++;
					drop = pc.power_generated(pre, wf, tpf, post);
					if (drop < 0.0) {
						System.out.println("****ERROR: drop " + drop + " bad at generator " + generator + " of wind "
								+ wind + " at repetition " + rep);
					}
					if (rep == 1) {
						sumDrop += drop;
						sumPower += post.deltaEnergy;
					}
					// display_PPoint(post, drop);
					PowerPoint tmp = pre;
					pre = post;
					pre.experimentTime++;
					post = tmp;
				}
				// display_PPoint(post, drop);
			} // End winds
		} // End reps
		long stop = System.currentTimeMillis();
		long elapsed = stop - start;
		System.out.println("\n******\n** " + elapsed + " MS required for " + INLINE_GENERATORS + " generators against "
				+ RANDOM_WIND_COUNT + " random wind speeds over " + REPEATS + " repetitions.");
		System.out.println("** Calculations  : " + count);
		long cases = RANDOM_WIND_COUNT * INLINE_GENERATORS;
		System.out.println("** Avg Power (Mw): " + (sumPower / cases));
		System.out.println("** Avg Speed drop: " + (sumDrop / cases) + " m/s");
		System.out.println("******");

		System.out.println("Done.");
	}

	private static void populate_test_coefficient(TurbinePowerFactors tpf) {
		final double[] e33_coef = { -2.683640e-01, 2.385518e-01, -2.001438e-02, 4.243324e-05, 3.780910e-05,
				-8.796243e-07 };
		for (int i = 0; i < tpf.coef.length; i++) {
			tpf.coef[i] = e33_coef[i];
		}
	}

	private static void test_time() {
		long now = System.currentTimeMillis();
		System.out.println("    Time0: " + now);
		double x = 2.3;
		for (long i = 0; i < 1000000; i++) {
			x = (1.0034 + x) * x * Math.pow(x, 1.002);
		}
		long then = System.currentTimeMillis();
		System.out.println("    Time1: " + now);
		System.out.println("    Elapsed = " + (then - now) + " ms.");
	}

	private static void test_PolyEval() {
		System.out.println("Testing polynomial evaluation:");
		final double coef[] = { 3.0, 2.0, 1.0 };
		System.out.print(" f(" + coef[0] + " + " + coef[1] + "*x + " + coef[2] + "*x^2");
		System.out.print("; x=");
		double x = 2.0;
		System.out.print(x);
		double y = new PolyEvaluatorImpl().polyEval(coef, x);
		System.out.println(") = " + y);
	}
}