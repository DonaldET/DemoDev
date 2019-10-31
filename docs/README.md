# Welcome

These _DemoDev_ projects illustrate useful techniques for building Java, Service Oriented Architecture and JEE related applications.  This collection of Java projects is the nucleus of an upcoming book on Java programming. Various object-oriented and functional techniques are illustrated with real examples. Finally, a collection of coding questions is included to illustrate assessment of Java coding skills.

Algorithms and performance testing approaches are included in the repository, as well as ready-to-use utility programs and libraries. Potential usage is demonstrated in a DevOps sub-project collection of projects.

#### Building Projects in the Repository

The Java projects use Maven to build and JUnit test artifacts for regression testing.  a Maven project is defined by a "***pom.xml***" file, and projects may be nested in a tree structure. The _root_ parent project defines common MAVEN dependencies and versions. Nested child projects are located under the top-level parent project.  A child project may be a parent of lower level child projects forming a tree of related projects sharing common Maven dependencies. Maven shared plugins and _localized_ dependencies are included in parent projects and optionally overridden in child projects. there are far too many Maven tutorials to mention here, so just search the web for one that helps you.

#### Java Requirement

Most of DemoDev is built using Java 8, but Java 9 introduced the REPL (Run-Execute-Print-Loop); an interactive Java runtime environment. Many of us are still tied to Java 8 for business reasons, but would like to experiment with the REPL. This site, https://github.com/javaterminal/tryjshell, offers a browser-accessible version of the REPL. They provide a hosted version at https://tryjshell.org/.

#### Java and Python Coding Practice, Data Management Free Resources 

Here are some educational web sites for testing your Java and Python coding skills:

- [https://www.hackerrank.com/](https://www.hackerrank.com/ "Hacker Rank"), test problems and competitive scoring.
- [https://leetcode.com/](https://leetcode.com/ "Leet Code"), presenting problems and solutions in a peer-reviewed development environment.
- [https://coderpad.io/](https://coderpad.io/ "Coder Pad"), a white-board development environment for coding exams.
- https://app.codility.com/demo/take-sample-test/, like coderpad, codility is a testing environment offering practice problems.
- [https://www.pramp.com/#/](https://www.pramp.com/#/ "Pramp"), an interactive coding interview environment for practicing coding interviews.

Additional support for testing SQL programming, which is heavily used in Data Engineering, includes these sites that run MySQL sandboxes:
- SQL runner only: [https://paiza.io/projects/featured?language=mysql](https://paiza.io/projects/featured?language=mysql).
- SQL skills testing: [https://coderpad.io/demo](https://coderpad.io/demo) and the practice sandbox [https://coderpad.io/sandbox](https://coderpad.io/sandbox).

Finally, must of the Python content of this repository is concerned with *Apache Spark*, and a good free Spark implementation is hosted by Databricks (the community edition) found at  https://databricks.com/try-databricks.

#### Commercial Opportunities

Please contact _Donald Trummell_ via Email using dtrummell@gmail.com for additional information regarding commercial use of projects in this repository.  His LinkedIn profile is found at [http://www.linkedin.com/in/donaldtrummell/](http://www.linkedin.com/in/donaldtrummell/ "here"). You may review the published form of this documentation at the "pretty view" [http://donaldet.github.io/DemoDev/](http://donaldet.github.io/DemoDev/).

# Getting Started

All the information needed to build these projects is detailed in the _BuildResources_ document described next.  The process you will follow is:
 1. Download required build dependencies (e.g., Java, MAVEN, and optionally ANT)
 2. Install and test the build dependencies
 3. Fork or download the source code from the GitHub repository (see [http://www.github.com/DonaldET/DemoDev/](http://www.github.com/DonaldET/DemoDev/ "the source repo"))
 4. On the command line, from the downloaded base directory, execute:
    _mvn clean install_

The _StaticResources_ directory contains instructions on how to setup a MAVEN and JAVA build environment (see document _BuildResources.pdf_.) BuildResources.pdf is a **GITHUB** hosted document that also contains links to the source code repository holding _DemoDev_.

## Sub-Project Categories

*DemoDev* is broken into multiple sub-project with each sub-project having a README.md file offering information about the sub-project and its contents.  Interesting sub-projects include:

- *algorithms*: Math algorithm demonstrations.
- *codingexams*: Coding and Algorithm interview questions asked over a whiteboard or in a collaboration environment.
- *hacks*: Unusual solutions to interesting problems that don't necessarily reflect so-called _best practices_.
- *jerseyservices*: RESTful Java service implementations using the **Jersey framework**
- *memoryindexing*: In-memory search algorithms
- *utils*: Utilities used by _DemoDev_ and related projects (code generation used by _WindPower Explorer_)


## Sub-Project Structure

Sub-projects usually include a _business_ motivation for the coding example.  The examples also show good _OO design principals_, such as _KISS_, _DRY_ (don't repeat yourself), and _SOLID_ (Single responsibility, Open-closed, Liskov substitution, Interface segregation and Dependency inversion.) This site violates _YAGNI_ for educational purposes. Spring has a very readable overview of the *Liskov Substitution Principle* (see <https://springframework.guru/principles-of-object-oriented-design/liskov-substitution-principle/>.)

The sub-projects have their _JavaDoc_ entries explaining their role in the solution and the portion of the problem they address. All sub-projects include JUnit tests because this reflects good _TDD_ practice and allows maintenance of the examples over time.  Stability over time is accomplished by using the regression aspect of these tests. Many of the projects appear _complicated_ by use of the *Spring* framework to solve a potentially simple problem.  This is done to illustrate _Spring_ usage in addition to the basic techniques shown in the example. Overall, a professional Software Engineer would provide background for the code, some guidelines on usage in the _JavaDoc_ entries, and tips to help maintain the code.  The example invocations within unit tests help in achieving these goals.
## Sub-Project Math Algorithms

These Mathematical Algorithms show methods of performance testing and demonstrate coding principles like:
- Separation of interface and implementation
- Use of multiple implementations (Strategy and Command Patterns)
- JUnit tests where feasible

### Selected _Math and General Algorithms_ Content

Project | Description
------- | -----------
_badaddr_ | Demonstrates uncontrolled relative errors from floating point addition and the Kahn algorithm for mitigating some of the errors. This is the basis for the article: [https://www.linkedin.com/pulse/just-doesnt-add-up-part-1-donald-trummell/](https://www.linkedin.com/pulse/just-doesnt-add-up-part-1-donald-trummell/). 
_quadradic_ | Example of a Quadradic Equation solver using both direct and iterative methods.
_largenumeric_ | Example of the Kahn algorithm for mitigating some of the errors. 
_nasa-sensor_ | Monitor sensor radiation exposure with multiple algorithms; trade off space and time. This is the basis of the article: [https://www.linkedin.com/pulse/needs-really-fast-donald-trummell/](https://www.linkedin.com/pulse/needs-really-fast-donald-trummell/). 
_sieve_ | Implements the classical method to find primes in an interval using the Sieve of Eratosthenes.

## Sub-Project Coding Exams

These coding exams are elaborations on topics discussed during interviews and "take home" coding problems.  Some problems were from sites offering coding practice. The resulting solutions demonstrate a "professional engineering" approach to simple coding questions asked in these situations.  Solutions presented here include MAVEN builds, JUnit tests, and JavaDoc. Many of these problems were originally done under time constraints and considerable cleanup was required before presentation here.

These solutions also demonstrate coding principles like:
- Separation of interface and implementation.
- Use of multiple implementations (e.g., Strategy and Command Patterns.)
- Test driven development (shown by reviewing testing order; simple initial conditions to complex corner cases.)
- JUnit framework tests where feasible, or custom test cases when created in an interview environment.

### Selected _Exam_ Content

Client | Project | Description
------ | ------- | -----------
Amazon | _codingchallange_ | Create a recommendation feature (recommendation engine) called "Games Your Friends Play". The recommendation logic is based on the following rules: a customer should only be recommended games that their friends own but they don"t; the recommendations priority is driven by how many friends own a game - if multiple friends own a particular game, it should be higher in the recommendations than a game that only one friend owns. 
Amazon | _invoiceparser_ | Scan an Amazon EBook invoice and extract date, title, type and amount of book purchase.
Amazon | _rangeconsolidator_ | Scan a list of integer ranges and merge overlapping ranges, collapsing them into an all-inclusive range. Performance test as well. This is the basis of the article: [https://www.linkedin.com/pulse/lies-damn-algorithm-analysis-donald-trummell/]( https://www.linkedin.com/pulse/lies-damn-algorithm-analysis-donald-trummell/). 
AppDynamics | _top X query_ | Query the top 10 game scores using a priority queue; note that this is in sub-project _MemoryIndexing_.
Apple | _compounditerator_ | Concatenate data sources using a list of iterators for each data source. 
Apple | _factorial_ | Contrast two factorial algorithms. 
Cisco | _sieve_ | Capture the _top X_ elements of a random integer array using four techniques. 
CreditKarma | _list dependencies_ | Given a list of modules names, each with a list of named dependencies, output a list with dependencies of a module appearing before the module itself.
Cruise-Control | _task processor_ | Process tasks sequentially  with parameterizable cooling time and fixed execution times. 
EasyPost | _pick_ | pick create-pick event pairs from an input stream and summarize times by location. 
Elance | _diagonal_ | Square matrix diagonal elements list generator. 
Gap | _gap cart_ | A sample shopping cart implementation with pricing strategies. 
GE | _gesail twowriters_ | Apply two threads alternately incrementing a counter; uses multiple counter implementations, and includes unit tests and a runnable jar. 
GE | _geturner binarysearch_ | Compare a recursive and iterative solution to the classical binary search algorithm; includes example of the _template pattern_. 
Grand Tour | _mars rover_ | Simulate a planetary rover vehicle remove command facility. 
Granular | _Sudoku Game Checker_ | Check rows, columns, and 3x3 cells for duplicates. 
IJSDE | _Problems_ | Programming skills validation examples associated with a job-placement web-site. 
Liveramp | _autoboxing_ | Explores consequences of Java "autoboxing" of primative values, and examines integer comparison in the context of a Java ***Comparator***. This is the basis of the article: [https://www.linkedin.com/pulse/pilot-error-java-autoboxing-donald-trummell/](https://www.linkedin.com/pulse/pilot-error-java-autoboxing-donald-trummell/). 
Liveramp | _bitsearch_ | We must count unique 32 bit client IPs accessing our web-site over some time period (a day let’s say). This is the basis of my article: [https://www.linkedin.com/pulse/test-driven-development-tdd-really-works-donald-trummell-1c/](https://www.linkedin.com/pulse/test-driven-development-tdd-really-works-donald-trummell-1c/). The article show cases **TDD**., and will lead to an article about generating test data (e.g.., see [https://queue.acm.org/detail.cfm?id=3355565](https://queue.acm.org/detail.cfm?id=3355565).) 
Lumendata | _duplicatechecker_ | Finds duplicate column values in a row.
PINTR | _expand_ | Write a function that expands a HashMap to an array. The HashMap contains a key and a value, from which your function will produce an array with the key repeated the respective number of times. Output order of the array does not matter.
Rakuten | _Merge_ | Merge two ordered arrays of integers, in place, using space O(1).
Socotra | _ordered_ | Print by first by frequency, but then by first appearance for tie. 
Trulia | _treesearch_ | Find the "second largest value" in a specially constructed binary tree.
WeWork | _spiral_ | Spiral-print a matrix (from LeetCode).
WeWork | _patterns_ | Examples of Strategy, Factory, Bridge, and Decorator patterns.

## Sub-Project Hacks

These short examples demonstrate _"less than the best"_ coding practices that are sometimes used to get around a solution constraint until a better solution can be found.

### Selected _Hacks_ Content

Project | Description
------- | -----------
_serialhack_ | Use serialization to assign value to a private field lacking a setter
## Sub-Project Memory Indexing

These examples demonstrate in-memory searching algorithms that stress simple approaches where possible.

### Selected _MemoryIndexing_ Content

Project | Description
------- | -----------
_simple linear search_ | Use specially formatted names and attributes and perform "_brute force_" linear search . . . it is both simple and fast
_top X query_| Compares the performance of priority queue and sorted array based scanners on "top X" queries across a random collection of Game instances. Game instances have an ascending comparator that allows the instances to be ordered on several attributes. The "Top X" query returns a sorted collection of the desired subset of Game instances.

The _top X query_ example uses the motivation of finding the (say) top 10 scores of a complex game where several game attributes allow for unique ordering. We vary the number of inputs to scan and the size of the top set. Since 100 elements is a large list, that is our upper limit for this performance test.
## Sub-Project NoSQL

These examples demonstrate NoSQL persistence.

### Selected _NoSQL_ Content

Project | Description
------- | -----------
__TBD__ | __TBD__
## Sub-Project Utilities

These projects are working code used by *DemoDev* and related projects. They illustrate good engineering practices and represent useful utilities.

### Selected _Utilities_ Content

Project | Description
------- | -----------
_DemoGenerator_ | A Freemarker based text generation utility supporting configurable symbol interpolation, composible context/model properties specifications, composable template specifications, and driven by command-line parameters.  This utility is used by _WindPower Explorer_ to create Oozie properties files and HQL table specifications.  This utility is the basis of the DevOps article: [https://www.linkedin.com/pulse/apache-ant-devops-practices-donald-trummell](https://www.linkedin.com/pulse/apache-ant-devops-practices-donald-trummell). 

These utilities are distributed using Java JAR files and invoked as executable Jars.

## Sub-Project DevOps

DevOps projects are not directly used by *DemoDev* and related projects, but illustrate good development environment practices and useful Templates for creating those environments.

### Selected *DevOps* Content

Project | Description
------- | -----------
*Dependencies* | A sample Python project (***myapppy***), with *PyBuilder* and *PyCharm*, is set up to mimic Java development environments (*Maven* and *Eclipse*). The project enables creation of binary deployable artifacts.  This utility is the basis of the DevOps article: [https://www.linkedin.com/pulse/setting-up-python-project-virtual-environment-pycharm-donald-trummell](https://www.linkedin.com/pulse/setting-up-python-project-virtual-environment-pycharm-donald-trummell). 

The DevOps projects are templates meant to be forked (copied) and modified.

## Build Information
Naming and structural conventions, along with collected statistics on Maven build structure.

### Java Package Naming Conventions

Projects have a package structure that reflects potential use outside of the ***DemoDev*** repository ("don") and for repository use only ("demo").

- Packaging Conventions - "don."*:


`D:\GitHub>findstr /srb "package " *.java | find "package don" > pkg_don.log`
`D:\GitHub>find /C "package" pkg_don.log`
`---------- PKG_DON.LOG: 78`

- Packaging Conventions - "demo."*:



`D:\GitHub>findstr /srb "package " *.java | find "package demo" > pkg_demo.log`
`D:\GitHub>find /C "package" pkg_demo.log`
`---------- PKG_DEMO.LOG: 280`

- Packaging Conventions - Other:


`D:\GitHub>findstr /srb "package " *.java | find /V "package don" | find /V "package demo" > pkg_other.log`
`D:\GitHub>find /C "package" pkg_other.log`
`---------- PKG_OTHER.LOG: 0`

### Detailed Build Statistics

**TBD**