# spark json explode array
# https://sparkbyexamples.com/spark/spark-explode-nested-array-to-rows/
# https://grkamarnath.medium.com/how-to-read-complex-json-data-in-pyspark-16a0e9149f0
#
#
#

from pyspark.sql import SparkSession, DataFrame
from pyspark.sql.types import StructType, StructField, StringType, IntegerType, DoubleType

INPUT_FILE = "I_Don't_KNOW"
OUTPUT_DELTA_PATH = "I_Don't_KNOW"

spark = (SparkSession.builder
         .appName("part1_programming")
         .master("local[*]")
         .getOrCreate())


def read_json(file_path: str, schema: StructType) -> DataFrame:
    """
    The goal of this method is to parse the input json data using the schema from another method.

    :param file_path: purchase.json will be provided
    :param schema: schema that needs to be passed to this method
    :return: Dataframe containing records from purchase.json
    """

    return None


def get_struct_type() -> StructType:
    """
    Create a schema instance based on a JSON types found in purchase.json.

    :return: the corresponding struct type for purchase.json
    """
    discount_type = StructType([StructField("amount", IntegerType(), True),
                                StructField("description", StringType(), True)])

    child_item_type = StructType([StructField("InsuranceNumber", StringType(), True),
                                  StructField("InsuranceLabel", StringType(), True),
                                  StructField("Insurancequantity", DoubleType(), True),
                                  StructField("Insuranceprice", IntegerType(), True),
                                  StructField("discountsreceived", discount_type, True)])  # TODO

    item_type = StructType([StructField("InsuranceNumber", StringType(), True),
                            StructField("InsuranceLabel", StringType(), True),
                            StructField("Insurancequantity", DoubleType(), True),
                            StructField("Insuranceprice", IntegerType(), True),
                            StructField("discountsreceived", discount_type, True),  # TODO
                            StructField("childItems", child_item_type, True)])  # TODO

    order_paid_type = StructType([StructField("Insuranceid", StringType(), True),
                                  StructField("Insurancedesc", StringType(), True),
                                  StructField("purchaseditems", item_type, True)])  # TODO

    message_type = StructType([StructField("PaidIn", order_paid_type, True)])  # TODO

    data_type = StructType([StructField("Client", message_type, True)])  # TODO

    body_type = StructType([StructField("id", StringType(), True),
                            StructField("InsuranceProvider", StringType(), True),
                            StructField("Type", data_type, True),  # TODO
                            StructField("eventTime", StringType(), True)])
    return body_type


def get_rows_from_array(df: DataFrame) -> DataFrame:
    """
    Input data frame contains columns of type array. FLATTEN THE ENTIRE NESTED JSON INTO ROWS. Dataframe returned
    shouldn't contain any Array's

    :param df: Contains column with data type of type array.
    :return: The dataframe should not contain any columns of type array
    """

    return None


def write_df_as_csv(df: DataFrame) -> None:
    """
    Write the data frame to a local  destination of your choice with headers

    :param df: Contains flattened order data
    """

    return None


def create_delta_table(session: SparkSession) -> None:
    session.sql('CREATE DATABASE IF NOT EXISTS EXERCISE')

    session.sql('''
    CREATE TABLE IF NOT EXISTS EXERCISE.ORDERS(
        Insuranceid String,
        Insurancedesc  String,
        InsuranceNumber String,
        InsuranceLabel String,
        Insurancequantity Double,
        Insuranceprice Integer,
        amount Integer,
        description String,
        ChildInsuranceNumber String, 
        ChildInsuranceLabel String,
        ChildInsurancequantity Double,
        ChildInsuranceprice Integer,
        ChildItemDiscountAmount Integer,
        ChildItemDiscountDescription String
    ) USING DELTA
    LOCATION "{0}"
    '''.format(OUTPUT_DELTA_PATH))

    print("Table created")

    return None


def write_df_as_delta(df: DataFrame) -> None:
    """
    Write the dataframe output to the table created, overwrite mode can be used

    :param df: flattened input with orders
    :return: Data from the orders table
    """

    return


def read_data_delta(session: SparkSession) -> DataFrame:
    """
    Read data from the table created
    
    :param session:
    :return:
    """

    return


if __name__ == '__main__':
    input_schema = get_struct_type()
    input_df = read_json(INPUT_FILE, input_schema)
    arrays_to_rows_df = get_rows_from_array(input_df)
    write_df_as_csv(arrays_to_rows_df)
    create_delta_table(spark)
    write_df_as_delta(arrays_to_rows_df)
    result_df = read_data_delta(spark)
    result_df.show(truncate=False)
    print("**********Part -I Completed **********")
