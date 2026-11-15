"""Compare directory trees by case-insensitive filename, size, and SHA-256.

Copyright (c) 2026. Donald E. Trummell. All Rights Reserved.
Permission to use, copy, modify, and distribute this software and its documentation
for educational, research, and not-for-profit purposes, without fee and without
a signed licensing agreement, is hereby granted, provided that the above
copyright notice, and this paragraph, appear in all copies, modifications, and
distributions. Contact dtrummell@gmail.com for commercial licensing opportunities.
"""

from __future__ import annotations

import argparse
import csv
from dataclasses import asdict, dataclass
import hashlib
import json
import os
from pathlib import Path
import stat
from typing import Iterable


@dataclass
class FileRecord:
    """Metadata for a regular file; hash is absent for unrelated destination files."""

    path: str
    relative_path: str
    filename: str
    size: int | None = None
    sha256: str | None = None
    error: str | None = None


def normalize(filename: str) -> str:
    """Return a case-insensitive Unicode comparison key for filename."""
    return filename.upper()


def signature(info: os.stat_result) -> tuple:
    """Return identity and mutation indicators from a stat result, info."""
    # Windows Python 3.13 can return different ctime values from path stat
    # and handle fstat (creation time versus change time). Compare only fields
    # with consistent semantics on that platform.
    return (info.st_dev, info.st_ino, info.st_size, info.st_mtime_ns,
            info.st_ctime_ns if os.name != "nt" else None)


def is_link(info: os.stat_result) -> bool:
    """Return whether info identifies a symlink or Windows reparse point."""
    return stat.S_ISLNK(info.st_mode) or bool(
        getattr(info, "st_file_attributes", 0) & 0x400
    )


def examine(path: Path, root: Path, hash_file: bool) -> FileRecord:
    """Read path metadata and optionally hash_file, recording errors relative to root."""
    record = FileRecord(str(path), str(path.relative_to(root)), path.name)
    try:
        before = path.stat(follow_symlinks=False)
        if is_link(before) or not stat.S_ISREG(before.st_mode):
            raise OSError("File is no longer a regular non-link file")
        record.size = before.st_size
        if hash_file:
            digest = hashlib.sha256()
            with path.open("rb") as stream:
                if signature(os.fstat(stream.fileno())) != signature(before):
                    raise OSError("File changed before hashing")
                while chunk := stream.read(1024 * 1024):
                    digest.update(chunk)
                after = os.fstat(stream.fileno())
            if signature(before) != signature(after) or signature(before) != signature(
                path.stat(follow_symlinks=False)
            ):
                raise OSError("File changed during hashing")
            record.sha256 = digest.hexdigest()
    except OSError as exc:
        record.error = str(exc)
    return record


def scan(root: Path, names: set[str] | None) -> tuple[list[FileRecord], list[dict]]:
    """Iteratively enumerate root; hash only names in names, or all if None."""
    records, errors = [], []
    pending = [root]
    while pending:
        directory = pending.pop()
        try:
            with os.scandir(directory) as iterator:
                entries = sorted(iterator, key=lambda e: (normalize(e.name), e.name))
        except OSError as exc:
            errors.append({"path": str(directory), "explanation": str(exc)})
            continue
        for entry in entries:
            path = Path(entry.path)
            try:
                info = entry.stat(follow_symlinks=False)
                if is_link(info):
                    continue
                if stat.S_ISDIR(info.st_mode):
                    pending.append(path)
                elif stat.S_ISREG(info.st_mode):
                    records.append(examine(path, root, names is None or normalize(entry.name) in names))
            except OSError as exc:
                records.append(FileRecord(str(path), str(path.relative_to(root)), path.name, error=str(exc)))
    records.sort(key=lambda r: (normalize(r.relative_path), r.relative_path))
    return records, errors


def index_files(records: Iterable[FileRecord]) -> dict[str, list[FileRecord]]:
    """Group records by normalized filename, preserving deterministic record order."""
    result: dict[str, list[FileRecord]] = {}
    for record in records:
        result.setdefault(normalize(record.filename), []).append(record)
    return result


def compare_trees(source: str | Path, destination: str | Path) -> dict:
    """Compare source against destination and return a JSON-serializable report.

    Invalid or inaccessible roots raise OSError. Repeated source names are
    validation errors, but assignments are still computed for diagnostics.
    """
    roots = [Path(p).absolute() for p in (source, destination)]
    for root in roots:
        info = root.stat(follow_symlinks=False)
        if is_link(info) or not stat.S_ISDIR(info.st_mode):
            raise OSError(f"Root must be a non-link directory: {root}")
    a, errors_a = scan(roots[0], None)
    source_index = index_files(a)
    b, errors_b = scan(roots[1], set(source_index))
    destination_index = index_files(b)
    errors = errors_a + errors_b
    for root, records in zip(roots, (a, b)):
        if not records:
            errors.append({"path": str(root),
                           "explanation": f"Tree contains no regular files: {root}"})
    for record in a + b:
        if record.error:
            errors.append({"path": record.path, "explanation": record.error})
    repeated = [group for group in source_index.values() if len(group) > 1]
    for group in repeated:
        errors.append({"path": group[0].path, "paths": [r.path for r in group],
                       "explanation": "Source filenames must be unique (case-insensitive): "
                       + "; ".join(r.path for r in group)})
    used: set[str] = set()
    results, duplicates = [], []
    seen_groups = set()
    for record in a:
        candidates = destination_index.get(normalize(record.filename), [])
        matches = [c for c in candidates if not c.error and not record.error
                   and (c.size, c.sha256) == (record.size, record.sha256)]
        available = [c for c in matches if c.path not in used]
        assigned = available[0] if available else None
        different_contents = len({(c.size, c.sha256) for c in candidates if not c.error}) > 1
        if record.error:
            status, reason = "ERROR", "Source file could not be examined"
        elif assigned:
            used.add(assigned.path)
            status = "DUPLICATE" if len(matches) > 1 else "MATCHED"
            reason = "Assigned one distinct destination file"
            if different_contents and len(matches) == 1:
                status = "DUPLICATE_NAME"
                reason += "; other same-name candidates have different contents"
        elif matches:
            status, reason = "UNASSIGNED", "Identical destination files are already assigned"
        elif any(c.error for c in candidates):
            status, reason = "ERROR", "A destination candidate could not be examined"
        elif not candidates:
            status, reason = "MISSING", "No destination file has this filename"
        else:
            status, reason = "CONTENT_MISMATCH", "No candidate has identical size and SHA-256"
            if different_contents:
                status = "DUPLICATE_NAME"
                reason += "; same-name candidates also differ from one another"
        key = (normalize(record.filename), record.size, record.sha256)
        if len(matches) > 1 and key not in seen_groups:
            seen_groups.add(key)
            duplicates.append({"source": asdict(record), "matches": [asdict(c) for c in matches]})
        results.append({"status": status, "source": asdict(record),
                        "candidates": [asdict(c) for c in candidates],
                        "assigned_destination": assigned.path if assigned else None,
                        "duplicate_name": different_contents,
                        "explanation": reason})
    matched = sum(r["assigned_destination"] is not None for r in results)
    summary = {"total_source_files": len(a), "total_destination_files": len(b),
               "successfully_matched": matched,
               "missing": sum(r["status"] == "MISSING" for r in results),
               "content_mismatches": sum(r["status"] == "CONTENT_MISMATCH" or
                                          (r["status"] == "DUPLICATE_NAME" and
                                           r["assigned_destination"] is None) for r in results),
               "duplicate_name_groups": len({normalize(r["source"]["filename"])
                                             for r in results if r["duplicate_name"]}),
               "duplicate_groups": len(duplicates), "lacking_assignment": len(a) - matched,
               "additional_destination_files": len(b) - len(used),
               "errors": len(errors)}
    code = 2 if errors else (1 if matched != len(a) else 0)
    summary.update(overall="PASS" if code == 0 else "FAIL", exit_code=code)
    return {"source_root": str(roots[0]), "destination_root": str(roots[1]),
            "summary": summary, "results": results, "duplicate_groups": duplicates,
            "additional_destination_files": [asdict(r) for r in b if r.path not in used],
            "errors": errors}


def write_reports(report: dict, csv_path: Path, json_path: Path) -> None:
    """Write report to csv_path and json_path in UTF-8, without altering input trees."""
    with json_path.open("w", encoding="utf-8") as stream:
        json.dump(report, stream, ensure_ascii=False, indent=2)
        stream.write("\n")
    fields = ["status", "source_path", "source_size", "source_sha256",
              "assigned_destination", "duplicate_name", "candidates", "explanation"]
    with csv_path.open("w", newline="", encoding="utf-8-sig") as stream:
        writer = csv.DictWriter(stream, fieldnames=fields)
        writer.writeheader()
        for row in report["results"]:
            writer.writerow({"status": row["status"], "source_path": row["source"]["path"],
                             "source_size": row["source"]["size"],
                             "source_sha256": row["source"]["sha256"],
                             "assigned_destination": row["assigned_destination"],
                             "duplicate_name": row["duplicate_name"],
                             "candidates": json.dumps(row["candidates"], ensure_ascii=False),
                             "explanation": row["explanation"]})
        for record in report["additional_destination_files"]:
            writer.writerow({"status": "ADDITIONAL", "candidates": json.dumps([record], ensure_ascii=False),
                             "explanation": "Destination file not assigned to a source"})
        for error in report["errors"]:
            writer.writerow({"status": "ERROR", "source_path": error["path"],
                             "explanation": error["explanation"]})


def main(argv: list[str] | None = None) -> int:
    """Parse argv (or process arguments), write reports, and return the exit code."""
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("source", type=Path)
    parser.add_argument("destination", type=Path)
    parser.add_argument("--report", type=Path, default=Path("comparison_report.csv"))
    parser.add_argument("--json", type=Path, default=Path("comparison_report.json"))
    args = parser.parse_args(argv)
    try:
        roots = [p.resolve() for p in (args.source, args.destination)]
        outputs = [p.resolve() for p in (args.report, args.json)]
        if outputs[0] == outputs[1]:
            raise OSError("CSV and JSON report paths must be distinct")
        for output in outputs:
            if any(output.is_relative_to(root) for root in roots):
                raise OSError("Report paths must be outside both input trees")
        report = compare_trees(args.source, args.destination)
        write_reports(report, args.report, args.json)
    except (OSError, ValueError) as exc:
        print(f"ERROR: {exc}")
        return 2
    for label, value in report["summary"].items():
        print(f"{label.replace('_', ' ').capitalize()}: {value}")
    for error in report["errors"]:
        print(f"ERROR: {error['path']}: {error['explanation']}")
    return report["summary"]["exit_code"]


if __name__ == "__main__":
    raise SystemExit(main())
