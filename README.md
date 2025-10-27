# Orders Service API Tests

This project contains JUnit5 and Rest-Assured tests for the Orders Service API.

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher

## Running the Tests

1. Ensure the Orders Service is running locally on port 8002 or set the `baseUrl` in `config.properties` or as a system property/environment variable.

2. Run the tests using Maven:

   ```bash
   mvn test
   ```

## Configuration

- The base URL for the API can be configured via:
  - System property: `-DbaseUrl=http://your-api-url`
  - Environment variable: `BASE_URL`
  - `config.properties` file

## Test Coverage

- **Happy Path**: Successful requests with valid data
- **Validation Errors**: Invalid inputs, missing required fields
- **Not Found**: Requests for non-existent resources
- **Response Validation**: Schema validation, required fields, data types

## Dependencies

- JUnit 5
- Rest-Assured
- Hamcrest
- Jackson Databind