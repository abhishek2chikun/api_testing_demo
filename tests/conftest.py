import pytest
import httpx

@pytest.fixture(scope="module")
def base_url():
    """Base URL for the Orders Service API."""
    return "http://localhost:8002"

@pytest.fixture(scope="module")
def valid_order_data():
    """Fixture for valid order data."""
    return {
        "tradingsymbol": "INFY",
        "quantity": 10,
        "order_type": "LIMIT",
        "price": 1500.0,
        "trigger_price": None
    }

@pytest.fixture(scope="module")
def invalid_order_data():
    """Fixture for invalid order data."""
    return {
        "tradingsymbol": "INFY",
        "quantity": -5,  # Invalid quantity
        "order_type": "LIMIT",
        "price": None,  # Missing price
        "trigger_price": None
    }

@pytest.fixture(scope="module")
def auth_tokens():
    """Fixture for valid authentication tokens."""
    return {
        "upstox": "valid_upstox_token",
        "zerodha": "valid_zerodha_token"
    }