# API Tests for Orders Service

## Overview
This project contains automated tests for the Orders Service API using JUnit5 and Rest-Assured.

## Prerequisites
- Java 11 or higher
- Maven

## Running the Tests
1. Clone the repository.
2. Navigate to the project directory.
3. Run the following command to execute the tests:
   ```bash
   mvn test
   ```

## Test Coverage
- Happy path scenarios for placing and retrieving orders.
- Validation error scenarios for missing or invalid fields.
- Not found scenarios for non-existent resources.
- Health check and service metadata retrieval.

## Configuration
The base URL for the API is set in `src/test/resources/config.properties`. Adjust it if necessary.