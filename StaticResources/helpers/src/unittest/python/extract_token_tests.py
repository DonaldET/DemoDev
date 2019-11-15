import unittest

from helpers.extraction import countBoundedTokens


class MyTestCase(unittest.TestCase):
    def test_using_marker(self):
        info = countBoundedTokens("mydir", "myfile", "mybegin", "myend")
        self.assertDictEqual({"__MAIN__": "mydir|myfile|mybegin|myend"}, info)


if __name__ == '__main__':
    unittest.main()
