/*
 * WPE_PowerCalc.cpp
 *
 *  Created on: Sep 3, 2021
 *      Author: Donald Trummell
 */

#include "WPE_PowerCalc.hpp"

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

//////// --- Power calculations

// See https://www.raeng.org.uk/publications/other/23-wind-turbine and
// http://www.windandwet.com/windturbine/power_calc/index.php
double power_extracted(const double v, const Wind_Factors wf,
		const Turbine_Power_Factors tp) {
	return (double) 0.5 * wf.rho * tp.a * v * v * v * tp.cp / 1000000.0;
}

// v = cube_root(2*p/(rho*cp)), but remove cp because we did not extract all potential power
double wind_speed_drop(double p_extracted, const Wind_Factors &wf,
		const Turbine_Power_Factors &tp) {
	return cbrt((double) 2.0 * p_extracted / wf.rho);
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
