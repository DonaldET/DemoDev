/*
 * WPE_PowerCalc.hpp
 *
 *  Created on: Sep 3, 2021
 *      Author: Donald Trummell
 */

#ifndef WPESIMULATION_WPE_POWERCALC_HPP_
#define WPESIMULATION_WPE_POWERCALC_HPP_

#include <iostream>
using namespace std;
# define _USE_MATH_DEFINES
#include <cmath>

//////// --- Wind Simulation Constructs

struct Power_Point {
	int position;
	int generator_type;
	long exp_time;              // t (milliseconds)
	double speed;               // m/s
	double delta_power;         // w (x10^6)
};

//////// --- Turbine Power Simulation Constructs

const int TPF_npower = 1;
struct Turbine_Power_Factors {
	double l;                  // Blade length
	double a;                  // Blade swept area
	double coef[TPF_npower];   // Polynomial approximation of Cp as function of wind speed'
	double cut_in;             // Initial power generation wind speed
	double cut_out;            // Just exceeds greatest operational wind speed
};

//////// --- Power Generation Constructs

struct Wind_Factors {
	double rho;                // Air density, kg/m^3
};

void display_TPF(const Turbine_Power_Factors &tpf);

void display_WF(const Wind_Factors &wf);

void display_PPoint(const Power_Point &powerPoint);

double power_extracted(const double v, const Wind_Factors wf,
		const Turbine_Power_Factors tp);

double wind_speed_drop(double p_extracted, const Wind_Factors &wf,
		const Turbine_Power_Factors &tp);

void power_generated(const Power_Point &input, const Wind_Factors &wf,
		const Turbine_Power_Factors &tp, Power_Point &output);

#endif /* WPESIMULATION_WPE_POWERCALC_HPP_ */
