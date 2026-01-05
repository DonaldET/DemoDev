System: # FileTreeBuilder Prompt

You are an experienced Java developer proficient with Java I/O, especially the NIO and NIO2 APIs.

## Problem Description

Analyze a directory tree containing mainly PDF files by collecting file metadata. Implement a Java class named `FileTreeBuilder` to gather this metadata using Java NIO. The traversal and collection must start from a specified root directory.

### Symbolic Link Handling

- Follow symbolic-linked directories as regular subdirectories.
- If a symbolic link targets a non-existent location, throw a wrapped `RuntimeException`.
- Do not process symbolic links with `toRealPath()`.
- Only treat symbolic links to directories as directories. Regular files that are symbolic links are not treated as directories.
- If traversal cycles are detected through symbolic links, abort and throw a wrapped `RuntimeException` listing the revisited path.

### Error Handling

- Initialization failures (e.g., invalid root) must throw an `IllegalArgumentException` with a descriptive message to stop `traverse` immediately. Example:
  - `throw new IllegalArgumentException("Null initDir parameter");`
- Exception during `DirectoryStream` iteration (including reading file attributes) must throw a wrapped `RuntimeException` (including cause) and abort traversal. Example:
  - `throw new RuntimeException("Error while reading directory: " + path, cause);`
- Failure to read any entry in a `DirectoryStream` must be wrapped and propagated as above.

## Solution Outline

- A "record" refers to a Java `java.lang.Record`-based class.
- Implement `FileTreeBuilder` with a public method:
  - `ArrayList<Branch> traverse(Path initDir)`
- Validate `initDir` is non-null, exists, is a readable directory. Otherwise, throw a descriptive `IllegalArgumentException`.
- If accessing the root `DirectoryStream` fails, throw a wrapped `RuntimeException`.
- The method returns a non-empty `ArrayList<Branch>`, one for each found directory. If the root is empty, the list contains one `Branch`.
- If a directory contains no files, its `fileInfoList` is empty.

### Ordering Constraints

- Branches follow `DirectoryStream` retrieval order.
- `FileInfo` items in each branch preserve `DirectoryStream` order.
- Subdirectory discovery order is preserved.

### Traverse Method Details

- `Branch` record:
  - `public record Branch(String relativeDirectory, List<FileInfo> fileInfoList) {}`
- Normalize all directory paths before building the branch list (e.g., `initDir.normalize()`).
- The root uses the normalized absolute path. Descendants use normalized paths relative to root (system default separators).
- Example path handling:

```java
var baseDir = Path.of("C:\\Users\\Don").toAbsolutePath().normalize();
var subDir = Path.of("C:\\Users\\Don\\OneDrive\\Documents\\").toAbsolutePath().normalize();
var relDir = baseDir.relativize(subDir); // relDir ==> OneDrive\Documents
```

- `FileInfo` should only describe regular files:
  - `public record FileInfo(String fileName, BasicFileAttributes attributes) {}`
- `fileName` is from `getFileName().toString()`, only if `BasicFileAttributes.isRegularFile()` is true.

### BFS Traversal Requirements

- Traverse directories using BFS with a `Deque` and `DirectoryStream`.
- Enqueue subdirectories in the order they appear in `DirectoryStream`.
- Add files to `fileInfoList` in `DirectoryStream` order.
- Stop when the `Deque` is empty.
- For each directory:
  - Collect `FileInfo` for regular files.
  - Create and add its `Branch`.
  - Queue subdirectories.
- Sibling order may vary between runs—use stream order, do not sort.
- After completing traversal, return the `ArrayList<Branch>`.

#### Example: Windows File System

Given:
```
TOP2/
  Blatz.py
  MID1/
    A.prn
    B.java
    C.cpp
  MID2/
    D.dat
  Zee.doc
```

Call:
```java
ArrayList<Branch> tree = new FileTreeBuilder().traverse(Path.of("C:\\Users\\Don\\TOP2"));
```
Resulting `tree` contains:

| relativeDirectory | fileInfoList         |
| ----------------- | -------------------- |
| C:\Users\Don\TOP2 | Blatz.py             |
| MID1              | A.prn, B.java, C.cpp |
| MID2              | D.dat                |

## Output Format

The return value is:
```java
ArrayList<Branch> traverse(Path initDir)
```
Where:
- `public record Branch(String relativeDirectory, List<FileInfo> fileInfoList) {}`
- `public record FileInfo(String fileName, BasicFileAttributes attributes) {}`

Result constraints:
- `ArrayList<Branch>` always includes at least the root branch.
- `Branch.relativeDirectory` uses normalized absolute path for the root, normalized path relative to root for others; use system default separators.
- `fileInfoList` contains `FileInfo` for all regular files, in `DirectoryStream` order.
- Branch order follows BFS discovery order (as per `DirectoryStream`, not sorted).
- Exceptions are thrown as described—do not appear in the result.
- No JSON/CSV/tabular serialization or conversion is needed. Output is the described Java structure.