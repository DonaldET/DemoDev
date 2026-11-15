# BuildTreeCompare — Utility Construction Prompt

## Shared development policy

[PySupport repository development policy](../../AGENTS.md)

Follow the linked repository-level `AGENTS.md` policy. **If that policy is already available in the current Codex session context, do not read it again.** Otherwise, read the linked file once before implementation. The Markdown link is a reference, not an automatic include. If the file cannot be located, stop and ask for clarification.

## Expected location

This prompt is intended to reside at: `PySupport/TreeCompare/Prompts/BuildTreeCompare.md`

The relative link `../../AGENTS.md` resolves to `PySupport/AGENTS.md` from that location.

## Task: Develop a command-line Python Directory Tree Comparison Utility

Name the complete program module `compare_trees.py` to compare two Windows directory trees recursively. Compare both file names and the binary content of the files in the two directory trees. For example:

**TreeA**
`\src`
`alpha.py (hash X)`
`beta.py (hash Y)`
`gamma.py (hash W)`   --- this file is unmatched, so tree comparison fails.

**TreeB**
`\archive`
`alpha.py (hash X)`

`\backup`
`beta.py (hash Y)`
`extra.py (hash Z)`

## 1. Comparison semantics

The comparison is one-directional: every file in TreeA must have at least one matching file in TreeB. Note the file names in TreeA must be unique.

A file matches in the two trees when:

- Its filename is identical, using Windows case-insensitive filename comparison, and
- Its binary contents is identical.

Note that its directory location is irrelevant.

Use only file size and SHA-256 hashes to establish content equality.

Additional copies of a file in TreeA found in TreeB are permitted and must not cause the comparison to fail. Similarly, files in TreeB not found in TreeA are also permitted and must not cause the comparison to fail.

## 2. Matching algorithm

1. Recursively enumerate regular files in both trees.
1. Record each file's absolute path, relative path, filename, size, and SHA-256 hash.
1. Index TreeA files by normalized filename AND verify they are unique.
1. Index TreeB files by normalized filename.
1. For every file in TreeA, identify TreeB candidates with the same normalized filename.
1. Compare file sizes and SHA-256 hashes to determine identical contents.
1. Classify the file comparison results as follows:
   - MATCHED: At least one file in TreeB has the same filename and contents as TreeA.
   - DUPLICATE: More than one file in TreeB has the same filename and identical contents.
   - DUPLICATE_NAME: More than one file in TreeB has the same filename but different contents.
   - MISSING: No file with the same filename exists in TreeB.
   - MISMATCH: One or more files with the same filename exist in TreeB, but none has identical contents.
   - UNASSIGNED: A file in TreeB has no matching file in TreeA.
   - ERROR: The file could not be examined or compared.
1. Report unmatched additional TreeB files (UNASSIGNED) separately. They are informational and do not cause failure.
1. Report multiple matching TreeB files separately. They are informational and do not cause failure.

## 3. Important duplicate semantics

A duplicate match is defined by the combination of normalized filename and identical content, not by content alone. Ignore all other file attributes in the comparison.

Files with different filenames but identical contents are not duplicate matches.

File names in TreeA must be unique.

Use a deterministic matching algorithm that maximizes the number of one-to-one matches. Do not allow iteration order to determine whether the comparison passes.

## 4. Reporting

Produce:

- List, in a console summary:
  - the source (TreeA) and destination (TreeB) file paths.
  - total files in TreeA and in TreeB.
  - the definition of comparison classifications.
  - counts of files for each comparison classification.
  - File access or processing errors.
  - Overall PASS or FAIL.
- A detailed CSV report named `comparison_report.csv`.

The summary must contain:

- Total files in TreeA.
- Total files in TreeB.
- Successfully matched TreeA files.
- Missing files.
- Content mismatches.
- Duplicate groups.
- Files lacking a unique one-to-one assignment.
- Additional unmatched TreeB files.
- File access or processing errors.
- Overall PASS or FAIL.

For each discrepancy, include the source file path, candidate destination paths, file sizes, hashes, and explanation.

Clearly distinguish duplicate candidates from a failure to establish one-to-one correspondence.

## 5. Exit codes

- 0: PASS — every TreeA file has a match in TreeB, with no comparison errors.
- 1: FAIL — one or more TreeA files cannot be matched with a file in TreeB.
- 2: ERROR — invalid arguments, empty trees, inaccessible directories, or other errors that prevent a reliable comparison.

## 6. Implementation requirements

- Be aware of the project directory structure defined in topic 11 below.
- Use Python 3.13.
- Standard library only for the application.
- Provide module docstrings for modules and function docstring, explaining parameters, for functions.
- Include the copyright text in the module docstring, obtain the copyright text from COPYRIGHT.txt file in the project directory.
- Use `argparse` for command-line arguments.
- Support arbitrarily nested directories.
- Handle Windows case-insensitive filenames.
- Handle file-access errors gracefully.
- Do not follow symbolic links or directory junctions by default.
- Read files in chunks when calculating hashes.
- Use deterministic sorting for reproducible results.
- Support Unicode filenames.
- Avoid hashing files in TreeB that cannot possibly match a filename in TreeA, unless required for reporting.
- Do not modify either directory tree.
- Detect files that change during comparison where practical, and report them as errors rather than trusting an inconsistent result.

## 7. Testing

Create pytest tests using temporary directory trees.

Include tests for:

1. Source or target directory empty (an error).
1. Source directory has duplicate file names (an error, list all duplicates in the error message).
1. Identical files in different subdirectories.
1. Same filename but different contents.
1. Missing destination file.
1. Multiple identical matching files in TreeB.
1. Additional unrelated files in TreeB.
1. Two identical source files requiring two separate destination matches.
1. Identical contents with different filenames.
1. Filename case differences on Windows.
1. Inaccessible files and symbolic links.
1. Deterministic results regardless of enumeration order.

## 8. Command-line interface

Support:

```
python compare_trees.py "D:\Projects\TreeA" "D:\Projects\TreeB" --report comparison_report.csv --json comparison_report.json
```

## 9. Deliverables

Create these files:

1. `compare_trees.py`
1. `test_compare_trees.py` --> pytest unit tests
1. `README.md` --> provides documentation for using this utility

Place the generated files in the location identified by the project structure defined in topic 11.

Do not copy, modify, rename, or delete files in either input tree.

## 10. Additional non-standard Libraries

Allow use of Recordclass library .

## 11. Project Structure

The directory structure under the project directory (`\TreeCompare`):

`Folder PATH listing for volume DATA`
`|   .gitignore`
`|   README.md`
`|   TreeCompare.toml`
`+---.venv` --> Python virtual environment
`+---Docs`
`+---Prompts`
`|   TreeComparisonUtility.md` --> this file
`+---scripts`
`+---src`
`|   \---tree_compare`
`|       |   compare_trees.py`
`|       |   __init__.py`
`+---tests`
`|   |   test_compare_trees.py`
`|   |   __init__.py`

## Execution and acceptance

1. Inspect the existing `TreeCompare` files and environment.
1. Explain any ambiguities or changes requiring approval.
1. Implement only the authorized files, after any necessary approvals.
1. Execute relevant tests using the utility-specific environment.
1. Report changed files, test counts and results, dependencies, and unresolved issues.

Acceptance requires correct one-to-one matching, read-only handling of both trees, meaningful CLI output, and passing tests. Do not claim tests passed unless they were run.
