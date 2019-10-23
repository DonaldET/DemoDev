import unittest.mock
from unittest import TestCase

from myapppy import who_am_i


class Test(TestCase):

    def test_who_am_i_is_me(self):
        mock_stdout = unittest.mock.Mock()
        who_am_i(mock_stdout, "version_info_tests.py")
        mock_stdout.write.assert_called_with("executing file __init__.py from version_info_tests.py\n")
