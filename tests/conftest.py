import pytest
import requests
from uuid import uuid4

BASE_URL = "http://localhost:8002"

@pytest.fixture(scope="session")
def base_url():
    return BASE_URL

@pytest.fixture
def unique_order_id():
    return f"order-{uuid4()}"

@pytest.fixture
def valid_order_data():
    return {
        "tradingsymbol": "INFY",
        "quantity": 10,
        "order_type": "LIMIT",
        "price": 1500.0
    }

@pytest.fixture
def invalid_order_data():
    return {
        "tradingsymbol": "INFY",
        "quantity": -10,  # Invalid quantity
        "order_type": "LIMIT"
    }

@pytest.fixture
def valid_query_params():
    return {
        "broker": "upstox",
        "user_id": 12345,
        "use_cache": True
    }

@pytest.fixture
def invalid_query_params():
    return {
        "broker": "unknown_broker",  # Invalid broker
        "user_id": "not_an_int",  # Invalid user_id
        "use_cache": "not_a_bool"  # Invalid use_cache
    }