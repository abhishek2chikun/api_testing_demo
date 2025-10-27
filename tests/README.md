# Orders Service API Tests

This directory contains tests for the Orders Service API. The tests are written using `pytest` and `requests`.

## Running the Tests

1. Ensure the Orders Service is running locally on port 8002.
2. Install the required dependencies:
   ```bash
   pip install -r requirements.txt
   ```
3. Run the tests using `pytest`:
   ```bash
   pytest
   ```

## Test Structure

- `conftest.py`: Contains fixtures for test setup.
- `test_orders.py`: Contains tests for the Orders API endpoints.

## Test Scenarios

- **Happy Path**: Tests for successful requests with valid data.
- **Validation Errors**: Tests for invalid inputs and missing required fields.
- **Not Found**: Tests for requests to non-existent resources.
- **Response Validation**: Tests for schema validation and required fields.