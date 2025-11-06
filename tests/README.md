# Orders Service API Tests

## Overview

This test suite is designed to validate the functionality of the Orders Service API. It covers various scenarios including successful requests, validation errors, and edge cases.

## Prerequisites

- Python 3.7+
- `pytest`
- `requests`

## Setup

1. Install the required dependencies:
   ```bash
   pip install -r requirements.txt
   ```

2. Ensure the Orders Service API is running locally on port 8002.

## Running Tests

To execute the tests, run the following command in the terminal:

```bash
pytest
```

## Test Structure

- `conftest.py`: Contains fixtures for test setup and teardown.
- `test_orders.py`: Contains tests for the Orders API endpoints.

## Notes

- Ensure that the API is accessible at `http://localhost:8002` before running the tests.
- Modify the `base_url` fixture in `conftest.py` if the API is hosted on a different URL or port.