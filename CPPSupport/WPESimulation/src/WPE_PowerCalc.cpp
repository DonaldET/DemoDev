/*
 * WPE_PowerCalc.cpp
 *
 *  Created on: Sep 3, 2021
 *      Author: Donald Trummell
 */

#include "WPE_PowerCalc.hpp"
#include "WPE_PolyEval.hpp"

using namespace std;

void display_TPF(const Turbine_Power_Factors &tpf) {
	cout << "TPF   -> Blade Length: " << tpf.l << "; swept area: " << tpf.a
			<< "; cut-in speed: " << tpf.cut_in << "; cut-out speed: "
			<< tpf.cut_out << endl;
	cout << "         Coef: ";
	for (int i = 0; i < TPF_npower; i++) {
		if (i > 0) {
			cout << ", ";
		}
		cout << tpf.coef[i];
	}
	cout << endl;
}

void display_WF(const Wind_Factors &wf) {
	cout << "WF    -> rho: " << wf.rho << endl;
}

void display_PPoint(const Power_Point &powerPoint, const double drop) {
	cout << "Power -> Pos: " << powerPoint.position << ";  Type: "
			<< powerPoint.generator_type;
	cout << ";  Time: " << powerPoint.exp_time << ";  Speed: "
			<< powerPoint.speed;
	cout << ";  delta: " << powerPoint.delta_power << "; drop: " << drop
			<< endl;
}

//////// --- Power calculations

// See https://www.raeng.org.uk/publications/other/23-wind-turbine and
// http://www.windandwet.com/windturbine/power_calc/index.php
// mega-whatts
double power_extracted(const double v, const Wind_Factors &wf,
		const Turbine_Power_Factors &tp) {
	if (v < tp.cut_in || v > tp.cut_out) {
		return 0.0;
	}
	const int ncoef = sizeof(tp.coef) / sizeof(double);
	double cp = polyEval(&tp.coef[0], ncoef, v);
	if (cp < 0.0) {
		cp = 0.0;
	} else if (cp > 0.59) {
		cp = 0.59;
	}
	return (double) 0.5 * wf.rho * tp.a * v * v * v * cp / 1000000.0;
}

// v = cube_root(2*p/(rho*cp)), but use Cp == 1; because while
// we did not extract all potential power, we still impeded the wind
double wind_speed_drop(const double p_extracted, const Wind_Factors &wf) {
	return cbrt((double) 2.0 * p_extracted / wf.rho);
}

// Calculate the net power generated (using Cp) and the wind speed drop
double power_generated(const Power_Point &input, const Wind_Factors &wf,
		const Turbine_Power_Factors &tp, Power_Point &output) {
	output.position = input.position;
	output.generator_type = input.generator_type;
	output.exp_time = input.exp_time;
	const double v = input.speed;
	const double p = power_extracted(v, wf, tp);
	output.delta_power = p;
	const double drop = wind_speed_drop(p, wf);
	output.speed = v - drop;
	return (drop);
}
