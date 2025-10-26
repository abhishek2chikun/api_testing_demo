import pytest
import httpx
from uuid import uuid4
from tests.conftest import base_url, valid_order_data, invalid_order_data, auth_tokens

@pytest.mark.parametrize("broker, user_id", [
    ("upstox", 1),
    ("zerodha", 2)
])
def test_place_order_success(base_url, broker, user_id, valid_order_data, auth_tokens):
    """Test successful order placement with valid data."""
    response = httpx.post(f"{base_url}/orders?broker={broker}&user_id={user_id}&use_cache=true",
                           json=valid_order_data,
                           headers={"Authorization": f"Bearer {auth_tokens[broker]}"})
    assert response.status_code == 200
    assert "order_id" in response.json()
    assert response.json()["status"] == "success"

@pytest.mark.parametrize("broker, user_id", [
    ("upstox", 1),
    ("zerodha", 2)
])
def test_place_order_validation_error(base_url, broker, user_id, invalid_order_data, auth_tokens):
    """Test order placement with invalid data."""
    response = httpx.post(f"{base_url}/orders?broker={broker}&user_id={user_id}&use_cache=true",
                           json=invalid_order_data,
                           headers={"Authorization": f"Bearer {auth_tokens[broker]}"})
    assert response.status_code == 422
    assert "detail" in response.json()

@pytest.mark.parametrize("broker, user_id, order_id", [
    ("upstox", 1, "valid_order_id"),
    ("zerodha", 2, "invalid_order_id")  # Testing for a non-existent order
])
def test_get_order(base_url, broker, user_id, order_id, auth_tokens):
    """Test retrieving order details."""
    response = httpx.get(f"{base_url}/orders/{order_id}?broker={broker}&user_id={user_id}&use_cache=true",
                          headers={"Authorization": f"Bearer {auth_tokens[broker]}"})
    if order_id == "valid_order_id":
        assert response.status_code == 200
        assert "order_id" in response.json()
    else:
        assert response.status_code == 404
        assert "detail" in response.json()

@pytest.mark.parametrize("broker, user_id", [
    ("upstox", 1),
    ("zerodha", 2)
])
def test_list_orders(base_url, broker, user_id, auth_tokens):
    """Test listing orders for a user."""
    response = httpx.get(f"{base_url}/orders?broker={broker}&user_id={user_id}&use_cache=true",
                          headers={"Authorization": f"Bearer {auth_tokens[broker]}"})
    assert response.status_code == 200
    assert isinstance(response.json(), list)

@pytest.mark.parametrize("broker, user_id", [
    ("invalid_broker", 1),  # Invalid broker
    ("upstox", None)  # Missing user_id
])
def test_invalid_requests(base_url, broker, user_id):
    """Test invalid requests for order placement and retrieval."""
    if user_id is None:
        response = httpx.get(f"{base_url}/orders?broker={broker}&use_cache=true")
    else:
        response = httpx.get(f"{base_url}/orders?broker={broker}&user_id={user_id}&use_cache=true")
    
    assert response.status_code == 422  # Expecting validation error

@pytest.mark.parametrize("broker, user_id", [
    ("upstox", 1),
    ("zerodha", 2)
])
def test_health_check(base_url):
    """Test health check endpoint."""
    response = httpx.get(f"{base_url}/health")
    assert response.status_code == 200
    assert response.json() == {"status": "healthy"}

@pytest.mark.parametrize("broker, user_id", [
    ("upstox", 1),
    ("zerodha", 2)
])
def test_service_metadata(base_url):
    """Test service metadata endpoint."""
    response = httpx.get(f"{base_url}/")
    assert response.status_code == 200
    assert isinstance(response.json(), dict)