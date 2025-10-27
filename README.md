# API Tests for Orders Service

## Overview
This project contains automated tests for the Orders Service API using JUnit5 and Rest-Assured.

## Requirements
- Java 11 or higher
- Maven

## Running the Tests
1. Clone the repository.
2. Navigate to the project directory.
3. Run the tests using Maven:
   ```bash
   mvn test
   ```
4. To specify a different base URL, use the `-DbaseUrl=<your_base_url>` option:
   ```bash
   mvn test -DbaseUrl=http://your.api.url
   ```

## Test Coverage
- Happy path scenarios for placing and retrieving orders.
- Validation error scenarios for missing or invalid fields.
- Not found scenarios for non-existent resources.
- Health check and root endpoint tests.
- Authentication enforcement and error response structure validation.
