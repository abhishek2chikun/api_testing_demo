# Orders Service API Tests

## Running the Tests

1. Ensure you have the Orders Service running locally on port 8002.
2. Install the required dependencies:
   ```bash
   pip install -r requirements.txt
   ```
3. Run the tests using pytest:
   ```bash
   pytest tests/
   ```

## Test Structure

- `conftest.py`: Contains fixtures and configuration for the tests.
- `test_orders.py`: Tests for the Orders API endpoints.
- `test_schemathesis.py`: Tests for validating API endpoints against the OpenAPI schema.