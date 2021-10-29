# ============================================================================
# Name        : WPESimulation.py
# Author      : Donald Trummell
# Version     : 0.1.0
# Copyright   : (c) 2021
# Description : Estimate wind power generation
# ============================================================================

from math import pi

import WPE_PolyEval
import WPE_PowerCalc
import WPE_Util

# The number of sequential generators
_INLINE_GENERATORS: int = 10
# Setup some random wind
_winds: list[float] = [8.0, 11.0, 5.0, 24.0, 30.0, 7.0, 3.0, 26.0, 2.0, 9.0,
                       4.0, 9.0, 17.0, 20.0, 29.0, 18.0, 7.0, 13.0, 20.0, 25.0, 22.0, 18.0,
                       21.0, 13.0, 3.0, 6.0, 20.0, 22.0, 26.0, 6.0, 29.0, 23.0, 6.0, 16.0]
# Number of random wind values
_RANDOM_WIND_COUNT = len(_winds)
# Number of repetitions
_REPEATS = 25000


def populate_test_coefficient() -> list[float]:
    e33_coef: list[float] = [-2.683640e-01, 2.385518e-01,
                             -2.001438e-02, 4.243324e-05, 3.780910e-05, -8.796243e-07]
    return e33_coef


def test_time() -> None:
    now: int = WPE_Util.get_current_time_ms()
    print("    Time0: " + str(now))
    x: float = 2.3
    for i in range(1, 1000000):
        x = (1.0034 + x) * x * x ** 1.002

    then: int = WPE_Util.get_current_time_ms()
    print("    Time1: " + str(now))
    print("    Elapsed = " + str(then - now) + " ms.\n")


def test_poly_eval() -> None:
    print("Testing polynomial evaluation:")
    coef: list[float] = [3.0, 2.0, 1.0]
    print(" f(" + str(coef[0]) + " + " + str(coef[1]) + "*x + " + str(coef[2])
          + "*x^2", end='')
    print("; x=", end='')
    x: float = 2.0
    print(str(x), end='')
    y: float = WPE_PolyEval.poly_eval(coef, x)
    print(") = " + str(y) + "\n")


if __name__ == '__main__':
    print("*** Simulate Wind Power Generation (Python) ***")

    test_time()
    test_poly_eval()

    l: float = 52.0
    a: float = pi * l * l
    tp = WPE_PowerCalc.Turbine_Power_Factors_Record(l, a,
                                                    populate_test_coefficient(), 2,
                                                    27)
    WPE_PowerCalc.display_tpf(tp)

    wf = WPE_PowerCalc.Wind_Factors_Record(1.23)
    WPE_PowerCalc.display_wf(wf)

    count: int = 0
    sumPower: float = 0.0
    sumDrop: float = 0.0
    start: int = WPE_Util.get_current_time_ms()
    for rep in range(_REPEATS):
        for wind in _winds:
            p1 = WPE_PowerCalc.Power_Point_Record(3344, 0, 0, wind, 0)
            p2 = WPE_PowerCalc.Power_Point_Record(3344, 0, 0, 0, 0)
            pre = p1
            post = p2

            # WPE_PowerCalc.display_ppoint(pre, 0)
            pre = WPE_PowerCalc.Power_Point_Record(pre.position, pre.generator_type, pre.exp_time + 1, pre.speed,
                                                   pre.delta_power)

            drop: float = 0.0
            for generator in range(_INLINE_GENERATORS):
                count += 1
                post_output = []
                drop = WPE_PowerCalc.power_generated(pre, wf, tp, post_output)
                post = post_output[0]
                if drop < 0.0:
                    print("****ERROR: drop " + str(drop) + " bad at generator "
                          + str(generator) + " of wind " + str(wind)
                          + " at repetition " + str(rep))

                if rep == 1:
                    sumDrop += drop
                    sumPower += post.delta_power

                # WPE_PowerCalc.display_ppoint(post, drop)
                tmp = pre
                pre = WPE_PowerCalc.Power_Point_Record(post.position, post.generator_type, post.exp_time + 1,
                                                       post.speed, post.delta_power)
                post = tmp

    stop: int = WPE_Util.get_current_time_ms()
    elapsed: int = stop - start
    print("\n******\n** " + str(elapsed) + " MS required for "
          + str(_INLINE_GENERATORS) + " generators against " + str(_RANDOM_WIND_COUNT)
          + " random wind speeds over " + str(_REPEATS) + " repetitions.")
    print("** Calculations  : " + str(count))
    cases: int = _RANDOM_WIND_COUNT * _INLINE_GENERATORS
    print("** Avg Power (Mw): " + str(sumPower / cases))
    print("** Avg Speed drop: " + str(sumDrop / cases) + " m/s")
    print("******")
    print("Done.")
