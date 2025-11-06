import schemathesis
import pytest

schema = schemathesis.from_uri("http://localhost:8002/openapi.json")

@schema.parametrize()
def test_api_compliance(case):
    """Test API compliance with the OpenAPI specification."""
    response = case.call_and_validate()
    assert response.status_code in {200, 400, 404, 422, 500}