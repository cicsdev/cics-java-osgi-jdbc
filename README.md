# cics-java-osgi-jdbc
This sample demonstrates an OSGi JDBC application integrated with IBM CICS that can be deployed to a CICS OSGi JVM server.

The application makes use of the employee sample table supplied with Db2 for z/OS. The application allows you to display employee information from the table EMP. The sample also provides a set of Gradle and Maven build files for use either in Eclipse or standalone build environments.

## Requirements
* CICS TS V5.5 or later
* A connected CICS DB2CONN resource. For more information, see [Deploying](#deploying)
* An OSGi JVM server
* Java SE 1.8 or later on the workstation
* IBM Db2 V11 or later on z/OS
* An Eclipse development environment on the workstation (optional)
* Either Gradle or Apache Maven on the workstation (optional if using Wrappers)

## Building
You can build the sample using an IDE of your choice, or you can build it from the command line. For both approaches, using the supplied Gradle or Maven wrapper is the recommended way to get a consistent version of build tooling.

On the command line, you simply swap the Gradle or Maven command for the wrapper equivalent, `gradlew` or `mvnw` respectively.

For an IDE, taking Eclipse as an example, the plug-ins for Gradle *buildship* and Maven *m2e* will integrate with the "Run As..." capability, allowing you to specify whether you want to build the project with a Wrapper, or a specific version of your chosen build tool.

The required build-tasks are typically `clean build` for Gradle and `clean verify` for Maven. Once run, Gradle will generate a JAR file in the `build/libs` directory, while Maven will generate it in the `target` directory.

**Note:** If you import the project to your IDE, you might experience local project compile errors. To resolve these errors you should run a tooling refresh on that project.
For example, in Eclipse: 
* for Gradle, right-click on "Project", select "Gradle -> Refresh Gradle Project", 
* for Maven, right-click on "Project", select "Maven -> Update Project...".

> Tip: *In Eclipse, Gradle (buildship) is able to fully refresh and resolve the local classpath even if the project was previously updated by Maven. However, Maven (m2e) does not currently reciprocate that capability. If you previously refreshed the project with Gradle, you'll need to manually remove the 'Project Dependencies' entry on the Java build-path of your Project Properties to avoid duplication errors when performing a Maven Project Update.*

#### Gradle Wrapper (command line)
Run the following in a local command prompt:

On Linux or Mac:

```shell
./gradlew clean build
```
On Windows:

```shell
gradlew.bat clean build
```

This creates a JAR file in the `build/libs` directory.

#### Maven Wrapper (command line)
Run the following in a local command prompt:

On Linux or Mac:

```shell
./mvnw clean verify
```

On Windows:

```shell
mvnw.cmd clean verify
```

This creates a JAR file in the `target` directory.

## Deploying
Configure the JVM profile of the OSGi JVM server to include the Db2 driver JARs in the `OSGI_BUNDLES` environment variable and the Db2 library in the `LIBRARY_SUFFIX` environment variable. For more information, see the provided [JVM profile template](etc/jvmprofiles/DFHOSGI.jvmprofile). If necessary, restart the JVM server.

Ensure a CICS DB2CONN is installed and connected.

### Deploying using command line tools
1. Upload the built CICS bundle ZIP file from your *projects/cics-java-osgi-jdbc-bundle/target* or *projects/cics-java-osgi-jdbc-bundle/build/distributions* directory to z/FS on the host system (e.g. FTP).
2. Connect to USS on the host system (e.g. SSH).
3. Create the bundle directory for the project.
4. Move the CICS bundle ZIP file into the bundle directory.
5. Change directory into the bundle directoy.
6. Extract the CICS bundle ZIP file. This can be done using the `jar` command. For example:
   ```shell
   jar xf file.zip
   ```

### Deploying using CICS Explorer
1. Copy and paste the built JAR from your *projects/cics-java-osgi-jdbc-app/target* or *projects/cics-java-osgi-jdbc-app/build/libs* directory into a Eclipse CICS bundle project.
2. Create a new OSGi bundlepart that references the JAR file. 
3. Deploy the CICS bundle project from CICS Explorer using the **Export Bundle Project to z/OS UNIX File System** wizard.

### Installing the bundle
1. Create a new bundle definition, setting the bundle directory to the deployed bundle directory:
   ```
   BUNDLE(CDEVJODB) GROUP(CICSDEV) BUNDLEDIR(/path/to/bundle/directory)
   ```
2. Install the bundle

## Running
To run the sample, run the transaction `JODB`.

The terminal should contain contents similar to the following:

```
JODB                                                                            
000001        BILBO         BAGGINS     2004.00                                 
000010    CHRISTINE            HAAS    52750.00                                 
000020      MICHAEL        THOMPSON    41250.00                                 
000030          SAL            KWAN    38250.00                                 
000050         JOHN           GEYER    40175.00                                 
000060       IRVING           STERN    32250.00                                 
000070          EVA         PULASKI    36170.00                                 
000090       EILEEN       HENDERSON    29750.00                                 
000100     THEODORE         SPENSER    26150.00                                 
000110     VINCENZO        LUCCHESI    46500.00                                  
000120         SEAN       O'CONNELL    29250.00                                 
000130      DOLORES        QUINTANA    23800.00                                 
000140      HEATHER        NICHOLLS    28420.00                                 
000150        BRUCE         ADAMSON    25280.00                                 
000160    ELIZABETH          PIANKA    22250.00                                 
```

The actual contents will be based on the values in the `EMP` table in the database.

## License
This project is licensed under [Apache License Version 2.0](LICENSE).
