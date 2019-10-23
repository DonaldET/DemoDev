""" Project wide setup """
import ntpath
import os


def who_am_i(file_like, from_file):
    """
    Display this module name along with the module name from the caller
    :param file_like: A std-out mock
    :param from_file: the file name of the invoking script
    :return: A call-path component
    """
    cur_file_name = os.path.realpath(__file__)
    file_like.write("executing file {} from {}\n".format(ntpath.basename(cur_file_name), from_file))
