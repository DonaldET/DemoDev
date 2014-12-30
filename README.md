#Welcome

These small _DemoDev_ projects illustrate useful techniques for building Java and J2EE related applications.  This collection of projects is the nuclease of a book on Java programming.

The projects use Maven to build and JUnit test artifacts.  The _root_ parent project defines common MAVEN dependencies and versions. Nested child projects are located under the top-level parent project.  A child project may be a parent of lower level child projects forming a tree. Shared plugins and _localized_ dependencies are included in parent projects and sometimes overridden in child projects.

Please contact _Donald Trummell_ via Email using dtrummell@gmail.com.  You may see his detailed profile [here](http://www.linkedin.com/in/donaldtrummell/).

#Getting Started

All the information needed to build these projects is detailed in the _BuildResources_ document described next.  The process you will follow is:
 1. Download required dependencies (e.g., Java and MAVEN)
 2. Install and test the dependencies
 3. Download the source code from the GitHub repository (see [http://www.github.com/DonaldET/DemoDev/](http://www.github.com/DonaldET/DemoDev/)
 4. ON the command line, from the downloaded directory, execute:
    _mvn clean install_

The _StaticResources_ directory contains instructions on how to setup a MAVEN and JAVA build environment (see document _BuildResources.pdf_.) This **GITHUB** hosted document contains links to the source code repository holding _DonaldET\DemoDev_.

##Sub-Project Categories

*DemoDev* is broken into multiple sub-project with each sub-project having a README.md file offering information about the sub-project and its contents.  Sub-projects include:
- *algorithms*: Math algorithm demonstrations.
- *codingexams*: Codind and Algorithm interview questions asked over a whiteboard or in a collaboration environment.
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
Amazon | _codingchallange_ | Create a recommendation feature (recommendation engine) called “Games Your Friends Play”. The recommendation logic is based on the following rules: a customer should only be recommended games that their friends own but they don’t; the recommendations priority is driven by how many friends own a game - if multiple friends own a particular game, it should be higher in the recommendations than a game that only one friend owns
Amazon | _invoiceparser_ | Scan an Amazon EBook invoice and extract date, title, type and amount of book purchase.
Appdynamics | _top X query_ | Query the top 10 game scores using a priority queue; note that this is in sub-project _MemoryIndexing_.
Apple | _compounditerator_ | Concatenate data sources using a list of iterators for each data source
Apple | _factorial_ | Contrast two factorial algorithms
Elance | _diagonal_ | Square matrix diagonal elements list generator
Gap | _gap cart_ | A sample shopping cart implementation with pricing strategies
GE | _gesail twowriters_ | Has two threads alternately incrementing a counter; uses multiple counter implementations, and includes unit tests and a runnable jar
GE | _geturner binarysearch_ | compare a recursive and iterative solution to the classical binary search algorithm; includes example of the _template pattern_.

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
_top X query_| Compares the performance of priority queue and sorted array based scanners on "top X" queries across a random collection of Game instances. Game instances have an ascending comparator that allows the instances to be ordered on several attributes. The "Top X” query returns a sorted collection of the desired subset of Game instances.

This example uses the motivation of finding the (say) top 10 scores of a complex game where several game attributes allow for unique ordering. We vary the number of inputs to scan and the size of the top set. Since 100 elements is a large list, that is our upper limit for this performance test.

_topx-query_ | Compare a _PriorityQueue_ and simple sorted-array based scanner that captures the “top X” elements of a list of “game” instances.
##Sub-Project NoSQL

These examples demonstrate NoSQL persistence.

###Selected _NoSQL_ Content

Project | Description
------- | -----------
__TBD__ | __TBD__
.
##Build Information

  **** Count of Repository Development Files and Resources

 MAVEN POM files: 19

 Class Files    : 0

 Archive Files  : 0

 WAR Files      : 0

 Java files     : 115

 Java lines     : 12396

 Java Test lines: 3880

 XML files      : 43


##MAVEN Summary

[INFO] ------------------------------------------------------------------------

[INFO] Reactor Summary:

[INFO]

[INFO] Demonstration of Development _TOPICS_ .............. SUCCESS [  0.218 s]

[INFO] Demonstration of Development _UTILS_ ............... SUCCESS [  0.012 s]

[INFO] Testing Utilities .................................. SUCCESS [  2.481 s]

[INFO] Demonstration of Development _HACKS_ ............... SUCCESS [  0.019 s]

[INFO] Serial Hack and stand-alone Runner ................. SUCCESS [  0.851 s]

[INFO] Demonstration of Development _JERSEY_ .............. SUCCESS [  0.015 s]

[INFO] Safe Collection Service ............................ SUCCESS [  2.096 s]

[INFO] Safe Collection Service War ........................ SUCCESS [  1.100 s]

[INFO] Demonstration of Development _CODINGEXAMS_ ......... SUCCESS [  0.017 s]

[INFO] Apple - Compound Iterator .......................... SUCCESS [  3.625 s]

[INFO] Apple - Factorial Calculator ....................... SUCCESS [  1.462 s]

[INFO] Elance - Matrix Diagonal Lister .................... SUCCESS [  0.944 s]

[INFO] Gap - Shopping Cart Example ........................ SUCCESS [  0.981 s]

[INFO] GE-Sail - Two Writers .............................. SUCCESS [  1.344 s]

[INFO] GE-Turner - Binary Search .......................... SUCCESS [  1.766 s]

[INFO] LumenData - Duplicate Column Checker ............... SUCCESS [  1.104 s]

[INFO] Demonstration of Development _MEMORYINDEX_ ......... SUCCESS [  0.015 s]

[INFO] Memory Index - Simple Linear Search ................ SUCCESS [  1.890 s]

[INFO] Memory Index - PriorityQueue to capture Top X values SUCCESS [  4.459 s]

[INFO] ------------------------------------------------------------------------

[INFO] BUILD SUCCESS

[INFO] ------------------------------------------------------------------------

[INFO] Total time: 24.634 s

[INFO] Finished at: 2014-09-02T16:32:30-07:00

[INFO] Final Memory: 28M/113M

[INFO] ------------------------------------------------------------------------
