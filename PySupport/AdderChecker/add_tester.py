# add_tester.py - check large sequence addition
from __future__ import division
from __future__ import print_function

import data_builder

if __name__ == '__main__':

    n = int(50000000)
    print('Addition Accuracy Test for sequence {:d} long'.format(n))
    test_seq, sum_exp = data_builder.build_test_sequence(n)

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
