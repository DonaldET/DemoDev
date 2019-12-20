import unittest
import partitioner.assigner.lefttorightassigner as assign
from partitioner.scorer.sum_square_scorer import SumSquareScorer

class MyTestCase(unittest.TestCase):
    def test_simple(self):
        scorer = SumSquareScorer()
        assigner = assign.LeftToRightAssigner()
        self.assertEqual(True, False)


if __name__ == '__main__':
    unittest.main()
