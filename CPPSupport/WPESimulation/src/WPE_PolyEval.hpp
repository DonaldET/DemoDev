/*
 * WPE_PolyEval.hpp
 *
 *  Created on: Sep 2, 2021
 *      Author: Donald Trummell
 */

#ifndef WPE_POLYEVAL_HPP_
#define WPE_POLYEVAL_HPP_

/**
 * Evaluate a polynomial from coefficients; y = coef[n - 1] * x^(n - 1) + . . .
 * + coef[1] * x + coef[0].
 *
 * Parameters:
 * coef: double array with polynommial coefficients, lowest power first.
 * ncoef: number of coefficients (polynomial power minus one.)
 * X: a double value at which we evaluate the polynomial.
 */
double polyEval(const double coef[], const int ncoef, const double x);

#endif /* WPE_POLYEVAL_HPP_ */
