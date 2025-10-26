# Orders Service API Tests

## Running the Tests

1. Ensure that the Orders Service API is running locally on port 8002.
2. Install the required dependencies:
   ```bash
   pip install -r requirements.txt
   ```
3. Run the tests using pytest:
   ```bash
   pytest tests/
   ```

## Test Structure

- `conftest.py`: Contains fixtures for setting up test data and configurations.
- `test_orders.py`: Tests for the Orders API endpoints.
- `test_schemathesis.py`: Property tests using Schemathesis to validate API responses against the OpenAPI schema.