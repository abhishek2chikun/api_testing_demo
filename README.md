# Orders API Test Suite

This test suite is designed to validate the Orders Service API using JUnit5 and Rest-Assured.

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher

## Setup

1. Clone the repository.
2. Navigate to the project directory.

## Configuration

- The base URL for the API can be configured via the `baseUrl` system property or the `BASE_URL` environment variable.
- Default fallback URL is `http://localhost:8002`.

## Running Tests

To run the tests, execute the following command:

```bash
mvn clean test
```

## Test Coverage

The test suite covers the following scenarios:

- Successful order placement and retrieval
- Validation errors for incorrect inputs
- Handling of non-existent resources
- Health check endpoint validation

## Dependencies

- JUnit 5
- Rest-Assured
- Hamcrest
- Jackson Databind

## Notes

- Ensure the Orders Service API is running and accessible at the configured base URL before executing the tests.