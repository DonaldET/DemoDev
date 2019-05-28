## Sub-Project Coding Exams

These coding exams are elaborations on topics discussed during interviews and "take home" coding problems.  Some problems were from sites offering coding practice. The resulting solutions demonstrate a "professional engineering" approach to simple coding questions asked in these situations.  Solutions presented here include MAVEN builds, JUnit tests, and JavaDoc. Many of these problems were originally done under time constraints and considerable cleanup was required for presentation here.

These solutions also demonstrate coding principles like:
- Separation of interface and implementation.
- Use of multiple implementations (Strategy and Command Patterns.)
- Test driven development (shown by reviewing testing order.)
- JUnit tests where feasible.

### Selected _Exam_ Content

Client | Project | Description
------ | ------- | -----------
Amazon | _codingchallange_ | Create a recommendation feature (recommendation engine) called "Games Your Friends Play". The recommendation logic is based on the following rules: a customer should only be recommended games that their friends own but they don"t; the recommendations priority is driven by how many friends own a game - if multiple friends own a particular game, it should be higher in the recommendations than a game that only one friend owns
Amazon | _invoiceparser_ | Scan an Amazon EBook invoice and extract date, title, type and amount of book purchase.
Amazon | _rangeconsolidator_ | Scan a list of integer ranges and merge overlapping ranges, collapsing them into an all-inclusive range. Performance test as well.
AppDynamics | _top X query_ | Query the top 10 game scores using a priority queue; note that this is in sub-project _MemoryIndexing_.
Apple | _compounditerator_ | Concatenate data sources using a list of iterators for each data source
Apple | _factorial_ | Contrast two factorial algorithms
Cisco | _sieve_ | Capture the _top X_ elements of a random integer array using four techniques
CreditKarma | _List Dependencies_ | Given a list of modules names, each with a list of named dependencies, output a list with dependencies of a module appearing before the module itself.
Cruise-Control | _task processor_ | Process tasks sequentially  with parameterizable cooling time and fixed execution times
EasyPost | _pick_ | pick create-pick event pairs from an input stream and summarize times by location
Elance | _diagonal_ | Square matrix diagonal elements list generator
Gap | _gap cart_ | A sample shopping cart implementation with pricing strategies
GE | _gesail twowriters_ | Has two threads alternately incrementing a counter; uses multiple counter implementations, and includes unit tests and a runnable jar
GE | _geturner binarysearch_ | compare a recursive and iterative solution to the classical binary search algorithm; includes example of the _template pattern_.
Granular | _Sudoku Game Checker_ | Check rows, columns, and 3x3 cells for duplicates
IJSDE | _Problems_ | Programming skills validation examples associated with a placement web-site. 
Lumendata | _duplicatechecker_ | Finds duplicate column values in a row
Trulia | _treesearch_ | Find the "second largest value" in a specially constructed binary tree
WeWork | _spiral_ | Spiral-print a matrix (from LeetCode)

