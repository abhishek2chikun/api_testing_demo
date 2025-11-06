import pytest
import requests
from uuid import uuid4

BASE_URL = "http://localhost:8002"

@pytest.fixture
def unique_order_id():
    """Generate a unique order ID for testing."""
    return f"order-{uuid4()}"

@pytest.fixture
def valid_broker():
    """Provide a valid broker name."""
    return "upstox"

@pytest.fixture
def valid_user_id():
    """Provide a valid user ID."""
    return 12345

@pytest.fixture
def valid_order_payload():
    """Provide a valid order payload."""
    return {
        "tradingsymbol": "INFY",
        "quantity": 10,
        "order_type": "LIMIT",
        "price": 1500.0
    }