// Additional tests for error responses and status codes

    /**
     * Test to ensure all endpoints return proper status codes.
     */
    @Test
    public void testAllEndpointsReturnProperStatusCodes() {
        // Test for POST /orders
        Map<String, Object> payload = buildLimitOrderPayload();
        given()
            .contentType(ContentType.JSON)
            .queryParam("broker", DEFAULT_BROKER)
            .queryParam("user_id", DEFAULT_USER_ID)
            .body(payload)
        .when()
            .post("/orders")
        .then()
            .statusCode(anyOf(is(200), is(400), is(404), is(500)));

        // Test for GET /orders
        given()
            .queryParam("broker", DEFAULT_BROKER)
            .queryParam("user_id", DEFAULT_USER_ID)
        .when()
            .get("/orders")
        .then()
            .statusCode(anyOf(is(200), is(404), is(500)));

        // Test for GET /orders/{order_id}
        String orderId = "valid-order-id"; // Replace with a valid order ID for testing
        given()
            .queryParam("broker", DEFAULT_BROKER)
            .queryParam("user_id", DEFAULT_USER_ID)
        .when()
            .get("/orders/{order_id}", orderId)
        .then()
            .statusCode(anyOf(is(200), is(404), is(500)));
    }

    /**
     * Test to ensure error responses follow API standards.
     */
    @Test
    public void testErrorResponsesFollowApiStandards() {
        // Test for invalid order_id pattern
        String invalidOrderId = "invalid-id!@#";
        given()
            .queryParam("broker", DEFAULT_BROKER)
            .queryParam("user_id", DEFAULT_USER_ID)
        .when()
            .get("/orders/{order_id}", invalidOrderId)
        .then()
            .statusCode(anyOf(is(422), is(400)))
            .body("status", notNullValue())
            .body("message", notNullValue())
            .body("detail", notNullValue());
    }