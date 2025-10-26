# Orders Service API Tests

## Overview
This project contains JUnit5 and Rest-Assured tests for the Orders Service API. The tests cover various scenarios including successful requests, validation errors, and edge cases.

## Running the Tests
1. Ensure that the Orders Service API is running on `http://localhost:8002`.
2. Use Maven to run the tests:
   ```bash
   mvn test
   ```

## Test Coverage
- Happy path: Successful order placement and retrieval.
- Validation errors: Tests for invalid inputs and missing fields.
- Not found: Tests for non-existent resources.
- Health check and service metadata retrieval.

## Dependencies
- JUnit5
- Rest-Assured
- Hamcrest
- Jackson Databind

## Authentication
Ensure valid credentials are provided in `auth_tokens` for the API to function correctly.