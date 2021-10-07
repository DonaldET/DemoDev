/*
 * WPE_PolyEval.cpp
 *
 *  Created on: Sep 2, 2021
 *      Author: Donald Trummell
 */

#include "WPE_PolyEval.hpp"

double polyEval(const double coef[], const int ncoef, const double x) {

	double yval = 0.0;
	if (ncoef > 0) {
		for (int i = ncoef - 1; i >= 0; i--) {
			yval = x * yval + coef[i];
		}
	}
	return yval;
}
