import abstract_scorer as scorer


class SumSquareScorer(scorer.AbstractScorer):
    """
    Score a raw assignment (the collection of partition capacities) by summing squares of counts
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
