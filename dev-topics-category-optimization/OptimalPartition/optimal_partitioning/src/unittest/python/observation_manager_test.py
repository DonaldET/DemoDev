import unittest

from partitioner.observation_manager import ObservedValues
from partitioner.observation_manager import Observation

class MyTestCase(unittest.TestCase):
    def test_observation(self):
        obs1 = Observation(3)

    def test_empty_observations(self):
        values = ObservedValues()
        self.assertTrue(values.observations is not None)
        self.assertEqual(0, len(values.observations))

    def test_one_int_obs(self):
        values = ObservedValues()


if __name__ == '__main__':
    unittest.main()

    # print('Testing Observations and ObservedValues\n')
    # obs = Observation(42)
    # print('an observation:', obs)
    # obs = Observation(3.14, 2.78)
    # print('another observation:', obs, '\n')
    #
    # obs = ObservedValues()
    # print('Created a collection of Observations in ObservedValues', '\nObservations 0:', str(obs))
    #
    # y = 0.0000
    # obs.add_observation(Observation(y))
    # print('Observations 1:', str(obs))
    #
    # y = [0.0001]
    # obs.add_observations([Observation(yp) for yp in y])
    # print('Observations 1a:', str(obs))
    #
    # y = [0.3333, 0.3334, 0.33335, 0.6666, 0.6667]
    # obs.add_observations([Observation(yp) for yp in y])
    # print('Observations 2:', str(obs))
    #
    # y = [3.1415, 2.7818, -0.5000, -0.5002, 2.7819, 3.1416, 1.0000, 0.9999, 7.6300]
    # obs.add_observations([Observation(yp) for yp in y])
    # print('Observations 3:\n{:}\n'.format(str(obs)))
    #
    # print('All un-sorted Observations:', [str(x) for x in obs.observations])
    # print('Sort ME:', obs.sort_me())
    # print('Sorted Observations:\n{:}\n'.format(str(obs)))
    #
    # print('All un-compressed Observations:', [str(x) for x in obs.observations])
    # print('Compress ME:', obs.compress(0.0001))
    # print('Compressed Observations:\n{:}'.format(str(obs)))
    # print('All compressed Observations:', [str(x) for x in obs.observations])
