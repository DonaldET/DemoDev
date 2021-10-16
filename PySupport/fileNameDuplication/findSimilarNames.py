"""Scan files names in a directory looking for matching file names

We look for blocks of similar file names in a directory. We sort the file names and transform them to a comparison
format. A difference block is made up of file names with some characters matching. The comparison of formatted names
starts at the first (left-most) character and proceeds for up to M characters, to find the first difference.

Transforms/Comparison Format Details:
 o  Standardized transformer - Special characters are converted to spaces, and multiple spaces to a single space.
 o  Tokenized transformer - Perform standard transformation, then split on space and underscore, rejoin with just spaces

The program reports difference blocks, which are sequential names that match leading characters, based on
a comparison methods. Matching statistics are collected and optionally reported.
"""
import argparse
import os
import re
from collections import Counter

VERSION = '1.01'


class StringComparator:
    """ Compare two strings returning the index of the first difference. Return string length if identical. """

    def __init__(self, max_check_lth):
        self.max_check_lth = max_check_lth

    def _compare_str_impl(self, lhs, rhs):
        lhs_lth = len(lhs)
        rhs_lth = len(rhs)
        chk_lth = min(lhs_lth, rhs_lth, self.max_check_lth)
        for i in range(chk_lth):
            if lhs[i] != rhs[i]:
                return False, i
        return True, -1

    def _compare_str(self, lhs, rhs):
        lhs_lth = len(lhs)
        rhs_lth = len(rhs)
        if lhs_lth < 1:
            return True, -1 if rhs_lth < 1 else False, 0
        if rhs_lth < 1:
            return False, 0
        return self._compare_str_impl(lhs, rhs)

    def compare(self, param_lhs, param_rhs):
        if param_lhs is None:
            return True, -1 if param_lhs is None else False
        if param_rhs is None:
            return False, 0
        return self._compare_str(param_lhs, param_rhs)


class UniformFilenameTransformer:
    """ Standardize case, spaces, and remove special characters """

    def __init__(self):
        self.space = ' +'
        self.underscore = '_+'
        self.dash = '-+'
        self.replacement = ' '

    def standardize(self, src):
        if src is None:
            return ''
        wrk: str = str(src).strip()
        if len(wrk) < 1:
            return wrk
        wrk = re.sub(self.space, self.replacement, wrk)
        wrk = re.sub(self.underscore, self.replacement, wrk)
        wrk = re.sub(self.dash, self.replacement, wrk)
        wrk = re.sub(self.space, self.replacement, wrk)
        return wrk.strip().lower()


class TokenizedFilenameTransformer:
    """ Assuming standard case, spaces, and special characters are removed, tokenize """

    def __init__(self):
        self.uniform_transformer = UniformFilenameTransformer()
        self.separators = '[_ ]'

    def standardize(self, src):
        if src is None:
            return ''
        wrk = str(src).strip()
        if len(wrk) < 1:
            return wrk
        wrk = self.uniform_transformer.standardize(wrk)
        tokens = re.split(self.separators, wrk)
        tokens = [s.replace(' ', '') for s in tokens]
        return ' '.join(tokens)


class DuplicateFilenameFinder:
    """ Locate Similar file names """

    def __init__(self, min_match_lth, filename_transformer):
        if min_match_lth < 1 or min_match_lth > 500:
            raise ValueError(f"min_match_lth ({min_match_lth}) out of range")
        self.min_match_lth = min_match_lth
        if filename_transformer is None:
            raise ValueError(f"transformer required")
        self.transformer = filename_transformer
        self.comparator = StringComparator(min_match_lth)
        self.in_group = 0
        self.diff_counter = Counter()
        self.group_count = 0
        self.num_checked = 0
        self.last_name_value = None

    def check_value(self, input_file_name):
        if input_file_name is None:
            return False, 0
        file_name = self.transformer.standardize(input_file_name)
        self.num_checked += 1
        if self.last_name_value is None:
            self.last_name_value = file_name
            return False, 0

        match, index = self.comparator.compare(self.last_name_value, file_name)
        if match:
            if self.in_group < 1:
                self.group_count += 1
                print(". . . . . .")
                print(f"  {self.last_name_value}")
            print(f"  {file_name}")
            self.in_group += 1
        else:
            self.in_group = 0
            self.diff_counter.update([index])
        self.last_name_value = file_name
        return match, index


def _setup():
    parser = argparse.ArgumentParser()
    def_start_dir = 'D:\\ex_libris\\MySafari'
    parser.add_argument("starting_dir", nargs='?',
                        help="the directory to scan locating duplication (" + def_start_dir + ")",
                        default=def_start_dir)
    def_minimum_match_length = 26
    parser.add_argument("-m", "--minimum-match-length", required=False,
                        help="minimum comparison length of transformed file name (" + str(
                            def_minimum_match_length) + ")",
                        default=def_minimum_match_length, type=int)
    parser.add_argument("-idx", "--display_index", required=False,
                        help="display the counts of records associated with the first difference column (False)",
                        action="store_true")
    parser.add_argument("-std", "--standard_diff", required=False, default=False,
                        help="standard transform: remove special characters and spaces (False)",
                        action="store_true")
    parser.add_argument("-token", "--token_diff", required=False, default=False,
                        help="standard transform but tokenize on uderscore or space (False)",
                        action="store_true")
    args = parser.parse_args()

    starting_dir = args.starting_dir
    minimum_match_length = args.minimum_match_length
    display_index = args.display_index
    standard_diff = args.standard_diff
    token_diff = args.token_diff
    if not standard_diff and not token_diff:
        standard_diff = True
    return starting_dir, minimum_match_length, display_index, standard_diff, token_diff


def process_dir(title, base_dir, min_match_lth, display_diffs, name_transformer):
    print(f"    - {str(title)}")
    dir_count = 0
    for root, dirs, files in os.walk(base_dir):
        dir_count += 1
        if dir_count > 1:
            break
        if len(files) > 0:
            sorted(files)
            finder = DuplicateFilenameFinder(min_match_lth, name_transformer)
            for file in files:
                finder.check_value(file)
            if finder.group_count > 0:
                print(". . . . . .")
                print(f"Number checked: {finder.num_checked};  Difference Blocks: {finder.group_count}")
            if display_diffs:
                print("\nCount of Records by Index of First Difference:")
                for k in sorted(finder.diff_counter):
                    print(f"{k}: {finder.diff_counter[k]}")


if __name__ == '__main__':
    print(f"Find Similar File Names in a Directory - {VERSION}")
    start_dir, min_chk_lth, display_index_of_differences, show_standard_diff, show_token_diff = _setup()
    print(f"\t- Starting Directory   : {start_dir}")
    print(f"\t- Min match name length: {min_chk_lth}")
    print(f"\t- IDX diff distribution: {display_index_of_differences}")
    print(f"\t- Standard differences : {show_standard_diff}")
    print(f"\t- Tokenized differences: {show_token_diff}")

    if show_standard_diff:
        print("\n------------------------\n")
        process_dir("Standard Name Formatter", start_dir, min_chk_lth, display_index_of_differences,
                    UniformFilenameTransformer())
    if show_token_diff:
        print("\n------------------------\n")
        process_dir("Tokenized Name Formatter", start_dir, min_chk_lth, display_index_of_differences,
                    TokenizedFilenameTransformer())
