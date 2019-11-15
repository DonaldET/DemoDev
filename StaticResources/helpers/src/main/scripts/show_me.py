import ntpath
import sys

from helpers import who_am_i


def _main():
    who_am_i(sys.stdout, ntpath.basename(__file__))


if __name__ == '__main__':
    _main()
