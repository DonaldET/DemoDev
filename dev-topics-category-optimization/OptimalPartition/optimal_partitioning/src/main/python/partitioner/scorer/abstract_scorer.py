"""
abstract_scorer.py

Defines API for a Scorer
"""
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
    Score a raw assignment (the collection of partition category_counts.) A partition creates a category by establishing
    score boundaries that define category membership. A scorer is expected to have access to the actual scores
    referenced by the category_counts. A scorer is expected to have access to the observations referenced by the
    category sequence.
    """

    @abstractmethod
    def measure(self, category_counts):
        """
        Compute the score associated with a partition assignment; each partition has as many elements as the
        corresponding category count entry. The scorer references the observations based on the category counts.
        Args:
            category_counts: the count of members of a potential category; each element of the category counts
            defines the count of observations in the category.
        Returns: the numeric score (measure) associated with that assignment
        """
        pass
