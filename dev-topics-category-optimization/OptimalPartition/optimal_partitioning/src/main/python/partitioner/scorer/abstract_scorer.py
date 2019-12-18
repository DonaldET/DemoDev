from abc import ABC, abstractmethod


# #################################################################################### #
# Copyright (c) 2019. Donald E. Trummell. All Rights Reserved.                         #
# Permission to use, copy, modify, and distribute this software and its documentation  #
# for educational, research, and not-for-profit purposes, without fee and without      #
# a signed licensing agreement, is hereby granted, provided that the above             #
# copyright notice, and this paragraph, appear in all copies, modifications, and       #
# distributions. Contact dtrummell@gmail.com for commercial licensing opportunities.   #
# #################################################################################### #

class AbstractScorer(ABC):
    """
    Score a raw assignment (the collection of partition capacities)
    """

    @abstractmethod
    def measure(self, capacity_sequence):
        """
        Compute the score associated with a partition assignment
        Args:
            capacity_sequence: the sequences of partitions
        Returns: the numeric score (measure) associated with that assignment
        """
        pass
