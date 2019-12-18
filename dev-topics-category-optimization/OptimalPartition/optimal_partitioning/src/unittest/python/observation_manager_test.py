import collections
import unittest

from partitioner.observation_manager import Observation
from partitioner.observation_manager import ObservedValues


class MyTestCase(unittest.TestCase):
    def test_observation(self):
        obs1 = Observation(3)
        self.assertEqual(3, obs1.value, "value differs")
        self.assertEqual(1, obs1.weight)
        obs2 = Observation(3, 2)
        self.assertEqual(3, obs2.value, "value differs")
        self.assertEqual(2, obs2.weight)

        obs3 = Observation(3)
        self.assertEqual(obs3, obs3, "self equality failed")
        self.assertEqual(obs1, obs3, "content equal failed")
        self.assertNotEqual(obs1, obs2, "unequal content seemed equal")

    def test_empty_observations(self):
        values = ObservedValues()
        self.assertTrue(values.observations is not None)
        self.assertEqual(0, len(values.observations))

    def test_one_int_observation(self):
        values = ObservedValues()
        obs1 = Observation(3, 2)
        values.add_observation(obs1)
        self.assertEqual(1, len(values.observations), "length differs")
        self.assertEqual([obs1], values.observations, "stored observation differs")

    def test_multiple_observations(self):
        values = ObservedValues()
        case = [7, 0, 8, 1, 3, 5, 6, 2, 4]
        values.add_observations([Observation(x) for x in case])
        self.assertEqual(len(case), len(values.observations), "stored observations differ")
        self.assertEqual([Observation(x) for x in case], values.observations, "stored observations differ")

    def test_observations_sort(self):
        values = ObservedValues()
        case = [7, 0, 8, 1, 3, 5, 6, 2, 4]
        values.add_observations([Observation(x) for x in case])
        case.sort()
        values.sort_me()
        self.assertEqual([Observation(x) for x in case], values.observations, "sort failed")

    def test_observations_compress(self):
        values = ObservedValues()
        case = [7, 0, 8, 1, 3, 5, 6, 2, 4, 7, 8, 3, 3, 3, 1, 7, 7, 7, 7]
        counts = collections.Counter(case)

        values.add_observations([Observation(x) for x in case])
        values.sort_me()
        values.compress_me(0.001)

        obs = values.observations
        self.assertEqual(len(counts), len(obs), "compressed lengths differ")
        for i in range(len(counts)):
            x = obs[i]
            y = counts[x.value]
            self.assertIsNotNone(y, "value " + str(x.value) + "not found in counts: " + str(counts))
            self.assertEqual(x.weight, y, "weight (count) for " + str(x.value) + " differs")

    def test_observations_compress_delta(self):
        values = ObservedValues()
        case = [7, 0, 8, 1, 3, 5, 6, 2, 4, 7, 8, 3, 3, 3, 1, 7, 7, 7, 7]
        counts = collections.Counter(case)

        values.add_observations([Observation(x) for x in case])
        delta = 0.001
        t = 1
        values.add_observation(Observation(t + delta / 2.0))
        values.sort_me()
        values.compress_me(delta)

        obs = values.observations
        self.assertEqual(len(counts), len(obs), "compressed lengths differ")
        for i in range(len(counts)):
            x = obs[i]
            y = counts[x.value]
            self.assertIsNotNone(y, "value " + str(x.value) + "not found in counts: " + str(counts))
            self.assertEqual(x.weight, y + 1 if x.value == t else y, "weight (count) for " + str(x.value) + " differs")


if __name__ == '__main__':
    unittest.main()
