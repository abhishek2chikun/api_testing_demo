import pytest
import requests

@pytest.mark.parametrize("endpoint", ["/orders", "/orders/{order_id}", "/"])
def test_endpoint_accessibility(base_url, endpoint):
    """Test that all endpoints are accessible and return a 200 status code."""
    response = requests.get(f"{base_url}{endpoint}")
    assert response.status_code == 200


def test_place_order_success(base_url, valid_order_data, valid_query_params):
    """Test successful order placement with valid data."""
    response = requests.post(f"{base_url}/orders", params=valid_query_params, json=valid_order_data)
    assert response.status_code == 200
    assert "order_id" in response.json()


def test_place_order_validation_error(base_url, invalid_order_data, valid_query_params):
    """Test order placement with invalid data returns a validation error."""
    response = requests.post(f"{base_url}/orders", params=valid_query_params, json=invalid_order_data)
    assert response.status_code == 422


def test_list_orders_success(base_url, valid_query_params):
    """Test successful retrieval of orders list."""
    response = requests.get(f"{base_url}/orders", params=valid_query_params)
    assert response.status_code == 200
    assert isinstance(response.json(), list)


def test_get_order_not_found(base_url, unique_order_id, valid_query_params):
    """Test retrieval of a non-existent order returns 404."""
    response = requests.get(f"{base_url}/orders/{unique_order_id}", params=valid_query_params)
    assert response.status_code == 404


def test_get_order_success(base_url, valid_query_params):
    """Test successful retrieval of a specific order."""
    # Assuming an order with ID 'order-123' exists for testing
    response = requests.get(f"{base_url}/orders/order-123", params=valid_query_params)
    assert response.status_code == 200
    assert "order_id" in response.json()


def test_invalid_query_params(base_url, invalid_query_params):
    """Test that invalid query parameters return a validation error."""
    response = requests.get(f"{base_url}/orders", params=invalid_query_params)
    assert response.status_code == 422


def test_service_metadata(base_url):
    """Test that the root endpoint returns service metadata."""
    response = requests.get(f"{base_url}/")
    assert response.status_code == 200
    assert "available_brokers" in response.json()


def test_health_check(base_url):
    """Test that the health check endpoint returns a 200 status code."""
    response = requests.get(f"{base_url}/health")
    assert response.status_code == 200


def test_authentication_required(base_url, valid_query_params):
    """Test that authentication is required for accessing endpoints."""
    response = requests.get(f"{base_url}/orders", params=valid_query_params)
    assert response.status_code == 401  # Assuming 401 for unauthorized access


def test_error_response_format(base_url, invalid_order_data, valid_query_params):
    """Test that error responses follow API standards."""
    response = requests.post(f"{base_url}/orders", params=valid_query_params, json=invalid_order_data)
    assert response.status_code == 422
    error_response = response.json()
    assert "status" in error_response
    assert "message" in error_response
    assert "detail" in error_response
    assert "path" in error_response