# Assets-Service

## About
The Assets Service manages core information about physical assets, 
including buildings, floors, rooms, and devices. It serves as the 
system’s source of truth for asset structure and relationships, 
supporting location-based operations and access control.

## System Requirements

- Java 21
- Apache Maven 3.9.9
- Docker (if running the service within the Docker container)

## Configuration

### Database
Configure database connection in `application.yaml` file:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/evidentor
    driverClassName: org.postgresql.Driver
    username: postgres
    password: postgres
```

## How to Install?

### 1. Clone the repository
```shell
git clone https://github.com/Evidentor/Assets-Service.git
cd Assets-Service
```

### 2. Install dependencies
```shell
mvn clean install
```

## How to Run?

### Run with java
```shell
java --enable-preview -jar target/*.jar
```

### Run with docker
#### 1. Build the docker image
```shell
docker build -t assets-service .
```

#### 2. Create the docker container
```shell
docker run -d --network host --name assets-service assets-service:latest
```

#### 3. Stop the docker container
```shell
docker stop assets-service
```

#### 4. Start the docker container
```shell
docker start assets-service
```

## How to Test?
```shell
mvn test
```
