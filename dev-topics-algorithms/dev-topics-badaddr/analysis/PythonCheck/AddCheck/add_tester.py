import random

RAND_SEED = 607
random.seed(RAND_SEED)
LARGE_PRIME = float(7919)

if __name__ == '__main__':
    n = int(50000000)
    print('\nPython Addition Accuracy Test for sequence {:d} long using seed {:d}'.format(n, RAND_SEED))

    test_seq = list()
    for i in range(n):
        test_seq.append((float(i) + 1.0))
    print('  forward: ', str(test_seq[:6]), '. . .', test_seq[-1:])

    sum_exp = (float(n) / 2.0) * (float(n) + 1.0) if (n % 2 == 0) else float(n) * ((float(n) + 1.0) / 2)
    print('  exp sum: {:.0f}'.format(sum_exp))
    assert int(sum_exp) == sum_exp

    sum_f = sum(test_seq)
    print('  act sum: {:.0f} -- using SUM function'.format(sum_f))
    assert sum_f == sum_exp

    sum_f = 0.0
    for i in range(n):
        sum_f += test_seq[i]
    print('  act sum: {:.0f} -- using RAW sum, smallest to largest'.format(sum_f))
    assert sum_f == sum_exp

    # Divided integer values by a prime to introduce representation error

    print('\n --- Now divide by large prime {:.0f} ---'.format(LARGE_PRIME))
    for i in range(n):
        test_seq[i] = test_seq[i] / LARGE_PRIME
    sum_exp /= LARGE_PRIME
    print('True sum is now {:f}'.format(sum_exp))

    sum_f = sum(test_seq)
    delta = sum_f - sum_exp
    print('\nBuiltIn SUM is {:f};  delta: {:f};'
          '  relative error: {:e}'.format(sum_f, delta, delta / sum_exp))

    sum_f = 0.0
    for i in range(n):
        sum_f += test_seq[i]
    delta = sum_f - sum_exp
    print('\nSmallest to largest sum is {:f};  delta: {:f};'
          '  relative error: {:e}'.format(sum_f, delta, delta / sum_exp))

    sum_f = 0.0
    for i in range(n):
        sum_f += test_seq[(n - 1) - i]
    delta = sum_f - sum_exp
    print('\nLargest to smallest sum is {:f};  delta: {:f};'
          '  relative error: {:e}'.format(sum_f, delta, delta / sum_exp))

    # Randomize addition order

    print('\n --- Now Shuffle ---')
    random.seed(3677)
    random.shuffle(test_seq)
    print('Randomized!')

    sum_f = sum(test_seq)
    delta = sum_f - sum_exp
    print('\nBuiltIn sum is {:f};  delta: {:f};'
          '  relative error: {:e}'.format(sum_f, delta, delta / sum_exp))

    sum_f = 0.0
    for i in range(n):
        sum_f += test_seq[i]
    delta = sum_f - sum_exp
    print('\nSimple sum is {:f};  delta: {:f};'
          '  relative error: {:e}'.format(sum_f, delta, delta / sum_exp))

    print('\nPython Addition Check Done.\n')
