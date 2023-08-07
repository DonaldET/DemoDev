
./bin/spark-submit \
    --deploy-mode cluster \
    --master yarn \
    --class org.apache.spark.examples.SparkPi \
    /spark-home/examples/jars/spark-examples_versionxx.jar 80
