# This is a Local Spark Smoke Test collection, in p[art taken from
# https://github.com/spark-examples/pyspark-examples/blob/master/convert-column-python-list.py

from pyspark.sql import SparkSession

spark = SparkSession.builder.master("local[*]") \
    .appName('CollectedSmokeTests') \
    .getOrCreate()


def smoke2(name):
    print(f"Smoke Test 2: {name}")
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
    fileName = '/tmp/spark_output/smoketest'
    # fileName = 'file::///tmp/spark_output/smoketest'
    print("***************************************")
    print(f"  -- File: {fileName}")
    df.coalesce(1).write.format("csv").options(header='True', delimiter=',').mode('overwrite').save(
        fileName)
    print("*** DataFrame created as a CSV file ***")
    print("***************************************")

    print('\nSmoke Test2 Done')


if __name__ == '__main__':
    print('\n***************************')
    print('*** PySpark Smoke Tests ***')
    print('***************************')

    smoke2('Create a DataFrame')

    spark.stop()
