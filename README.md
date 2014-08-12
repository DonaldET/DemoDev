#Welcome

These small _DemoDev_ projects illustrate useful techniques for building Java and J2EE related applications.  They use Maven to build the project artifacts and JUnit to test artifacts.  The _root_ parent project defines common MAVEN dependencies and versions. Nested child projects are located under the top-level parent project.  A child project may be a parent of lower level child projects forming a tree. Shared plugins and _localized_ dependencies are included in parent projects and sometimes overridden in child projects.

Please contact _Donald Trummell_ via Email using dtrummell@gmail.com.  You may see his detailed profile [here](http://www.linkedin/in/donaldtrummell/).

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

#Getting Started

There are several useful tools in the _StaticResources_ directory.  This includes MAVEN archetypes for generating projects; various scripts for maintaining documentation, and instructions on setting up a build environment in **BuildResources.pdf**.
##Sub-Project _Coding Exams_

These coding exams are elaborations on topics discussed during interviews and “take home” coding problems.  The resulting solutions demonstrate a “professional engineering” approach to simple coding questions asked during an interview.  Solutions include MAVEN builds, JUnit tests, and JavaDoc.

These solutions also demonstrate coding principles like:
- Separation of interface and implementation
- Use of multiple implementations (Strategy and Command Patterns)
- JUnit tests where feasible

#Selected Exam Content

Client | Project | Description
------ | ------- | -----------
Apple | _compounditerator_ | Concatenate data sources with a list of iterators
Apple | _factorial_ | Contrast two factorial algorithms
Elance | _diagonal_ | Square matrix diagonal elements list generator
Gap | _gap cart_ | A sample shopping cart implementation with pricing strategies
##Sub-Project Hacks

These short examples demonstrate _“less than the best”_ coding practices that are sometimes used to get around a solution constraint until a better solution can be found.

#Selected _Hacks_ Content

Project | Description
------- | -----------
_serialhack_ | Use serialization to assign value to a private field lacking a setter
##Sub-Project Memory Indexing

These examples demonstrate in-memory searching algorithms that stress simple approaches where possible.

#Selected _MemoryIndexing_ Content

Project | Description
------- | -----------
_simple linear search_ | Use specially formatted names and attributes and perform “_brute_” linear search . . . it is both simple and fast
