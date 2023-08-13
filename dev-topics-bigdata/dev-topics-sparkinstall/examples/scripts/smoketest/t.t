
 Summit Spark Smoke Tests script using driver smoketest.py on D:\util\spark-3.4.1-bin-hadoop3-scala2.13 executing $0.cmd
   -- Base Path  : D:\GitHub\DemoDev\dev-topics-bigdata\dev-topics-sparkinstall\examples\scripts\smoketest\
   -- Driver Path: D:\GitHub\DemoDev\dev-topics-bigdata\dev-topics-sparkinstall\examples\smoketest\
   -- Spark Home : D:\util\spark-3.4.1-bin-hadoop3-scala2.13
java version "17.0.6" 2023-01-17 LTS
Java(TM) SE Runtime Environment (build 17.0.6+9-LTS-190)
Java HotSpot(TM) 64-Bit Server VM (build 17.0.6+9-LTS-190, mixed mode, sharing)
 D:\util\spark-3.4.1-bin-hadoop3-scala2.13\bin\spark-submit D:\GitHub\DemoDev\dev-topics-bigdata\dev-topics-sparkinstall\examples\smoketest\smoketest.py

***************************
*** PySpark Smoke Tests ***
***************************

Smoke Test 1: Spark Attributes
23/08/11 10:41:49 INFO SparkContext: Running Spark version 3.4.1
23/08/11 10:41:50 WARN SparkConf: Note that spark.local.dir will be overridden by the value set by the cluster manager (via SPARK_LOCAL_DIRS in mesos/standalone/kubernetes and LOCAL_DIRS in YARN).
23/08/11 10:41:50 INFO ResourceUtils: ==============================================================
23/08/11 10:41:50 INFO ResourceUtils: No custom resources configured for spark.driver.
23/08/11 10:41:50 INFO ResourceUtils: ==============================================================
23/08/11 10:41:50 INFO SparkContext: Submitted application: CollectedSmokeTests1
23/08/11 10:41:50 INFO ResourceProfile: Default ResourceProfile created, executor resources: Map(cores -> name: cores, amount: 1, script: , vendor: , memory -> name: memory, amount: 1024, script: , vendor: , offHeap -> name: offHeap, amount: 0, script: , vendor: ), task resources: Map(cpus -> name: cpus, amount: 1.0)
23/08/11 10:41:50 INFO ResourceProfile: Limiting resource is cpu
23/08/11 10:41:50 INFO ResourceProfileManager: Added ResourceProfile id: 0
23/08/11 10:41:50 INFO SecurityManager: Changing view acls to: Don
23/08/11 10:41:50 INFO SecurityManager: Changing modify acls to: Don
23/08/11 10:41:50 INFO SecurityManager: Changing view acls groups to: 
23/08/11 10:41:50 INFO SecurityManager: Changing modify acls groups to: 
23/08/11 10:41:50 INFO SecurityManager: SecurityManager: authentication disabled; ui acls disabled; users with view permissions: Don; groups with view permissions: EMPTY; users with modify permissions: Don; groups with modify permissions: EMPTY
23/08/11 10:41:50 INFO Utils: Successfully started service 'sparkDriver' on port 58182.
23/08/11 10:41:50 INFO SparkEnv: Registering MapOutputTracker
23/08/11 10:41:50 INFO SparkEnv: Registering BlockManagerMaster
23/08/11 10:41:50 INFO BlockManagerMasterEndpoint: Using org.apache.spark.storage.DefaultTopologyMapper for getting topology information
23/08/11 10:41:50 INFO BlockManagerMasterEndpoint: BlockManagerMasterEndpoint up
23/08/11 10:41:50 INFO SparkEnv: Registering BlockManagerMasterHeartbeat
23/08/11 10:41:50 INFO DiskBlockManager: Created local directory at D:\Temp\blockmgr-b248772c-9921-44fa-adb5-763962b2b250
23/08/11 10:41:50 INFO MemoryStore: MemoryStore started with capacity 434.4 MiB
23/08/11 10:41:50 INFO SparkEnv: Registering OutputCommitCoordinator
23/08/11 10:41:51 INFO JettyUtils: Start Jetty 0.0.0.0:4040 for SparkUI
23/08/11 10:41:51 WARN Utils: Service 'SparkUI' could not bind on port 4040. Attempting port 4041.
23/08/11 10:41:51 INFO Utils: Successfully started service 'SparkUI' on port 4041.
23/08/11 10:41:51 INFO Executor: Starting executor ID driver on host THOR.mshome.net
23/08/11 10:41:51 INFO Executor: Starting executor with user classpath (userClassPathFirst = false): ''
23/08/11 10:41:51 INFO Utils: Successfully started service 'org.apache.spark.network.netty.NettyBlockTransferService' on port 58184.
23/08/11 10:41:51 INFO NettyBlockTransferService: Server created on THOR.mshome.net:58184
23/08/11 10:41:51 INFO BlockManager: Using org.apache.spark.storage.RandomBlockReplicationPolicy for block replication policy
23/08/11 10:41:51 INFO BlockManagerMaster: Registering BlockManager BlockManagerId(driver, THOR.mshome.net, 58184, None)
23/08/11 10:41:51 INFO BlockManagerMasterEndpoint: Registering block manager THOR.mshome.net:58184 with 434.4 MiB RAM, BlockManagerId(driver, THOR.mshome.net, 58184, None)
23/08/11 10:41:51 INFO BlockManagerMaster: Registered BlockManager BlockManagerId(driver, THOR.mshome.net, 58184, None)
23/08/11 10:41:51 INFO BlockManager: Initialized BlockManager: BlockManagerId(driver, THOR.mshome.net, 58184, None)
Python Version:3.11
Spark Version :3.4.1
APP Name      :CollectedSmokeTests1
APP ID        :local-1691775711270
Master        :local[*]
23/08/11 10:41:52 INFO SharedState: Setting hive.metastore.warehouse.dir ('null') to the value of spark.sql.warehouse.dir.
23/08/11 10:41:52 INFO SharedState: Warehouse path is 'file:/D:/GitHub/DemoDev/dev-topics-bigdata/dev-topics-sparkinstall/examples/scripts/smoketest/spark-warehouse'.
Configuration :<pyspark.sql.conf.RuntimeConfig object at 0x000001F7C7811650>
Context       :<SparkContext master=local[*] appName=CollectedSmokeTests1>
Environment   :{'PYTHONHASHSEED': '0'}
23/08/11 10:41:52 INFO SparkContext: SparkContext is stopping with exitCode 0.
23/08/11 10:41:52 INFO SparkUI: Stopped Spark web UI at http://THOR.mshome.net:4041
23/08/11 10:41:52 INFO MapOutputTrackerMasterEndpoint: MapOutputTrackerMasterEndpoint stopped!
23/08/11 10:41:52 INFO MemoryStore: MemoryStore cleared
23/08/11 10:41:52 INFO BlockManager: BlockManager stopped
23/08/11 10:41:52 INFO BlockManagerMaster: BlockManagerMaster stopped
23/08/11 10:41:52 INFO OutputCommitCoordinator$OutputCommitCoordinatorEndpoint: OutputCommitCoordinator stopped!
23/08/11 10:41:52 INFO SparkContext: Successfully stopped SparkContext

Smoke Test 2: Create a DataFrame
23/08/11 10:41:53 INFO SparkContext: Running Spark version 3.4.1
23/08/11 10:41:53 WARN SparkConf: Note that spark.local.dir will be overridden by the value set by the cluster manager (via SPARK_LOCAL_DIRS in mesos/standalone/kubernetes and LOCAL_DIRS in YARN).
23/08/11 10:41:53 INFO ResourceUtils: ==============================================================
23/08/11 10:41:53 INFO ResourceUtils: No custom resources configured for spark.driver.
23/08/11 10:41:53 INFO ResourceUtils: ==============================================================
23/08/11 10:41:53 INFO SparkContext: Submitted application: CollectedSmokeTests2
23/08/11 10:41:53 INFO ResourceProfile: Default ResourceProfile created, executor resources: Map(cores -> name: cores, amount: 1, script: , vendor: , memory -> name: memory, amount: 1024, script: , vendor: , offHeap -> name: offHeap, amount: 0, script: , vendor: ), task resources: Map(cpus -> name: cpus, amount: 1.0)
23/08/11 10:41:53 INFO ResourceProfile: Limiting resource is cpu
23/08/11 10:41:53 INFO ResourceProfileManager: Added ResourceProfile id: 0
23/08/11 10:41:53 INFO SecurityManager: Changing view acls to: Don
23/08/11 10:41:53 INFO SecurityManager: Changing modify acls to: Don
23/08/11 10:41:53 INFO SecurityManager: Changing view acls groups to: 
23/08/11 10:41:53 INFO SecurityManager: Changing modify acls groups to: 
23/08/11 10:41:53 INFO SecurityManager: SecurityManager: authentication disabled; ui acls disabled; users with view permissions: Don; groups with view permissions: EMPTY; users with modify permissions: Don; groups with modify permissions: EMPTY
23/08/11 10:41:53 INFO Utils: Successfully started service 'sparkDriver' on port 58186.
23/08/11 10:41:53 INFO SparkEnv: Registering MapOutputTracker
23/08/11 10:41:53 INFO SparkEnv: Registering BlockManagerMaster
23/08/11 10:41:53 INFO BlockManagerMasterEndpoint: Using org.apache.spark.storage.DefaultTopologyMapper for getting topology information
23/08/11 10:41:53 INFO BlockManagerMasterEndpoint: BlockManagerMasterEndpoint up
23/08/11 10:41:53 INFO SparkEnv: Registering BlockManagerMasterHeartbeat
23/08/11 10:41:53 INFO DiskBlockManager: Created local directory at D:\Temp\blockmgr-6934a640-8d8a-4b97-ba7e-a8d86f2c9c9b
23/08/11 10:41:53 INFO MemoryStore: MemoryStore started with capacity 434.4 MiB
23/08/11 10:41:53 INFO SparkEnv: Registering OutputCommitCoordinator
23/08/11 10:41:53 INFO JettyUtils: Start Jetty 0.0.0.0:4040 for SparkUI
23/08/11 10:41:53 WARN Utils: Service 'SparkUI' could not bind on port 4040. Attempting port 4041.
23/08/11 10:41:53 INFO Utils: Successfully started service 'SparkUI' on port 4041.
23/08/11 10:41:53 INFO Executor: Starting executor ID driver on host THOR.mshome.net
23/08/11 10:41:53 INFO Executor: Starting executor with user classpath (userClassPathFirst = false): ''
23/08/11 10:41:53 INFO Utils: Successfully started service 'org.apache.spark.network.netty.NettyBlockTransferService' on port 58187.
23/08/11 10:41:53 INFO NettyBlockTransferService: Server created on THOR.mshome.net:58187
23/08/11 10:41:53 INFO BlockManager: Using org.apache.spark.storage.RandomBlockReplicationPolicy for block replication policy
23/08/11 10:41:53 INFO BlockManagerMaster: Registering BlockManager BlockManagerId(driver, THOR.mshome.net, 58187, None)
23/08/11 10:41:53 INFO BlockManagerMasterEndpoint: Registering block manager THOR.mshome.net:58187 with 434.4 MiB RAM, BlockManagerId(driver, THOR.mshome.net, 58187, None)
23/08/11 10:41:53 INFO BlockManagerMaster: Registered BlockManager BlockManagerId(driver, THOR.mshome.net, 58187, None)
23/08/11 10:41:53 INFO BlockManager: Initialized BlockManager: BlockManagerId(driver, THOR.mshome.net, 58187, None)
Default Parallelism: 8
23/08/11 10:42:08 INFO SharedState: Setting hive.metastore.warehouse.dir ('null') to the value of spark.sql.warehouse.dir.
23/08/11 10:42:08 INFO SharedState: Warehouse path is 'file:/D:/GitHub/DemoDev/dev-topics-bigdata/dev-topics-sparkinstall/examples/scripts/smoketest/spark-warehouse'.
23/08/11 10:42:13 INFO CodeGenerator: Code generated in 355.4828 ms
23/08/11 10:42:13 INFO SparkContext: Starting job: showString at NativeMethodAccessorImpl.java:0
23/08/11 10:42:13 INFO DAGScheduler: Got job 0 (showString at NativeMethodAccessorImpl.java:0) with 1 output partitions
23/08/11 10:42:13 INFO DAGScheduler: Final stage: ResultStage 0 (showString at NativeMethodAccessorImpl.java:0)
23/08/11 10:42:13 INFO DAGScheduler: Parents of final stage: List()
23/08/11 10:42:13 INFO DAGScheduler: Missing parents: List()
23/08/11 10:42:13 INFO DAGScheduler: Submitting ResultStage 0 (MapPartitionsRDD[6] at showString at NativeMethodAccessorImpl.java:0), which has no missing parents
23/08/11 10:42:13 INFO MemoryStore: Block broadcast_0 stored as values in memory (estimated size 13.0 KiB, free 434.4 MiB)
23/08/11 10:42:13 INFO MemoryStore: Block broadcast_0_piece0 stored as bytes in memory (estimated size 6.7 KiB, free 434.4 MiB)
23/08/11 10:42:13 INFO BlockManagerInfo: Added broadcast_0_piece0 in memory on THOR.mshome.net:58187 (size: 6.7 KiB, free: 434.4 MiB)
23/08/11 10:42:13 INFO SparkContext: Created broadcast 0 from broadcast at DAGScheduler.scala:1535
23/08/11 10:42:14 INFO DAGScheduler: Submitting 1 missing tasks from ResultStage 0 (MapPartitionsRDD[6] at showString at NativeMethodAccessorImpl.java:0) (first 15 tasks are for partitions Vector(0))
23/08/11 10:42:14 INFO TaskSchedulerImpl: Adding task set 0.0 with 1 tasks resource profile 0
23/08/11 10:42:14 INFO TaskSetManager: Starting task 0.0 in stage 0.0 (TID 0) (THOR.mshome.net, executor driver, partition 0, PROCESS_LOCAL, 7481 bytes) 
23/08/11 10:42:14 INFO Executor: Running task 0.0 in stage 0.0 (TID 0)
23/08/11 10:42:15 INFO PythonRunner: Times: total = 954, boot = 894, init = 60, finish = 0
23/08/11 10:42:15 INFO Executor: Finished task 0.0 in stage 0.0 (TID 0). 2018 bytes result sent to driver
23/08/11 10:42:15 INFO TaskSetManager: Finished task 0.0 in stage 0.0 (TID 0) in 1259 ms on THOR.mshome.net (executor driver) (1/1)
23/08/11 10:42:15 INFO TaskSchedulerImpl: Removed TaskSet 0.0, whose tasks have all completed, from pool 
23/08/11 10:42:15 INFO PythonAccumulatorV2: Connected to AccumulatorServer at host: 127.0.0.1 port: 58188
23/08/11 10:42:15 INFO DAGScheduler: ResultStage 0 (showString at NativeMethodAccessorImpl.java:0) finished in 1.712 s
23/08/11 10:42:15 INFO DAGScheduler: Job 0 is finished. Cancelling potential speculative or zombie tasks for this job
23/08/11 10:42:15 INFO TaskSchedulerImpl: Killing all running tasks in stage 0: Stage finished
23/08/11 10:42:15 INFO DAGScheduler: Job 0 finished: showString at NativeMethodAccessorImpl.java:0, took 1.775401 s
23/08/11 10:42:15 INFO SparkContext: Starting job: showString at NativeMethodAccessorImpl.java:0
23/08/11 10:42:15 INFO DAGScheduler: Got job 1 (showString at NativeMethodAccessorImpl.java:0) with 4 output partitions
23/08/11 10:42:15 INFO DAGScheduler: Final stage: ResultStage 1 (showString at NativeMethodAccessorImpl.java:0)
23/08/11 10:42:15 INFO DAGScheduler: Parents of final stage: List()
23/08/11 10:42:15 INFO DAGScheduler: Missing parents: List()
23/08/11 10:42:15 INFO DAGScheduler: Submitting ResultStage 1 (MapPartitionsRDD[6] at showString at NativeMethodAccessorImpl.java:0), which has no missing parents
23/08/11 10:42:15 INFO MemoryStore: Block broadcast_1 stored as values in memory (estimated size 13.0 KiB, free 434.4 MiB)
23/08/11 10:42:15 INFO MemoryStore: Block broadcast_1_piece0 stored as bytes in memory (estimated size 6.7 KiB, free 434.4 MiB)
23/08/11 10:42:15 INFO BlockManagerInfo: Added broadcast_1_piece0 in memory on THOR.mshome.net:58187 (size: 6.7 KiB, free: 434.4 MiB)
23/08/11 10:42:15 INFO SparkContext: Created broadcast 1 from broadcast at DAGScheduler.scala:1535
23/08/11 10:42:15 INFO DAGScheduler: Submitting 4 missing tasks from ResultStage 1 (MapPartitionsRDD[6] at showString at NativeMethodAccessorImpl.java:0) (first 15 tasks are for partitions Vector(1, 2, 3, 4))
23/08/11 10:42:15 INFO TaskSchedulerImpl: Adding task set 1.0 with 4 tasks resource profile 0
23/08/11 10:42:15 INFO TaskSetManager: Starting task 0.0 in stage 1.0 (TID 1) (THOR.mshome.net, executor driver, partition 1, PROCESS_LOCAL, 7536 bytes) 
23/08/11 10:42:15 INFO TaskSetManager: Starting task 1.0 in stage 1.0 (TID 2) (THOR.mshome.net, executor driver, partition 2, PROCESS_LOCAL, 7481 bytes) 
23/08/11 10:42:15 INFO TaskSetManager: Starting task 2.0 in stage 1.0 (TID 3) (THOR.mshome.net, executor driver, partition 3, PROCESS_LOCAL, 7537 bytes) 
23/08/11 10:42:15 INFO TaskSetManager: Starting task 3.0 in stage 1.0 (TID 4) (THOR.mshome.net, executor driver, partition 4, PROCESS_LOCAL, 7481 bytes) 
23/08/11 10:42:15 INFO Executor: Running task 1.0 in stage 1.0 (TID 2)
23/08/11 10:42:15 INFO Executor: Running task 0.0 in stage 1.0 (TID 1)
23/08/11 10:42:15 INFO Executor: Running task 2.0 in stage 1.0 (TID 3)
23/08/11 10:42:15 INFO Executor: Running task 3.0 in stage 1.0 (TID 4)
23/08/11 10:42:15 INFO BlockManagerInfo: Removed broadcast_0_piece0 on THOR.mshome.net:58187 in memory (size: 6.7 KiB, free: 434.4 MiB)
23/08/11 10:42:16 INFO PythonRunner: Times: total = 909, boot = 833, init = 76, finish = 0
23/08/11 10:42:17 INFO PythonRunner: Times: total = 1783, boot = 1732, init = 51, finish = 0
23/08/11 10:42:18 INFO PythonRunner: Times: total = 2773, boot = 2729, init = 44, finish = 0
23/08/11 10:42:18 INFO Executor: Finished task 3.0 in stage 1.0 (TID 4). 1932 bytes result sent to driver
23/08/11 10:42:18 INFO Executor: Finished task 2.0 in stage 1.0 (TID 3). 2004 bytes result sent to driver
23/08/11 10:42:18 INFO Executor: Finished task 1.0 in stage 1.0 (TID 2). 1932 bytes result sent to driver
23/08/11 10:42:18 INFO TaskSetManager: Finished task 3.0 in stage 1.0 (TID 4) in 3560 ms on THOR.mshome.net (executor driver) (1/4)
23/08/11 10:42:18 INFO TaskSetManager: Finished task 2.0 in stage 1.0 (TID 3) in 3561 ms on THOR.mshome.net (executor driver) (2/4)
23/08/11 10:42:18 INFO TaskSetManager: Finished task 1.0 in stage 1.0 (TID 2) in 3566 ms on THOR.mshome.net (executor driver) (3/4)
23/08/11 10:42:19 INFO PythonRunner: Times: total = 3591, boot = 3526, init = 65, finish = 0
23/08/11 10:42:19 INFO Executor: Finished task 0.0 in stage 1.0 (TID 1). 2043 bytes result sent to driver
23/08/11 10:42:19 INFO TaskSetManager: Finished task 0.0 in stage 1.0 (TID 1) in 3634 ms on THOR.mshome.net (executor driver) (4/4)
23/08/11 10:42:19 INFO TaskSchedulerImpl: Removed TaskSet 1.0, whose tasks have all completed, from pool 
23/08/11 10:42:19 INFO DAGScheduler: ResultStage 1 (showString at NativeMethodAccessorImpl.java:0) finished in 3.649 s
23/08/11 10:42:19 INFO DAGScheduler: Job 1 is finished. Cancelling potential speculative or zombie tasks for this job
23/08/11 10:42:19 INFO TaskSchedulerImpl: Killing all running tasks in stage 1: Stage finished
23/08/11 10:42:19 INFO DAGScheduler: Job 1 finished: showString at NativeMethodAccessorImpl.java:0, took 3.652561 s
23/08/11 10:42:19 INFO SparkContext: Starting job: showString at NativeMethodAccessorImpl.java:0
23/08/11 10:42:19 INFO DAGScheduler: Got job 2 (showString at NativeMethodAccessorImpl.java:0) with 3 output partitions
23/08/11 10:42:19 INFO DAGScheduler: Final stage: ResultStage 2 (showString at NativeMethodAccessorImpl.java:0)
23/08/11 10:42:19 INFO DAGScheduler: Parents of final stage: List()
23/08/11 10:42:19 INFO DAGScheduler: Missing parents: List()
23/08/11 10:42:19 INFO DAGScheduler: Submitting ResultStage 2 (MapPartitionsRDD[6] at showString at NativeMethodAccessorImpl.java:0), which has no missing parents
23/08/11 10:42:19 INFO MemoryStore: Block broadcast_2 stored as values in memory (estimated size 13.0 KiB, free 434.4 MiB)
23/08/11 10:42:19 INFO MemoryStore: Block broadcast_2_piece0 stored as bytes in memory (estimated size 6.7 KiB, free 434.4 MiB)
23/08/11 10:42:19 INFO BlockManagerInfo: Added broadcast_2_piece0 in memory on THOR.mshome.net:58187 (size: 6.7 KiB, free: 434.4 MiB)
23/08/11 10:42:19 INFO SparkContext: Created broadcast 2 from broadcast at DAGScheduler.scala:1535
23/08/11 10:42:19 INFO DAGScheduler: Submitting 3 missing tasks from ResultStage 2 (MapPartitionsRDD[6] at showString at NativeMethodAccessorImpl.java:0) (first 15 tasks are for partitions Vector(5, 6, 7))
23/08/11 10:42:19 INFO TaskSchedulerImpl: Adding task set 2.0 with 3 tasks resource profile 0
23/08/11 10:42:19 INFO TaskSetManager: Starting task 0.0 in stage 2.0 (TID 5) (THOR.mshome.net, executor driver, partition 5, PROCESS_LOCAL, 7540 bytes) 
23/08/11 10:42:19 INFO TaskSetManager: Starting task 1.0 in stage 2.0 (TID 6) (THOR.mshome.net, executor driver, partition 6, PROCESS_LOCAL, 7481 bytes) 
23/08/11 10:42:19 INFO TaskSetManager: Starting task 2.0 in stage 2.0 (TID 7) (THOR.mshome.net, executor driver, partition 7, PROCESS_LOCAL, 7536 bytes) 
23/08/11 10:42:19 INFO Executor: Running task 2.0 in stage 2.0 (TID 7)
23/08/11 10:42:19 INFO Executor: Running task 0.0 in stage 2.0 (TID 5)
23/08/11 10:42:19 INFO Executor: Running task 1.0 in stage 2.0 (TID 6)
23/08/11 10:42:19 INFO PythonRunner: Times: total = 849, boot = 781, init = 68, finish = 0
23/08/11 10:42:20 INFO PythonRunner: Times: total = 1732, boot = 1657, init = 75, finish = 0
23/08/11 10:42:21 INFO Executor: Finished task 0.0 in stage 2.0 (TID 5). 2004 bytes result sent to driver
23/08/11 10:42:21 INFO Executor: Finished task 2.0 in stage 2.0 (TID 7). 2043 bytes result sent to driver
23/08/11 10:42:21 INFO TaskSetManager: Finished task 0.0 in stage 2.0 (TID 5) in 2571 ms on THOR.mshome.net (executor driver) (1/3)
23/08/11 10:42:21 INFO TaskSetManager: Finished task 2.0 in stage 2.0 (TID 7) in 2570 ms on THOR.mshome.net (executor driver) (2/3)
23/08/11 10:42:21 INFO BlockManagerInfo: Removed broadcast_1_piece0 on THOR.mshome.net:58187 in memory (size: 6.7 KiB, free: 434.4 MiB)
23/08/11 10:42:21 INFO PythonRunner: Times: total = 2631, boot = 2549, init = 82, finish = 0
23/08/11 10:42:21 INFO Executor: Finished task 1.0 in stage 2.0 (TID 6). 1932 bytes result sent to driver
23/08/11 10:42:21 INFO TaskSetManager: Finished task 1.0 in stage 2.0 (TID 6) in 2663 ms on THOR.mshome.net (executor driver) (3/3)
23/08/11 10:42:21 INFO TaskSchedulerImpl: Removed TaskSet 2.0, whose tasks have all completed, from pool 
23/08/11 10:42:21 INFO DAGScheduler: ResultStage 2 (showString at NativeMethodAccessorImpl.java:0) finished in 2.677 s
23/08/11 10:42:21 INFO DAGScheduler: Job 2 is finished. Cancelling potential speculative or zombie tasks for this job
23/08/11 10:42:21 INFO TaskSchedulerImpl: Killing all running tasks in stage 2: Stage finished
23/08/11 10:42:21 INFO DAGScheduler: Job 2 finished: showString at NativeMethodAccessorImpl.java:0, took 2.681963 s
23/08/11 10:42:21 INFO CodeGenerator: Code generated in 79.1373 ms
+---------+--------+-------+-----+
|firstname|lastname|country|state|
+---------+--------+-------+-----+
|    James|   Smith|    USA|   CA|
|  Michael|    Rose|    USA|   NY|
|   Robert|Williams|    USA|   CA|
|    Maria|   Jones|    USA|   FL|
+---------+--------+-------+-----+


root
 |-- firstname: string (nullable = true)
 |-- lastname: string (nullable = true)
 |-- country: string (nullable = true)
 |-- state: string (nullable = true)


23/08/11 10:42:21 INFO SparkContext: Starting job: collect at D:\GitHub\DemoDev\dev-topics-bigdata\dev-topics-sparkinstall\examples\smoketest\smoketest.py:53
23/08/11 10:42:21 INFO DAGScheduler: Got job 3 (collect at D:\GitHub\DemoDev\dev-topics-bigdata\dev-topics-sparkinstall\examples\smoketest\smoketest.py:53) with 8 output partitions
23/08/11 10:42:21 INFO DAGScheduler: Final stage: ResultStage 3 (collect at D:\GitHub\DemoDev\dev-topics-bigdata\dev-topics-sparkinstall\examples\smoketest\smoketest.py:53)
23/08/11 10:42:21 INFO DAGScheduler: Parents of final stage: List()
23/08/11 10:42:21 INFO DAGScheduler: Missing parents: List()
23/08/11 10:42:21 INFO DAGScheduler: Submitting ResultStage 3 (MapPartitionsRDD[8] at collect at D:\GitHub\DemoDev\dev-topics-bigdata\dev-topics-sparkinstall\examples\smoketest\smoketest.py:53), which has no missing parents
23/08/11 10:42:21 INFO MemoryStore: Block broadcast_3 stored as values in memory (estimated size 13.0 KiB, free 434.4 MiB)
23/08/11 10:42:21 INFO MemoryStore: Block broadcast_3_piece0 stored as bytes in memory (estimated size 6.7 KiB, free 434.4 MiB)
23/08/11 10:42:21 INFO BlockManagerInfo: Added broadcast_3_piece0 in memory on THOR.mshome.net:58187 (size: 6.7 KiB, free: 434.4 MiB)
23/08/11 10:42:21 INFO SparkContext: Created broadcast 3 from broadcast at DAGScheduler.scala:1535
23/08/11 10:42:21 INFO BlockManagerInfo: Removed broadcast_2_piece0 on THOR.mshome.net:58187 in memory (size: 6.7 KiB, free: 434.4 MiB)
23/08/11 10:42:21 INFO DAGScheduler: Submitting 8 missing tasks from ResultStage 3 (MapPartitionsRDD[8] at collect at D:\GitHub\DemoDev\dev-topics-bigdata\dev-topics-sparkinstall\examples\smoketest\smoketest.py:53) (first 15 tasks are for partitions Vector(0, 1, 2, 3, 4, 5, 6, 7))
23/08/11 10:42:21 INFO TaskSchedulerImpl: Adding task set 3.0 with 8 tasks resource profile 0
23/08/11 10:42:21 INFO TaskSetManager: Starting task 0.0 in stage 3.0 (TID 8) (THOR.mshome.net, executor driver, partition 0, PROCESS_LOCAL, 7481 bytes) 
23/08/11 10:42:21 INFO TaskSetManager: Starting task 1.0 in stage 3.0 (TID 9) (THOR.mshome.net, executor driver, partition 1, PROCESS_LOCAL, 7536 bytes) 
23/08/11 10:42:21 INFO TaskSetManager: Starting task 2.0 in stage 3.0 (TID 10) (THOR.mshome.net, executor driver, partition 2, PROCESS_LOCAL, 7481 bytes) 
23/08/11 10:42:21 INFO TaskSetManager: Starting task 3.0 in stage 3.0 (TID 11) (THOR.mshome.net, executor driver, partition 3, PROCESS_LOCAL, 7537 bytes) 
23/08/11 10:42:21 INFO TaskSetManager: Starting task 4.0 in stage 3.0 (TID 12) (THOR.mshome.net, executor driver, partition 4, PROCESS_LOCAL, 7481 bytes) 
23/08/11 10:42:21 INFO TaskSetManager: Starting task 5.0 in stage 3.0 (TID 13) (THOR.mshome.net, executor driver, partition 5, PROCESS_LOCAL, 7540 bytes) 
23/08/11 10:42:21 INFO TaskSetManager: Starting task 6.0 in stage 3.0 (TID 14) (THOR.mshome.net, executor driver, partition 6, PROCESS_LOCAL, 7481 bytes) 
23/08/11 10:42:21 INFO TaskSetManager: Starting task 7.0 in stage 3.0 (TID 15) (THOR.mshome.net, executor driver, partition 7, PROCESS_LOCAL, 7536 bytes) 
23/08/11 10:42:21 INFO Executor: Running task 0.0 in stage 3.0 (TID 8)
23/08/11 10:42:21 INFO Executor: Running task 2.0 in stage 3.0 (TID 10)
23/08/11 10:42:21 INFO Executor: Running task 1.0 in stage 3.0 (TID 9)
23/08/11 10:42:21 INFO Executor: Running task 3.0 in stage 3.0 (TID 11)
23/08/11 10:42:21 INFO Executor: Running task 4.0 in stage 3.0 (TID 12)
23/08/11 10:42:21 INFO Executor: Running task 6.0 in stage 3.0 (TID 14)
23/08/11 10:42:21 INFO Executor: Running task 7.0 in stage 3.0 (TID 15)
23/08/11 10:42:22 INFO Executor: Running task 5.0 in stage 3.0 (TID 13)
23/08/11 10:42:22 INFO PythonRunner: Times: total = 892, boot = 809, init = 83, finish = 0
23/08/11 10:42:23 INFO PythonRunner: Times: total = 1638, boot = 1581, init = 57, finish = 0
23/08/11 10:42:24 INFO PythonRunner: Times: total = 2452, boot = 2394, init = 58, finish = 0
23/08/11 10:42:25 INFO PythonRunner: Times: total = 3249, boot = 3207, init = 42, finish = 0
23/08/11 10:42:26 INFO PythonRunner: Times: total = 4079, boot = 4028, init = 51, finish = 0
23/08/11 10:42:26 INFO PythonRunner: Times: total = 4969, boot = 4910, init = 59, finish = 0
23/08/11 10:42:27 INFO PythonRunner: Times: total = 5831, boot = 5762, init = 69, finish = 0
23/08/11 10:42:28 INFO Executor: Finished task 4.0 in stage 3.0 (TID 12). 1968 bytes result sent to driver
23/08/11 10:42:28 INFO Executor: Finished task 3.0 in stage 3.0 (TID 11). 2040 bytes result sent to driver
23/08/11 10:42:28 INFO Executor: Finished task 6.0 in stage 3.0 (TID 14). 2011 bytes result sent to driver
23/08/11 10:42:28 INFO Executor: Finished task 2.0 in stage 3.0 (TID 10). 2054 bytes result sent to driver
23/08/11 10:42:28 INFO Executor: Finished task 5.0 in stage 3.0 (TID 13). 2040 bytes result sent to driver
23/08/11 10:42:28 INFO Executor: Finished task 7.0 in stage 3.0 (TID 15). 2036 bytes result sent to driver
23/08/11 10:42:28 INFO TaskSetManager: Finished task 6.0 in stage 3.0 (TID 14) in 6578 ms on THOR.mshome.net (executor driver) (1/8)
23/08/11 10:42:28 INFO Executor: Finished task 0.0 in stage 3.0 (TID 8). 1968 bytes result sent to driver
23/08/11 10:42:28 INFO TaskSetManager: Finished task 4.0 in stage 3.0 (TID 12) in 6580 ms on THOR.mshome.net (executor driver) (2/8)
23/08/11 10:42:28 INFO TaskSetManager: Finished task 3.0 in stage 3.0 (TID 11) in 6581 ms on THOR.mshome.net (executor driver) (3/8)
23/08/11 10:42:28 INFO TaskSetManager: Finished task 5.0 in stage 3.0 (TID 13) in 6580 ms on THOR.mshome.net (executor driver) (4/8)
23/08/11 10:42:28 INFO TaskSetManager: Finished task 2.0 in stage 3.0 (TID 10) in 6582 ms on THOR.mshome.net (executor driver) (5/8)
23/08/11 10:42:28 INFO TaskSetManager: Finished task 7.0 in stage 3.0 (TID 15) in 6580 ms on THOR.mshome.net (executor driver) (6/8)
23/08/11 10:42:28 INFO TaskSetManager: Finished task 0.0 in stage 3.0 (TID 8) in 6587 ms on THOR.mshome.net (executor driver) (7/8)
23/08/11 10:42:28 INFO PythonRunner: Times: total = 6645, boot = 6559, init = 86, finish = 0
23/08/11 10:42:28 INFO Executor: Finished task 1.0 in stage 3.0 (TID 9). 2036 bytes result sent to driver
23/08/11 10:42:28 INFO TaskSetManager: Finished task 1.0 in stage 3.0 (TID 9) in 6677 ms on THOR.mshome.net (executor driver) (8/8)
23/08/11 10:42:28 INFO TaskSchedulerImpl: Removed TaskSet 3.0, whose tasks have all completed, from pool 
23/08/11 10:42:28 INFO DAGScheduler: ResultStage 3 (collect at D:\GitHub\DemoDev\dev-topics-bigdata\dev-topics-sparkinstall\examples\smoketest\smoketest.py:53) finished in 6.691 s
23/08/11 10:42:28 INFO DAGScheduler: Job 3 is finished. Cancelling potential speculative or zombie tasks for this job
23/08/11 10:42:28 INFO TaskSchedulerImpl: Killing all running tasks in stage 3: Stage finished
23/08/11 10:42:28 INFO DAGScheduler: Job 3 finished: collect at D:\GitHub\DemoDev\dev-topics-bigdata\dev-topics-sparkinstall\examples\smoketest\smoketest.py:53, took 6.693970 s
dataframe : [Row(firstname='James', lastname='Smith', country='USA', state='CA'), Row(firstname='Michael', lastname='Rose', country='USA', state='NY'), Row(firstname='Robert', lastname='Williams', country='USA', state='CA'), Row(firstname='Maria', lastname='Jones', country='USA', state='FL')]
partitions: 8

***************************************
**** Write DataFrame as a CSV file ****
***************************************
  -- File: /tmp/spark_output/smoketest
23/08/11 10:42:29 INFO FileOutputCommitter: File Output Committer Algorithm version is 1
23/08/11 10:42:29 INFO FileOutputCommitter: FileOutputCommitter skip cleanup _temporary folders under output directory:false, ignore cleanup failures: false
23/08/11 10:42:29 INFO SQLHadoopMapReduceCommitProtocol: Using output committer class org.apache.hadoop.mapreduce.lib.output.FileOutputCommitter
23/08/11 10:42:29 INFO SparkContext: Starting job: save at NativeMethodAccessorImpl.java:0
23/08/11 10:42:29 INFO DAGScheduler: Got job 4 (save at NativeMethodAccessorImpl.java:0) with 1 output partitions
23/08/11 10:42:29 INFO DAGScheduler: Final stage: ResultStage 4 (save at NativeMethodAccessorImpl.java:0)
23/08/11 10:42:29 INFO DAGScheduler: Parents of final stage: List()
23/08/11 10:42:29 INFO DAGScheduler: Missing parents: List()
23/08/11 10:42:29 INFO DAGScheduler: Submitting ResultStage 4 (MapPartitionsRDD[15] at save at NativeMethodAccessorImpl.java:0), which has no missing parents
23/08/11 10:42:29 INFO MemoryStore: Block broadcast_4 stored as values in memory (estimated size 215.8 KiB, free 434.2 MiB)
23/08/11 10:42:29 INFO MemoryStore: Block broadcast_4_piece0 stored as bytes in memory (estimated size 78.4 KiB, free 434.1 MiB)
23/08/11 10:42:29 INFO BlockManagerInfo: Added broadcast_4_piece0 in memory on THOR.mshome.net:58187 (size: 78.4 KiB, free: 434.3 MiB)
23/08/11 10:42:29 INFO SparkContext: Created broadcast 4 from broadcast at DAGScheduler.scala:1535
23/08/11 10:42:29 INFO DAGScheduler: Submitting 1 missing tasks from ResultStage 4 (MapPartitionsRDD[15] at save at NativeMethodAccessorImpl.java:0) (first 15 tasks are for partitions Vector(0))
23/08/11 10:42:29 INFO TaskSchedulerImpl: Adding task set 4.0 with 1 tasks resource profile 0
23/08/11 10:42:29 INFO TaskSetManager: Starting task 0.0 in stage 4.0 (TID 16) (THOR.mshome.net, executor driver, partition 0, PROCESS_LOCAL, 8158 bytes) 
23/08/11 10:42:29 INFO Executor: Running task 0.0 in stage 4.0 (TID 16)
23/08/11 10:42:29 INFO FileOutputCommitter: File Output Committer Algorithm version is 1
23/08/11 10:42:29 INFO FileOutputCommitter: FileOutputCommitter skip cleanup _temporary folders under output directory:false, ignore cleanup failures: false
23/08/11 10:42:29 INFO SQLHadoopMapReduceCommitProtocol: Using output committer class org.apache.hadoop.mapreduce.lib.output.FileOutputCommitter
23/08/11 10:42:30 INFO PythonRunner: Times: total = 939, boot = 896, init = 43, finish = 0
23/08/11 10:42:31 INFO PythonRunner: Times: total = 919, boot = 870, init = 49, finish = 0
23/08/11 10:42:32 INFO PythonRunner: Times: total = 907, boot = 868, init = 39, finish = 0
23/08/11 10:42:33 INFO PythonRunner: Times: total = 837, boot = 786, init = 51, finish = 0
23/08/11 10:42:34 INFO PythonRunner: Times: total = 886, boot = 839, init = 47, finish = 0
23/08/11 10:42:35 INFO PythonRunner: Times: total = 893, boot = 817, init = 76, finish = 0
23/08/11 10:42:36 INFO PythonRunner: Times: total = 991, boot = 949, init = 42, finish = 0
23/08/11 10:42:37 INFO PythonRunner: Times: total = 932, boot = 892, init = 40, finish = 0
23/08/11 10:42:37 INFO FileOutputCommitter: Saved output of task 'attempt_2023081110422926117971990072717_0004_m_000000_16' to file:/tmp/spark_output/smoketest/_temporary/0/task_2023081110422926117971990072717_0004_m_000000
23/08/11 10:42:37 INFO SparkHadoopMapRedUtil: attempt_2023081110422926117971990072717_0004_m_000000_16: Committed. Elapsed time: 4 ms.
23/08/11 10:42:37 INFO Executor: Finished task 0.0 in stage 4.0 (TID 16). 3176 bytes result sent to driver
23/08/11 10:42:37 INFO TaskSetManager: Finished task 0.0 in stage 4.0 (TID 16) in 7770 ms on THOR.mshome.net (executor driver) (1/1)
23/08/11 10:42:37 INFO TaskSchedulerImpl: Removed TaskSet 4.0, whose tasks have all completed, from pool 
23/08/11 10:42:37 INFO DAGScheduler: ResultStage 4 (save at NativeMethodAccessorImpl.java:0) finished in 7.894 s
23/08/11 10:42:37 INFO DAGScheduler: Job 4 is finished. Cancelling potential speculative or zombie tasks for this job
23/08/11 10:42:37 INFO TaskSchedulerImpl: Killing all running tasks in stage 4: Stage finished
23/08/11 10:42:37 INFO DAGScheduler: Job 4 finished: save at NativeMethodAccessorImpl.java:0, took 7.898238 s
23/08/11 10:42:37 INFO FileFormatWriter: Start to commit write Job 9e532dfd-ab5c-42ae-bc62-5b681b578a95.
23/08/11 10:42:37 INFO FileFormatWriter: Write Job 9e532dfd-ab5c-42ae-bc62-5b681b578a95 committed. Elapsed time: 27 ms.
23/08/11 10:42:37 INFO FileFormatWriter: Finished processing stats for write job 9e532dfd-ab5c-42ae-bc62-5b681b578a95.
*** DataFrame created as a CSV file ***
***************************************
23/08/11 10:42:37 INFO SparkContext: SparkContext is stopping with exitCode 0.
23/08/11 10:42:37 INFO SparkUI: Stopped Spark web UI at http://THOR.mshome.net:4041
23/08/11 10:42:37 INFO MapOutputTrackerMasterEndpoint: MapOutputTrackerMasterEndpoint stopped!
23/08/11 10:42:37 INFO MemoryStore: MemoryStore cleared
23/08/11 10:42:37 INFO BlockManager: BlockManager stopped
23/08/11 10:42:37 INFO BlockManagerMaster: BlockManagerMaster stopped
23/08/11 10:42:37 INFO OutputCommitCoordinator$OutputCommitCoordinatorEndpoint: OutputCommitCoordinator stopped!
23/08/11 10:42:37 INFO SparkContext: Successfully stopped SparkContext

Smoke Test2 Done

Smoke Test 3: Read a DataFrame
23/08/11 10:42:38 INFO SparkContext: Running Spark version 3.4.1
23/08/11 10:42:38 WARN SparkConf: Note that spark.local.dir will be overridden by the value set by the cluster manager (via SPARK_LOCAL_DIRS in mesos/standalone/kubernetes and LOCAL_DIRS in YARN).
23/08/11 10:42:38 INFO ResourceUtils: ==============================================================
23/08/11 10:42:38 INFO ResourceUtils: No custom resources configured for spark.driver.
23/08/11 10:42:38 INFO ResourceUtils: ==============================================================
23/08/11 10:42:38 INFO SparkContext: Submitted application: CollectedSmokeTests3
23/08/11 10:42:38 INFO ResourceProfile: Default ResourceProfile created, executor resources: Map(cores -> name: cores, amount: 1, script: , vendor: , memory -> name: memory, amount: 1024, script: , vendor: , offHeap -> name: offHeap, amount: 0, script: , vendor: ), task resources: Map(cpus -> name: cpus, amount: 1.0)
23/08/11 10:42:38 INFO ResourceProfile: Limiting resource is cpu
23/08/11 10:42:38 INFO ResourceProfileManager: Added ResourceProfile id: 0
23/08/11 10:42:38 INFO SecurityManager: Changing view acls to: Don
23/08/11 10:42:38 INFO SecurityManager: Changing modify acls to: Don
23/08/11 10:42:38 INFO SecurityManager: Changing view acls groups to: 
23/08/11 10:42:38 INFO SecurityManager: Changing modify acls groups to: 
23/08/11 10:42:38 INFO SecurityManager: SecurityManager: authentication disabled; ui acls disabled; users with view permissions: Don; groups with view permissions: EMPTY; users with modify permissions: Don; groups with modify permissions: EMPTY
23/08/11 10:42:38 INFO Utils: Successfully started service 'sparkDriver' on port 58407.
23/08/11 10:42:38 INFO SparkEnv: Registering MapOutputTracker
23/08/11 10:42:38 INFO SparkEnv: Registering BlockManagerMaster
23/08/11 10:42:38 INFO BlockManagerMasterEndpoint: Using org.apache.spark.storage.DefaultTopologyMapper for getting topology information
23/08/11 10:42:38 INFO BlockManagerMasterEndpoint: BlockManagerMasterEndpoint up
23/08/11 10:42:38 INFO SparkEnv: Registering BlockManagerMasterHeartbeat
23/08/11 10:42:38 INFO DiskBlockManager: Created local directory at D:\Temp\blockmgr-31c9d81c-79fc-463b-b222-bb9ca36409c7
23/08/11 10:42:38 INFO MemoryStore: MemoryStore started with capacity 434.4 MiB
23/08/11 10:42:38 INFO SparkEnv: Registering OutputCommitCoordinator
23/08/11 10:42:38 INFO JettyUtils: Start Jetty 0.0.0.0:4040 for SparkUI
23/08/11 10:42:38 WARN Utils: Service 'SparkUI' could not bind on port 4040. Attempting port 4041.
23/08/11 10:42:38 INFO Utils: Successfully started service 'SparkUI' on port 4041.
23/08/11 10:42:38 INFO Executor: Starting executor ID driver on host THOR.mshome.net
23/08/11 10:42:38 INFO Executor: Starting executor with user classpath (userClassPathFirst = false): ''
23/08/11 10:42:38 INFO Utils: Successfully started service 'org.apache.spark.network.netty.NettyBlockTransferService' on port 58408.
23/08/11 10:42:38 INFO NettyBlockTransferService: Server created on THOR.mshome.net:58408
23/08/11 10:42:38 INFO BlockManager: Using org.apache.spark.storage.RandomBlockReplicationPolicy for block replication policy
23/08/11 10:42:38 INFO BlockManagerMaster: Registering BlockManager BlockManagerId(driver, THOR.mshome.net, 58408, None)
23/08/11 10:42:38 INFO BlockManagerMasterEndpoint: Registering block manager THOR.mshome.net:58408 with 434.4 MiB RAM, BlockManagerId(driver, THOR.mshome.net, 58408, None)
23/08/11 10:42:38 INFO BlockManagerMaster: Registered BlockManager BlockManagerId(driver, THOR.mshome.net, 58408, None)
23/08/11 10:42:38 INFO BlockManager: Initialized BlockManager: BlockManagerId(driver, THOR.mshome.net, 58408, None)
23/08/11 10:42:38 INFO SparkContext: SparkContext is stopping with exitCode 0.
23/08/11 10:42:38 INFO SparkUI: Stopped Spark web UI at http://THOR.mshome.net:4041
23/08/11 10:42:38 INFO MapOutputTrackerMasterEndpoint: MapOutputTrackerMasterEndpoint stopped!
23/08/11 10:42:38 INFO MemoryStore: MemoryStore cleared
23/08/11 10:42:38 INFO BlockManager: BlockManager stopped
23/08/11 10:42:38 INFO BlockManagerMaster: BlockManagerMaster stopped
23/08/11 10:42:38 INFO OutputCommitCoordinator$OutputCommitCoordinatorEndpoint: OutputCommitCoordinator stopped!
23/08/11 10:42:38 INFO SparkContext: Successfully stopped SparkContext
PySpark Smoke Tests Done.
23/08/11 10:42:39 INFO ShutdownHookManager: Shutdown hook called
23/08/11 10:42:39 INFO ShutdownHookManager: Deleting directory D:\Temp\spark-7b4d9f2d-0972-4e0c-9ddf-8b4149f9a939\pyspark-c9b4737e-f9b6-46ef-8712-1f9532f69de4
23/08/11 10:42:39 INFO ShutdownHookManager: Deleting directory D:\Temp\spark-7b4d9f2d-0972-4e0c-9ddf-8b4149f9a939\pyspark-197cb47c-ea6a-4261-b210-3f14cd5b0df3
23/08/11 10:42:39 INFO ShutdownHookManager: Deleting directory C:\Users\Don\AppData\Local\Temp\spark-30e2eb64-a699-4afa-83e0-53b3474ebf6d
23/08/11 10:42:39 INFO ShutdownHookManager: Deleting directory D:\Temp\spark-7b4d9f2d-0972-4e0c-9ddf-8b4149f9a939\pyspark-1ac0eda6-4122-4a16-8150-d5ad6332d853
23/08/11 10:42:39 INFO ShutdownHookManager: Deleting directory D:\Temp\spark-7b4d9f2d-0972-4e0c-9ddf-8b4149f9a939

 Smoke Tests Script Done

