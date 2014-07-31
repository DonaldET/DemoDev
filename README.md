##Project Intent

These small _DemoDev_ projects illustrate useful techniques for building Java and J2EE related applications.  They use Maven to build the project artifacts and JUnit to test artifacts.  The _uber_ parent project defines common dependencies and versions. Child projects are located under the parent project, and individual plugins and _localized_ dependencies are included in child projects.

##Sub-Project Categories

*DemoDev* is broken into multiple sub-project with each sub-project having a README.md file offering information about the sub-project and its contents.  Sub-projects include:
- *codingexams*: Questions asked over a whiteboard or in a collaboration environment.
- *hacks*: Unusual solutions to interesting problems that don't necessarily reflect so-called _best practices_.
- *jerseyservices*: RESTful service implementations
- *utils*: Utilities used by _DemoDev_


##Sub-Project Structure

Sub-projects usually include a “business” motivation for the example.  Solution collaborators have their _JavaDoc_ entries explaining their role in the solution and the portion of the problem they address. All sub-projects include ‘JUnit’ tests because this reflects good _TDD_ practice and allows maintenance of the examples over time.  This is accomplished by using the regression aspect of these tests. Many of the projects appear _complicated_ by use of the *Spring* framework.  This is done to illustrate Spring usage in addition to the basic technique shown in the example. Overall, a professional Software Engineer would provide background for the code, some guidelines on usage in the _JavaDoc_ entries, and tips to help maintain the code.  Unit test help in achieving these goals.
