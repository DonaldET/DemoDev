from __future__ import division
from __future__ import print_function

import random

from pyspark.sql.types import StructType
from pyspark.sql.types import StructField
from pyspark.sql.types import StringType, IntegerType

n = int(50000000)
LARGE_PRIME = float(7919)
RAND_SEED = 3677
random.seed(RAND_SEED)


def _make_test_data(seq_size):
    """
    Create a known test sequence of the specified size
    :param seq_size:
    :return: A list of float values and their correct sum
    """
    gen_seq = list()
    for i in range(seq_size):
        gen_seq.append((float(i) + 1.0) / LARGE_PRIME)
    true_sum = (float(seq_size) / 2.0) * (float(seq_size) + 1.0) if seq_size % 2 == 0 else float(seq_size) * (
            float(seq_size) + 1.0) / 2.0
    random.shuffle(gen_seq)
    return gen_seq, true_sum


def _make_df(test_seq):
    schema = StructType([StructField("value", IntegerType(), True)])
    rdd = spark.parallelize(test_seq)
    df = sqlContext.createDataFrame(rdd, schema)
    return df