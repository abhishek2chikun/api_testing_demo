def test_place_order_auth_error(valid_broker, valid_user_id):
    """Test order placement without authentication (expected 404)."""
    response = requests.post(f"{BASE_URL}/orders", params={
        "broker": valid_broker,
        "user_id": valid_user_id,
        "use_cache": True
    }, json={})
    assert response.status_code == 404


def test_order_error_response_structure(valid_broker, valid_user_id):
    """Test order placement with invalid data (missing required fields) and validate error response structure."""
    invalid_payload = {
        "tradingsymbol": "INFY",
        "quantity": 0  # Invalid quantity
    }
    response = requests.post(f"{BASE_URL}/orders", params={
        "broker": valid_broker,
        "user_id": valid_user_id,
        "use_cache": True
    }, json=invalid_payload)
    assert response.status_code == 422
    error_response = response.json()
    assert "status" in error_response
    assert "message" in error_response
    assert "detail" in error_response
    assert "path" in error_response


def test_service_metadata_structure():
    """Test retrieving service metadata and validate response structure."""
    response = requests.get(f"{BASE_URL}/")
    assert response.status_code == 200
    metadata = response.json()
    assert "available_brokers" in metadata
    assert isinstance(metadata["available_brokers"], list)