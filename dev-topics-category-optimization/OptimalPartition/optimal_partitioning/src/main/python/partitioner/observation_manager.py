"""
observation_manager.py

Observation management - handle weighted observations; and observation has a weight and a value.
"""


# #################################################################################### #
# Copyright (c) 2019. Donald E. Trummell. All Rights Reserved.                         #
# Permission to use, copy, modify, and distribute this software and its documentation  #
# for educational, research, and not-for-profit purposes, without fee and without      #
# a signed licensing agreement, is hereby granted, provided that the above             #
# copyright notice, and this paragraph, appear in all copies, modifications, and       #
# distributions. Contact dtrummell@gmail.com for commercial licensing opportunities.   #
# #################################################################################### #

#
# ObservationValues is a list of weighted observations, which frequently have (nearly) identical values, each having an
# independent, and possibly identical weight. Often the weights associated with unweighted observations has value 1.0.
# In this case, the weight is the count of the number of identical observations, identical values meaning:
# abs(x[i] - x[j]) < EPS for all i and j in the set of equal observations.
#
# Implementation notes for Python vs Java
# -- general comparison for Python sorting:
#    http://stackoverflow.com/questions/30043067/python3-style-sorting-old-cmp-method-functionality-in-new-key-mechanism
# -- sorting overview:
#    The list of observations is large, so the Java/Python implementation sorts in place (list.sort())
#

def _key_comp(observation):
    """
    The observation sort by value comparison key
    Args:
        observation: A value and weight pair
    Returns: the floating point representation for the value of the observation
    """
    return float(observation.value)


class Observation(object):
    """
    An  observation has a weight and a value, representing a data element in a partition. A partition is a collection
    of observations
    """

    def __init__(self, value, weight=1.0):
        self.value = float(value)
        self.weight = float(weight)

    def __hash__(self):
        return 37 * hash(self.value) + hash(self.weight)

    def __eq__(self, other):
        return self.__class__ == other.__class__ and self.value == other.value and self.weight == other.weight

    def __ne__(self, other):
        return not self.__eq__(other)

    def __repr__(self):
        msg = str(self.value)
        if self.weight != 1.0:
            msg += '(' + str(self.weight) + ')'
        return msg

    def __str__(self):
        return self.__repr__()


class ObservedValues:
    """
    Observed values are a collection of observation values and associated weights created from weighted input
    observations. Once accumulated, (nearly) identical values are compressed to create single entries with weights
    corresponding to the count of identical observations.
    """

    def __init__(self):
        self.observations = []

    def add_observation(self, value):
        """
        Add an un-weighed observation (weight is one.)
            value: the observation numeric value
        Returns: number of accumulated unweighted observations
        """
        assert value is not None

        self.observations.append(value)
        return len(self.observations)

    def add_observations(self, values):
        assert values is not None
        assert len(values) > 0

        for value in values:
            self.add_observation(value)

        return len(self.observations)

    def sort_me(self):
        """
        Orders unweighted observations and combines the weights of nominally equal observations.

        Returns: the count of grouped observations, where equal observations are represented by a single value and
        an accumulated weight.
        """
        assert self.observations is not None
        n = len(self.observations)
        if n > 1:
            self.observations.sort(key=lambda x: _key_comp(x))
        return len(self.observations)

    def compress_me(self, delta):
        """
        Convert a sorted list of unweighted observations into a list of weighted observation by summing the weights
        associated with a common (nearly identical) observation value. Note, observations are modified in place.
            delta: the minimum difference in values for two observations to be considered different
        Returns: the total weights of all observations and modifies the observations inplace
        """
        assert delta is not None
        delta = float(delta)
        assert delta >= 0.0
        assert self.observations is not None

        n = len(self.observations)
        w = n
        if n > 1:
            last_value = self.observations[0].value
            new_obs = []
            for unweighted_obs in self.observations:
                assert last_value <= unweighted_obs.value
                last_value = unweighted_obs.value
                if len(new_obs) < 1:
                    new_obs.append(unweighted_obs)
                else:
                    if abs(new_obs[-1].value - unweighted_obs.value) > delta:
                        new_obs.append(unweighted_obs)
                    else:
                        new_obs[-1].weight += unweighted_obs.weight
            self.observations = new_obs
            w = sum([observation.weight for observation in self.observations])
        return w

    def __repr__(self):
        msg = '[' + str(self.__class__)
        msg += ';  Nk: '
        n = len(self.observations)
        msg += str(n)
        if n > 0:
            msg += ';  {'
            if n < 10:
                for i in range(n):
                    if i > 0:
                        msg += ';  '
                    msg += str(self.observations[i])
            else:
                k = min(5, n)
                for i in range(k):
                    if i > 0:
                        msg += ';  '
                    msg += str(self.observations[i])
                if n > k:
                    msg += '; . . .'
                    remaining = n - k
                    for i in range(remaining + 1, n):
                        if i > 0:
                            msg += ';  '
                        msg += str(self.observations[i])
            msg += '}'
        msg += ']'
        return msg

    def __str__(self):
        return self.__repr__()
