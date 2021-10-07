//============================================================================
// Name        : WPESimulation.cpp
// Author      : Donald Trummell
// Version     : 0.1.0
// Copyright   : (c) 2021
// Description : Estimate wind power generation
//============================================================================

#include "WPESimulation.hpp"
using namespace std;

void populate_test_coefficient(struct Turbine_Power_Factors &tp);
void test_PolyEval();

int main() {
	cout << "*** Simulate Wind Power Generation ***" << endl;
	cout << "    Time: " << get_current_time_ms() << endl << endl;

	test_PolyEval();
	struct Turbine_Power_Factors tp;
	tp.l = 52.0;               // m
	tp.a = M_PI * tp.l * tp.l; // m^2
	populate_test_coefficient(tp);
	tp.cut_in = 2;
	tp.cut_out = 27;
	display_TPF(tp);

	struct Wind_Factors wf;
	wf.rho = 1.23;             // kg/m3
	display_WF(wf);

	struct Power_Point p1, p2;

	p1.position = 3344;
	p1.generator_type = 0;
	p1.exp_time = 0;
	p1.speed = 12;
	p1.delta_power = 0;

	struct Power_Point &pre = p1;
	struct Power_Point &post = p2;

	display_PPoint(pre, 0);
	pre.exp_time++;

	for (int i = 0; i < 13; i++) {
		const double drop = power_generated(pre, wf, tp, post);
		display_PPoint(post, drop);
		struct Power_Point &tmp = pre;
		pre = post;
		pre.exp_time++;
		post = tmp;
	}
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

void test_PolyEval() {
	cout << endl << "Testing polynomial evaluation:" << endl;
	const double coef[] = { 3.0, 2.0, 1.0 };
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
