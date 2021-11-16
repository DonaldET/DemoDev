# ============================================================================
# Name        : WPESimulation.py
# Author      : Donald Trummell
# Version     : 0.1.0
# Copyright   : (c) 2021
# Description : Simulate wind power generation
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


def populate_test_coefficient(tpf: WPE_PowerCalc.TurbinePowerFactors) -> list[float]:
    e33_coef: list[float] = [-2.683640e-01, 2.385518e-01,
                             -2.001438e-02, 4.243324e-05, 3.780910e-05, -8.796243e-07]
    for _i in range(WPE_PowerCalc.TPF_npower):
        tpf.coef[_i] = e33_coef[_i]
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
    cp_est_coef: list[float] = [3.0, 2.0, 1.0]
    print(" f(" + str(cp_est_coef[0]) + " + " + str(cp_est_coef[1]) + "*x + " + str(cp_est_coef[2])
          + "*x^2", end='')
    print("; x=", end='')
    x: float = 2.0
    print(str(x), end='')
    y: float = WPE_PolyEval.poly_eval(cp_est_coef, x)
    print(") = " + str(y) + "\n")


if __name__ == '__main__':
    print("*** Simulate Wind Power Generation (Python) ***")

    test_time()
    test_poly_eval()

    #
    # Simulation Process is:
    # 1) Organize statistics
    # 2) Create input structure
    # 3) Create output structure
    # 4) Calculate power generation, input -> output
    # 5) Update statistics
    # 6) Swap input and output
    # 7) Repeat from (4) N times (N is number of generators in a wind channel
    #

    blade_length: float = 52.0  # m
    swept_area: float = pi * blade_length * blade_length  # m^2
    coef: list[float] = [0.0 for _i in range(WPE_PowerCalc.TPF_npower)]
    cut_in = 2  # m/s
    cut_out = 27  # m/s
    tpf: WPE_PowerCalc.TurbinePowerFactors = WPE_PowerCalc.TurbinePowerFactors(blade_length, swept_area, coef, cut_in,
                                                                               cut_out)
    populate_test_coefficient(tpf)
    WPE_PowerCalc.display_tpf(tpf)

    wf = WPE_PowerCalc.WindFactors(1.23)
    WPE_PowerCalc.display_wf(wf)

    p1 = WPE_PowerCalc.PowerPoint(3344, 0, 0, 0, 0)
    p2 = WPE_PowerCalc.PowerPoint(3344, 0, 0, 0, 0)

    count: int = 0
    sumPower: float = 0.0
    sumDrop: float = 0.0
    start: int = WPE_Util.get_current_time_ms()
    for rep in range(_REPEATS):
        for wind in _winds:
            # self.position: int = position  # generator position on grid
            # # enum-like value linking generator characteristics to position in wind farm.
            # self.generator_type: int = generator_type
            # self.exp_time: int = exp_time  # t (milliseconds)
            # self.speed: float = speed  # m / s
            # self.delta_power: float = delta_power  # watts (x10 ^ 6)

            p1.position = 3344
            p1.generator_type = 0
            p1.exp_time = 0
            p1.speed = wind
            p1.delta_power = 0.0

            pre = p1
            post = p2
            # WPE_PowerCalc.display_PPoint(pre, 0)
            pre.exp_time += 1

            drop: float = 0.0
            for generator in range(_INLINE_GENERATORS):
                count += 1
                drop = WPE_PowerCalc.power_generated(pre, wf, tpf, post)
                if drop < 0.0:
                    print("****ERROR: drop " + str(drop) + " bad at generator "
                          + str(generator) + " of wind " + str(wind)
                          + " at repetition " + str(rep))

                if rep == 1:
                    sumDrop += drop
                    sumPower += post.delta_power

                # WPE_PowerCalc.display_PPoint(post, drop);

                tmp: WPE_PowerCalc.PowerPoint = pre
                pre = post
                pre.exp_time += 1
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
