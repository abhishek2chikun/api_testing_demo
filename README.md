# Orders Service API Tests

## Overview
This project contains JUnit5 and Rest-Assured tests for the Orders Service API. The tests cover various scenarios including happy paths, validation errors, not found cases, and response validation.

## Prerequisites
- Java 11 or higher
- Maven 3.6 or higher

## Running the Tests
1. Ensure the Orders Service API is running locally on port 8002 or set the `baseUrl` in `config.properties`.
2. Run the tests using Maven:
   ```bash
   mvn test
   ```

## Configuration
- The base URL for the API can be configured via the `baseUrl` property in `config.properties` or by setting the `BASE_URL` environment variable.

## Test Scenarios
- **Happy Path**: Successful requests with valid data.
- **Validation Errors**: Invalid inputs, missing required fields, wrong data types.
- **Not Found**: Requests for non-existent resources (404).
- **Response Validation**: Schema validation, required fields, data types.

## Dependencies
- JUnit 5
- Rest-Assured
- Hamcrest
- Jackson Databind

## License
This project is licensed under the MIT License.