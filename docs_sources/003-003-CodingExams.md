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
Amazon | _findmedian_ | Illustrate two methods of finding the median of a (non-infinite) stream of integers. One method is highly optimized compared to the other.
Amazon | _infrastructure_ | We solve two problems: find the minimum times to complete a list of tasks in dependency order; find the longest substring of a string, with the substring having k or fewer unique characters.
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
Gesicht Buch | _Infrastructure_ | Minimum Task Timer, Longest Substring with Unique Characters, Stream to Lines, and Sum of three nearest target. 
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

