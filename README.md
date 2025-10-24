# API Tests for Orders Service

## Overview
This project contains automated tests for the Orders Service API using JUnit5 and Rest-Assured.

## Running the Tests
1. Ensure that the Orders Service is running on `http://localhost:8002`.
2. Use Maven to run the tests:
   ```bash
   mvn test
   ```

## Test Coverage
- Successful order placement
- Validation errors
- Not found errors
- Health check
- Service metadata
- Listing orders
- Retrieving specific orders

## Dependencies
- JUnit5
- Rest-Assured
- Hamcrest
- Jackson Databind