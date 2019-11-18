"""
Parse a file for instance of token values and count instances.
Sample command line: "<artifactId>" "</artifactId>" "../../unittest/resources/plugins_data.txt"
"""
import argparse

import helpers.extraction as extract


def _setup():
    parser = argparse.ArgumentParser(description='Find and count tokens in file')
    parser.add_argument('tk_begin', type=str, help='token begin marker')
    parser.add_argument('tk_end', type=str, help='token end marker')
    parser.add_argument('path', type=str, help='path to file')
    args = parser.parse_args()
    tk_begin = args.tk_begin
    print('  -- tk_begin: ' + tk_begin)
    tk_end = args.tk_end
    print('  -- tk_end  : ' + tk_end)
    tk_path = args.path
    print('  -- path    : ' + tk_path)
    return tk_begin, tk_end, tk_path


if __name__ == '__main__':
    print("Count Tokens in Path")
    tk_left, tk_right, path = _setup()
    counts = extract.count_bounded_tokens(path, tk_left, tk_right)
    keys = sorted(counts.keys())
    for key in keys:
        print("{0:25} {1:>6}".format(key, counts[key]))
    print("-- Done")
