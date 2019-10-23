import unittest

import myapppy.generate as gen


class MyTestCase(unittest.TestCase):
    def test_something(self):
        f = gen.fib()
        self.assertEqual(1, next(f))
        self.assertEqual(1, next(f))
        self.assertEqual(2, next(f))
        self.assertEqual(3, next(f))
        self.assertEqual(5, next(f))


if __name__ == '__main__':
    unittest.main()
