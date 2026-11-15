# BuildTreeCompare — Utility Construction Prompt

## Shared development policy

[PySupport repository development policy](../../AGENTS.md)

Follow the linked repository-level `AGENTS.md` policy. **If that policy is already available in the current Codex session context, do not read it again.** Otherwise, read the linked file once before implementation. The Markdown link is a reference, not an automatic include. If the file cannot be located, stop and ask for clarification.

## Expected location

This prompt is intended to reside at:

`PySupport/TreeCompare/Prompts/BuildTreeCompare.md`

The relative link `../../AGENTS.md` resolves to `PySupport/AGENTS.md` from that location.

## Objective

Build a Python 3.13+ command-line utility that compares two directory trees, A and B. Every file in A must have **exactly one** corresponding file in B with the same basename and identical byte content, irrespective of directory location. Extra files in B are permitted.

Matching is one-to-one: a file in B cannot satisfy more than one file in A. Identical duplicate filenames in B do not automatically satisfy an A file uniquely; report ambiguity when multiple eligible B files exist. State any assumptions before implementing edge cases not covered here.

## Authorized files

Within `PySupport/TreeCompare/`, create or modify only:

- `tree_compare.py` — implementation and CLI entry point.
- `README.md` — usage, behavior, examples, and limitations.
- `tests/test_tree_compare.py` — pytest unit tests.

Inspect other files as needed, but do not modify them without approval. Do not delete or weaken existing tests.

## Virtual environment

`PySupport/` **does not have a shared virtual environment**. `TreeCompare` must use its own environment, preferably `PySupport/TreeCompare/.venv/`.

1. Check `TreeCompare/` for an existing `.venv` or other configured virtual environment.
1. Verify the interpreter is Python 3.13 or newer.
1. If no compatible environment exists, **ask permission before creating** `TreeCompare/.venv`.
1. Use that environment's Python executable for running the utility and tests. Ask permission before installing `pytest` or any other dependency.
1. Do not create a virtual environment at the `PySupport/` root.

## Functional requirements

- Accept two directory arguments via `argparse`: `tree_a` and `tree_b`.
- Recursively enumerate regular files under each tree, ignoring directory placement for matching.
- Match using exact filename (basename) and byte-for-byte content equality.
- Do not rely on filename alone, size alone, or hash alone as proof of identical content.
- Require exactly one eligible B counterpart per A file, with one-to-one assignment.
- Allow additional unmatched B files.
- Produce deterministic, human-readable reports of successful matches and failures, including missing matches and ambiguous/duplicate candidates.
- Return exit status `0` when the requirement is satisfied and nonzero when it is not or when an operational error occurs.
- Handle inaccessible paths and I/O errors clearly; do not modify either input tree.
- Support Windows and Linux. Prefer `pathlib` and standard-library modules.

## Unit tests

Use pytest to cover at least:

- Identical trees.
- Same files arranged in different directory structures.
- Extra files in B.
- Missing file in B.
- Same filename but different content.
- Multiple B files with identical names and content (ambiguous match).
- Duplicate A files requiring distinct B counterparts.
- Empty trees.
- Invalid or inaccessible input paths.
- CLI exit codes.

Preserve any existing unit tests and assertions. Ask permission before deleting, disabling, or weakening them.

## Execution and acceptance

1. Inspect the existing `TreeCompare` files and environment.
1. Explain any ambiguities or changes requiring approval.
1. Implement only the authorized files, after any necessary approvals.
1. Execute relevant tests using the utility-specific environment.
1. Report changed files, test counts and results, dependencies, and unresolved issues.

Acceptance requires correct one-to-one matching, read-only handling of both trees, meaningful CLI output, and passing tests. Do not claim tests passed unless they were run.
