from abc import ABC, abstractmethod


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
