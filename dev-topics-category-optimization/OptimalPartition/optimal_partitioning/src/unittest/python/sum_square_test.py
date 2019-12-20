import unittest

from partitioner.observation_manager import Observation, ObservedValues
from partitioner.scorer.sum_square_scorer import SumSquareScorer


class SumSquareTestCase(unittest.TestCase):

    def test_precondition(self):
        with self.assertRaises(AssertionError):
            scorer = SumSquareScorer(None)

        observation_values = ObservedValues()
        with self.assertRaises(AssertionError):
            scorer = SumSquareScorer(observation_values)

        observation_values.add_observation(Observation(1, 1))
        with self.assertRaises(AssertionError):
            scorer = SumSquareScorer(observation_values)

        observation_values.add_observation(Observation(2, 1))
        scorer = SumSquareScorer(observation_values)
        self.assertIsNotNone(scorer)

    def test_two_categories(self):
        observation_values = ObservedValues()
        observation_values.add_observation(Observation(1, 1))
        observation_values.add_observation(Observation(2, 2))
        observation_values.add_observation(Observation(3, 3))
        observation_values.add_observation(Observation(4, 2))
        observation_values.add_observation(Observation(5, 1))
        observation_values.add_observation(Observation(6, 1))
        observation_values.add_observation(Observation(7, 1))
        scorer = SumSquareScorer(observation_values)
        score = scorer.measure([3, 4])

        observed = observation_values.observation_values
        assert len(observed) == 7
        local1 = _local_deviation_sum([observed[i] for i in range(0, 3)])
        local2 = _local_deviation_sum([observed[i] for i in range(3, 7)])
        expected = -(local1 + local2)
        self.assertAlmostEqual(expected, score, places=12, msg="score differs")


def _local_deviation_sum(observations):
    w = sum([x.weight for x in observations])
    mean = float(sum([x.value * x.weight for x in observations])) / float(w)
    return sum([((x.value - mean) ** 2) * x.weight for x in observations])

    if __name__ == '__main__':
        unittest.main()
