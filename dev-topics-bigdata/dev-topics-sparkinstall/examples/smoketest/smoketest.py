# This is a Local Spark Smoke Test collection, in part taken from:
#  -- https://spark.apache.org/examples.html
#  -- https://sparkbyexamples.com/pyspark-tutorial/

from pyspark.sql import SparkSession
import time

shared_file_name = '/tmp/spark_output/smoketest'


def _get_spark_session():
    session = (SparkSession.
               builder.
               master("local[*]").
               appName('CollectedSmokeTests1').
               getOrCreate())
    session.sparkContext.setLogLevel('WARN')
    return session


def _default_parallelism(session):
    # based on https://stackoverflow.com/questions/51342460/getexecutormemorystatus-size-not-outputting-correct-num-of-executors?noredirect=1&lq=1
    time.sleep(15)
    dp_exc = session._jsc.sc().defaultParallelism()
    return dp_exc


def smoke1(name):
    print(f"\nSmoke Test 1: {name}")
    spark = _get_spark_session()

    print('Python Version:' + spark.sparkContext.pythonVer)
    print('Spark Version :' + spark.version)
    print('APP Name      :' + spark.sparkContext.appName)
    print('APP ID        :' + spark.sparkContext.applicationId)
    print('Master        :' + spark.sparkContext.master)
    print('Configuration :' + str(spark.conf))
    print('Context       :' + str(spark.sparkContext))
    print('Environment   :' + str(spark.sparkContext.environment))

    spark.stop()


def smoke2(name):
    # Taken, in part, from:
    # https://github.com/spark-examples/pyspark-examples/blob/master/convert-column-python-list.py
    print(f"\nSmoke Test 2: {name}")
    spark = _get_spark_session()
    print('Default Parallelism: ' + str(_default_parallelism(spark)))

    data = [("James", "Smith", "USA", "CA"), ("Michael", "Rose", "USA", "NY"),
            ("Robert", "Williams", "USA", "CA"), ("Maria", "Jones", "USA", "FL")]
    columns = ["firstname", "lastname", "country", "state"]
    df = spark.createDataFrame(data=data, schema=columns)
    df.show()
    print('')
    df.printSchema()
    print('')
    print('dataframe : ' + str(df.collect()))
    print('partitions: ' + str(df.rdd.getNumPartitions()))

    #
    # Write CSV taken from https://sparkbyexamples.com/pyspark/pyspark-write-dataframe-to-csv-file/
    #
    print("\n***************************************")
    print("**** Write DataFrame as a CSV file ****")
    print("***************************************")
    print(f"  -- File: {shared_file_name}")
    df.coalesce(1).write.format("csv").options(header='True', delimiter=',').mode('overwrite').save(
        shared_file_name)
    print("*** DataFrame created as a CSV file ***")
    print("***************************************")

    spark.stop()
    print('\nSmoke Test2 Done')


def smoke3(name):
    print(f"\nSmoke Test 3: {name}")
    spark = _get_spark_session()

    print("\n**************************************")
    print("**** Read DataFrame as a CSV file ****")
    print("**************************************")
    print(f"  -- File: {shared_file_name}")
    df = spark.read.options(header='True', inferSchema='True', delimiter=',').csv(shared_file_name)
    print("*** DataFrame Read as a CSV file ***")
    print("***************************************")
    df.printSchema()
    print('  -- Display')
    df.show()

    spark.stop()
    print('\nSmoke Test3 Done')


if __name__ == '__main__':
    print('\n***************************')
    print('*** PySpark Smoke Tests ***')
    print('***************************')

    print('\n-----------------------------------------------')
    smoke1('Print Spark Attributes')
    smoke2('Create a DataFrame')
    smoke3('Read a DataFrame')
    print('-----------------------------------------------')

    print('PySpark Smoke Tests Done.')
