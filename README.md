# CICS OSGi Java database connectivity (JDBC) application

This sample demonstrates an OSGi JDBC application integrated with IBM CICS that can be deployed to a CICS OSGi JVM server.

The application makes use of the employee sample table supplied with Db2 for z/OS. The application allows you to display employee information from the table EMP. The sample also provides a set of Gradle and Maven build files for use either in Eclipse or standalone build environments.

Branches are provided for both CICS TS V5 and CICS TS V6 highlighting changes in JCICS v2 at CICS TS V6. The V5 branch uses the OSGi CICS-MainClass header to denote the program entry point, and the CICS TS V6 branch uses the new @CICSProgram annotation.

## Versions
| CICS TS for z/OS Version | Branch                                 | Minimum Java Version | Build Status |
|--------------------------|----------------------------------------|----------------------|--------------|
| 5.5, 5.6                 | [cicsts/v5.5](/../../tree/cicsts/v5.5) | 8                    | [![Build](https://github.com/cicsdev/cics-java-osgi-jdbc/actions/workflows/java.yml/badge.svg?branch=cicsts%2Fv5.5)](https://github.com/cicsdev/cics-java-osgi-jdbc/actions/workflows/java.yml) |
| 6.1                      | [cicsts/v6.1](/../../tree/cicsts/v6.1) | 17                   | [![Build](https://github.com/cicsdev/cics-java-osgi-jdbc/actions/workflows/java.yml/badge.svg?branch=cicsts%2Fv6.1)](https://github.com/cicsdev/cics-java-osgi-jdbc/actions/workflows/java.yml) |

## License
This project is licensed under [Eclipse Public License - v 2.0](LICENSE).

## Usage terms
By downloading, installing, and/or using this sample, you acknowledge that separate license terms may apply to any dependencies that might be required as part of the installation and/or execution and/or automated build of the sample, including the following IBM license terms for relevant IBM components:

• IBM CICS development components terms: https://www.ibm.com/support/customer/csol/terms/?id=L-ACRR-BBZLGX
