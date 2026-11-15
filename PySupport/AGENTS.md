# AGENTS.md — PySupport Repository Development Policy

## Purpose and applicability

This file defines conservative development rules for all Python utilities beneath `PySupport/`. Product-specific prompts define the work to be performed; this file defines the safeguards. If instructions conflict, stop and request clarification rather than silently weakening these safeguards.

## Repository and environment layout

- `PySupport/` is a collection of independent utilities, **not** a Python virtual environment. Do not assume or create `PySupport/.venv`.
- **Each utility must have its own virtual environment**, normally `<utility>/.venv/` (for example, `TreeCompare/.venv/`). Never reuse another utility's environment merely because it exists.
- Before running Python or installing dependencies, inspect the target utility for `.venv`, `venv`, or another explicitly configured environment. Check its interpreter and version.
- If no suitable environment exists, propose creating `<utility>/.venv` and **request approval before creating it**. Do not silently install packages or change interpreter versions.
- Prefer Python **3.13 or newer**; treat 3.13 as the minimum supported version unless the utility prompt specifies otherwise. Report when an appropriate interpreter is unavailable.
- Invoke the utility environment's interpreter explicitly (e.g., `.venv\Scripts\python.exe` on Windows or `.venv/bin/python` on Linux). Do not require shell activation.
- Ensure virtual environments and generated caches are excluded from Git; request approval before changing an existing `.gitignore`.

## Virtual environment

`PySupport/` **does not have a shared virtual environment**. `TreeCompare` must use its own environment, preferably `PySupport/TreeCompare/.venv/`.

1. Check `<utility>/` for an existing `.venv` or other configured virtual environment.
1. Verify the interpreter is Python 3.13 or newer.
1. If no compatible environment exists, **ask permission before creating** `utility/.venv`.
1. Use that environment's Python executable for running the utility and tests. Ask permission before installing `pytest` or any other dependency.
1. Do not create a virtual environment at the `PySupport/` root.

## Conservative modification policy

- Inspect relevant files first and make the smallest changes that satisfy the current utility prompt.
- Without additional approval, create or edit only files explicitly authorized by the utility prompt, and run non-destructive inspections and tests.
- **Request explicit approval before** deleting, renaming, or overwriting existing files; modifying files outside the authorized scope; changing public APIs; broad refactoring; modifying shared configuration; installing/upgrading/removing dependencies; creating/replacing a virtual environment; or making Git commits, pushes, resets, or branch changes.
- Never remove, disable, skip, mark `xfail`, weaken assertions in, or otherwise reduce the effectiveness of **existing unit tests** without explicit permission. If a test appears wrong, explain the evidence and propose the change first.
- Do not run destructive cleanup commands or modify unrelated utilities. Never treat silence as approval.
- Preserve existing behavior unless the product prompt expressly requires a change.

## Python implementation conventions

- Prefer the standard library, `pathlib`, and `argparse` for straightforward command-line utilities.
- Follow PEP 8, use four-space indentation, meaningful names, type annotations for public interfaces, and docstrings for public functions/classes.
- Use `logging` for library diagnostics and explicit error handling for anticipated failures.
- Keep code compatible with Windows and Linux where practical. Use UTF-8 for text files.
- Prefer `pytest` for tests and `ruff` for linting/formatting **when already available**; propose dependency additions rather than installing automatically.
- Do not reformat unrelated files or invent author/copyright ownership.

## Testing and validation

- Add or update focused pytest tests for new behavior, edge cases, and invalid input, within the authorized files.
- Run focused tests first, then broader relevant tests when practical, using the target utility's virtual environment.
- Use temporary directories for filesystem tests; do not modify real user data.
- Report tests run, passed, failed, skipped, and not run. Never claim success without execution.

## Workflow and reporting

1. Read the utility-specific prompt and identify authorized paths and acceptance criteria.
1. Inspect existing implementation, tests, and the utility's virtual environment.
1. State a short plan for substantial changes and request approval for restricted actions.
1. Implement only authorized changes; preserve existing tests.
1. Validate using the utility's environment.
1. Summarize created/modified files, test results, dependency changes, and unresolved issues.

## Prompt reuse

Product prompts may link to this file with relative Markdown links. Codex normally discovers applicable `AGENTS.md` instructions automatically. If these instructions are already present in the active session context, **do not read this file again merely because a product prompt links to it**. If not present, read it once before making changes. A Markdown link is a reference, not an automatic textual include.
