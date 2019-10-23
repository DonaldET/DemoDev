# __init__.py
import ntpath
import os

""" Project wide setup """


def who_am_i(file_like, from_file):
    cur_file_name = os.path.realpath(__file__)
    file_like.write("executing file {} from {}\n".format(ntpath.basename(cur_file_name), from_file))
