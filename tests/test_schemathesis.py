import schemathesis
import pytest

schema = schemathesis.from_uri("http://localhost:8002/openapi.json")

@schema.parametrize()
def test_api(schema, case):
    """Test API endpoints against the OpenAPI schema."""
    response = case.call()
    case.validate(response)