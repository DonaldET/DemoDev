##Sub-Project Memory Indexing

These examples demonstrate in-memory searching algorithms that stress simple approaches where possible.

###Selected _MemoryIndexing_ Content

Project | Description
------- | -----------
_simple linear search_ | Use specially formatted names and attributes and perform “_brute_” linear search . . . it is both simple and fast
_top X query_| Compares the performance of priority queue and sorted array based scanners on "top X" queries across a random collection of Game instances. Game instances have an ascending comparator that allows the instances to be ordered on several attributes. The "Top X” query returns a sorted collection of the desired subset of Game instances.

This example uses the motivation of finding the (say) top 10 scores of a complex game where several game attributes allow for unique ordering. We vary the number of inputs to scan and the size of the top set. Since 100 elements is a large list, that is our upper limit for this performance test.

_topx-query_ | Compare a _PriorityQueue_ and simple sorted-array based scanner that captures the “top X” elements of a list of “game” instances.
