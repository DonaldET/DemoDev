//============================================================================
// Name        : WPESimulation.cpp
// Author      : Donald Trummell
// Version     : 0.1.0
// Copyright   : (c) 2021
// Description : Simulate wind electric power generation - alpha C++ version
//============================================================================

#include "WPESimulation.hpp"
using namespace std;

/* The number of sequential generators */
const long INLINE_GENERATORS = 10;
/* Setup some random wind */
const double winds[] = { 8.0, 11.0, 5.0, 24.0, 30.0, 7.0, 3.0, 26.0, 2.0, 9.0,
		4.0, 9.0, 17.0, 20.0, 29.0, 18.0, 7.0, 13.0, 20.0, 25.0, 22.0, 18.0,
		21.0, 13.0, 3.0, 6.0, 20.0, 22.0, 26.0, 6.0, 29.0, 23.0, 6.0, 16.0 };
/* The number of random wind speeds */
const long RANDOM_WIND_COUNT = sizeof(winds) / sizeof(double);
/* Number of repetitions */
const long REPEATS = 25000;

void populate_test_coefficient(struct Turbine_Power_Factors &tp);
void test_time();
void test_PolyEval();

/**
 * Calculate power generated in a wind channel of N generators; perform the calculation
 * for multiple random wind speeds; repeat sufficient CPU is used to get a good timing.
 */
int main() {
	cout << "*** Simulate Wind Power Generation (C++ alpha) ***" << endl;

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

	struct Turbine_Power_Factors tp;
	tp.l = 52.0;               	// m
	tp.a = M_PI * tp.l * tp.l;	// m^2
	populate_test_coefficient(tp);
	tp.cut_in = 2;				// m/s
	tp.cut_out = 27;			// m/s
	display_TPF(tp);

	struct Wind_Factors wf;
	wf.rho = 1.23;             // kg/m3
	display_WF(wf);

	/* The input-output power generation points */
	struct Power_Point p1, p2;

	long count = 0;
	double sumPower = 0.0;
	double sumDrop = 0.0;
	time_t start = get_current_time_ms();
	for (int rep = 0; rep < REPEATS; rep++) {
		for (int wind = 0; wind < RANDOM_WIND_COUNT; wind++) {
			p1.position = 3344;
			p1.generator_type = 0;
			p1.exp_time = 0;
			p1.speed = winds[wind];
			p1.delta_power = 0.0;

			struct Power_Point &pre = p1;
			struct Power_Point &post = p2;

			//display_PPoint(pre, 0);
			pre.exp_time++;

			double drop = 0.0;
			for (long generator = 0; generator < INLINE_GENERATORS;
					generator++) {
				count++;
				drop = power_generated(pre, wf, tp, post);
				if (drop < 0.0) {
					cout << "****ERROR: drop " << drop << " bad at generator "
							<< generator << " of wind " << wind
							<< " at repetition " << rep << endl;
				}
				if (rep == 1) {
					sumDrop += drop;
					sumPower += post.delta_power;
				}
				//display_PPoint(post, drop);
				struct Power_Point &tmp = pre;
				pre = post;
				pre.exp_time++;
				post = tmp;
			}
			//display_PPoint(post, drop);
		} // End winds
	} // End reps
	time_t stop = get_current_time_ms();
	long elapsed = stop - start;
	cout << endl << "******" << endl << "** " << elapsed << " MS required for "
			<< INLINE_GENERATORS << " generators against " << RANDOM_WIND_COUNT
			<< " random wind speeds over " << REPEATS << " repetitions."
			<< endl;
	cout << "** Calculations  : " << count << endl;
	long cases = RANDOM_WIND_COUNT * INLINE_GENERATORS;
	cout << "** Avg Power (Mw): " << (sumPower / cases) << endl;
	cout << "** Avg Speed drop: " << (sumDrop / cases) << " m/s" << endl;
	cout << "******" << endl;

	cout << "Done." << endl;
	return EXIT_SUCCESS;
}

void populate_test_coefficient(struct Turbine_Power_Factors &tp) {
	const double e33_coef[TPF_npower] = { -2.683640e-01, 2.385518e-01,
			-2.001438e-02, 4.243324e-05, 3.780910e-05, -8.796243e-07 };
	for (int i = 0; i < TPF_npower; i++) {
		tp.coef[i] = e33_coef[i];
	}
}

void test_time() {
	time_t now = get_current_time_ms();
	cout << "    Time0: " << now << endl;
	double x = 2.3;
	for (long i = 0; i < 1000000; i++) {
		x = (1.0034 + x) * x * pow(x, 1.002);
	}
	time_t then = get_current_time_ms();
	cout << "    Time1: " << now << endl;
	cout << "    Elapsed = " << (then - now) << " ms." << endl << endl;
}

void test_PolyEval() {
	cout << "Testing polynomial evaluation:" << endl;
	const double coef[] = { 3.0, 2.0, 1.0 };
	const int n = sizeof coef / sizeof(double);
	cout << " f(" << coef[0] << " + " << coef[1] << "*x + " << coef[2]
			<< "*x^2";
	cout << "; x=";
	double x = 2.0;
	cout << x;
	double y = polyEval(coef, n, x);
	cout << ") = " << y << endl << endl;
}
