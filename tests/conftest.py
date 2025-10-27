import pytest
import requests
from uuid import uuid4

BASE_URL = "http://localhost:8002"

@pytest.fixture
def unique_order_id():
    """Generate a unique order ID for testing."""
    return f"order-{uuid4()}"

@pytest.fixture
def valid_order_data():
    """Provide valid order data for testing."""
    return {
        "tradingsymbol": "INFY",
        "quantity": 10,
        "order_type": "LIMIT",
        "price": 1500.0
    }

@pytest.fixture
def invalid_order_data():
    """Provide invalid order data for testing."""
    return {
        "tradingsymbol": "INFY",
        "quantity": -10,  # Invalid quantity
        "order_type": "LIMIT"
    }

@pytest.fixture
def auth_headers():
    """Provide authentication headers for testing."""
    return {
        "Authorization": "Bearer valid_token"
    }