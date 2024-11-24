# shrink.it

A microservices-based URL shortening system built with Java Spring Boot and Go.

## Build Instructions

### Prerequisites

- JDK 17+ (Eclipse Temurin preferred)
- Gradle 8.10.2+
- Docker 27.2.0+
- Nginx 1.26.2+
- Node.js 20.11.0+ (for the frontend)

### Setting Up the Project

- Configure **nginx** using the provided `nginx/default.conf.template` file. Modify it to fit your deployment environment.

### Running the Backend
#### Building

Build the entire backend project:

```bash
./gradlew build
```

Build a specific backend module:

```bash
./gradlew :<module-name>:build
```

#### Running
Run the entire backend project:

```bash
./gradlew bootRun
```

Run a specific backend module:

```bash
./gradlew :<module-name>:bootRun
```

To run multiple instances of a module on different ports:

```bash
./gradlew :<module-name>:bootRun --args='--server.port=<port>'
```

### Running the Frontend

#### Setting Up
Create a `.env` file based on the `ui/.env.template` file and populate environment variables.

Install dependencies:
```bash
cd ui
npm ci
```

#### Running
Start development server:
```bash
npm run dev
```


### Running all services with Docker Compose
Docker Compose allows running both the backend and frontend services together.

Build Docker images:
```bash
docker-compose build
```

Start Docker containers:
```bash
docker-compose up
```

Or, to build and start the containers in one step:
```bash
docker-compose up --build
```

Stop running containers:
```bash
docker-compose down
```