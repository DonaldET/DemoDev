#Welcome

These small _DemoDev_ projects illustrate useful techniques for building Java and J2EE related applications.  They use Maven to build the project artifacts and JUnit to test artifacts.  The _root_ parent project defines common MAVEN dependencies and versions. Nested child projects are located under the parent project, and individual plugins and _localized_ dependencies are included in child projects.  A child project may be a parent of lower level child projects forming a tree.

_Source Repository_: [Code and **README.md**](http://www.github.com/DonaldET/DemoDev/)
_Source Documentation_: [Built from the **README.md** file](http://donaldet.github.io/DemoDev/)

##Sub-Project Categories

*DemoDev* is broken into multiple sub-project with each sub-project having a README.md file offering information about the sub-project and its contents.  Sub-projects include:
- *codingexams*: Questions asked over a whiteboard or in a collaboration environment.
- *hacks*: Unusual solutions to interesting problems that don't necessarily reflect so-called _best practices_.
- *jerseyservices*: RESTful service implementations
- *utils*: Utilities used by _DemoDev_


##Sub-Project Structure

Sub-projects usually include a “business” motivation for the coding example.  The examples also show good _OO design principals_, such as *KISS*, *DRY* (don’t repeat yourself), and *SOLID* (Single responsibility, Open-closed, Liskov substitution, Interface segregation and Dependency inversion.)

The sub-project solution collaborators have their _JavaDoc_ entries explaining their role in the solution and the portion of the problem they address. All sub-projects include ‘JUnit’ tests because this reflects good _TDD_ practice and allows maintenance of the examples over time.  This is accomplished by using the regression aspect of these tests. Many of the projects appear _complicated_ by use of the *Spring* framework.  This is done to illustrate _Spring_ usage in addition to the basic technique shown in the example. Overall, a professional Software Engineer would provide background for the code, some guidelines on usage in the _JavaDoc_ entries, and tips to help maintain the code.  Example invocation with unit tests help in achieving these goals.

#Getting Started

There are several useful tools in the _StaticResources_ directory.  This includes MAVEN archetypes for generating projects; various scripts for maintaining documentation, and instructions on setting up a build environment (**BuildResources.pdf**).
##Sub-Project Coding Exams

These projects are elaborations on topics discussed during interviews and “take home” exams.  These projects demonstrate a “professional engineering” approach to simple coding questions asked during the interview.  Solutions include MAVEN builds, JUnit tests, and JavaDoc.

These also demonstrate coding principles like:
- Separation of interface and implementation
- Use of multiple implementations (Strategy and Command Patterns)

#Selected Exam Content

Client | Project | Description
------ | ------- | -----------
Apple | _compounditerator_ | Concatenate data sources with a list of iterators |
Apple | _factorial_ | Contrast two factorial algorithms
