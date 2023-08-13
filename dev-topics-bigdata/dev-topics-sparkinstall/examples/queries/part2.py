from pyspark.sql import SparkSession

spark = (
    SparkSession
        .builder
        .appName("programming")
        .master("local")
        .config("spark.jars.packages", "io.delta:delta-core_2.12:0.7.0")
        .config("spark.sql.extensions", "io.delta.sql.DeltaSparkSessionExtension")
        .config("spark.sql.catalog.spark_catalog", "org.apache.spark.sql.delta.catalog.DeltaCatalog")
        .config('spark.ui.port', '4050')
        .getOrCreate()
)

# generate orders_df by orders.csv 
orders_df = None

# generate customers_df by customers.csv
customers_df = None

# Compute total sales ($) per customer (Hint: Include Customer ID as seperate columns )

[ ]

# Compute average sales ($) per day

[ ]

# Compute total number of orders and cumulative sales ($)

[ ]

# For every day, identify the order with the highest sales amount ($)

[ ]

# What is the cumulative sale ($) per zip code?

[ ]

# First, compute the number of orders per customer. Then, identify customers with the least number of total ord

[ ]
# Please provide 3-5 Spark SQL Performance techniques and configurations you would use to improve a query?
#
# 1) Reorder queries so only one active dataframe is using cache at a time
# 2) Use Parquet or ORC files for tables that are columnar
# 3) Avoid CSV to save converting numbers and dates to binary representation



