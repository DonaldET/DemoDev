from pyspark.sql import SparkSession, DataFrame
from pyspark.sql.functions import trim, sum, avg, max, min, mean, count, expr
from collections import namedtuple
import os

Report_data = namedtuple('Report_data', ['orders_df', 'customers_df'])

ORDERS_FILE = '../../queries/data/orders.csv'
CUSTOMERS_FILE = '../../queries/data/customers.csv'

spark = (SparkSession.
         builder.
         appName("part2_SQL").
         enableHiveSupport().
         master("local[*]").
         getOrCreate())
spark.sparkContext.setLogLevel('WARN')


def _runtime_info() -> None:
    """
    Displays edge node local storage
    :return:
    """
    os.system("echo. 'Checking Local File System'")
    os.system("dir")
    os.system("echo. '--------------------'")
    return


def _runtime_dataframe_info(loaded_file: str, loaded_df: DataFrame) -> None:
    """
    Display loaded dataframe info
    :return: 
    """
    print(f"\n{loaded_file} Info")
    loaded_df.printSchema()
    print(' -- Display')
    loaded_df.show()


def load_data() -> Report_data:
    # generate orders_df by orders.csv
    loaded_orders_df = spark.read.options(header='True', inferSchema='True', delimiter=',').csv(ORDERS_FILE)

    # generate customers_df by customers.csv
    loaded_customers_df = spark.read.options(header='True', inferSchema='True', delimiter=',').csv(CUSTOMERS_FILE)

    return Report_data(loaded_orders_df, loaded_customers_df)


# Compute total sales ($) per customer (Hint: Include Customer ID as separate columns )
def compute_total_sales_per_customer() -> None:
    print('\nCompute total sales ($) per customer')
    qry = \
        """
        select  customer_id,
                count(customer_id) records,
                sum(total_price)
        from ORDERS
        group by customer_id
        """
    spark.sql(qry.strip()).show()
    return


# Compute average sales ($) per day
def compute_average_sales_per_day() -> None:
    print('\nCompute average sales ($) per day')
    qry = \
        """
        select  customer_id,
                avg(customer_id) records,
                sum(total_price)
        from ORDERS
        group by customer_id
        """
    spark.sql(qry.strip()).show()
    return


# Compute total number of orders and cumulative sales ($)
def compute_total_number_of_orders_and_cumulative_sales() -> None:
    print('\nCompute total number of orders and cumulative sales ($)')
    qry = \
        """
        select  count(id) orders,
                sum(total_price)
        from ORDERS
        """
    spark.sql(qry.strip()).show()
    return


# For every day, identify the order with the highest sales amount ($)
def for_every_day_identify_order_with_highest_sales_amount() -> None:
    print('\nFor every day, identify the order with the highest sales amount ($)')

    qry = \
        """
        with DAYS_MAX as (
            select  date,
                    max(total_price) as max_price
            from ORDERS
            group by date
        )
        select  ord.date as day,
                min(ord.id) as id,
                min(dm.max_price) as max_price
        from ORDERS ord, DAYS_MAX dm
        where   ord.date == dm.date and
                ord.total_price >= dm.max_price
        group by day
        order by day
        """
    spark.sql(qry.strip()).show()
    return


# ADDED: For every day, identify the orders with the highest sales amount ($)
def for_every_day_identify_orders_with_highest_sales_amount() -> None:
    print('\nFor every day, identify the order with the highest sales amount ($)')

    qry = \
        """
        with DAYS_MAX as (
            select  date,
                    max(total_price) as max_price
            from ORDERS
            group by date
        )
        select  ord.date as day,
                ord.id as id,
                dm.max_price as max_price
        from ORDERS ord, DAYS_MAX dm
        where   ord.date == dm.date and
                ord.total_price >= dm.max_price
        order by day
        """
    spark.sql(qry.strip()).show()
    return


# What is the cumulative sale ($) per zip code?
def cumulative_sale_per_zip_code() -> None:
    print('\nFind cumulative sale ($) per zip code')

    qry = \
        """
        select  substring(trim(zipcode), 1, 5) as zip,
                count(id) as records,
                sum(total_price)
        from ORDERS ord, CUSTOMERS cus
        where   ord.customer_id == cus.id
        group by zip
        order by zip
        """
    spark.sql(qry.strip()).show()
    return


# First, compute the number of orders per customer. Then, identify customers with the least number of total orders
def identify_customers_with_least_number_of_orders() -> None:
    return


if __name__ == '__main__':
    print("**********Part -II Started **********")
    # _runtime_info()

    print(f"\nLoad {ORDERS_FILE} and {CUSTOMERS_FILE}")
    report_data: Report_data = load_data()
    # _runtime_dataframe_info(ORDERS_FILE, report_data.orders_df)
    # _runtime_dataframe_info(CUSTOMERS_FILE, report_data.customers_df)

    report_data.orders_df.createOrReplaceTempView("ORDERS")
    report_data.customers_df.createOrReplaceTempView("CUSTOMERS")

    # compute_total_sales_per_customer()
    # compute_average_sales_per_day()
    # compute_total_number_of_orders_and_cumulative_sales()
    # for_every_day_identify_orders_with_highest_sales_amount()
    # for_every_day_identify_order_with_highest_sales_amount()
    cumulative_sale_per_zip_code()

    print("**********Part -II Completed **********")
    spark.stop()

# Please provide 3-5 Spark SQL Performance techniques and configurations you would use to improve a query?
#
# 1) Reorder queries so only one active dataframe is using cache at a time if possible
# 2) Use Parquet or ORC files for tables that are columnar storage
# 2a) Use Parquet or ORC files for tables with partitions
# 3) Avoid CSV to save converting numbers and dates to binary representation
