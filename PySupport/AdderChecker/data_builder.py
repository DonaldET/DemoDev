from __future__ import division
from __future__ import print_function

import random

# data_builder.py - create randomized test sequence
RAND_SEED = 5741
LARGE_PRIME = float(7919)


def build_test_sequence(seq_length):
    assert seq_length is not None
    assert isinstance(seq_length, int)
    assert seq_length > 0

    print('Generate Test Data sequence {:d} long using seed {:d}'.format(seq_length, RAND_SEED))

    test_seq = list()
    for i in range(seq_length):
        test_seq.append((float(i) + 1.0) / LARGE_PRIME)
    print('  -- Forward : ', test_seq[:6], '. . .', test_seq[-1:])
    random.seed(RAND_SEED)
    random.shuffle(test_seq)
    print('  -- Shuffled: ', test_seq[:6], '. . .', test_seq[-1:])

    sum_exp = (float(seq_length) / 2.0) * (float(seq_length) + 1.0)
    assert int(sum_exp) == sum_exp
    sum_exp = sum_exp / LARGE_PRIME
    print('  -- True sum:   {:.16f}'.format(sum_exp))

    return test_seq, sum_exp


if __name__ == '__main__':
    n = 10
    print('Test of creating a {:d} element test sequence'.format(n))
    seq, true_sum = build_test_sequence(n)
    print('\nGot sum: {:.16f} -- {:s}'.format(true_sum, str(seq)))
