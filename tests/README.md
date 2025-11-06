# Orders Service API Tests

## Overview

This test suite is designed to validate the Orders Service API, which allows placing and retrieving orders across multiple brokers. The tests cover various scenarios including successful requests, validation errors, not found errors, and service metadata retrieval.

## Test Structure

- `conftest.py`: Contains common fixtures used across tests.
- `test_orders.py`: Contains tests for order placement, retrieval, and listing.
- `test_schemathesis.py`: Uses Schemathesis to validate the API against its OpenAPI specification.

## Running the Tests

1. Ensure the Orders Service is running locally on port 8002.
2. Install the required dependencies:
   ```bash
   pip install -r requirements.txt
   ```
3. Run the tests using pytest:
   ```bash
   pytest
   ```

## Dependencies

- `pytest`: For running the test suite.
- `requests`: For making HTTP requests in tests.
- `schemathesis`: For property-based testing against the OpenAPI specification.

## Notes

- Ensure that the OpenAPI specification is accessible at `http://localhost:8002/openapi.json`.
- The service should be configured to allow CORS for all origins during testing.