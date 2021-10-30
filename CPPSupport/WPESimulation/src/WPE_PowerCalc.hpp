/*
 * WPE_PowerCalc.hpp
 *
 *  Created on: Sep 3, 2021
 *      Author: Donald Trummell
 */

#ifndef WPESIMULATION_WPE_POWERCALC_HPP_
#define WPESIMULATION_WPE_POWERCALC_HPP_

#include <iostream>
#define _USE_MATH_DEFINES
#include <cmath>

//////// --- Wind Simulation Constructs

struct Power_Point {
	int position;				// generator position on grip
	int generator_type;	// enum linking generator charactoristics to position in wind farm.
	long exp_time;              // t (milliseconds)
	double speed;               // m/s
	double delta_power;         // watts (x10^6)
};

//////// --- Turbine Power Simulation Constructs

const int TPF_npower = 6;
struct Turbine_Power_Factors {
	double l;                  	// Blade length
	double a;                 	// Blade swept area
	double coef[TPF_npower]; // Polynomial approximation of Cp as function of wind speed'
	double cut_in;             	// Initial power generation wind speed
	double cut_out;            	// Just exceeds greatest operational wind speed
};

//////// --- Power Generation Constructs

struct Wind_Factors {
	double rho;                // Air density, kg/m^3
};

void display_TPF(const Turbine_Power_Factors &tpf);

void display_WF(const Wind_Factors &wf);

void display_PPoint(const Power_Point &powerPoint, const double drop);

/**
 * Net power extracted from wind by a generator; computed as total power * Cp. This ignores
 * the influence of turbulence on power generation
 * (see https://www.wind-watch.org/documents/how-turbulence-can-impact-power-performance/)
 *
 * Parameters:
 * v: double value representing wind speed, units are M/S
 * wf: A Wind_Factors reference, which defines wind characteristics affecting power generation.
 * tp: A Turbine_Power_Factors reference, which defines generator characteristics affecting power generation.
 */
double power_extracted(const double v, const Wind_Factors &wf,
		const Turbine_Power_Factors &tp);

/**
 * Computes the drop in wind speed after passing through a generator, assuming the extracted
 * power drops the velocity in the same manner (that is, using the same formula) as the
 * generated power is influenced by wind velocity.
 *
 * Note: v = cube_root(2*p/(rho*cp)), but we use Cp == 1; because while
 * we did not extract all the potential power of the wind, we still impeded the wind
 *
 * Parameters:
 * p_extracted: a double value, in units of watts, that quantifies the power extracted from the wind by the previous generator.
 * wf: A Wind_Factors reference, which defines wind characteristics affecting power generation.
 */
double wind_speed_drop(double p_extracted, const Wind_Factors &wf);

/**
 * Calculates the power generated at a grid location and the wind speed drop associated
 * with the generated power. The calculation accepts an input point and stores the result
 * in an output point, and returns the wind speed drop as the function value
 */
double power_generated(const Power_Point &input, const Wind_Factors &wf,
		const Turbine_Power_Factors &tp, Power_Point &output);

#endif /* WPESIMULATION_WPE_POWERCALC_HPP_ */
