# FreightFox JSON Dataset API

A Java Spring Boot REST API for storing JSON records in a relational database and querying datasets with dynamic grouping and sorting.

## Live Deployment

**Base URL:** https://freightfox-json-dataset-api-production.up.railway.app

## API Endpoints

### 1. Insert a Record

**POST** `/api/dataset/{datasetName}/record`

Example request body:

```json
{
  "name": "Amit",
  "department": "IT",
  "salary": 50000
}
```

### 2. Query a Dataset

**GET** `/api/dataset/{datasetName}/query`

Supported query parameters:

- `groupBy` — field used to group records.
- `sortBy` — field used to sort records in ascending order.

Example:

`GET /api/dataset/employees/query?groupBy=department&sortBy=salary`

The response contains `groupedRecords` and `sortedRecords`.

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

Create the database:

```sql
CREATE DATABASE freightfox_json_dataset;
```

Configure the following environment variables:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`

## Run the Application

On Windows PowerShell, run:

```powershell
.\mvnw.cmd spring-boot:run
```

If port 8080 is occupied, configure another available port.

## Run Tests

```powershell
.\mvnw.cmd test
```

## Build

```powershell
.\mvnw.cmd package
```

## Security

Never commit database passwords, credentials, or other secrets to the repository.
