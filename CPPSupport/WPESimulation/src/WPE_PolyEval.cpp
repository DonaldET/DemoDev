/*
 * WPE_PolyEval.cpp
 *
 *  Created on: Sep 2, 2021
 *      Author: Donald Trummell
 */

#include "WPE_PolyEval.hpp"

double polyEval(const double coef[], const int ncoef, const double x) {

	double yval = coef[0];
	for (int i = 1; i < ncoef; i++) {
		yval = x * yval + coef[i];
	}
	return yval;
}
