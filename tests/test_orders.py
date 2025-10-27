import pytest
import requests
from uuid import uuid4

BASE_URL = "http://localhost:8002"

def test_place_order_success(valid_order_data, auth_headers):
    """Test successful order placement with valid data."""
    response = requests.post(f"{BASE_URL}/orders", params={
        "broker": "upstox",
        "user_id": 1,
        "use_cache": True
    }, json=valid_order_data, headers=auth_headers)
    assert response.status_code == 200
    assert "order_id" in response.json()
    assert "order_id" in response.json()  # Check response structure


def test_place_order_validation_error(invalid_order_data, auth_headers):
    """Test order placement with invalid data resulting in validation error."""
    response = requests.post(f"{BASE_URL}/orders", params={
        "broker": "upstox",
        "user_id": 1,
        "use_cache": True
    }, json=invalid_order_data, headers=auth_headers)
    assert response.status_code == 422


def test_list_orders_success(auth_headers):
    """Test successful retrieval of orders list."""
    response = requests.get(f"{BASE_URL}/orders", params={
        "broker": "upstox",
        "user_id": 1,
        "use_cache": True
    }, headers=auth_headers)
    assert response.status_code == 200
    assert isinstance(response.json(), list)


def test_get_order_not_found(unique_order_id, auth_headers):
    """Test retrieval of a non-existent order resulting in 404."""
    response = requests.get(f"{BASE_URL}/orders/{unique_order_id}", params={
        "broker": "upstox",
        "user_id": 1,
        "use_cache": True
    }, headers=auth_headers)
    assert response.status_code == 404


def test_service_metadata(auth_headers):
    """Test retrieval of service metadata."""
    response = requests.get(f"{BASE_URL}/", headers=auth_headers)
    assert response.status_code == 200
    assert "available_brokers" in response.json()
    assert isinstance(response.json(), dict)  # Validate response structure


def test_health_check():
    """Test health check endpoint."""
    response = requests.get(f"{BASE_URL}/health")
    assert response.status_code == 200


def test_openapi_accessible():
    """Test if OpenAPI documentation is accessible."""
    response = requests.get(f"{BASE_URL}/openapi.json")
    assert response.status_code == 200
    assert response.headers["Content-Type"] == "application/json"


def test_authentication_error():
    """Test that authentication is enforced."""
    response = requests.get(f"{BASE_URL}/orders", params={
        "broker": "upstox",
        "user_id": 1,
        "use_cache": True
    })
    assert response.status_code == 401  # Assuming 401 for unauthorized access


def test_error_response_structure():
    """Test error response structure for validation errors."""
    response = requests.post(f"{BASE_URL}/orders", params={
        "broker": "upstox",
        "user_id": 1,
        "use_cache": True
    }, json={})  # Invalid data
    assert response.status_code == 422
    assert "status" in response.json()
    assert "message" in response.json()
    assert "detail" in response.json()
    assert "path" in response.json()