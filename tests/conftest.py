import pytest
import httpx

@pytest.fixture(scope="module")
def base_url():
    return "http://localhost:8002"

@pytest.fixture(scope="module")
def client(base_url):
    with httpx.Client(base_url=base_url) as client:
        yield client