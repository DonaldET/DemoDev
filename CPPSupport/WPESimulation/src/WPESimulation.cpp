//============================================================================
// Name        : WPESimulation.cpp
// Author      : Donald Trummell
// Version     : 0.1.0
// Copyright   : (c) 2021
// Description : Estimate wind power generation
//============================================================================

# define _USE_MATH_DEFINES
#include <cmath>
#include <iostream>
using namespace std;
#include <sys/time.h>

long get_current_time_ms() {
	struct timeval time_now { };
	gettimeofday(&time_now, nullptr);
	return (time_now.tv_sec * 1000L) + ((time_now.tv_usec + 500) / 1000L);
}

//////// --- Wind Simulation Constructs

struct Power_Point {
	int position;
	int generator_type;
	long exp_time;              // t (milliseconds)
	double speed;               // m/s
	double delta_power;         // w (x10^6)
};

//////// --- Turbine Power Simulation Constructs

struct Turbine_Power_Factors {
	double l;                  // Blade length
	double a;                  // Blade swept area
	double cp;	               // Efficiency (0 < Cp <= .59
};

//////// --- Power Generation Constructs

struct Wind_Factors {
	double rho;
};

//////// --- Power calculations

// See https://www.raeng.org.uk/publications/other/23-wind-turbine
double power_extracted(const double v, const Wind_Factors wf,
		const Turbine_Power_Factors tp) {
	return (double) 0.5 * wf.rho * tp.a * v * v * v * tp.cp / 1000000.0;
}

// v = cube_root(2*p/(rho*cp)), but remove cp because we did not extract it all
double wind_speed_drop(double p, const Wind_Factors &wf,
		const Turbine_Power_Factors &tp) {
	return cbrt((double) 2.0 * p / wf.rho);
}

void power_generated(const Power_Point &input, const Wind_Factors &wf,
		const Turbine_Power_Factors &tp, Power_Point &output) {
	output.position = input.position;
	output.generator_type = input.generator_type;
	output.exp_time = input.exp_time;
	double v = input.speed;
	double p = power_extracted(v, wf, tp);
	output.delta_power = p;
	output.speed = v - wind_speed_drop(p, wf, tp);
}

void display_TPF(const Turbine_Power_Factors &tpf) {
	cout << "TPF   -> Blade Length: " << tpf.l << "; swept area: " << tpf.a
			<< "; Cp: " << tpf.cp << endl;
}

void display_WF(const Wind_Factors &wf) {
	cout << "WF    -> rho: " << wf.rho << endl;
}

void display_PPoint(const Power_Point &powerPoint) {
	cout << "Power -> Pos: " << powerPoint.position << ";  Type: "
			<< powerPoint.generator_type;
	cout << ";  Time: " << powerPoint.exp_time << ";  Speed: "
			<< powerPoint.speed;
	cout << ";  delta: " << powerPoint.delta_power << endl;
}

int main() {
	cout << "*** Simulate Wind Power Generation ***" << endl;
	cout << "    Time: " << get_current_time_ms() << endl << endl;

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
