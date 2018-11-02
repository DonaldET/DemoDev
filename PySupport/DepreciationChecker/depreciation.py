# depreciation.py - print depreciation schedule
import math
import sys

LOW_VALUE = 2000.0
RECURSION_LIMIT = int(500)


def _get_params(args):
    if len(args) != 3:
        raise ValueError('incorrect arg count, expected 3 but got {:d}'.format(len(args)))

    try:
        amt = float(args[1])
    except ValueError:
        raise ValueError('amount not a number:', str(args[1]))
    assert amt > 0.0

    try:
        dep_rate = float(args[2])
    except ValueError:
        raise ValueError('rate not a number:', str(args[2]))
    assert dep_rate > 0.0
    assert dep_rate < 1.0

    return amt, dep_rate


def recurse_depreciate(amt, r):
    """
    Depreciate using float, but carry 4 digits like the NYSE, with recursion
    :param amt: Beginning amount
    :param r: depreciation rate
    :return: number of iterations
    """
    assert r > 0.0
    assert r < 1.0
    assert amt >= 0.0
    end_period = 0
    iterations = _recurse_dep_helper(end_period, amt, r)
    assert iterations <= RECURSION_LIMIT
    return iterations


def _recurse_dep_helper(period, init_amt, dep_rate):
    if period >= RECURSION_LIMIT:
        print('maximum recursion depth reached')
        return -period

    if init_amt <= LOW_VALUE:
        end_amt = LOW_VALUE
    else:
        end_amt = round((1.0 - dep_rate) * init_amt, 4)
        if end_amt <= LOW_VALUE:
            end_amt = LOW_VALUE
    print('{:5d}.  Initial value: {:.2f}  End Value {:.2f}'.format(period, init_amt, end_amt))

    if end_amt <= LOW_VALUE:
        return period
    return _recurse_dep_helper(period + 1, end_amt, dep_rate)


def iterative_depreciate(amt, r):
    """
    Depreciate with float, but carry 4 digits like the NYSE, with iteration
    :param amt: Beginning amount
    :param r: depreciation rate
    :return: number of iterations
    """
    assert r > 0.0
    assert r < 1.0
    assert amt >= 0.0
    iterations = 0
    end_amt = _iterative_dep_helper(iterations, amt, r)
    while end_amt > LOW_VALUE:
        iterations += 1
        end_amt = _iterative_dep_helper(iterations, end_amt, r)
    assert iterations <= RECURSION_LIMIT
    return iterations


def _iterative_dep_helper(period, init_amt, dep_rate):
    if period >= RECURSION_LIMIT:
        print('maximum iteration count reached')
        return -period

    if init_amt <= LOW_VALUE:
        end_amt = LOW_VALUE
    else:
        end_amt = round((1.0 - dep_rate) * init_amt, 4)
        if end_amt <= LOW_VALUE:
            end_amt = LOW_VALUE
    print('{:5d}.  Initial value: {:.2f}  End Value {:.2f}'.format(period, init_amt, end_amt))
    return end_amt


def math_depreciate(amt, r):
    """
    Use precise geometric sum
    :param amt:
    :param r:
    :return:
    """
    assert r > 0.0
    assert r < 1.0
    assert amt >= 0.0
    # find the number of periods
    if amt <= LOW_VALUE:
        print('{:5d}.  Initial value: {:.2f}  End Value {:.2f}'.format(0, amt, LOW_VALUE))
        return 0
    np = math.ceil((math.log(float(LOW_VALUE)) - math.log(amt)) / math.log((1.0 - r)))
    if np > 0:
        prior_amt = amt
        for iteration in range(np):
            dep_amt = round(amt * (1.0 - r) ** (iteration + 1), 4)
            if dep_amt <= LOW_VALUE:
                dep_amt = LOW_VALUE
            print('{:5d}.  Initial value: {:.2f}  End Value {:.2f}'.format(iteration, prior_amt, dep_amt))
            prior_amt = dep_amt
    return np


if __name__ == '__main__':
    print('Depreciation Calculation')
    amount, rate = _get_params(sys.argv)
    print('  -- Initial Amount: ${:s}'.format(str(amount)))
    print('  -- Depreciation  : {:.02f}%'.format(100.0 * rate))

    print('\nRecursion Schedule:')
    iter_recurse = recurse_depreciate(amount, rate)
    print('\nIteration Schedule')
    iter_depreciate = iterative_depreciate(amount, rate)
    print('\nGeometric Series Schedule')
    iter_math = math_depreciate(amount, rate)
