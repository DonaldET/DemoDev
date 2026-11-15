# Directory tree comparison

`compare_trees.py` compares regular files recursively by filename (case-insensitive
Unicode uppercase), file size, and chunked SHA-256. Directory placement does not
matter. Requires Python 3.13; the application uses only the standard library.

```powershell
python src/tree_compare/compare_trees.py "D:\Projects\TreeA" "D:\Projects\TreeB" --report comparison_report.csv --json comparison_report.json
```

Reports default to `comparison_report.csv` and `comparison_report.json` in the
current directory. Choose report locations outside both input trees. Input files
are read only; symbolic links, junctions, and other Windows reparse points are
skipped, including root paths that are links (which are rejected).

Each source file needs a distinct destination file with the same normalized
filename, size, and hash. Additional destination files and multiple identical
destination candidates are allowed. Duplicate groups require both identical
filenames and identical content; equal content under different names does not
match. Candidates are assigned in sorted relative-path order. Since candidates
are partitioned by filename, size, and hash, this deterministic assignment
maximizes the number of matches.

The specification requires unique source filenames while also requesting
duplicate-source assignment diagnostics. Repeated source names therefore produce
a validation error (exit 2), even when enough destination copies exist. The
report still assigns distinct copies and records any `UNASSIGNED` source files.

Results are `MATCHED`, `DUPLICATE`, `DUPLICATE_NAME`, `MISSING`, `CONTENT_MISMATCH`, `ERROR`, or
`UNASSIGNED`. The console includes totals, assigned sources, missing files,
content mismatches, duplicate groups, sources lacking assignments, additional
destination files, errors, and PASS/FAIL. JSON includes complete source records,
candidate records, chosen assignments, duplicate groups, extras, and errors.
CSV contains one row per source with candidate metadata encoded as JSON, plus
additional-file and error rows. Unrelated destination files have sizes but null
hashes because their content is never read.

`DUPLICATE_NAME` identifies same-name destination candidates with different
contents. If exactly one candidate matches the source, assignment succeeds and
this is informational. If none matches, it also counts as a content mismatch and
fails. Multiple identical matches use `DUPLICATE`; the separate `duplicate_name`
field still records any other same-name candidates with differing content.

Both trees must contain at least one regular file; an empty tree is an error.
All paths sharing a source filename appear in the console and report error
messages, regardless of whether their contents are identical.

Exit codes:

- **0**: PASS, all sources assigned with no errors.
- **1**: FAIL, missing, mismatched, or unassigned sources.
- **2**: ERROR, invalid arguments/roots, empty trees, source-name violations, access errors,
  detected file changes, or report-writing errors.

Traversal uses an explicit stack rather than recursive Python calls. Paths remain
subject to the host OS path-length limits. Filename normalization is deliberately
case-insensitive even on non-Windows hosts; Unicode uppercase approximates Windows
name comparison without applying Unicode canonical normalization. File identity,
size, and modification time are checked around hashing (also change time on
non-Windows hosts, where path and handle APIs expose consistent values). This detects
common concurrent modifications but is not an atomic filesystem snapshot; keep
both trees stable during comparison.

Run tests from the project root:

```powershell
python -m pip install pytest
python -m pytest -q
```

The implementation is in `src/tree_compare/compare_trees.py`, with package marker
`src/tree_compare/__init__.py`; all unit tests are in `tests`. `pytest.ini` adds
`src` to the test import path. For module invocation without installing a package,
set `$env:PYTHONPATH = "$PWD\src"`, then run
`python -m tree_compare.compare_trees SOURCE DESTINATION`.

`TreeCompare.toml` contains the supplied project metadata; pip and Hatch normally
require the standard filename `pyproject.toml` for installation. Direct script
execution and the pytest configuration above do not require installation.

If pytest is installed locally with `python -m pip install --target .test_dependencies pytest`, set `$env:PYTHONPATH = "$PWD\.test_dependencies"`
before running the tests in PowerShell. This directory is ignored by Git.

Tests use temporary trees and simulate access failures independently of OS user
privileges. Actual symlink tests skip when Windows does not permit creating links.
If a shared temporary directory has permission problems, create a fresh parent
directory and use `python -m pytest -q -p no:cacheprovider --basetemp PATH`, where
`PATH` is a new child beneath that parent dedicated to this test run. Pytest owns
and may clear its base temporary directory, so never use an input tree there.
