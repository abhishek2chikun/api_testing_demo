# Orders Service API Tests

## Overview
This project contains JUnit5 and Rest-Assured tests for the Orders Service API. The tests cover various scenarios including happy paths, validation errors, not found cases, and response validation.

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
mvn test
```

## Test Structure
- `OrdersApiTest.java`: Contains tests for the Orders Service API.
- `config.properties`: Configuration file for test properties.
- `pom.xml`: Maven configuration file with dependencies.

## Dependencies
- JUnit5
- Rest-Assured
- Hamcrest
- Jackson Databind

## License
This project is licensed under the MIT License.
