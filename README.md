## Automated tests using Serenity, Cucumber and Maven

This project contains automation tests for Data Governance application

## Pre-Requisite

1. Java with JAVA_HOME
2. Maven
3. Intellij idea
4. Drivers according to the new version of browser
5. Browser installed

## Key Features

1. `drivers`: This folder contains a list of drivers for windows/linux/mac

2. `src/test/java/utilities`: This folder consists of all the utilities(eg: screenshot, DB Connection etc) that are used in the application

3. `src/test/java/steps`: This folder contains all the files that represent the level of abstraction between the code that interacts with the application

4. `src/test/java/services`: This folder contains all the files that specifies the methods to call the API end points.

5. `src/test/resources/feature` : This folder consists of all the feature file that acts as an entry point to the Cucumber tests.It serves as an automation test script. A feature file can contain multiple scenarios.

6. `src/test/resource/env.conf` : This file consists of list of environment variables

7. `pom.xml` : It is maven configuration file. It is an XML file that contains information about the project and configuration details used by Maven to build the project.

8. `serenity.properties`: It specifies the browser and other settings to be done.

## Getting Started

This section is for executing the application through serenity framework on test environment

1. Clone the repository:

```
git clone <link>
```

2. Run the tests like this:

```
mvn clean verify
```
By default, the tests run against TEST environment but you can override it by passing env property as below. Configuration details exist in env.conf file under resources folder
```
mvn clean verify -Denv=demo
```

By default, the web tests run on Chrome (assuming windows) in headless mode. If you need to run with other platforms/browsers, modify the serenity.properties file or run the tests like this:
```
mvn clean verify -Dwebdriver.driver=firefox
```
The reports will be generated in `target/site/serenity`.

