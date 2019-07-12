DevOps ANT example

We seek to incrementally build the system described below, customized by deployment target, that creates
a directory of deployable artifacts, and not duplicating generation newly created targets.

Runtime System: A Hive query populates an HDFS file, followed by an extractor program that copies
--------------  the HDFS file to a local file. The target system has an orchestrator, the extractor,
                and generated Hive and Extractor artifacts.

Generator Inputs
----------------
Shared Properties [SP]: property values shared across all targets, conditioned by TP
Target Properties [TP]: property values related to the target, overwritting [SP]
Effective Model   <EM>: Virtual, created within the generator by [TP] overwritting [SP]

Generator Templates and Outputs
-------------------------------
Run Label            [RL]: A runtime label documenting configuration provenance
Hive Variables       [HV]: Hive variable definitions
Hive Query           [HQ]: Hive queries
Extractor properties [EP]: Extractor program properties

This ANT build file combines the source file templates with the [TP] definitions to produce the target
specific versions of [HV], [HQ], and [EP]. In addition, the immutable files are copied into the target
as well. ANT can recognize that a target is already built (e.g., [SP], depended upon by all deployed
artifacts, and is created only once from [TP].

Source System: Dependencies illustrated with arrows.
-------------

         |------> <EM> <--|
         |         ^      |
         |------> [LB] <--|
         |                |
       [HV] [HQ]         [EP]
  

Target System: Template files modifid by the ANT+Generator in build.xml script. The are identified by
-------------  underscores around the names, and pre-existing (constant) artifact names are surrounded
               by braces (copied unchanged from immutable.)

       {Orchestrator - run Hive, then run Extractor}
       [_LB_]
       [_HV_] [_HQ_] {Hive Runner}
       [_EP_] {Extractor}
  
 Resources:
   - Ant On-line user manual: https://ant.apache.org/manual/
   - The DemoDev generation utility: https://github.com/DonaldET/DemoDev/tree/master/dev-topics-generationutils
   - Eclipse Ant integration example: https://community.synopsys.com/s/article/Setting-up-ant-build-for-Java-Workspace-in-Eclipse
   - Freemarker site: https://freemarker.apache.org/
   - Freemarker tutorial-1: http://zetcode.com/java/freemarker/
   - Freemarker tutorial-2: https://www.vogella.com/tutorials/FreeMarker/article.html
   - Freemarker manual: https://freemarker.apache.org/docs/index.html
 