## Build Information
Naming and structural conventions, along with collected statistics on Maven build structure.

### Java Package Naming Conventions

Projects have a package structure that reflects potential use outside of the ***DemoDev*** repository ("don") and for repository use only ("demo").

- Packaging Conventions - "don."*:


`D:\GitHub>findstr /srb "package " *.java | find "package don" > pkg_don.log`
`D:\GitHub>find /C "package" pkg_don.log`
`---------- PKG_DON.LOG: 78`

- Packaging Conventions - "demo."*:



`D:\GitHub>findstr /srb "package " *.java | find "package demo" > pkg_demo.log`
`D:\GitHub>find /C "package" pkg_demo.log`
`---------- PKG_DEMO.LOG: 280`

- Packaging Conventions - Other:


`D:\GitHub>findstr /srb "package " *.java | find /V "package don" | find /V "package demo" > pkg_other.log`
`D:\GitHub>find /C "package" pkg_other.log`
`---------- PKG_OTHER.LOG: 0`

### Detailed Build Statistics

**TBD**