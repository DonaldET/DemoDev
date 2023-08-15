from pyspark.sql.types import StructType
import part1 as p1

if __name__ == '__main__':
    print("Testing Spark Queries Part 1")
    print(str(p1.spark))

    print('\n<><><><> Check StructType Creation')
    purchase_struct: StructType = p1.get_struct_type()
    print(str(purchase_struct))
    print('------------------')

    print('\n<><><><> Check Table Creation')
    p1.create_delta_table(p1.spark)
    print('------------------')

    p1.spark.stop()
