import pytest
import httpx
from uuid import uuid4

BASE_URL = "http://localhost:8002"

@pytest.fixture
def valid_order_data():
    """Fixture to provide valid order data for tests."""
    return {
        "tradingsymbol": "INFY",
        "quantity": 10,
        "order_type": "LIMIT",
        "price": 1500,
        "trigger_price": None
    }

@pytest.fixture
def invalid_order_data():
    """Fixture to provide invalid order data for tests."""
    return {
        "tradingsymbol": "INFY",
        "quantity": -5,  # Invalid quantity
        "order_type": "LIMIT",
        "price": None,  # Missing price for LIMIT order
        "trigger_price": None
    }

def test_place_order_success(client, valid_order_data):
    """Test successful order placement with valid data."""
    response = client.post(f"{BASE_URL}/orders?broker=upstox&user_id=1", json=valid_order_data)
    assert response.status_code == 200
    assert "order_id" in response.json()
    assert response.json()["status"] == "success"

def test_place_order_validation_error(client, invalid_order_data):
    """Test order placement with invalid data returns validation error."""
    response = client.post(f"{BASE_URL}/orders?broker=upstox&user_id=1", json=invalid_order_data)
    assert response.status_code == 422
    assert "detail" in response.json()

def test_list_orders_success(client):
    """Test listing orders for a user."""
    response = client.get(f"{BASE_URL}/orders?broker=upstox&user_id=1")
    assert response.status_code == 200
    assert isinstance(response.json(), list)

def test_get_order_success(client):
    """Test retrieving a specific order by ID."""
    # Assuming an order with ID '12345' exists
    response = client.get(f"{BASE_URL}/orders/12345?broker=upstox&user_id=1")
    assert response.status_code == 200
    assert "order_id" in response.json()

def test_get_order_not_found(client):
    """Test retrieving a non-existent order returns 404."""
    response = client.get(f"{BASE_URL}/orders/99999?broker=upstox&user_id=1")
    assert response.status_code == 404
    assert "detail" in response.json()

def test_health_check(client):
    """Test health check endpoint."""
    response = client.get(f"{BASE_URL}/health")
    assert response.status_code == 200

def test_service_metadata(client):
    """Test service metadata endpoint."""
    response = client.get(f"{BASE_URL}/")
    assert response.status_code == 200
    assert isinstance(response.json(), dict)

@pytest.mark.parametrize("broker, user_id", [
    ("upstox", 1),
    ("zerodha", 1),
    ("shoonya", 1),
    ("groww", 1),
    ("angelone", 1),
    ("fyers", 1),
])
def test_list_orders_various_brokers(client, broker, user_id):
    """Test listing orders for various brokers."""
    response = client.get(f"{BASE_URL}/orders?broker={broker}&user_id={user_id}")
    assert response.status_code == 200
    assert isinstance(response.json(), list)

@pytest.mark.parametrize("order_id", [
    "invalid_id_1",
    "invalid_id_2",
    "invalid_id_3",
])
def test_get_order_invalid_id(client, order_id):
    """Test retrieving an order with invalid ID returns 404."""
    response = client.get(f"{BASE_URL}/orders/{order_id}?broker=upstox&user_id=1")
    assert response.status_code == 404
    assert "detail" in response.json()