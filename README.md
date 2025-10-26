# Orders Service API Tests

## Overview
This project contains automated tests for the Orders Service API using JUnit5 and Rest-Assured.

## Requirements
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
- Validation error scenarios for invalid inputs.
- Not found scenarios for non-existent resources.
- Health check and root endpoint tests.