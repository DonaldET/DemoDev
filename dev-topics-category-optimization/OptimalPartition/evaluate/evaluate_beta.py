"""
evaluate_beta.py

Filtered search through all possible partitions of the ordered sample space and find maximum ETA-SQUARED partition
"""
import os

import evaluate_utils

wp = os.path.dirname(os.path.realpath(__file__))
mp = os.path.realpath(os.path.join(wp, '../Partition'))

if __name__ == '__main__':
    evaluate_utils.process_sample('BETA', 'BETA1.csv', wp)
    evaluate_utils.process_sample('BETA', 'BETA2.csv', wp)
    evaluate_utils.process_sample('BETA', 'BETA3.csv', wp)
