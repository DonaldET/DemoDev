//============================================================================
// Name        : WPESimulation.cpp
// Author      : Donald Trummell
// Version     : 0.1.0
// Copyright   : (c) 2021
// Description : Estimate wind power generation
//============================================================================

#include "WPESimulation.hpp"

void test_PolyEval();

int main() {
	cout << "*** Simulate Wind Power Generation ***" << endl;
	cout << "    Time: " << get_current_time_ms() << endl << endl;

	test_PolyEval();

	struct Turbine_Power_Factors tp;
	tp.l = 52.0;               // m
	tp.a = M_PI * tp.l * tp.l; // m^2
	tp.cp = 0.4;               // unitless
	display_TPF(tp);

	struct Wind_Factors wf;
	wf.rho = 1.23;             // kg/m3
	display_WF(wf);

	struct Power_Point p1, p2;

	p1.position = 3344;
	p1.generator_type = 0;
	p1.exp_time = 0;
	p1.speed = 12;                                      // m/s
	double p = power_extracted(p1.speed, wf, tp);
	p1.delta_power = p;                                 // mw
	display_PPoint(p1);
	cout << "                         Wind Speed Reduction -> "
			<< wind_speed_drop(p, wf, tp) << endl;
	cout << endl;

	struct Power_Point &pre = p1;

	struct Power_Point &post = p2;

	for (int i = 0; i < 3; i++) {
		power_generated(pre, wf, tp, post);
		display_PPoint(post);
		struct Power_Point &tmp = pre;
		pre = post;
		pre.exp_time++;
		post = tmp;
	}
	cout << "Done." << endl;
	return EXIT_SUCCESS;
}

void test_PolyEval() {
	cout << endl << "Testing polynomial evaluation:" << endl;
	const double coef[] = { 1.0, 2.0, 3.0 };
	const int n = sizeof coef / sizeof(double);
	cout << " f(";
	for (int i = 0; i < n; i++) {
		if (i > 0)
			cout << ", ";
		cout << coef[i];
	}
	cout << "; x=";
	double x = 2.0;
	cout << x;
	double y = polyEval(coef, n, x);
	cout << ") = " << y << endl << endl;
}
