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
    Score a raw assignment (the collection of partition capacities) by summing squares of counts. An example
    """

    def __init__(self):
        pass

    def measure(self, capacity_sequence):
        """
        Sum squares of counts
        :param capacity_sequence: an ordered collection of partition capacity values in an assignment
        :return: sum of squared counts
        """

        return sum([x * x for x in capacity_sequence])
