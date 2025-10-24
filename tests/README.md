# Orders Service API Tests

## Overview
This repository contains tests for the Orders Service API, which allows for placing and retrieving orders across multiple brokers.

## Running Tests
1. Ensure the Orders Service API is running locally on port 8002.
2. Install the required dependencies:
   ```bash
   pip install -r requirements.txt
   ```
3. Run the tests using pytest:
   ```bash
   pytest tests/
   ```

## Test Coverage
- Happy path: Successful requests with valid data.
- Validation errors: Invalid inputs, missing required fields, wrong data types.
- Not found: Requests for non-existent resources (404).
- Authentication: Unauthorized/forbidden requests (401/403) if auth is used.
- Edge cases: Empty lists, boundary values, concurrent operations.
- Response validation: Schema validation, required fields, data types.