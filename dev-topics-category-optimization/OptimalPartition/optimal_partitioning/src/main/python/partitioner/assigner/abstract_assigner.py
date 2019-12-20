from abc import ABC, abstractmethod

from partitioner.scorer import abstract_scorer


# #################################################################################### #
# Copyright (c) 2019. Donald E. Trummell. All Rights Reserved.                         #
# Permission to use, copy, modify, and distribute this software and its documentation  #
# for educational, research, and not-for-profit purposes, without fee and without      #
# a signed licensing agreement, is hereby granted, provided that the above             #
# copyright notice, and this paragraph, appear in all copies, modifications, and       #
# distributions. Contact dtrummell@gmail.com for commercial licensing opportunities.   #
# #################################################################################### #

class Assignment(object):
    """
    An assignment associates a score with a sequence of category_counts (representing a particular partition) and has an
    identifier
    """

    def __init__(self, assignment_id, capacities, score):
        """
        Create an assignment with a sequence of category_counts (a particular partition), an identifier, and a score
        :param assignment_id: a unique identifier for this assignment
        :param capacities: an ordered sequence of category_counts, corresponding to a particular partition
        :param score: a value associated with the partition category_counts
        """
        self.partition_id = assignment_id
        self.capacities = capacities
        self.score = score

    def __str__(self):
        msg = '['
        msg += super(Assignment, self).__str__()
        msg += ' :: '
        msg += ';  '.join(
            ['AssignID: {:s}'.format(str(self.partition_id)),
             'score: {:s}'.format(str(self.score)),
             'category_counts: {:s}'.format(str(self.capacities))])
        msg += ']'
        return msg


class Collector(object):
    """
    A collector examines many raw assignments and applies a score to each assignment, an d then it also records the
    assignment with the maximum score
    """

    def __init__(self, scorer):
        """
        Examine in-coming assignments and apply a score; record the assignment with the maximum score
        :param scorer: the scorer used to assign a measure value to the sequence of category_counts
        """
        assert scorer is not None
        assert isinstance(scorer, abstract_scorer.AbstractScorer)
        self.scorer = scorer

        self.max_assignment = None
        self.examined = 0

    def reset(self):
        self.max_assignment = None
        self.examined = 0

    def add_assignment(self, assignment_id, category_counts, trace=False):
        """
        Assess an assignment, apply the score and record the assignment if it is the current max
        :param assignment_id: the identifier to apply to the assignment
        :param category_counts: the ordered sequence of category_counts
        :param trace: if True, display execution information
        :return: the current maximum assignment
        """
        assignment = Assignment(assignment_id, list(category_counts), self.scorer.measure(category_counts))
        if self.max_assignment is None:
            self.max_assignment = assignment
        elif self.max_assignment.score < assignment.score:
            self.max_assignment = assignment
        self.examined += 1
        if trace:
            print('--- ADD: ', assignment_id, assignment.score, '-- max assignment -->', str(self.max_assignment),
                  '----\n')
        return self.max_assignment

    def __str__(self):
        msg = '['
        msg += super(Collector, self).__str__()
        msg += ' :: max_assignment: '
        msg += str(self.max_assignment)
        msg += ';  examined:' + str(self.examined)
        msg += ']'
        return msg


class AbstractAssigner(ABC):
    """
    An assigner implementation generates all scored assignments for the specified partitioning parameters
    """

    @abstractmethod
    def __init__(self, scorer, partition_count, space_size, min_size, max_size):
        assert scorer is not None
        assert partition_count > 1
        assert space_size > 0
        assert min_size > 0
        assert max_size > min_size

        assert space_size >= partition_count * min_size
        assert space_size <= partition_count * max_size

        self.scorer = scorer
        self.partition_count = partition_count
        self.space_size = space_size
        self.min_size = min_size
        self.max_size = max_size

    @abstractmethod
    def generate_all(self):
        pass
