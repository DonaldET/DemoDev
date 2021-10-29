#
# WPE_PolyEval.py
#
#  Evaluates a polynomial
#

Coefficients = list[float]


def poly_eval(coefficients: Coefficients, x: float) -> float:
    value: float = 0.0
    n: int = len(coefficients)
    if n > 0.0:
        for i in range(n - 1, -1, -1):
            value = x * value + coefficients[i]
    return value
