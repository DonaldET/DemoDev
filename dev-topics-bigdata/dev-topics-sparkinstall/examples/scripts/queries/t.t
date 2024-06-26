
 Summit Spark Queries Test 1 script using driver TestPart1.py on D:\util\spark-3.4.1-bin-hadoop3-scala2.13 executing $0.cmd
   -- Base Path  : D:\GitHub\DemoDev\dev-topics-bigdata\dev-topics-sparkinstall\examples\scripts\queries\
   -- Driver Path: D:\GitHub\DemoDev\dev-topics-bigdata\dev-topics-sparkinstall\examples\queries\
   -- Spark Home : D:\util\spark-3.4.1-bin-hadoop3-scala2.13
java version "17.0.6" 2023-01-17 LTS
Java(TM) SE Runtime Environment (build 17.0.6+9-LTS-190)
Java HotSpot(TM) 64-Bit Server VM (build 17.0.6+9-LTS-190, mixed mode, sharing)
The system cannot find the file specified.
 D:\util\spark-3.4.1-bin-hadoop3-scala2.13\bin\spark-submit D:\GitHub\DemoDev\dev-topics-bigdata\dev-topics-sparkinstall\examples\queries\TestPart1.py --py-files part1.py --files ..\..\queries\data\customers.csv, ..\..\queries\data\orders.csv, ..\..\queries\data\purchases.json
23/08/24 14:02:05 INFO SparkContext: Running Spark version 3.4.1
23/08/24 14:02:06 WARN SparkConf: Note that spark.local.dir will be overridden by the value set by the cluster manager (via SPARK_LOCAL_DIRS in mesos/standalone/kubernetes and LOCAL_DIRS in YARN).
23/08/24 14:02:06 INFO ResourceUtils: ==============================================================
23/08/24 14:02:06 INFO ResourceUtils: No custom resources configured for spark.driver.
23/08/24 14:02:06 INFO ResourceUtils: ==============================================================
23/08/24 14:02:06 INFO SparkContext: Submitted application: part1_programming
23/08/24 14:02:06 INFO ResourceProfile: Default ResourceProfile created, executor resources: Map(cores -> name: cores, amount: 1, script: , vendor: , memory -> name: memory, amount: 1024, script: , vendor: , offHeap -> name: offHeap, amount: 0, script: , vendor: ), task resources: Map(cpus -> name: cpus, amount: 1.0)
23/08/24 14:02:06 INFO ResourceProfile: Limiting resource is cpu
23/08/24 14:02:06 INFO ResourceProfileManager: Added ResourceProfile id: 0
23/08/24 14:02:06 INFO SecurityManager: Changing view acls to: Don
23/08/24 14:02:06 INFO SecurityManager: Changing modify acls to: Don
23/08/24 14:02:06 INFO SecurityManager: Changing view acls groups to: 
23/08/24 14:02:06 INFO SecurityManager: Changing modify acls groups to: 
23/08/24 14:02:06 INFO SecurityManager: SecurityManager: authentication disabled; ui acls disabled; users with view permissions: Don; groups with view permissions: EMPTY; users with modify permissions: Don; groups with modify permissions: EMPTY
23/08/24 14:02:07 INFO Utils: Successfully started service 'sparkDriver' on port 54554.
23/08/24 14:02:07 INFO SparkEnv: Registering MapOutputTracker
23/08/24 14:02:07 INFO SparkEnv: Registering BlockManagerMaster
23/08/24 14:02:07 INFO BlockManagerMasterEndpoint: Using org.apache.spark.storage.DefaultTopologyMapper for getting topology information
23/08/24 14:02:07 INFO BlockManagerMasterEndpoint: BlockManagerMasterEndpoint up
23/08/24 14:02:07 INFO SparkEnv: Registering BlockManagerMasterHeartbeat
23/08/24 14:02:08 INFO DiskBlockManager: Created local directory at D:\Temp\blockmgr-58185f68-eab2-4505-900b-32b72cb5ea28
23/08/24 14:02:08 INFO MemoryStore: MemoryStore started with capacity 434.4 MiB
23/08/24 14:02:08 INFO SparkEnv: Registering OutputCommitCoordinator
23/08/24 14:02:08 INFO JettyUtils: Start Jetty 0.0.0.0:4040 for SparkUI
23/08/24 14:02:08 INFO Utils: Successfully started service 'SparkUI' on port 4040.
23/08/24 14:02:08 INFO Executor: Starting executor ID driver on host THOR.mshome.net
23/08/24 14:02:08 INFO Executor: Starting executor with user classpath (userClassPathFirst = false): ''
23/08/24 14:02:09 INFO Utils: Successfully started service 'org.apache.spark.network.netty.NettyBlockTransferService' on port 54555.
23/08/24 14:02:09 INFO NettyBlockTransferService: Server created on THOR.mshome.net:54555
23/08/24 14:02:09 INFO BlockManager: Using org.apache.spark.storage.RandomBlockReplicationPolicy for block replication policy
23/08/24 14:02:09 INFO BlockManagerMaster: Registering BlockManager BlockManagerId(driver, THOR.mshome.net, 54555, None)
23/08/24 14:02:09 INFO BlockManagerMasterEndpoint: Registering block manager THOR.mshome.net:54555 with 434.4 MiB RAM, BlockManagerId(driver, THOR.mshome.net, 54555, None)
23/08/24 14:02:09 INFO BlockManagerMaster: Registered BlockManager BlockManagerId(driver, THOR.mshome.net, 54555, None)
23/08/24 14:02:09 INFO BlockManager: Initialized BlockManager: BlockManagerId(driver, THOR.mshome.net, 54555, None)
Testing Spark Queries Part 1
Spark object: <pyspark.sql.session.SparkSession object at 0x0000015F0C00DF50>

<><><><> Check StructType Creation
StructType([StructField('id', StringType(), True), StructField('InsuranceProvider', StringType(), True), StructField('Type', StructType([StructField('Client', StructType([StructField('PaidIn', StructType([StructField('Insuranceid', StringType(), True), StructField('Insurancedesc', StringType(), True), StructField('purchaseditems', StructType([StructField('InsuranceLabel', StringType(), True), StructField('InsuranceNumber', StringType(), True), StructField('Insuranceprice', DoubleType(), True), StructField('Insurancequantity', IntegerType(), True), StructField('childItems', StructType([StructField('InsuranceLabel', StringType(), True), StructField('InsuranceNumber', StringType(), True), StructField('Insuranceprice', DoubleType(), True), StructField('Insurancequantity', IntegerType(), True), StructField('discountsreceived', StructType([StructField('amount', IntegerType(), True), StructField('description', StringType(), True)]), True)]), True), StructField('discountsreceived', StructType([StructField('amount', IntegerType(), True), StructField('description', StringType(), True)]), True)]), True)]), True)]), True)]), True), StructField('eventTime', StringType(), True)])
------------------

<><><><> Read purchases JSON file
=== read in===
root
 |-- InsuranceProvider: string (nullable = true)
 |-- Type: struct (nullable = true)
 |    |-- Client: struct (nullable = true)
 |    |    |-- PaidIn: struct (nullable = true)
 |    |    |    |-- Insurancedesc: string (nullable = true)
 |    |    |    |-- Insuranceid: string (nullable = true)
 |    |    |    |-- purchaseditems: array (nullable = true)
 |    |    |    |    |-- element: struct (containsNull = true)
 |    |    |    |    |    |-- InsuranceLabel: string (nullable = true)
 |    |    |    |    |    |-- InsuranceNumber: string (nullable = true)
 |    |    |    |    |    |-- Insuranceprice: long (nullable = true)
 |    |    |    |    |    |-- Insurancequantity: long (nullable = true)
 |    |    |    |    |    |-- childItems: array (nullable = true)
 |    |    |    |    |    |    |-- element: struct (containsNull = true)
 |    |    |    |    |    |    |    |-- InsuranceLabel: string (nullable = true)
 |    |    |    |    |    |    |    |-- InsuranceNumber: string (nullable = true)
 |    |    |    |    |    |    |    |-- Insuranceprice: long (nullable = true)
 |    |    |    |    |    |    |    |-- Insurancequantity: long (nullable = true)
 |    |    |    |    |    |    |    |-- discountsreceived: array (nullable = true)
 |    |    |    |    |    |    |    |    |-- element: struct (containsNull = true)
 |    |    |    |    |    |    |    |    |    |-- amount: long (nullable = true)
 |    |    |    |    |    |    |    |    |    |-- description: string (nullable = true)
 |    |    |    |    |    |-- discountsreceived: array (nullable = true)
 |    |    |    |    |    |    |-- element: struct (containsNull = true)
 |    |    |    |    |    |    |    |-- amount: long (nullable = true)
 |    |    |    |    |    |    |    |-- description: string (nullable = true)
 |-- eventTime: string (nullable = true)
 |-- id: string (nullable = true)

***converted***
root
 |-- id: string (nullable = true)
 |-- InsuranceProvider: string (nullable = true)
 |-- Type: struct (nullable = true)
 |    |-- Client: struct (nullable = true)
 |    |    |-- PaidIn: struct (nullable = true)
 |    |    |    |-- Insuranceid: string (nullable = true)
 |    |    |    |-- Insurancedesc: string (nullable = true)
 |    |    |    |-- purchaseditems: struct (nullable = true)
 |    |    |    |    |-- InsuranceLabel: string (nullable = true)
 |    |    |    |    |-- InsuranceNumber: string (nullable = true)
 |    |    |    |    |-- Insuranceprice: double (nullable = true)
 |    |    |    |    |-- Insurancequantity: integer (nullable = true)
 |    |    |    |    |-- childItems: struct (nullable = true)
 |    |    |    |    |    |-- InsuranceLabel: string (nullable = true)
 |    |    |    |    |    |-- InsuranceNumber: string (nullable = true)
 |    |    |    |    |    |-- Insuranceprice: double (nullable = true)
 |    |    |    |    |    |-- Insurancequantity: integer (nullable = true)
 |    |    |    |    |    |-- discountsreceived: struct (nullable = true)
 |    |    |    |    |    |    |-- amount: integer (nullable = true)
 |    |    |    |    |    |    |-- description: string (nullable = true)
 |    |    |    |    |-- discountsreceived: struct (nullable = true)
 |    |    |    |    |    |-- amount: integer (nullable = true)
 |    |    |    |    |    |-- description: string (nullable = true)
 |-- eventTime: string (nullable = true)

<<<<returned>>>>
  -- Read and parsed JSON file
root
 |-- id: string (nullable = true)
 |-- InsuranceProvider: string (nullable = true)
 |-- Type: struct (nullable = true)
 |    |-- Client: struct (nullable = true)
 |    |    |-- PaidIn: struct (nullable = true)
 |    |    |    |-- Insuranceid: string (nullable = true)
 |    |    |    |-- Insurancedesc: string (nullable = true)
 |    |    |    |-- purchaseditems: struct (nullable = true)
 |    |    |    |    |-- InsuranceLabel: string (nullable = true)
 |    |    |    |    |-- InsuranceNumber: string (nullable = true)
 |    |    |    |    |-- Insuranceprice: double (nullable = true)
 |    |    |    |    |-- Insurancequantity: integer (nullable = true)
 |    |    |    |    |-- childItems: struct (nullable = true)
 |    |    |    |    |    |-- InsuranceLabel: string (nullable = true)
 |    |    |    |    |    |-- InsuranceNumber: string (nullable = true)
 |    |    |    |    |    |-- Insuranceprice: double (nullable = true)
 |    |    |    |    |    |-- Insurancequantity: integer (nullable = true)
 |    |    |    |    |    |-- discountsreceived: struct (nullable = true)
 |    |    |    |    |    |    |-- amount: integer (nullable = true)
 |    |    |    |    |    |    |-- description: string (nullable = true)
 |    |    |    |    |-- discountsreceived: struct (nullable = true)
 |    |    |    |    |    |-- amount: integer (nullable = true)
 |    |    |    |    |    |-- description: string (nullable = true)
 |-- eventTime: string (nullable = true)

  -- Display JSON file
23/08/24 14:02:20 ERROR Executor: Exception in task 0.0 in stage 1.0 (TID 1)
org.apache.spark.api.python.PythonException: Traceback (most recent call last):
  File "D:\util\spark-3.4.1-bin-hadoop3-scala2.13\python\lib\pyspark.zip\pyspark\worker.py", line 830, in main
  File "D:\util\spark-3.4.1-bin-hadoop3-scala2.13\python\lib\pyspark.zip\pyspark\worker.py", line 822, in process
  File "D:\util\spark-3.4.1-bin-hadoop3-scala2.13\python\lib\pyspark.zip\pyspark\serializers.py", line 274, in dump_stream
    vs = list(itertools.islice(iterator, batch))
         ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
  File "D:\util\spark-3.4.1-bin-hadoop3-scala2.13\python\lib\pyspark.zip\pyspark\util.py", line 81, in wrapper
    return f(*args, **kwargs)
           ^^^^^^^^^^^^^^^^^^
  File "D:\util\spark-3.4.1-bin-hadoop3-scala2.13\python\lib\pyspark.zip\pyspark\sql\session.py", line 1292, in prepare
  File "D:\util\spark-3.4.1-bin-hadoop3-scala2.13\python\lib\pyspark.zip\pyspark\sql\types.py", line 2001, in verify
  File "D:\util\spark-3.4.1-bin-hadoop3-scala2.13\python\lib\pyspark.zip\pyspark\sql\types.py", line 1979, in verify_struct
  File "D:\util\spark-3.4.1-bin-hadoop3-scala2.13\python\lib\pyspark.zip\pyspark\sql\types.py", line 2001, in verify
  File "D:\util\spark-3.4.1-bin-hadoop3-scala2.13\python\lib\pyspark.zip\pyspark\sql\types.py", line 1985, in verify_struct
TypeError: field Type: StructType can not accept object '2020-05-19T01:59:10.379Z' in type <class 'str'>

	at org.apache.spark.api.python.BasePythonRunner$ReaderIterator.handlePythonException(PythonRunner.scala:561)
	at org.apache.spark.api.python.PythonRunner$$anon$3.read(PythonRunner.scala:767)
	at org.apache.spark.api.python.PythonRunner$$anon$3.read(PythonRunner.scala:749)
	at org.apache.spark.api.python.BasePythonRunner$ReaderIterator.hasNext(PythonRunner.scala:514)
	at org.apache.spark.InterruptibleIterator.hasNext(InterruptibleIterator.scala:37)
	at scala.collection.Iterator$$anon$10.hasNext(Iterator.scala:594)
	at scala.collection.Iterator$$anon$9.hasNext(Iterator.scala:576)
	at scala.collection.Iterator$$anon$9.hasNext(Iterator.scala:576)
	at org.apache.spark.sql.catalyst.expressions.GeneratedClass$GeneratedIteratorForCodegenStage1.processNext(Unknown Source)
	at org.apache.spark.sql.execution.BufferedRowIterator.hasNext(BufferedRowIterator.java:43)
	at org.apache.spark.sql.execution.WholeStageCodegenExec$$anon$1.hasNext(WholeStageCodegenExec.scala:760)
	at org.apache.spark.sql.execution.SparkPlan.$anonfun$getByteArrayRdd$1(SparkPlan.scala:388)
	at org.apache.spark.rdd.RDD.$anonfun$mapPartitionsInternal$2(RDD.scala:888)
	at org.apache.spark.rdd.RDD.$anonfun$mapPartitionsInternal$2$adapted(RDD.scala:888)
	at org.apache.spark.rdd.MapPartitionsRDD.compute(MapPartitionsRDD.scala:52)
	at org.apache.spark.rdd.RDD.computeOrReadCheckpoint(RDD.scala:364)
	at org.apache.spark.rdd.RDD.iterator(RDD.scala:328)
	at org.apache.spark.scheduler.ResultTask.runTask(ResultTask.scala:92)
	at org.apache.spark.TaskContext.runTaskWithListeners(TaskContext.scala:161)
	at org.apache.spark.scheduler.Task.run(Task.scala:139)
	at org.apache.spark.executor.Executor$TaskRunner.$anonfun$run$3(Executor.scala:554)
	at org.apache.spark.util.Utils$.tryWithSafeFinally(Utils.scala:1529)
	at org.apache.spark.executor.Executor$TaskRunner.run(Executor.scala:557)
	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
	at java.base/java.lang.Thread.run(Thread.java:833)
23/08/24 14:02:20 WARN TaskSetManager: Lost task 0.0 in stage 1.0 (TID 1) (THOR.mshome.net executor driver): org.apache.spark.api.python.PythonException: Traceback (most recent call last):
  File "D:\util\spark-3.4.1-bin-hadoop3-scala2.13\python\lib\pyspark.zip\pyspark\worker.py", line 830, in main
  File "D:\util\spark-3.4.1-bin-hadoop3-scala2.13\python\lib\pyspark.zip\pyspark\worker.py", line 822, in process
  File "D:\util\spark-3.4.1-bin-hadoop3-scala2.13\python\lib\pyspark.zip\pyspark\serializers.py", line 274, in dump_stream
    vs = list(itertools.islice(iterator, batch))
         ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
  File "D:\util\spark-3.4.1-bin-hadoop3-scala2.13\python\lib\pyspark.zip\pyspark\util.py", line 81, in wrapper
    return f(*args, **kwargs)
           ^^^^^^^^^^^^^^^^^^
  File "D:\util\spark-3.4.1-bin-hadoop3-scala2.13\python\lib\pyspark.zip\pyspark\sql\session.py", line 1292, in prepare
  File "D:\util\spark-3.4.1-bin-hadoop3-scala2.13\python\lib\pyspark.zip\pyspark\sql\types.py", line 2001, in verify
  File "D:\util\spark-3.4.1-bin-hadoop3-scala2.13\python\lib\pyspark.zip\pyspark\sql\types.py", line 1979, in verify_struct
  File "D:\util\spark-3.4.1-bin-hadoop3-scala2.13\python\lib\pyspark.zip\pyspark\sql\types.py", line 2001, in verify
  File "D:\util\spark-3.4.1-bin-hadoop3-scala2.13\python\lib\pyspark.zip\pyspark\sql\types.py", line 1985, in verify_struct
TypeError: field Type: StructType can not accept object '2020-05-19T01:59:10.379Z' in type <class 'str'>

	at org.apache.spark.api.python.BasePythonRunner$ReaderIterator.handlePythonException(PythonRunner.scala:561)
	at org.apache.spark.api.python.PythonRunner$$anon$3.read(PythonRunner.scala:767)
	at org.apache.spark.api.python.PythonRunner$$anon$3.read(PythonRunner.scala:749)
	at org.apache.spark.api.python.BasePythonRunner$ReaderIterator.hasNext(PythonRunner.scala:514)
	at org.apache.spark.InterruptibleIterator.hasNext(InterruptibleIterator.scala:37)
	at scala.collection.Iterator$$anon$10.hasNext(Iterator.scala:594)
	at scala.collection.Iterator$$anon$9.hasNext(Iterator.scala:576)
	at scala.collection.Iterator$$anon$9.hasNext(Iterator.scala:576)
	at org.apache.spark.sql.catalyst.expressions.GeneratedClass$GeneratedIteratorForCodegenStage1.processNext(Unknown Source)
	at org.apache.spark.sql.execution.BufferedRowIterator.hasNext(BufferedRowIterator.java:43)
	at org.apache.spark.sql.execution.WholeStageCodegenExec$$anon$1.hasNext(WholeStageCodegenExec.scala:760)
	at org.apache.spark.sql.execution.SparkPlan.$anonfun$getByteArrayRdd$1(SparkPlan.scala:388)
	at org.apache.spark.rdd.RDD.$anonfun$mapPartitionsInternal$2(RDD.scala:888)
	at org.apache.spark.rdd.RDD.$anonfun$mapPartitionsInternal$2$adapted(RDD.scala:888)
	at org.apache.spark.rdd.MapPartitionsRDD.compute(MapPartitionsRDD.scala:52)
	at org.apache.spark.rdd.RDD.computeOrReadCheckpoint(RDD.scala:364)
	at org.apache.spark.rdd.RDD.iterator(RDD.scala:328)
	at org.apache.spark.scheduler.ResultTask.runTask(ResultTask.scala:92)
	at org.apache.spark.TaskContext.runTaskWithListeners(TaskContext.scala:161)
	at org.apache.spark.scheduler.Task.run(Task.scala:139)
	at org.apache.spark.executor.Executor$TaskRunner.$anonfun$run$3(Executor.scala:554)
	at org.apache.spark.util.Utils$.tryWithSafeFinally(Utils.scala:1529)
	at org.apache.spark.executor.Executor$TaskRunner.run(Executor.scala:557)
	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
	at java.base/java.lang.Thread.run(Thread.java:833)

23/08/24 14:02:20 ERROR TaskSetManager: Task 0 in stage 1.0 failed 1 times; aborting job
Traceback (most recent call last):
  File "D:\GitHub\DemoDev\dev-topics-bigdata\dev-topics-sparkinstall\examples\queries\TestPart1.py", line 39, in <module>
    purchases.show()
  File "D:\util\spark-3.4.1-bin-hadoop3-scala2.13\python\lib\pyspark.zip\pyspark\sql\dataframe.py", line 899, in show
  File "D:\util\spark-3.4.1-bin-hadoop3-scala2.13\python\lib\py4j-0.10.9.7-src.zip\py4j\java_gateway.py", line 1322, in __call__
  File "D:\util\spark-3.4.1-bin-hadoop3-scala2.13\python\lib\pyspark.zip\pyspark\errors\exceptions\captured.py", line 169, in deco
  File "D:\util\spark-3.4.1-bin-hadoop3-scala2.13\python\lib\py4j-0.10.9.7-src.zip\py4j\protocol.py", line 326, in get_return_value
py4j.protocol.Py4JJavaError: An error occurred while calling o48.showString.
: org.apache.spark.SparkException: Job aborted due to stage failure: Task 0 in stage 1.0 failed 1 times, most recent failure: Lost task 0.0 in stage 1.0 (TID 1) (THOR.mshome.net executor driver): org.apache.spark.api.python.PythonException: Traceback (most recent call last):
  File "D:\util\spark-3.4.1-bin-hadoop3-scala2.13\python\lib\pyspark.zip\pyspark\worker.py", line 830, in main
  File "D:\util\spark-3.4.1-bin-hadoop3-scala2.13\python\lib\pyspark.zip\pyspark\worker.py", line 822, in process
  File "D:\util\spark-3.4.1-bin-hadoop3-scala2.13\python\lib\pyspark.zip\pyspark\serializers.py", line 274, in dump_stream
    vs = list(itertools.islice(iterator, batch))
         ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
  File "D:\util\spark-3.4.1-bin-hadoop3-scala2.13\python\lib\pyspark.zip\pyspark\util.py", line 81, in wrapper
    return f(*args, **kwargs)
           ^^^^^^^^^^^^^^^^^^
  File "D:\util\spark-3.4.1-bin-hadoop3-scala2.13\python\lib\pyspark.zip\pyspark\sql\session.py", line 1292, in prepare
  File "D:\util\spark-3.4.1-bin-hadoop3-scala2.13\python\lib\pyspark.zip\pyspark\sql\types.py", line 2001, in verify
  File "D:\util\spark-3.4.1-bin-hadoop3-scala2.13\python\lib\pyspark.zip\pyspark\sql\types.py", line 1979, in verify_struct
  File "D:\util\spark-3.4.1-bin-hadoop3-scala2.13\python\lib\pyspark.zip\pyspark\sql\types.py", line 2001, in verify
  File "D:\util\spark-3.4.1-bin-hadoop3-scala2.13\python\lib\pyspark.zip\pyspark\sql\types.py", line 1985, in verify_struct
TypeError: field Type: StructType can not accept object '2020-05-19T01:59:10.379Z' in type <class 'str'>

	at org.apache.spark.api.python.BasePythonRunner$ReaderIterator.handlePythonException(PythonRunner.scala:561)
	at org.apache.spark.api.python.PythonRunner$$anon$3.read(PythonRunner.scala:767)
	at org.apache.spark.api.python.PythonRunner$$anon$3.read(PythonRunner.scala:749)
	at org.apache.spark.api.python.BasePythonRunner$ReaderIterator.hasNext(PythonRunner.scala:514)
	at org.apache.spark.InterruptibleIterator.hasNext(InterruptibleIterator.scala:37)
	at scala.collection.Iterator$$anon$10.hasNext(Iterator.scala:594)
	at scala.collection.Iterator$$anon$9.hasNext(Iterator.scala:576)
	at scala.collection.Iterator$$anon$9.hasNext(Iterator.scala:576)
	at org.apache.spark.sql.catalyst.expressions.GeneratedClass$GeneratedIteratorForCodegenStage1.processNext(Unknown Source)
	at org.apache.spark.sql.execution.BufferedRowIterator.hasNext(BufferedRowIterator.java:43)
	at org.apache.spark.sql.execution.WholeStageCodegenExec$$anon$1.hasNext(WholeStageCodegenExec.scala:760)
	at org.apache.spark.sql.execution.SparkPlan.$anonfun$getByteArrayRdd$1(SparkPlan.scala:388)
	at org.apache.spark.rdd.RDD.$anonfun$mapPartitionsInternal$2(RDD.scala:888)
	at org.apache.spark.rdd.RDD.$anonfun$mapPartitionsInternal$2$adapted(RDD.scala:888)
	at org.apache.spark.rdd.MapPartitionsRDD.compute(MapPartitionsRDD.scala:52)
	at org.apache.spark.rdd.RDD.computeOrReadCheckpoint(RDD.scala:364)
	at org.apache.spark.rdd.RDD.iterator(RDD.scala:328)
	at org.apache.spark.scheduler.ResultTask.runTask(ResultTask.scala:92)
	at org.apache.spark.TaskContext.runTaskWithListeners(TaskContext.scala:161)
	at org.apache.spark.scheduler.Task.run(Task.scala:139)
	at org.apache.spark.executor.Executor$TaskRunner.$anonfun$run$3(Executor.scala:554)
	at org.apache.spark.util.Utils$.tryWithSafeFinally(Utils.scala:1529)
	at org.apache.spark.executor.Executor$TaskRunner.run(Executor.scala:557)
	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
	at java.base/java.lang.Thread.run(Thread.java:833)

Driver stacktrace:
	at org.apache.spark.scheduler.DAGScheduler.failJobAndIndependentStages(DAGScheduler.scala:2785)
	at org.apache.spark.scheduler.DAGScheduler.$anonfun$abortStage$2(DAGScheduler.scala:2721)
	at org.apache.spark.scheduler.DAGScheduler.$anonfun$abortStage$2$adapted(DAGScheduler.scala:2720)
	at scala.collection.immutable.List.foreach(List.scala:333)
	at org.apache.spark.scheduler.DAGScheduler.abortStage(DAGScheduler.scala:2720)
	at org.apache.spark.scheduler.DAGScheduler.$anonfun$handleTaskSetFailed$1(DAGScheduler.scala:1206)
	at org.apache.spark.scheduler.DAGScheduler.$anonfun$handleTaskSetFailed$1$adapted(DAGScheduler.scala:1206)
	at scala.Option.foreach(Option.scala:437)
	at org.apache.spark.scheduler.DAGScheduler.handleTaskSetFailed(DAGScheduler.scala:1206)
	at org.apache.spark.scheduler.DAGSchedulerEventProcessLoop.doOnReceive(DAGScheduler.scala:2984)
	at org.apache.spark.scheduler.DAGSchedulerEventProcessLoop.onReceive(DAGScheduler.scala:2923)
	at org.apache.spark.scheduler.DAGSchedulerEventProcessLoop.onReceive(DAGScheduler.scala:2912)
	at org.apache.spark.util.EventLoop$$anon$1.run(EventLoop.scala:49)
	at org.apache.spark.scheduler.DAGScheduler.runJob(DAGScheduler.scala:971)
	at org.apache.spark.SparkContext.runJob(SparkContext.scala:2263)
	at org.apache.spark.SparkContext.runJob(SparkContext.scala:2284)
	at org.apache.spark.SparkContext.runJob(SparkContext.scala:2303)
	at org.apache.spark.sql.execution.SparkPlan.executeTake(SparkPlan.scala:530)
	at org.apache.spark.sql.execution.SparkPlan.executeTake(SparkPlan.scala:483)
	at org.apache.spark.sql.execution.CollectLimitExec.executeCollect(limit.scala:61)
	at org.apache.spark.sql.Dataset.collectFromPlan(Dataset.scala:4177)
	at org.apache.spark.sql.Dataset.$anonfun$head$1(Dataset.scala:3161)
	at org.apache.spark.sql.Dataset.$anonfun$withAction$2(Dataset.scala:4167)
	at org.apache.spark.sql.execution.QueryExecution$.withInternalError(QueryExecution.scala:526)
	at org.apache.spark.sql.Dataset.$anonfun$withAction$1(Dataset.scala:4165)
	at org.apache.spark.sql.execution.SQLExecution$.$anonfun$withNewExecutionId$6(SQLExecution.scala:118)
	at org.apache.spark.sql.execution.SQLExecution$.withSQLConfPropagated(SQLExecution.scala:195)
	at org.apache.spark.sql.execution.SQLExecution$.$anonfun$withNewExecutionId$1(SQLExecution.scala:103)
	at org.apache.spark.sql.SparkSession.withActive(SparkSession.scala:827)
	at org.apache.spark.sql.execution.SQLExecution$.withNewExecutionId(SQLExecution.scala:65)
	at org.apache.spark.sql.Dataset.withAction(Dataset.scala:4165)
	at org.apache.spark.sql.Dataset.head(Dataset.scala:3161)
	at org.apache.spark.sql.Dataset.take(Dataset.scala:3382)
	at org.apache.spark.sql.Dataset.getRows(Dataset.scala:284)
	at org.apache.spark.sql.Dataset.showString(Dataset.scala:323)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.base/java.lang.reflect.Method.invoke(Method.java:568)
	at py4j.reflection.MethodInvoker.invoke(MethodInvoker.java:244)
	at py4j.reflection.ReflectionEngine.invoke(ReflectionEngine.java:374)
	at py4j.Gateway.invoke(Gateway.java:282)
	at py4j.commands.AbstractCommand.invokeMethod(AbstractCommand.java:132)
	at py4j.commands.CallCommand.execute(CallCommand.java:79)
	at py4j.ClientServerConnection.waitForCommands(ClientServerConnection.java:182)
	at py4j.ClientServerConnection.run(ClientServerConnection.java:106)
	at java.base/java.lang.Thread.run(Thread.java:833)
Caused by: org.apache.spark.api.python.PythonException: Traceback (most recent call last):
  File "D:\util\spark-3.4.1-bin-hadoop3-scala2.13\python\lib\pyspark.zip\pyspark\worker.py", line 830, in main
  File "D:\util\spark-3.4.1-bin-hadoop3-scala2.13\python\lib\pyspark.zip\pyspark\worker.py", line 822, in process
  File "D:\util\spark-3.4.1-bin-hadoop3-scala2.13\python\lib\pyspark.zip\pyspark\serializers.py", line 274, in dump_stream
    vs = list(itertools.islice(iterator, batch))
         ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
  File "D:\util\spark-3.4.1-bin-hadoop3-scala2.13\python\lib\pyspark.zip\pyspark\util.py", line 81, in wrapper
    return f(*args, **kwargs)
           ^^^^^^^^^^^^^^^^^^
  File "D:\util\spark-3.4.1-bin-hadoop3-scala2.13\python\lib\pyspark.zip\pyspark\sql\session.py", line 1292, in prepare
  File "D:\util\spark-3.4.1-bin-hadoop3-scala2.13\python\lib\pyspark.zip\pyspark\sql\types.py", line 2001, in verify
  File "D:\util\spark-3.4.1-bin-hadoop3-scala2.13\python\lib\pyspark.zip\pyspark\sql\types.py", line 1979, in verify_struct
  File "D:\util\spark-3.4.1-bin-hadoop3-scala2.13\python\lib\pyspark.zip\pyspark\sql\types.py", line 2001, in verify
  File "D:\util\spark-3.4.1-bin-hadoop3-scala2.13\python\lib\pyspark.zip\pyspark\sql\types.py", line 1985, in verify_struct
TypeError: field Type: StructType can not accept object '2020-05-19T01:59:10.379Z' in type <class 'str'>

	at org.apache.spark.api.python.BasePythonRunner$ReaderIterator.handlePythonException(PythonRunner.scala:561)
	at org.apache.spark.api.python.PythonRunner$$anon$3.read(PythonRunner.scala:767)
	at org.apache.spark.api.python.PythonRunner$$anon$3.read(PythonRunner.scala:749)
	at org.apache.spark.api.python.BasePythonRunner$ReaderIterator.hasNext(PythonRunner.scala:514)
	at org.apache.spark.InterruptibleIterator.hasNext(InterruptibleIterator.scala:37)
	at scala.collection.Iterator$$anon$10.hasNext(Iterator.scala:594)
	at scala.collection.Iterator$$anon$9.hasNext(Iterator.scala:576)
	at scala.collection.Iterator$$anon$9.hasNext(Iterator.scala:576)
	at org.apache.spark.sql.catalyst.expressions.GeneratedClass$GeneratedIteratorForCodegenStage1.processNext(Unknown Source)
	at org.apache.spark.sql.execution.BufferedRowIterator.hasNext(BufferedRowIterator.java:43)
	at org.apache.spark.sql.execution.WholeStageCodegenExec$$anon$1.hasNext(WholeStageCodegenExec.scala:760)
	at org.apache.spark.sql.execution.SparkPlan.$anonfun$getByteArrayRdd$1(SparkPlan.scala:388)
	at org.apache.spark.rdd.RDD.$anonfun$mapPartitionsInternal$2(RDD.scala:888)
	at org.apache.spark.rdd.RDD.$anonfun$mapPartitionsInternal$2$adapted(RDD.scala:888)
	at org.apache.spark.rdd.MapPartitionsRDD.compute(MapPartitionsRDD.scala:52)
	at org.apache.spark.rdd.RDD.computeOrReadCheckpoint(RDD.scala:364)
	at org.apache.spark.rdd.RDD.iterator(RDD.scala:328)
	at org.apache.spark.scheduler.ResultTask.runTask(ResultTask.scala:92)
	at org.apache.spark.TaskContext.runTaskWithListeners(TaskContext.scala:161)
	at org.apache.spark.scheduler.Task.run(Task.scala:139)
	at org.apache.spark.executor.Executor$TaskRunner.$anonfun$run$3(Executor.scala:554)
	at org.apache.spark.util.Utils$.tryWithSafeFinally(Utils.scala:1529)
	at org.apache.spark.executor.Executor$TaskRunner.run(Executor.scala:557)
	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
	... 1 more

23/08/24 14:02:20 ERROR ShutdownHookManager: Exception while deleting Spark temp dir: D:\Temp\spark-f1a1cf26-c5e4-475d-ba06-bc9d73d897c2\pyspark-2672f9ce-0d03-4bd3-96da-17bf2a621fb5
java.nio.file.NoSuchFileException: D:\Temp\spark-f1a1cf26-c5e4-475d-ba06-bc9d73d897c2\pyspark-2672f9ce-0d03-4bd3-96da-17bf2a621fb5
	at java.base/sun.nio.fs.WindowsException.translateToIOException(WindowsException.java:85)
	at java.base/sun.nio.fs.WindowsException.rethrowAsIOException(WindowsException.java:103)
	at java.base/sun.nio.fs.WindowsException.rethrowAsIOException(WindowsException.java:108)
	at java.base/sun.nio.fs.WindowsFileAttributeViews$Basic.readAttributes(WindowsFileAttributeViews.java:53)
	at java.base/sun.nio.fs.WindowsFileAttributeViews$Basic.readAttributes(WindowsFileAttributeViews.java:38)
	at java.base/sun.nio.fs.WindowsFileSystemProvider.readAttributes(WindowsFileSystemProvider.java:199)
	at java.base/java.nio.file.Files.readAttributes(Files.java:1851)
	at org.apache.spark.network.util.JavaUtils.deleteRecursivelyUsingJavaIO(JavaUtils.java:128)
	at org.apache.spark.network.util.JavaUtils.deleteRecursively(JavaUtils.java:121)
	at org.apache.spark.network.util.JavaUtils.deleteRecursively(JavaUtils.java:94)
	at org.apache.spark.util.Utils$.deleteRecursively(Utils.scala:1231)
	at org.apache.spark.util.ShutdownHookManager$.$anonfun$new$4(ShutdownHookManager.scala:65)
	at org.apache.spark.util.ShutdownHookManager$.$anonfun$new$4$adapted(ShutdownHookManager.scala:62)
	at scala.collection.ArrayOps$.foreach$extension(ArrayOps.scala:1328)
	at org.apache.spark.util.ShutdownHookManager$.$anonfun$new$2(ShutdownHookManager.scala:62)
	at org.apache.spark.util.SparkShutdownHook.run(ShutdownHookManager.scala:214)
	at org.apache.spark.util.SparkShutdownHookManager.$anonfun$runAll$2(ShutdownHookManager.scala:188)
	at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
	at org.apache.spark.util.Utils$.logUncaughtExceptions(Utils.scala:2088)
	at org.apache.spark.util.SparkShutdownHookManager.$anonfun$runAll$1(ShutdownHookManager.scala:188)
	at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
	at scala.util.Try$.apply(Try.scala:210)
	at org.apache.spark.util.SparkShutdownHookManager.runAll(ShutdownHookManager.scala:188)
	at org.apache.spark.util.SparkShutdownHookManager$$anon$2.run(ShutdownHookManager.scala:178)
	at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:539)
	at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:264)
	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
	at java.base/java.lang.Thread.run(Thread.java:833)

 Submit failed, RC=1

 Queries Tests 1 Script Done

