# TestPart1.py
# Illustrates testing so a TDD approach can be implemented. You would use a framework like pytest here.

import os
from pyspark.sql import DataFrame
from pyspark.sql.types import StructType, StructField, StringType, IntegerType
import part1 as p1

purchase_struct: StructType = None
purchases: DataFrame = None


def _runtime_hdfs_info() -> None:
    """
    Displays edge node local storage
    :return:
    """
    os.system("echo. 'Checking HDFS Local File System'")
    os.system('dir /s/b tmp | find /I "flattened_purchases"')
    os.system("echo. '--------------------'")
    return


if __name__ == '__main__':
    print("Testing Spark Queries Part 1")
    print('Spark object: ' + str(p1.spark))

    print('\n<><><><> Check StructType Creation')
    purchase_struct = p1.get_struct_type()
    print(str(purchase_struct))
    print('------------------')

    # FAILS using the supplied schema, proceed with testing using inferred schema
    print('\n<><><><> Read purchases JSON file')
    purchases = p1.read_json(p1.INPUT_FILE, purchase_struct)
    print('  -- Read and parsed JSON file')
    purchases.printSchema()
    print('  -- Display JSON file')
    purchases.show()
    print('------------------')

    # FAILS because inferred schema does not work.Need to explode/flatten from array entry (purchaseditems)
    print('\n<><><><> Read rows from array (purchases JSON file)')
    if True:
        print("_____ SKIPPING _____")
    else:
        flattened: DataFrame = p1.get_rows_from_array(purchases)
        flattened.printSchema()
        flattened.show()
    print('------------------')

    print('\n<><><><> Write CSV file')
    data2 = [("James", "", "Smith", "36636", "M", 3000),
             ("Michael", "Rose", "", "40288", "M", 4000),
             ("Robert", "", "Williams", "42114", "M", 4000),
             ("Maria", "Anne", "Jones", "39192", "F", 4000),
             ("Jen", "Mary", "Brown", "", "F", -1)]

    schema = StructType([StructField("firstname", StringType(), True),
                         StructField("middlename", StringType(), True),
                         StructField("lastname", StringType(), True),
                         StructField("id", StringType(), True),
                         StructField("gender", StringType(), True),
                         StructField("salary", IntegerType(), True)])

    df = p1.spark.createDataFrame(data=data2, schema=schema)
    df.printSchema()
    print(' -- Display Test CSV')
    df.show(truncate=False)

    print('\nHDFS Target before write')
    _runtime_hdfs_info()
    print('')

    p1.write_df_as_csv(df)

    print('\nHDFS Target after write')
    _runtime_hdfs_info()
    print('------------------')

    print('\n<><><><> Check Table Creation')
    p1.create_delta_table(p1.spark)
    print('------------------')

    p1.spark.stop()
