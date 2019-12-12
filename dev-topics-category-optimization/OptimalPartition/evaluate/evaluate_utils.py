"""
evaluate_utils.py

Evaluator utilities to read in data
"""
import os
import sys
import csv
import time

import pandas as pd

wp = os.path.dirname(os.path.realpath(__file__))
mp = os.path.realpath(os.path.join(wp, '../Partition'))
sys.path.append(mp)
import observation_manager
import partitioner
import anova_evaluator


def _read_file(flname, working_path):
    """
    Read in a sample of the distribution to test
    :param flname: the file name of the sampled distribution appended to the working path
    :param working_path: working path to data files
    :return: a list of values from the file
    """
    values = []
    flname = os.path.join(working_path, flname)
    with open(flname, newline='') as f:
        reader = csv.reader(f)
        for row in reader:
            values.append(float(row[1]))
    return values


def load_data(flname, working_path):
    """
    :param flname: the file name of the stored distribution sample, appended to the working path
    :param working_path: working path to data files
    :return: the collection of observations
    """
    observations = observation_manager.ObservedValues()
    data = [observation_manager.Observation(float(x)) for x in _read_file(flname, working_path)]
    observations.add_observations(data)
    return observations


def evaluate_partitions(test_collector, test_partition_count, test_space_size, test_min_size, test_max_size):
    """
    Iterate through all possible filtered partitions, evaluate, and record the largest eta-squared
    :param test_collector:
    :param test_partition_count:
    :param test_space_size:
    :param test_min_size:
    :param test_max_size:
    :return: the test collector with the maximum eta-squared value
    """
    spc = partitioner.Space(test_collector, test_partition_count, test_space_size, test_min_size, test_max_size)
    print('  -- Initial :', str(spc))
    spc.generate_all()
    return test_collector


def process_sample(label, fname, working_path):
    """
    Orchestrate loading data, iterating through partitions with eta-squared evaluation, record the largest, compare to inter quartile range
    :param label:
    :param fname:
    :param working_path:
    :return: None
    """
    evaluator = anova_evaluator.AnovaEvaluator(load_data(fname, working_path))
    n_k = len(evaluator.observed_values.observations)
    print('\n*****\nFrom file', fname, 'with', n_k, 'elements created:\n', str(evaluator))

    pd.set_option('expand_frame_repr', False)
    i = 0
    xvals = [('"' + str(i) + '"', evaluator.observed_values.observations[i].value) for i in range(n_k)]
    df = pd.DataFrame.from_records(xvals, columns=['id', 'xval'])
    print(df.describe().transpose())

    max_fraction = 0.75
    min_fraction = 0.20
    tst_label = label + '-({:.2f}% | {:.2f}%)'.format(min_fraction, max_fraction)
    print('\nStarting analysis', tst_label)
    partition_count = 3
    min_size = round(n_k * min_fraction)
    max_size = round(n_k * max_fraction)
    t_mark = time.process_time()
    collector = evaluate_partitions(partitioner.Collector(evaluator), partition_count, n_k, min_size, max_size)
    t_mark = time.process_time() - t_mark
    print('Result:', round(t_mark, 3), 'sec for', collector)

    tail = n_k // 4
    iqr_cnt = [tail, n_k - 2 * tail, tail]
    eta_squared = evaluator.measure(iqr_cnt)
    print('Inter-quartile Range:', iqr_cnt, '-- score {eta-squared}:', eta_squared)
