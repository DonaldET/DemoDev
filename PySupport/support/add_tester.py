from __future__ import division
from __future__ import print_function

import random

if __name__ == '__main__':

    n = int(50000000)
    print('Addition Accuracy Test for sequence {:d} long'.format(n))

    test_seq = list()
    for i in range(n):
        test_seq.append((float(i) + 1.0))
    print('  forward: ', test_seq[:6])

    sum_exp = (float(n) / 2.0) * (float(n) + 1.0)
    print('  exp sum: {:.0f}'.format(sum_exp))
    assert int(sum_exp) == sum_exp

    sum_f = sum(test_seq)
    print('  act sum: {:.0f}'.format(sum_f))
    assert sum_f == sum_exp

    LARGE_PRIME = float(7919)
    print('\nNow divide by large prime {:.0f}'.format(LARGE_PRIME))
    for i in range(n):
        test_seq[i] = test_seq[i] / LARGE_PRIME
    sum_exp /= LARGE_PRIME

    sum_f = sum(test_seq)
    delta = sum_f - sum_exp
    print('\nNo-shuffle fractional builtin sum is {:f};  delta: {:f};'
          '  relative error: {:e}'.format(sum_f, delta, delta / sum_exp))

    print('\nNow Shuffle')
    random.seed(3677)
    random.shuffle(test_seq)

    print('\nUse builtin SUM')
    sum_f = sum(test_seq)
    delta = sum_f - sum_exp
    print('Forward fractional builtin sum is {:f};  delta: {:f};'
          '  relative error: {:e}'.format(sum_f, delta, delta / sum_exp))

    print('\nUse uncorrected SUM')
    sum_f = 0.0
    for i in range(n):
        sum_f += test_seq[i]
    delta = sum_f - sum_exp
    print('Forward fractional simple sum is {:f};  delta: {:f};'
          '  relative error: {:e}'.format(sum_f, delta, delta / sum_exp))
