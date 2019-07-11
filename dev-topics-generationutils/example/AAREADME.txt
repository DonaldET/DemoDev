DevOps ANT example

We seek to incrementally build the following system, customized by deployment target, that creates
a directory of deployable artifacts if any of the dependencies change.

Runtime System: A Hive query populates an HDFS file, followed by an extractor program that copies
--------------  the HDFS file to a local file. The target system has an orchestrator, the extractor,
                and generated Hive and Extractor artifacts.

Target Properties [TP]: property values related to the target
Shared Properties [SP]: property values shared across all targets, conditioned by TP
Hive Variables    [HV]: Hive variable definitions, modified by [TP], [SP]
Hive Query        [HQ]: Hive queries
Extractor propers [EP]: Extractor program properties

This ANT build file combines the source file templates with the [TP] definitions to produce the target
specific versions of [HV], [HQ], and [EP]. In addition, the immutable files are copied into the target
as well. ANT can recognize that a target is already built (e.g., [SP], depended upon by all deployed
artifacts, and is created only once from [TP].

Source System: Dependencies illustrated with arrows.
-------------

         |------> [TP] <--|
         |         ^      |
         |------> [SP] <--|
         |                |
       [HV] [HQ]         [EP]
  

Target System: Modified files constructed by the ANT build.xml script, identified by Underscores, and
-------------  pre-existing (constant) artifacts surrounded by braces (copied from immutable.)

       {Orchestrator - run Hive, then run Extractor}
       [_HV_] [_HQ_] {Hive Runner}
       [_EP_] {Extractor}
  
 Resources:
   - Ant On-line user manual: https://ant.apache.org/manual/
   - The DemoDev generation utility: https://github.com/DonaldET/DemoDev/tree/master/dev-topics-generationutils
   - Eclipse Ant integration example: https://community.synopsys.com/s/article/Setting-up-ant-build-for-Java-Workspace-in-Eclipse
