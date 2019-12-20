import unittest

from partitioner.data_manager import data_manager


class DataManagerTestCase(unittest.TestCase):

    def test_assemble_data(self):
        in1 = [1, 2, 3]
        updated_collected = []
        float_in = data_manager.assemble_data(in1, updated_collected)
        self.assertIsNotNone(float_in, "float output None")
        self.assertEqual(len(in1), len(float_in), "float output length differs")
        flt_in = [float(x) for x in in1]
        self.assertEqual(flt_in, float_in, "float version of input differs")

        self.assertIsNotNone(updated_collected, "updated collection is None")
        self.assertEqual(1, len(updated_collected), "updated collection list differs")
        observations = updated_collected[0]
        self.assertEqual(len(in1), len(observations), "collected observation length differs")
        i = 0
        for observation in observations:
            self.assertEqual(float_in[i], observation.value, "observation[" + str(i) + "] differs")
            i = i + 1

        in2 = [4, 5, 6]
        float_in = data_manager.assemble_data(in2, updated_collected)
        self.assertIsNotNone(updated_collected, "second updated collection is None")
        self.assertEqual(2, len(updated_collected), "second updated len differs")
        observations = updated_collected[1]
        self.assertEqual(len(in2), len(observations), "collected observation2 length differs")
        i = 0
        for observation in observations:
            self.assertEqual(float_in[i], observation.value, "observation2[" + str(i) + "] differs")
            i = i + 1


if __name__ == '__main__':
    unittest.main()
