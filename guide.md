# Guide for Using in Linux Environment

## Prerequisites
- Java 21 must be installed and set as JAVA_HOME
- The scripts automatically set JAVA_HOME to `/usr/lib/jvm/java-21-openjdk-amd64`

## Building the Project

To build all modules, run:
```
./mvnw clean compile
```

## Running the Studies

### Microservices Study
- Run with Maven: `./microservices-study.sh`
- Run with JAR: First build `./mvnw -pl spring-microservices-questions package`, then `./microservices-study-jar.sh`

### Security Study
- Run with Maven: `./security-study.sh`
- Run with JAR: First build `./mvnw -pl spring-security-questions package`, then `./security-study-jar.sh`

### Spring Data Study
- Run with Maven: `./spring-data-study.sh`
- Run with JAR: First build `./mvnw -pl spring-data-interview-study package`, then `./spring-data-study-jar.sh`

### Concurrency Study
- Operator / search reference: `concurrency-study.messages.txt`
- Run with Maven: `./concurrency-study.sh` (pass args after the script, e.g. `./concurrency-study.sh list`)
- Run with JAR: First build `./mvnw -pl concurrency-interview-study package`, then `./concurrency-study-jar.sh`