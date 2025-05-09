# cics-java-osgi-jdbc
[![Build](https://github.com/cicsdev/cics-java-osgi-jdbc/actions/workflows/java.yaml/badge.svg?branch=cicsts/v5.5)](https://github.com/cicsdev/cics-java-osgi-jdbc/actions/workflows/java.yaml)

This sample demonstrates how to code, build, and deploy a CICS Java OSGi application that makes JDBC calls to Db2. It makes use of the employee sample table supplied with Db2 for z/OS, and allows you to display employee information from the table EMP.


## Requirements
* CICS TS V5.5 or later
* A connected CICS DB2CONN resource. For more information, see [CONFIGURING](#configuring)
* An OSGi JVM server
* Java SE 1.8 or later on the workstation
* IBM Db2 V11 or later on z/OS

## Downloading

- Clone the repository using your IDEs support, such as the Eclipse Git plugin
- **or**, download the sample as a [ZIP](https://github.com/cicsdev/cics-java-osgi-jdbc/archive/cicsts/v6.1.zip) and unzip onto the workstation

> [!TIP]
> Eclipse Git provides an 'Import existing Projects' check-box when cloning a repository.

---

## Building
The sample includes an Eclipse project configuration, a Gradle build, a Maven POM, and Gradle/Maven Wrappers offering a wide range of build options with the tooling and IDE of your choice.

Choose from the following approach:
* Use the built-in Eclipse and CICS Explorer SDK capability
* Use Eclipse with Buildship (Gradle), or m2e (Maven) to drive Gradle, or Maven.
* Use the command line, or IDE terminal, to drive Gradle or Apache Maven (if installed on your workstation)
* Use the command line, or IDE terminal, or IDE support for Wrappers, to drive the supplied Gradle or Apache Maven Wrappers (with no requirement for Gradle, Maven, Eclipse, or CICS Explorer SDK to be installed)


** Note: ** If you import the project to your IDE, you might experience local project compile errors. To resolve these errors follow the relevant build section below.


### Option 1 - Building with Eclipse

The sample comes pre-configured for use with a standard JDK 1.8 and the CICS TS V5.5 Java EE 6/7 Target Platform. When you initially import the project to your IDE, if your IDE is not configured for a JDK 1.8, or does not have CICS Explorer SDK installed with the correct 'target platform' set, you might experience local project compile errors. 

To resolve issues:
* ensure you have the CICS Explorer SDK plug-in installed
* configure the Project's build-path, and Application Project settings to use your preferred JDK and Java compiler settings
* set the CICS TS Target Platform to your intended CICS target (Hint: Window | Preferences | Plug-in Development | Target Platform | Add | Template | Other...) 


### Option 2 - Building with Gradle

You don't necessarily need to fix the local errors, but to do so, you can run a tooling refresh on the cics-java-osgi-jdbc-app project. For example, in Eclipse: right-click on "Project", select "Gradle | Refresh Gradle Project".

The CICS JVM server name should be modified in the  `cics.jvmserver` property in the gradle build [cics-java-osgi-jdbc-bundle/build.gradle](cics-java-osgi-jdbc-bundle/build.gradle) to match the required CICS JVMSERVER resource name, or alternatively can be set on the command line (see below).

If you have the Gradle buildship plug-in available, use the right-click **Run As...** menu on the cics-java-osgi-jdbc project to configure and run the `clean` and `build` tasks. Otherwise choose from the command-line approaches.

**Gradle Wrapper (Linux/Mac):**
```shell
./gradlew clean build
```

**Gradle Wrapper (Windows):**
```shell
gradle.bat clean build
```

**Gradle (command-line):**
```shell
gradle clean build
```

**Gradle (command-line & setting jvmserver):**
```shell
gradle clean build -Pcics.jvmserver=MYJVM
```

A JAR file is created inside the `cics-java-osgi-jdbc-app/build/libs` directory and a CICS bundle ZIP file inside the `cics-java-osgi-jdbc-bundle/build/distributions` directory.


### Option 3 - Building with Apache Maven

You don't necessarily need to fix the local errors, but to do so, you can run a tooling refresh on the cics-java-osgi-jdbc-app project. For example, in Eclipse: right-click on "Project", select "Maven -> Update Project...".

> [!TIP]
> In Eclipse, Gradle (buildship) is able to fully refresh and resolve the local classpath even if the project was previously updated by Maven. However, Maven (m2e) does not currently reciprocate that capability. If you previously refreshed the project with Gradle or with the CICS Explorer SDK Java Libraries, you'll need to manually remove the 'Project Dependencies' entry on the Java build-path of your Project Properties to avoid duplication errors when performing a Maven Project Update.

The CICS JVM server name should be modified in the `<cics.jvmserver>` property in the [`pom.xml`](pom.xml) to match the required CICS JVMSERVER resource name, or alternatively can be set on the command line (see below).

If you have the Maven m2e plug-in available, use the right-click **Run As...** menu on the cics-java-osgi-jdbc project to configure and run the `clean` and `verify` tasks. Otherwise choose from the command-line approaches.

**Maven Wrapper (Linux/Mac):**
```shell
./mvnw clean verify
```

**Maven Wrapper (Windows):**
```shell
mvnw.cmd clean verify
```

**Maven (command-line):**
```shell
mvn clean verify
```

**Maven (command-line & setting jvmserver):**
```shell
mvn clean verify -Dcics.jvmserver=MYJVM
```

A JAR file is created inside the `cics-java-osgi-jdbc-app/target` directory and a CICS bundle ZIP file inside the `cics-java-osgi-jdbc-bundle/target` directory.

---


## Configuring
### Configure CICS JCL
To allow your CICS region to connect to DB2, we need to add some configuration to the JCL.

```
//  SET DB2=V13                           - DB2 Version
...
<variables>
...
DB2CONN=YES
//STEPLIB
...
//         DD DISP=SHR,DSN=SYS2.DB2.&DB2..SDSNLOAD
//         DD DISP=SHR,DSN=SYS2.DB2.&DB2..SDSNLOD2
//         DD DISP=SHR,DSN=DSN&DB2.P2.RUNLIB.LOAD
```

### Configure the JVM Profile
Configure the JVM profile of the OSGi JVM server to include the Db2 driver JARs in the `OSGI_BUNDLES` environment variable and the Db2 library in the `LIBRARY_SUFFIX` environment variable.

> Note: The name of your JVM profile is assumed to be 'DFHOSGI' but can be changed in your Maven/Gradle build files
```
DB2_PATH=/usr/lpp/db2v13
OSGI_BUNDLES=&DB2_PATH;/jdbc/classes/db2jcc4.jar,\
             &DB2_PATH;/jdbc/classes/db2jcc_license_cisuz.jar
LIBPATH_SUFFIX=&DB2_PATH;/jdbc/lib
```
>Note: This example is using db2v13, this version must be consistent to the version set in your JCL.
As an example, see the provided [JVM profile template](etc/jvmprofiles/DFHOSGI.jvmprofile). If necessary, restart the JVM server.

### Option 1 - Configure the DB2CONN with CEDA at a terminal

Ensure a CICS DB2CONN is installed and connected. 

```
CEDA DEFINE DB2CONN(JODBCONN) GROUP(CDEVJODB)
```
```
CEDA INSTALL DB2CONN(CDEVJODB) GROUP(CDEVJODB)
```

### Option 2 - Configure the DB2CONN with CICS Explorer
1. Definitions > Db2 > Db2 Connection Definitions
2. Right-click > New...
3. Fill in the Name and Group with `CDEVJODB`
4. Right-click and install the new definition
5. Ensure it is CONNECTED

> Note: The DB2ID differs between DB2 versions and the system you are running your CICS region on. Consult your CICS system programmer if you are unsure.

---

## Deploying to CICS
### Option 1 - Deploying using CICS Explorer SDK and the provided CICS bundle project
1. Deploy the CICS bundle project 'com.ibm.cics.server.examples.osgi.jdbc.bundle' from CICS Explorer using the **Export Bundle Project to z/OS UNIX File System** wizard. This CICS bundle includes the osgi bundlepart, the JODB transaction and the CDEVJODB program to run the sample.


### Option 2 - Deploying using CICS Explorer SDK with own CICS bundle project
1. Copy and paste the built JAR from your *projects/cics-java-osgi-jdbc-app/target* or *projects/cics-java-osgi-jdbc-app/build/libs* directory into a new Eclipse CICS bundle project.
2. Create a new OSGi bundlepart that references the JAR (OSGi bundle) file. 
3. Optionally customise the CICS bundle contents, perhaps adding a TRANDEF of your choice
4. Right click using the ** Export Bundle Project to z/OS UNIX File System ** wizard.


### Option 3 - Deploying using CICS Explorer (Remote System Explorer) and CICS Bundle ZIP
1. Connect to USS on the host system
2. Create the bundle directory for the project.
3. Copy & paste the built CICS bundle ZIP file from your *projects/cics-java-osgi-jdbc-bundle/target* or *projects/cics-java-osgi-jdbc-bundle/build/distributions* directory to z/FS on the host system into the bundle directory.
4. Extract the ZIP by right-clicking on the ZIP file > User Action > unjar...
5. Refresh the bundle directory


### Option 4 - Deploying using command line tools
1. Upload the built CICS bundle ZIP file from your *projects/cics-java-osgi-jdbc-bundle/target* or *projects/cics-java-osgi-jdbc-bundle/build/distributions* directory to z/FS on the host system (e.g. FTP).
2. Connect to USS on the host system (e.g. SSH).
3. Create the bundle directory for the project.
4. Move the CICS bundle ZIP file into the bundle directory.
5. Change directory into the bundle directoy.
6. Extract the CICS bundle ZIP file. This can be done using the `jar` command. For example:
   ```shell
   jar xf file.zip
   ```

---

## Installing the CICS bundle
### Installing the CICS bundle from a CICS terminal
1. Create a new bundle definition, setting the bundle directory to the deployed bundle directory:
   ```
   BUNDLE(CDEVJODB) GROUP(CDEVJODB) BUNDLEDIR(/path/to/bundle/directory)
   ```
2. Install the bundle


### Installing the CICS bundle with CICS Explorer
1. Definitions > Bundle Definitions
2. Right-click > New...
3. Fill in the Bundle and Group names as `CDEVJODB`
4. Fill in the Bundle Directory to point to the directory you expanded the ZIP
5. Install the bundle

---

## Running
To run the sample, run the transaction `JODB`.

The terminal should contain contents similar to the following:

```
JODB                                            
000001        BILBO         BAGGINS     2004.00 
000010    CHRISTINE            HAAS       90.00 
000020      MICHAEL        THOMPSON    41250.00 
000030          SAL            KWAN    38250.00 
000050         JOHN           GEYER    40175.00 
000060       IRVING           STERN    32250.00 
000070          EVA         PULASKI    36170.00 
000090       EILEEN       HENDERSON    29750.00 
000100     THEODORE         SPENSER    26150.00 
000110     VINCENZO        LUCCHESI    46500.00 
000111          TES            TEST  1000000.00 
000120         SEAN       O'CONNELL    29250.00 
000130      DOLORES        QUINTANA    23800.00 
000140      HEATHER        NICHOLLS    28420.00 
000150        BRUCE         ADAMSON    25280.00                                 
```

The actual contents will be based on the values in the `EMP` table in the database.

## License
This project is licensed under [Apache License Version 2.0](LICENSE).

## Usage terms
By downloading, installing, and/or using this sample, you acknowledge that separate license terms may apply to any dependencies that might be required as part of the installation and/or execution and/or automated build of the sample, including the following IBM license terms for relevant IBM components:

• IBM CICS development components terms: https://www.ibm.com/support/customer/csol/terms/?id=L-ACRR-BBZLGX