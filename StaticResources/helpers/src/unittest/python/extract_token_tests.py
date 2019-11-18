import os
import unittest

from helpers.extraction import count_bounded_tokens


class ExtractTokenCase(unittest.TestCase):
    def test_just_marker(self):
        data_rel_location = '../resources/plugins_data.txt'
        cur_dir_name = os.path.dirname(os.path.abspath(__file__))
        data_location = os.path.abspath(os.path.join(cur_dir_name, data_rel_location))
        print("Data: " + data_location)
        info = count_bounded_tokens(data_location, '<artifactId>',
                                    '</artifactId>')
        self.assertIsNotNone(info)
        main_ky = '__MAIN__'
        self.assertTrue(main_ky in info)
        self.assertEqual(info[main_ky], data_location + '|<artifactId>|</artifactId>')


if __name__ == '__main__':
    unittest.main()
