# FreightFox JSON Dataset API
A Java Spring Boot REST API for storing JSON records and querying datasets with dynamic grouping and sorting.

## Tech Stack
- Java 21
- Spring Boot 4.1.1
- Spring Data JPA
- MySQL
- Maven
- Jackson
- JUnit

## Prerequisites
- Java 21
- MySQL Server

## Database Setup
Create the database: `CREATE DATABASE freightfox_json_dataset;`
Configure DB_URL, DB_USERNAME, and DB_PASSWORD environment variables.

## Run the Application
Run `.\mvnw.cmd spring-boot:run` from PowerShell.
If port 8080 is occupied, use port 8081.

## API Endpoints
POST `/api/dataset/{datasetName}/record` — insert a JSON record.
GET `/api/dataset/{datasetName}/query?groupBy=department` — group records.
GET `/api/dataset/{datasetName}/query?sortBy=salary` — sort records.
GET `/api/dataset/{datasetName}/query?groupBy=department&sortBy=salary` — group and sort records.

## Run Tests
Run `.\mvnw.cmd test`.

## Build
Run `.\mvnw.cmd package`.

## Security
Never commit database passwords or other secrets.
