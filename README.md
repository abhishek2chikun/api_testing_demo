# Orders Service API Tests

This project contains comprehensive JUnit 5 + Rest-Assured tests for the Orders Service API described in the KAN-4 Jira epic.

Prerequisites:
- Java 11+
- Maven 3.6+
- The Orders Service running locally (default: http://localhost:8002)
- (Optional) Schemathesis installed if you want to run property-based schema checks:
  pip install schemathesis

Configuration:
- The tests determine the base URL in the following order:
  1. System property `baseUrl` (e.g., mvn -DbaseUrl=http://host:port test)
  2. Environment variable `BASE_URL`
  3. Fallback to http://localhost:8002

Running tests:
- Run all tests:
  mvn -DbaseUrl=http://localhost:8002 test

- To run the Schemathesis-based property tests (optional):
  1. Ensure `schemathesis` CLI is installed and available in PATH.
  2. Set environment variable `RUN_SCHEMATHESIS=true` before running tests:
     RUN_SCHEMATHESIS=true mvn -DbaseUrl=http://localhost:8002 test
  The Schemathesis test will be skipped automatically if the CLI is not available or RUN_SCHEMATHESIS is not set to "true".

Notes:
- Tests are resilient to environments where broker credentials (auth_tokens) are absent. When credentials are absent, the service returns HTTP 404 per the spec; tests assert either successful behavior (when credentials are present) or correct error envelopes (when they are absent).
- The tests include validation, happy path, not-found, auth-related checks, edge cases, and a Schemathesis runner integration (optional).