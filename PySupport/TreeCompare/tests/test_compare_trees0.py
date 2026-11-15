"""Behavioral tests for directory comparison and reporting.

Copyright (c) 2026. Donald E. Trummell. All Rights Reserved.
Permission to use, copy, modify, and distribute this software and its documentation
for educational, research, and not-for-profit purposes, without fee and without
a signed licensing agreement, is hereby granted, provided that the above
copyright notice, and this paragraph, appear in all copies, modifications, and
distributions. Contact dtrummell@gmail.com for commercial licensing opportunities.
"""

import csv
import json

import os
print("\n********* Behavioral tests for directory comparison and reporting working in:", os.getcwd(), "*******")

from pathlib import Path
import pytest
from tree_compare import compare_trees as ct

@pytest.fixture
def trees(tmp_path):
    """Create two empty trees under tmp_path."""
    a, b = tmp_path / "a", tmp_path / "b"
    a.mkdir()
    b.mkdir()
    return a, b


def put(root, name, content=b"sample"):
    """Write content to name beneath root, creating parents."""
    path = root / name
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_bytes(content)
    return path


@pytest.mark.parametrize("kind, expected", [
    ("relocated", "MATCHED"), ("different", "CONTENT_MISMATCH"),
    ("missing", "MISSING"), ("duplicates", "DUPLICATE"),
    ("extra", "MATCHED"), ("different_name", "MISSING"),
    ("case", "MATCHED"), ("unicode", "MATCHED"),
])
def test_classifications(trees, kind, expected):
    """Check matching outcomes for kind in the temporary trees against expected."""
    a, b = trees
    name = "café.txt" if kind == "unicode" else "file.txt"
    put(a, "nested/" + name)
    if kind not in ("missing", "different_name"):
        put(b, "elsewhere/" + (name.upper() if kind == "case" else name),
                b"change" if kind == "different" else b"sample")
    if kind == "duplicates":
        put(b, "copy/" + name)
    if kind in ("extra", "different_name"):
        put(b, "unrelated.txt")
    if kind == "missing":
        put(b, "other.txt")
    report = ct.compare_trees(a, b)
    assert report["results"][0]["status"] == expected
    assert report["summary"]["exit_code"] == (0 if expected in ("MATCHED", "DUPLICATE") else 1)
    assert report["summary"]["duplicate_groups"] == (1 if kind == "duplicates" else 0)
    assert report["summary"]["additional_destination_files"] == (1 if kind in ("missing", "different", "duplicates", "extra", "different_name") else 0)


@pytest.mark.parametrize("populated", [None, 0, 1])
def test_empty(trees, populated):
    """Either empty tree is an error; populated selects a tree to seed."""
    if populated is not None:
        put(trees[populated], "file")
    report = ct.compare_trees(*trees)
    assert report["summary"]["exit_code"] == 2
    assert any("no regular files" in e["explanation"] for e in report["errors"])


@pytest.mark.parametrize("copies", [1, 2])
def test_duplicate_source_names(trees, copies):
    """Repeated source names require distinct assignments and violate uniqueness."""
    a, b = trees
    put(a, "one/file.txt")
    put(a, "two/FILE.txt")
    for i in range(copies):
        put(b, f"{i}/file.txt")
    report = ct.compare_trees(a, b)
    assert report["summary"]["successfully_matched"] == copies
    assert report["summary"]["lacking_assignment"] == 2 - copies
    assert report["summary"]["exit_code"] == 2
    assigned = [r["assigned_destination"] for r in report["results"] if r["assigned_destination"]]
    assert len(set(assigned)) == copies
    message = next(e["explanation"] for e in report["errors"] if "unique" in e["explanation"])
    assert str(a / "one/file.txt") in message
    assert str(a / "two/FILE.txt") in message


def test_maximal_assignments(trees):
    """Files with repeated names and distinct content receive all possible matches."""
    a, b = trees
    for root in (a, b):
        put(root, "one/file", b"one")
        put(root, "two/file", b"two")
    report = ct.compare_trees(a, b)
    assert report["summary"]["successfully_matched"] == 2
    assert report["summary"]["duplicate_groups"] == 0


def test_access_error(trees, monkeypatch):
    """Unreadable files are reported as errors using a portable permission simulation."""
    a, b = trees
    target = put(a, "file")
    put(b, "file")
    original = Path.open

    def denied(path, *args, **kwargs):
        """Deny opening target while forwarding other path, args, and kwargs."""
        if path == target:
            raise PermissionError("access denied")
        return original(path, *args, **kwargs)

    monkeypatch.setattr(Path, "open", denied)
    report = ct.compare_trees(a, b)
    assert report["summary"]["exit_code"] == 2
    assert report["results"][0]["status"] == "ERROR"


def test_directory_access_error(trees, monkeypatch):
    """Directory enumeration failures prevent a reliable result."""
    a, b = trees
    original = os.scandir

    def denied(path):
        """Deny enumeration of path when it is the destination root."""
        if Path(path) == b:
            raise PermissionError("directory denied")
        return original(path)

    monkeypatch.setattr(os, "scandir", denied)
    assert ct.compare_trees(a, b)["summary"]["exit_code"] == 2


def test_links(trees):
    """Skip file links, directory links, and cycles when symlinks are available."""
    a, b = trees
    target = put(a, "file")
    put(b, "file")
    try:
        (a / "file_link").symlink_to(target)
        (a / "cycle").symlink_to(a, target_is_directory=True)
    except OSError as exc:
        pytest.skip(f"Symlink creation unavailable: {exc}")
    report = ct.compare_trees(a, b)
    assert report["summary"]["total_source_files"] == 1
    assert report["summary"]["exit_code"] == 0


def test_reparse_detection():
    """Windows junction/reparse attributes are excluded even without link mode bits."""
    class Info:
        st_mode = 0o040755
        st_file_attributes = 0x400
    assert ct.is_link(Info())


def test_unrelated_files_not_hashed(trees, monkeypatch):
    """Unrelated destination files retain size but need no content reads."""
    a, b = trees
    unrelated = put(b, "extra")
    original = Path.open

    def guarded(path, *args, **kwargs):
        """Fail if unrelated path is opened; forward all other args and kwargs."""
        assert path != unrelated
        return original(path, *args, **kwargs)

    monkeypatch.setattr(Path, "open", guarded)
    report = ct.compare_trees(a, b)
    assert report["additional_destination_files"][0]["sha256"] is None


def test_change_during_hash(trees, monkeypatch):
    """Mutation detected by post-read stat yields an error."""
    a, b = trees
    target = put(a, "file")
    original = ct.os.fstat
    calls = 0

    def changing(fd):
        """Modify target during the second fstat of fd."""
        nonlocal calls
        calls += 1
        if calls == 2:
            target.write_bytes(b"changed and longer")
        return original(fd)

    monkeypatch.setattr(ct.os, "fstat", changing)
    report = ct.compare_trees(a, b)
    assert report["summary"]["exit_code"] == 2
    assert "changed" in report["results"][0]["source"]["error"].lower()


def test_deterministic(trees, monkeypatch):
    """Reversing enumeration order leaves complete reports unchanged."""
    a, b = trees
    for name in ("z/f", "a/g", "b/f"):
        put(b, name)
    put(a, "f")
    expected = ct.compare_trees(a, b)
    original = os.scandir

    class ReverseScan:
        """Context manager yielding entries in reverse filesystem order."""
        def __init__(self, path):
            """Capture reversed entries from path."""
            with original(path) as entries:
                self.entries = list(entries)[::-1]

        def __enter__(self):
            """Return captured entries."""
            return iter(self.entries)

        def __exit__(self, *args):
            """Release no resources; accept exception args."""
            return False

    monkeypatch.setattr(os, "scandir", ReverseScan)
    assert ct.compare_trees(a, b) == expected


def test_cli_reports(trees, tmp_path, capsys):
    """CLI writes parseable reports outside trees and prints the summary."""
    a, b = trees
    put(a, "file")
    put(b, "nested/file")
    csv_path, json_path = tmp_path / "report.csv", tmp_path / "report.json"
    assert ct.main([str(a), str(b), "--report", str(csv_path), "--json", str(json_path)]) == 0
    assert json.loads(json_path.read_text(encoding="utf-8"))["summary"]["overall"] == "PASS"
    with csv_path.open(encoding="utf-8-sig", newline="") as stream:
        assert list(csv.DictReader(stream))[0]["status"] == "MATCHED"
    assert "PASS" in capsys.readouterr().out


def test_invalid_root(tmp_path):
    """Missing input roots produce error exit status."""
    assert ct.main([str(tmp_path / "absent"), str(tmp_path)]) == 2


def test_reports_cannot_modify_inputs(trees):
    """Reject a report path inside either input tree before writing."""
    a, b = trees
    target = put(a, "file", b"preserve")
    assert ct.main([str(a), str(b), "--report", str(target)]) == 2
    assert target.read_bytes() == b"preserve"


def test_large_binary(trees):
    """Hash binary content across multiple read chunks, including a changed tail."""
    a, b = trees
    content = bytes(range(256)) * 9000
    put(a, "file", content)
    destination = put(b, "file", content)
    assert ct.compare_trees(a, b)["summary"]["exit_code"] == 0
    destination.write_bytes(content[:-1] + b"x")
    assert ct.compare_trees(a, b)["results"][0]["status"] == "CONTENT_MISMATCH"


def test_destination_access_error(trees, monkeypatch):
    """An inaccessible candidate prevents a reliable mismatch classification."""
    a, b = trees
    put(a, "file")
    target = put(b, "file")
    original = Path.open

    def denied(path, *args, **kwargs):
        """Deny reads of target; forward other path, args, and kwargs."""
        if path == target:
            raise PermissionError("destination access denied")
        return original(path, *args, **kwargs)

    monkeypatch.setattr(Path, "open", denied)
    report = ct.compare_trees(a, b)
    assert report["summary"]["exit_code"] == 2
    assert report["results"][0]["status"] == "ERROR"


def test_cli_failure_report(trees, tmp_path):
    """Missing sources' matches produce exit 1 and complete discrepancy records."""
    a, b = trees
    put(a, "file")
    put(b, "other")
    output = tmp_path / "result.json"
    assert ct.main([str(a), str(b), "--report", str(tmp_path / "result.csv"),
                    "--json", str(output)]) == 1
    result = json.loads(output.read_text(encoding="utf-8"))["results"][0]
    assert result["source"]["size"] == 6
    assert len(result["source"]["sha256"]) == 64
    assert result["candidates"] == []


def test_report_write_error(trees, tmp_path):
    """An unwritable report location yields exit 2 rather than a traceback."""
    assert ct.main([*(str(p) for p in trees), "--json",
                    str(tmp_path / "missing" / "report.json")]) == 2


def test_invalid_arguments():
    """Argparse rejects missing mandatory root arguments with exit 2."""
    with pytest.raises(SystemExit) as exc:
        ct.main([])
    assert exc.value.code == 2


def test_deep_tree(trees):
    """Nested paths are enumerated without depending on Python call recursion."""
    a, b = trees
    put(a, "/".join(["d"] * 30 + ["file"]))
    put(b, "file")
    assert ct.compare_trees(a, b)["summary"]["exit_code"] == 0


@pytest.mark.parametrize("matching_copies", [0, 1, 2])
def test_duplicate_destination_names(trees, matching_copies):
    """Same-name content conflicts are informational when matching_copies exist."""
    a, b = trees
    put(a, "file", b"source")
    put(b, "one/FILE", b"different")
    put(b, "two/file", b"another")
    for i in range(matching_copies):
        put(b, f"matching{i}/file", b"source")
    report = ct.compare_trees(a, b)
    assert report["summary"]["exit_code"] == (0 if matching_copies else 1)
    row = report["results"][0]
    assert row["status"] == ("DUPLICATE" if matching_copies == 2 else "DUPLICATE_NAME")
    assert row["duplicate_name"] is True
    assert report["summary"]["content_mismatches"] == (0 if matching_copies else 1)


def test_duplicate_names_in_console_and_reports(trees, tmp_path, capsys):
    """All duplicate source paths appear in console and serialized error details."""
    a, b = trees
    paths = [put(a, "one/file"), put(a, "two/FILE"), put(a, "three/file")]
    put(b, "file")
    csv_path, json_path = tmp_path / "report.csv", tmp_path / "report.json"
    assert ct.main([str(a), str(b), "--report", str(csv_path), "--json", str(json_path)]) == 2
    console = capsys.readouterr().out
    for path in paths:
        assert str(path) in console
    report = json.loads(json_path.read_text(encoding="utf-8"))
    message = next(e["explanation"] for e in report["errors"] if "unique" in e["explanation"])
    assert all(str(path) in message for path in paths)
