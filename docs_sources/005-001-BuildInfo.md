## Build Information
Naming and structural conventions, along with collected statistics on Maven build structure. Key Maven plugins are documented as well.

### Java Package Naming Conventions

Projects have a package structure that reflects potential use outside of the ***DemoDev*** repository ("don") and for repository use only ("demo").

- Packaging Conventions - "don.*"

`D:\GitHub\DemoDev>findstr /srb "package " *.java | find "package don" > pkg_don.log`

`D:\GitHub\DemoDev>find /C "package" pkg_don.log`

`---------- PKG_DON.LOG: 78`

- Packaging Conventions - "demo.*"

`D:\GitHub\DemoDev>findstr /srb "package " *.java | find "package demo" > pkg_demo.log`

`D:\GitHub\DemoDev>find /C "package" pkg_demo.log`

`---------- PKG_DEMO.LOG: 280`

- Packaging Conventions - Other:

`D:\GitHub\DemoDev>findstr /srb "package " *.java | find /V "package don" | find /V "package demo" > pkg_other.log`

`D:\GitHub\DemoDev>find /C "package" pkg_other.log`
`---------- PKG_OTHER.LOG: 0`

### Maven Plugins

The plugins are used in the build structure to achieve build tasks.

| Plugin                    | Description                                        | Link                                                    |
| :------------------------ | :------------------------------------------------- | :------------------------------------------------------ |
| *`maven-compiler-plugin`* | Defines Javac compiler source and runtime Java versions | https://maven.apache.org/plugins/maven-compiler-plugin/ |
| *`maven-resources-plugin`* | Copies source and test resources to output | https://maven.apache.org/plugins/maven-resources-plugin/ |
| *`maven-jar-plugin`* | Packages code and resources into a Jar with a Manifest | https://maven.apache.org/plugins/maven-jar-plugin/ |
| *`maven-surefire-plugin`* | Runs the application tests based on testing dependencies | https://maven.apache.org/surefire/maven-surefire-plugin/ |



### Detailed Build Statistics

**TBD**