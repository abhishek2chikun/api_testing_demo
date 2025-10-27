# Orders Service API Tests

## Overview
This project contains JUnit5 and Rest-Assured tests for the Orders Service API. The tests cover various scenarios including happy paths, validation errors, not found cases, and response validation.

## Prerequisites
- Java 11 or higher
- Maven 3.6 or higher

## Running the Tests
1. Ensure the Orders Service API is running locally on port 8002 or set the `baseUrl` in `config.properties` to the appropriate URL.
2. Run the tests using Maven:
   ```bash
   mvn test
   ```

## Configuration
- The base URL for the API can be configured via the `baseUrl` property in `config.properties` or by setting the `baseUrl` system property or `BASE_URL` environment variable.

## Test Structure
- Tests are organized in the `src/test/java/com/example/api/tests` directory.
- Each test class corresponds to a specific module or endpoint of the API.

## Dependencies
- JUnit 5 for testing framework
- Rest-Assured for HTTP requests
- Hamcrest for assertions
- Jackson Databind for JSON parsing
