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

Sub-projects usually include a business motivation for the coding example.  The examples also show good _OO design principals_, such as *KISS*, *DRY* (dont repeat yourself), and *SOLID* (Single responsibility, Open-closed, Liskov substitution, Interface segregation and Dependency inversion.)

The sub-project solution collaborators have their _JavaDoc_ entries explaining their role in the solution and the portion of the problem they address. All sub-projects include JUnit tests because this reflects good _TDD_ practice and allows maintenance of the examples over time.  This is accomplished by using the regression aspect of these tests. Many of the projects appear _complicated_ by use of the *Spring* framework.  This is done to illustrate _Spring_ usage in addition to the basic technique shown in the example. Overall, a professional Software Engineer would provide background for the code, some guidelines on usage in the _JavaDoc_ entries, and tips to help maintain the code.  Example invocation with unit tests help in achieving these goals.

#Getting Started

There are several useful tools in the _StaticResources_ directory.  This includes MAVEN archetypes for generating projects; various scripts for maintaining documentation, and instructions on setting up a build environment in **BuildResources.pdf**.
##Sub-Project _Coding Exams_

These coding exams are elaborations on topics discussed during interviews and take home coding problems.  The resulting solutions demonstrate a professional engineering approach to simple coding questions asked during an interview.  Solutions include MAVEN builds, JUnit tests, and JavaDoc.

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

These short examples demonstrate _less than the best_ coding practices that are sometimes used to get around a solution constraint until a better solution can be found.

#Selected _Hacks_ Content

Project | Description
------- | -----------
_serialhack_ | Use serialization to assign value to a private field lacking a setter
##Sub-Project Memory Indexing

These examples demonstrate in-memory searching algorithms that stress simple approaches where possible.

#Selected _MemoryIndexing_ Content

Project | Description
------- | -----------
_simple linear search_ | Use specially formatted names and attributes and perform _brute_ linear search . . . it is both simple and fast
аЯрЁБс                >  ўџ	                               ўџџџ        џџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџ§џџџўџџџўџџџ         ўџџџўџџџџџџџџџџџ                        џџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџR o o t   E n t r y                                               џџџџџџџџ   ВZЄ
бЄ РOЙ2К            ащ6ЦЕЯ   Р       C O N T E N T S                                                        џџџџ                                    
           C o m p O b j                                                   џџџџџџџџџџџџ                                        V       S P E L L I N G                                                   џџџџџџџџџџџџ                                                 ўџџџўџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџџN F O ]   F i n a l   M e m o r y :   2 5 M / 1 0 0 M  [ I N F O ]   - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -         іђрђрђрђрђрђрђрђрђрђрђрђрђрђрђрђрђрђрђрђрђрђрђрђрђрђрђр                                                                                                                                                 $   0 8    
   "	            "  О  Р  `      >  о  ~    О  ^  ў  	  >
  о
  ~    О  ^  ў    Ш  h     ќ  8  и  к  м  о  ќќќќќќќќќќќќќќќќќќќќќќќќќќќќќќдд                                                                                                                                                                                                                                                                            (   2"   ' (     X- X 0БZ 	Б         и  о  юъ                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            $   0 8 
      pЭэ            џџџџL      0Юэ           2    &   "Д! "	  $                  "PS 
* 4"рd                   t t               о                   о     @      0Юэ              C a l i b r i    T i m e s   N e w   R o m a n        џ   m            
    "   
    "   
    "    F    џџ "d5 "А "№Ъ] "pМ "№` 	"№`  "   "A   ."   @    џџ "d5 "№љ "№Ъ] "pМ "№` 	"№`  "   ."      
[     0 . 9 4 9   s ]  [  ўџ
  џџџџВZЄ
бЄ РOЙ2К   Quill96 Story Group Class џџџџ       є9Вq                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     CHNKWKS           ј џџџџ TEXT     TEXT   о   FDPP     FDPP       FDPC     FDPC       STSH     STSH       STSH    STSH  `    SYID     SYID~      SGP      SGP       INK      INK       BTEP     PLC       BTEC     PLC В      FONT     FONTЪ  T    STRS     PLC   :    FRAM     FRAMX      DOP      DOP р                                                                                                                                                     # # B u i l d   S u m m a r y   T h e r e   a r e   2 4   p r o j e c t   _ p o m . x m l _   f i l e s ,   i n c l u d i n g   s e v e r a l   p a r e n t   P O M s .     T h e s e   p r o j e c t s   h a v e   7 1   J A V A   s o u r c e   f i l e s ,   t o t a l i n g   o v e r   8 , 1 0 0   l i n e s   o f   c o d e ,   a n d   w i l l   p r o d u c e   7 9   c l a s s   f i l e s   a f t e r   t h e   b u i l d   c o m p l e t e s .   [ I N F O ]   - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  [ I N F O ]   R e a c t o r   S u m m a r y :  [ I N F O ]  [ I N F O ]   D e m o n s t r a t i o n   o f   D e v e l o p m e n t   _ T O P I C S _   . . . . . . . . . . . . . .   S U C C E S S   [     1 . 3 8 9   s ]  [ I N F O ]   D e m o n s t r a t i o n   o f   D e v e l o p m e n t   _ U T I L S _   . . . . . . . . . . . . . . .   S U C C E S S   [     0 . 0 2 9   s ]  [ I N F O ]   T e s t i n g   U t i l i t i e s   . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .   S U C C E S S   [   1 2 . 7 1 5   s ]  [ I N F O ]   D e m o n s t r a t i o n   o f   D e v e l o p m e n t   _ H A C K S _   . . . . . . . . . . . . . . .   S U C C E S S   [     0 . 0 2 6   s ]  [ I N F O ]   S e r i a l   H a c k   a n d   s t a n d - a l o n e   R u n n e r   . . . . . . . . . . . . . . . . .   S U C C E S S   [     0 . 9 4 9   s ]  [ I N F O ]   D e m o n s t r a t i o n   o f   D e v e l o p m e n t   _ J E R S E Y _   . . . . . . . . . . . . . .   S U C C E S S   [     0 . 0 4 6   s ]  [ I N F O ]   S a f e   C o l l e c t i o n   S e r v i c e   . . . . . . . . . . . . . . . . . . . . . . . . . . . .   S U C C E S S   [   1 0 . 7 5 2   s ]  [ I N F O ]   S a f e   C o l l e c t i o n   S e r v i c e   W a r   . . . . . . . . . . . . . . . . . . . . . . . .   S U C C E S S   [     1 . 8 2 1   s ]  [ I N F O ]   D e m o n s t r a t i o n   o f   D e v e l o p m e n t   _ C O D I N G E X A M S _   . . . . . . . . .   S U C C E S S   [     0 . 0 3 9   s ]  [ I N F O ]   A p p l e   -   C o m p o u n d   I t e r a t o r   . . . . . . . . . . . . . . . . . . . . . . . . . .   S U C C E S S   [     3 . 8 3 3   s ]  [ I N F O ]   A p p l e   -   F a c t o r i a l   C a l c u l a t o r   . . . . . . . . . . . . . . . . . . . . . . .   S U C C E S S   [     1 . 4 1 4   s ]  [ I N F O ]   E l a n c e   -   M a t r i x   D i a g o n a l   L i s t e r   . . . . . . . . . . . . . . . . . . . .   S U C C E S S   [     3 . 6 0 4   s ]  [ I N F O ]   G a p   -   S h o p p i n g   C a r t   . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .   S U C C E S S   [     1 . 0 8 9   s ]  [ I N F O ]   D e m o n s t r a t i o n   o f   D e v e l o p m e n t   _ M E M O R Y I N D E X _   . . . . . . . . .   S U C C E S S   [     0 . 0 1 9   s ]  [ I N F O ]   M e m o r y   I n d e x   -   S i m p l e   L i n e a r   S e a r c h   . . . . . . . . . . . . . . . .   S U C C E S S   [     2 . 1 5 2   s ]  [ I N F O ]   - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  [ I N F O ]   B U I L D   S U C C E S S  [ I N F O ]   - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  [ I N F O ]   T o t a l   t i m e :   4 0 . 5 0 8   s  [ I N F O ]   F i n i s h e d   a t :   2 0 1 4 - 0 8 - 1 1 T 1 7 : 3 2 : 2 1 - 0 7 : 0 0  [ I 