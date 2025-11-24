# Orders API Tests

This project contains automated tests for the Orders Service API using JUnit5 and Rest-Assured.

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

## Test Structure

- Tests are located in `src/test/java/com/example/api/tests`.
- Configuration files are located in `src/test/resources`.

## Dependencies

- JUnit 5 for testing framework
- Rest-Assured for HTTP requests
- Hamcrest for assertions
- Jackson for JSON parsing

## License

This project is licensed under the MIT License.
