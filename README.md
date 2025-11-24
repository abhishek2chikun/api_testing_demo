# Orders Service API Tests

## Overview
This project contains JUnit5 and Rest-Assured tests for the Orders Service API. The tests cover various scenarios including successful requests, validation errors, and not found errors.

## Prerequisites
- Java 11 or higher
- Maven 3.6 or higher

## Running the Tests
1. Ensure the Orders Service is running locally on port 8002 or set the `baseUrl` in `config.properties`.
2. Run the tests using Maven:
   ```bash
   mvn test
   ```

## Configuration
The base URL for the API can be configured via:
- System property: `-DbaseUrl=http://your-api-url`
- Environment variable: `BASE_URL`
- `config.properties` file

## Test Scenarios
- **Happy Path**: Tests for successful API requests with valid data.
- **Validation Errors**: Tests for invalid inputs and missing required fields.
- **Not Found**: Tests for requests to non-existent resources.
- **Health Check**: Tests the health endpoint for service availability.

## Dependencies
- JUnit 5
- Rest-Assured
- Hamcrest
- Jackson Databind