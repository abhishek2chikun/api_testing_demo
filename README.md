# Orders Service API Tests

## Overview
This project contains JUnit5 and Rest-Assured tests for the Orders Service API. The tests cover various scenarios including successful requests, validation errors, and edge cases.

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
- Happy path: Successful requests with valid data.
- Validation errors: Invalid inputs, missing required fields.
- Not found: Requests for non-existent resources.
- Health check and service metadata retrieval.