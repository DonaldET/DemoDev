"""
List Windows applications installed on the C: drive.

This script inspects the standard Windows Registry uninstall locations for
machine-wide 64-bit applications, machine-wide 32-bit applications, and
current-user applications. It extracts selected metadata for each installed
program, filters the results to entries that appear to reference the C: drive,
removes duplicate application records by display name, and prints a sorted
summary containing each application's name, version, and installation location.

The script is intended to be run on Windows because it depends on the
``winreg`` module and Windows Registry uninstall keys.
"""

import winreg
from typing import Any

UNINSTALL_KEYS = [
    (winreg.HKEY_LOCAL_MACHINE,
     r"SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall"),
    (winreg.HKEY_LOCAL_MACHINE,
     r"SOFTWARE\WOW6432Node\Microsoft\Windows\CurrentVersion\Uninstall"),
    (winreg.HKEY_CURRENT_USER,
     r"SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall"),
]

FIELDS = [
    "DisplayName",
    "DisplayVersion",
    "InstallLocation",
    "DisplayIcon",
    "UninstallString"
]


def _get_value(key, name) -> str:
    """
    Return a named value from an open Windows Registry key.

    Args:
        key: An open registry key handle returned by ``winreg.OpenKey``.
        name: The registry value name to read from the key.

    Returns:
        The registry value when it exists. If the value is missing, returns a
        descriptive placeholder string identifying the missing value.
    """
    try:
        return winreg.QueryValueEx(key, name)[0]
    except FileNotFoundError:
        return f"<<{key}[{name}] not found"


def _installed_on_c(app_id) -> bool:
    """
    Determine whether an application record appears to be installed on C:.

    The check examines selected registry fields that commonly contain file
    system paths: ``InstallLocation``, ``DisplayIcon``, and ``UninstallString``.
    A record is treated as being installed on C: when one of those fields is a
    string beginning with ``C:\\``.

    Args:
        app_id: Dictionary of application metadata read from the registry.

    Returns:
        True when the application metadata indicates a C: drive path;
        otherwise, False.
    """
    for field in ("InstallLocation", "DisplayIcon", "UninstallString"):
        value = app_id.get(field, "")
        return True if isinstance(value, str) and value.upper().startswith("C:\\") else False


def _build_collection() -> list[dict]:
    """
    Find registry entries that relate to the C: drive, uninstall, and
    installed applications.

    :return: list of dictionaries containing metadata about installed applications
    """
    apps = []

    for hive, path in UNINSTALL_KEYS:
        try:
            with winreg.OpenKey(hive, path) as uninstall:
                count = winreg.QueryInfoKey(uninstall)[0]

                for i in range(count):
                    try:
                        subkey_name = winreg.EnumKey(uninstall, i)
                        with winreg.OpenKey(uninstall, subkey_name) as subkey:
                            app = {field: _get_value(subkey, field) for field in FIELDS}

                            if app["DisplayName"] and _installed_on_c(app):
                                apps.append(app)

                    except OSError:
                        print(f"Registry error {OSError.errno}:'{OSError.strerror}' on entry {i} of {count}")

        except FileNotFoundError:
            print(f"error {FileNotFoundError.strerror} for {hive} and {path}")

    return apps


def _display_unique_collection(apps) -> list[Any]:
    """
    Filter to collect useful entries for a collection of application metadata about applications installed on the C drive.
    :param apps: a list of dictionaries containing metadata about installed applications
    :return: the filtered list of dictionaries containing metadata about installed applications
    """
    unique = {}
    for app in apps:
        if app["InstallLocation"].lower().endswith(" not found") or \
                app["DisplayName"].lower().endswith(" not found"):
            pass
        else:
            unique[app["DisplayName"]] = app

    apps = sorted(unique.values(), key=lambda a: a["DisplayName"].lower())

    print(f"\nApplications installed on C: ({len(apps)})")
    print("-" * 120)

    for app in apps:
        print(f"Name     : {app['DisplayName']}")
        print(f"Version  : {app['DisplayVersion']}")
        print(f"Location : {app['InstallLocation']}")
        print("-" * 120)
    return apps


def main():
    apps = _build_collection()
    _display_unique_collection(apps)


if __name__ == '__main__':
    main()
