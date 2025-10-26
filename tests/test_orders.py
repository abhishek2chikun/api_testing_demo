import pytest
import httpx
from uuid import uuid4
from tests.conftest import base_url, valid_order_data, invalid_order_data, auth_tokens

@pytest.mark.parametrize("broker, user_id", [
    ("upstox", 1),
    ("zerodha", 2)
])
def test_place_order_success(base_url, valid_order_data, broker, user_id, auth_tokens):
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
def test_place_order_validation_error(base_url, invalid_order_data, broker, user_id, auth_tokens):
    """Test order placement with invalid data returns validation error."""
    response = httpx.post(f"{base_url}/orders?broker={broker}&user_id={user_id}&use_cache=true",
                           json=invalid_order_data,
                           headers={"Authorization": f"Bearer {auth_tokens[broker]}"})
    assert response.status_code == 422
    assert "detail" in response.json()

@pytest.mark.parametrize("broker, user_id, order_id", [
    ("upstox", 1, "non_existent_order"),
    ("zerodha", 2, "invalid_order_id")
])
def test_get_order_not_found(base_url, broker, user_id, order_id, auth_tokens):
    """Test retrieving a non-existent order returns 404."""
    response = httpx.get(f"{base_url}/orders/{order_id}?broker={broker}&user_id={user_id}&use_cache=true",
                          headers={"Authorization": f"Bearer {auth_tokens[broker]}"})
    assert response.status_code == 404
    assert "detail" in response.json()

@pytest.mark.parametrize("broker, user_id", [
    ("upstox", 1),
    ("zerodha", 2)
])
def test_list_orders_success(base_url, broker, user_id, auth_tokens):
    """Test listing orders for a user returns successful response."""
    response = httpx.get(f"{base_url}/orders?broker={broker}&user_id={user_id}&use_cache=true",
                          headers={"Authorization": f"Bearer {auth_tokens[broker]}"})
    assert response.status_code == 200
    assert isinstance(response.json(), list)

@pytest.mark.parametrize("broker, user_id", [
    ("invalid_broker", 1),
    ("upstox", "invalid_user_id")  # Invalid user_id
])
def test_list_orders_validation_error(base_url, broker, user_id, auth_tokens):
    """Test listing orders with invalid parameters returns validation error."""
    response = httpx.get(f"{base_url}/orders?broker={broker}&user_id={user_id}&use_cache=true",
                          headers={"Authorization": f"Bearer {auth_tokens['upstox']}"})
    assert response.status_code == 422
    assert "detail" in response.json()

@pytest.mark.parametrize("broker, user_id, order_id", [
    ("upstox", 1, "valid_order_id"),
    ("zerodha", 2, "another_valid_order_id")
])
def test_get_order_success(base_url, broker, user_id, order_id, auth_tokens):
    """Test retrieving a valid order returns successful response."""
    response = httpx.get(f"{base_url}/orders/{order_id}?broker={broker}&user_id={user_id}&use_cache=true",
                          headers={"Authorization": f"Bearer {auth_tokens[broker]}"})
    assert response.status_code == 200
    assert "order_id" in response.json()
    assert isinstance(response.json()["order_id"], str)  # Check order_id format

@pytest.mark.parametrize("broker, user_id", [
    ("upstox", 1),
    ("zerodha", 2)
])
def test_health_check(base_url, broker, user_id, auth_tokens):
    """Test health check endpoint returns 200."""
    response = httpx.get(f"{base_url}/health",
                          headers={"Authorization": f"Bearer {auth_tokens[broker]}"})
    assert response.status_code == 200

@pytest.mark.parametrize("broker, user_id", [
    ("upstox", 1),
    ("zerodha", 2)
])
def test_service_metadata(base_url, broker, user_id, auth_tokens):
    """Test root endpoint returns service metadata."""
    response = httpx.get(f"{base_url}/",
                          headers={"Authorization": f"Bearer {auth_tokens[broker]}"})
    assert response.status_code == 200
    assert isinstance(response.json(), dict)

@pytest.mark.parametrize("broker, user_id", [
    ("upstox", 1),
    ("zerodha", 2)
])
def test_internal_server_error(base_url, broker, user_id, auth_tokens):
    """Test internal server error handling."""
    response = httpx.get(f"{base_url}/orders/{uuid4()}?broker={broker}&user_id={user_id}&use_cache=true",
                          headers={"Authorization": f"Bearer {auth_tokens[broker]}"})
    assert response.status_code == 500
    assert "detail" in response.json()