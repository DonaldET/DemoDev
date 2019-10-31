## Build Information
Naming and structural conventions, along with collected statistics on Maven build structure.

### Java Package Naming Conventions

*Packaging Conventions - "don."*:

`D:\GitHub>findstr /srb "package " *.java | find "package don" > pkg_don.log`
`D:\GitHub>find /C "package" pkg_don.log`
`---------- PKG_DON.LOG: 87`

*Packaging Conventions - "demo."*:

`findstr /srb "package " *.java | find "package demo" > pkg_demo.log`
`D:\GitHub>find /C "package" pkg_demo.log`
`---------- PKG_DEMO.LOG: 262`

Packaging Conventions - Other:

`D:\GitHub>findstr /srb "package " *.java | find /V "package don" | find /V "package demo" > pkg_other.log`
`D:\GitHub>find /C "package" pkg_other.log`
`---------- PKG_OTHER.LOG: 9`

Packages with "don.*" are used for utilities 



### Detailed Build Statistics

**TBD**