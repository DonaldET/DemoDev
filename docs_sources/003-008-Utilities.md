## Sub-Project Utilities

These projects are working code used by *DemoDev* and related projects. They illustrate good engineering practices and represent useful utilities.

### Selected _Utilities_ Content

Project | Description
------- | -----------
_category-optimizer_ | This is both a Java and Python based library and utility program that finds score boundaries in a collections of scores that optimizes the R-Squared value of the resulting categories. 
_DemoGenerator_ | A Freemarker based text generation utility supporting configurable symbol interpolation, composible context/model properties specifications, composable template specifications, and driven by command-line parameters.  This utility is used by the _WindPowerExplorer_ repository to create Oozie properties files and HQL table specifications.  It is also the basis of the DevOps article: [https://www.linkedin.com/pulse/apache-ant-devops-practices-donald-trummell](https://www.linkedin.com/pulse/apache-ant-devops-practices-donald-trummell). 

**Note**:
These utilities are distributed using Java JAR files and invoked as executable Jars. The Python versions are created by *PyBuilder* and typically distributed as wheel files.
