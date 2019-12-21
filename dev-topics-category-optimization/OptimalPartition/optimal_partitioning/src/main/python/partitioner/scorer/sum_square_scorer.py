"""
sum_square_scorer.py
"""
from partitioner.scorer import abstract_scorer as scorer


# #################################################################################### #
# Copyright (c) 2019. Donald E. Trummell. All Rights Reserved.                         #
# Permission to use, copy, modify, and distribute this software and its documentation  #
# for educational, research, and not-for-profit purposes, without fee and without      #
# a signed licensing agreement, is hereby granted, provided that the above             #
# copyright notice, and this paragraph, appear in all copies, modifications, and       #
# distributions. Contact dtrummell@gmail.com for commercial licensing opportunities.   #
# #################################################################################### #

class SumSquareScorer(scorer.AbstractScorer):
    """
    Score a raw assignment (the collection of partition category_counts) by summing squares of deviations around the
    mean. Since we really want to minimize this value, but we are using a GREATER-THAN operation, we return the negative
    sum of deviations around the mean.
    """

    # T = Sum( (x - u)**2 ) = Sum( x**2 - 2ux + u**2 ) = Sum( x**2 ) + 2u * Sum( x ) + Sum( u**2 ), so
    # T = Sum( x**2 ) - 2n * u**2 + n * u**2 = Sum ( x**2 ) - n * u**2 = Sum( x**2 ) - n * (Sum( x ) / n)**2, so
    # T = Sum( x**2 ) - Sum( x )**2 / n

    def __init__(self, prepared_observations):
        assert prepared_observations is not None
        assert len(prepared_observations.observation_values) > 1
        self.observation_values = prepared_observations.observation_values

    def _check_counts(self, category_counts):
        k = len(category_counts)
        assert k > 1
        for x in category_counts:
            assert int(x) > 0 and int(x) == float(x)
        n_in_bins = sum(category_counts)
        n_observed = len(self.observation_values)
        assert n_in_bins == n_observed
        return n_observed, k

    def measure(self, category_counts):
        """
        Sum squared deviation around the mean
        :param category_counts: an ordered collection of observation counts for each category in an assignment
        :return: sum of squared values
        """
        self._check_counts(category_counts)

        i = 0
        sum_squares = 0.0
        for count in category_counts:
            k = i + count
            sum_squares += _sum_squared_deviations(
                [self.observation_values[j] for j in range(i, k)])
            i = k

        return -sum_squares


def _sum_squared_deviations(x):
    assert x is not None
    assert len(x) > 1
    sum_x = sum([z.value * z.weight for z in x])
    sum_x_squared = sum([z.value * z.value * z.weight for z in x])
    sum_weights = sum([z.weight for z in x])
    return sum_x_squared - sum_x * sum_x / sum_weights
