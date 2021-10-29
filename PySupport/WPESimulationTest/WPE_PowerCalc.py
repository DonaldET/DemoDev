#
#  WPE_PowerCalc.cpp
#
# Calculate power output for a wind generator
#

from collections import namedtuple

import WPE_PolyEval

# //////--- Generator power Simulation Constructs

# Power_Point record
# 	position: int				Generator position on grid
# 	generator_type: int         Make/Model integer identifier
# 	exp_time: int				Experiment time t
# 	speed: float				Wind speed, m/s
# 	delta_power: float			Power generated, Watts (x10^6)

Power_Point_Record = namedtuple('Power_Point', 'position, generator_type, exp_time, speed, delta_power')

# //////--- Turbine Power Simulation Constructs

TPF_npower: int = 6  # number of polynomial coefficients (polynomial degree + 1)
# Turbine_Power_Factors record
# 	l: float                  	Blade length, m
# 	a: float;                  	Blade swept area, m^2
# 	coef: list[float]; 			Polynomial approximation of Cp as function of wind speed'
# 	cut_in: float             	Minimum wind speed to generate power
# 	cut_out: float            	Maximum operational wind speed

Turbine_Power_Factors_Record = namedtuple('Turbine_Power_Factors', 'l, a, coef, cut_in, cut_out')

# //////--- Power Generation Constructs

# Wind_Factors record
# 	rho: float             		Air density, kg/m^3

Wind_Factors_Record = namedtuple('Wind_Factors', 'rho')


# //////--- Structure Display

def display_tpf(tpf) -> None:
    print("TPF   -> Blade Length: " + str(tpf.l) + "; swept area: " + str(tpf.a)
          + "; cut-in speed: " + str(tpf.cut_in) + "; cut-out speed: "
          + str(tpf.cut_out))
    print("         Coef: " + str(tpf.coef))


def display_wf(wf):
    print("WF    -> rho: " + str(wf.rho))


def display_ppoint(power_point, drop: float) -> None:
    print("Power -> Pos: " + str(power_point.position) + ";  Type: "
          + str(power_point.generator_type), end='')
    print(";  Time: " + str(power_point.exp_time) + ";  Speed: "
          + str(power_point.speed), end='')
    print(";  delta: " + str(power_point.delta_power) + "; drop: " + str(drop))


# //////--- Power calculations

# See https://www.raeng.org.uk/publications/other/23-wind-turbine and
# http://www.windandwet.com/windturbine/power_calc/index.php
# mega-whatts
def power_extracted(v: float, wf,
                    tp) -> float:
    if v < tp.cut_in or v > tp.cut_out:
        return 0.0

    cp: float = WPE_PolyEval.poly_eval(tp.coef, v)
    if cp < 0.0:
        cp = 0.0
    else:
        if cp > 0.59:
            cp = 0.59

    return 0.5 * wf.rho * tp.a * v * v * v * cp / 1000000.0


_ONE_THIRD = 1.0 / 3.0


# v = cube_root(2*p/(rho*cp)), but remove cp because we did not extract all potential power
def wind_speed_drop(p_extracted: float, wf) -> float:
    return (2.0 * p_extracted / wf.rho) ** _ONE_THIRD


# Calculate the net power generated (using Cp) and the wind speed drop
def power_generated(input_point, wf,
                    tp, output_point) -> float:
    v: float = input_point.speed
    p: float = power_extracted(v, wf, tp)
    drop: float = wind_speed_drop(p, wf)
    outpoint = Power_Point_Record(input_point.position, input_point.generator_type, input_point.exp_time,
                                  v - drop, p)
    output_point.append(outpoint)
    return drop
