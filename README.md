# shrink.it

A microservices-based URL shortening system built with Java Spring Boot and Go.

## Build Instructions

### Prerequisites

- Eclipse Temurin JDK 17 or higher
- Gradle 8.10.2 or higher
- nginx 1.26.2 or higher

### Setting Up the Project

- Using `nginx/default.conf.template` as a template.

### Building the Project

To build the entire project, run the following command from the root directory:

```bash
./gradlew build
```

To build a specific module, run the following command from the root directory:

```bash
./gradlew :<module-name>:build
```

### Running the Project

To run the entire project, run the following command from the root directory:

```bash
./gradlew bootRun
```

To run a specific module, run the following command from the root directory:

```bash
./gradlew :<module-name>:bootRun
```

To run multiple instance, run the following command from the root directory:

```bash
./gradlew :<module-name>:bootRun --args='--server.port=<port>'
```
