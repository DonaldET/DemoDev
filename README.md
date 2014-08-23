#Welcome

These small _DemoDev_ projects illustrate useful techniques for building Java and J2EE related applications.  They use Maven to build the project artifacts and JUnit to test artifacts.  The _root_ parent project defines common MAVEN dependencies and versions. Nested child projects are located under the top-level parent project.  A child project may be a parent of lower level child projects forming a tree. Shared plugins and _localized_ dependencies are included in parent projects and sometimes overridden in child projects.

Please contact _Donald Trummell_ via Email using dtrummell@gmail.com.  You may see his detailed profile [here](http://www.linkedin.com/in/donaldtrummell/).

#Getting Started

The _StaticResources_ directory contains instructions on how to setup a MAVEN and JAVA build environment (see document _BuildResources.pdf_.) This **GITHUB** hosted document contains links to the source code repository holding _DonaldET\DemoDev_.

##Sub-Project Categories

*DemoDev* is broken into multiple sub-project with each sub-project having a README.md file offering information about the sub-project and its contents.  Sub-projects include:
- *codingexams*: Codind and Algorithm questions asked over a whiteboard or in a collaboration environment.
- *hacks*: Unusual solutions to interesting problems that don't necessarily reflect so-called _best practices_.
- *jerseyservices*: RESTful service implementations using the **Jersey framework**
- *memoryindexing*: In-memory search algorithms
- *utils*: Utilities used by _DemoDev_


##Sub-Project Structure

Sub-projects usually include a “business” motivation for the coding example.  The examples also show good _OO design principals_, such as *KISS*, *DRY* (don’t repeat yourself), and *SOLID* (Single responsibility, Open-closed, Liskov substitution, Interface segregation and Dependency inversion.)

The sub-project solution collaborators have their _JavaDoc_ entries explaining their role in the solution and the portion of the problem they address. All sub-projects include ‘JUnit’ tests because this reflects good _TDD_ practice and allows maintenance of the examples over time.  This is accomplished by using the regression aspect of these tests. Many of the projects appear _complicated_ by use of the *Spring* framework.  This is done to illustrate _Spring_ usage in addition to the basic technique shown in the example. Overall, a professional Software Engineer would provide background for the code, some guidelines on usage in the _JavaDoc_ entries, and tips to help maintain the code.  Example invocation with unit tests help in achieving these goals.
##Sub-Project Coding Exams

These coding exams are elaborations on topics discussed during interviews and “take home” coding problems.  The resulting solutions demonstrate a “professional engineering” approach to simple coding questions asked during an interview.  Solutions include MAVEN builds, JUnit tests, and JavaDoc.

These solutions also demonstrate coding principles like:
- Separation of interface and implementation
- Use of multiple implementations (Strategy and Command Patterns)
- JUnit tests where feasible

###Selected _Exam_ Content

Client | Project | Description
------ | ------- | -----------
Apple | _compounditerator_ | Concatenate data sources with a list of iterators
Apple | _factorial_ | Contrast two factorial algorithms
Elance | _diagonal_ | Square matrix diagonal elements list generator
Gap | _gap cart_ | A sample shopping cart implementation with pricing strategies
GE | _ gesail twowriters_ | Two threads alternately incrementing a counter; includes unit tests and a runnable jar

##Sub-Project Hacks

These short examples demonstrate _“less than the best”_ coding practices that are sometimes used to get around a solution constraint until a better solution can be found.

###Selected _Hacks_ Content

Project | Description
------- | -----------
_serialhack_ | Use serialization to assign value to a private field lacking a setter
##Sub-Project Memory Indexing

These examples demonstrate in-memory searching algorithms that stress simple approaches where possible.

###Selected _MemoryIndexing_ Content

Project | Description
------- | -----------
_simple linear search_ | Use specially formatted names and attributes and perform “_brute_” linear search . . . it is both simple and fast
##Build Information

There are 24 project _pom.xml_ files, including several parent POMs.  These projects have 71 JAVA source files, totaling over 8,100 lines of code, and will produce 79 class files after the build completes.  Here is the reactor summary:

[INFO] ------------------------------------------------------------------------
[INFO] Reactor Summary:

[INFO] Demonstration of Development _TOPICS_ .............. SUCCESS [  2.125 s]

[INFO] Demonstration of Development _UTILS_ ............... SUCCESS [  0.024 s]

[INFO] Testing Utilities .................................. SUCCESS [ 15.420 s]

[INFO] Demonstration of Development _HACKS_ ............... SUCCESS [  0.023 s]

[INFO] Serial Hack and stand-alone Runner ................. SUCCESS [  1.360 s]

[INFO] Demonstration of Development _JERSEY_ .............. SUCCESS [  0.143 s]

[INFO] Safe Collection Service ............................ SUCCESS [  7.882 s]

[INFO] Safe Collection Service War ........................ SUCCESS [  3.853 s]

[INFO] Demonstration of Development _CODINGEXAMS_ ......... SUCCESS [  0.021 s]

[INFO] Apple - Compound Iterator .......................... SUCCESS [  5.553 s]

[INFO] Apple - Factorial Calculator ....................... SUCCESS [  1.441 s]

[INFO] Elance - Matrix Diagonal Lister .................... SUCCESS [  6.470 s]

[INFO] Gap - Shopping Cart ................................ SUCCESS [  1.275 s]

[INFO] Demonstration of Development _MEMORYINDEX_ ......... SUCCESS [  0.038 s]

[INFO] Memory Index - Simple Linear Search ................ SUCCESS [  2.441 s]

[INFO] ------------------------------------------------------------------------

[INFO] BUILD SUCCESS

[INFO] ------------------------------------------------------------------------

[INFO] Total time: 48.575 s

[INFO] Finished at: 2014-08-18T11:51:57-07:00

[INFO] Final Memory: 25M/82M

[INFO] ------------------------------------------------------------------------
