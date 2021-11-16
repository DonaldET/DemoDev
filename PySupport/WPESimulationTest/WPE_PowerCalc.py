#
#  WPE_PowerCalc.cpp
#
# Calculate power output for a wind generator
#

import WPE_PolyEval


# //////--- Generator power Simulation Constructs

class PowerPoint:
    def __init__(self, position: int, generator_type: int, exp_time: int, speed: float, delta_power: float):
        self.position: int = position  # generator position on grid
        # enum-like value linking generator characteristics to position in wind farm.
        self.generator_type: int = generator_type
        self.exp_time: int = exp_time  # t (milliseconds)
        self.speed: float = speed  # m / s
        self.delta_power: float = delta_power  # watts (x10 ^ 6)


# //////--- Turbine Power Simulation Constructs

TPF_npower: int = 6  # number of polynomial coefficients (polynomial degree + 1)


class TurbinePowerFactors:
    def __init__(self, blade_length: float, a: float, coef: list[float], cut_in: float, cut_out: float):
        self.l: float = blade_length  # Blade length
        self.a: float = a  # Blade swept area
        # coefficients of a polynomial approximation of Cp as function of wind speed'
        self.coef: list[float] = coef
        self.cut_in: float = cut_in  # Initial power generation wind speed
        self.cut_out: float = cut_out  # Just exceeds greatest operational wind speed


# //////--- Power Generation Constructs

class WindFactors:
    def __init__(self, rho: float):
        self.rho: float = rho  # Air density, kg / m ^ 3


# //////--- Structure Display

def display_tpf(tpf: TurbinePowerFactors) -> None:
    print("TPF   -> Blade Length: " + str(tpf.l) + "; swept area: " + str(tpf.a)
          + "; cut-in speed: " + str(tpf.cut_in) + "; cut-out speed: "
          + str(tpf.cut_out))
    print("         Coef: " + str(tpf.coef))


def display_wf(wf: WindFactors):
    print("WF    -> rho: " + str(wf.rho))


def display_ppoint(power_point: PowerPoint, drop: float) -> None:
    print("Power -> Pos: " + str(power_point.position) + ";  Type: "
          + str(power_point.generator_type), end='')
    print(";  Time: " + str(power_point.exp_time) + ";  Speed: "
          + str(power_point.speed), end='')
    print(";  delta: " + str(power_point.delta_power) + "; drop: " + str(drop))


# //////--- Power calculations

# See https://www.raeng.org.uk/publications/other/23-wind-turbine and
# http://www.windandwet.com/windturbine/power_calc/index.php
# mega-whatts
def power_extracted(v: float, wf: WindFactors, tpf: TurbinePowerFactors) -> float:
    if v < tpf.cut_in or v > tpf.cut_out:
        return 0.0

    cp: float = WPE_PolyEval.poly_eval(tpf.coef, v)
    if cp < 0.0:
        cp = 0.0
    else:
        if cp > 0.59:
            cp = 0.59

    return 0.5 * wf.rho * tpf.a * v * v * v * cp / 1000000.0


_ONE_THIRD = 1.0 / 3.0


# v = cube_root(2*p/(rho*cp)), but remove cp because we did not extract all potential power
def wind_speed_drop(p_extracted: float, wf: WindFactors) -> float:
    return (2.0 * p_extracted / wf.rho) ** _ONE_THIRD


# Calculate the net power generated (using Cp) and the wind speed drop
def power_generated(input_point: PowerPoint, wf: WindFactors,
                    tpf: TurbinePowerFactors, output_point: PowerPoint) -> float:
    output_point.position = input_point.position
    output_point.generator_type = input_point.generator_type
    output_point.exp_time = input_point.exp_time
    v: float = input_point.speed
    p: float = power_extracted(v, wf, tpf)
    output_point.delta_power = p
    drop: float = wind_speed_drop(p, wf)
    output_point.speed = v - drop
    return drop
