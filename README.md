# Orders Service API Tests

## Overview
This project contains automated tests for the Orders Service API using JUnit5 and Rest-Assured.

## Running the Tests
1. Ensure that the Orders Service API is running on `http://localhost:8002`.
2. Use Maven to run the tests:
   ```bash
   mvn test
   ```

## Test Coverage
- Happy path scenarios for placing and retrieving orders.
- Validation error scenarios for missing or invalid inputs.
- Not found scenarios for non-existent resources.
- Health check and root endpoint tests.