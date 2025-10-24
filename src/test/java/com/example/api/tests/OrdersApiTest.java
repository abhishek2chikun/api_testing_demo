package com.example.api.tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class OrdersApiTest {

    private static final String BASE_URL = "http://localhost:8002";
    private static final String BROKER = "upstox"; // Example broker
    private static final int USER_ID = 123; // Example user ID

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    /**
     * Test successful order placement with valid data.
     */
    @Test
    public void testPlaceOrderSuccess() {
        String orderPayload = "{ \"tradingsymbol\": \"INFY\", \"quantity\": 10, \"order_type\": \"LIMIT\", \"price\": 1500 }";

        given()
            .contentType(ContentType.JSON)
            .queryParam("broker", BROKER)
            .queryParam("user_id", USER_ID)
            .body(orderPayload)
        .when()
            .post("/orders")
        .then()
            .statusCode(200)
            .body("order_id", notNullValue())
            .body("status", equalTo("success"));
    }

    /**
     * Test order placement with missing required fields.
     */
    @Test
    public void testPlaceOrderValidationError() {
        String orderPayload = "{ \"tradingsymbol\": \"INFY\", \"quantity\": 0 }"; // Invalid quantity

        given()
            .contentType(ContentType.JSON)
            .queryParam("broker", BROKER)
            .queryParam("user_id", USER_ID)
            .body(orderPayload)
        .when()
            .post("/orders")
        .then()
            .statusCode(422)
            .body("status", equalTo("error"))
            .body("message", containsString("Validation Error"));
    }

    /**
     * Test retrieval of a non-existent order.
     */
    @Test
    public void testGetOrderNotFound() {
        String nonExistentOrderId = "invalid_order_id";

        given()
            .queryParam("broker", BROKER)
            .queryParam("user_id", USER_ID)
        .when()
            .get("/orders/" + nonExistentOrderId)
        .then()
            .statusCode(404)
            .body("status", equalTo("error"))
            .body("message", containsString("Order not found"));
    }

    /**
     * Test listing orders for a user.
     */
    @Test
    public void testListOrdersSuccess() {
        given()
            .queryParam("broker", BROKER)
            .queryParam("user_id", USER_ID)
        .when()
            .get("/orders")
        .then()
            .statusCode(200)
            .body("orders", notNullValue())
            .body("orders", hasSize(greaterThanOrEqualTo(0))); // Expect at least 0 orders
    }

    /**
     * Test health check endpoint.
     */
    @Test
    public void testHealthCheck() {
        when()
            .get("/health")
        .then()
            .statusCode(200)
            .body("status", equalTo("healthy"));
    }

    /**
     * Test root endpoint for service metadata.
     */
    @Test
    public void testRootEndpoint() {
        when()
            .get("/")
        .then()
            .statusCode(200)
            .body("available_brokers", hasItems("upstox", "zerodha", "shoonya", "groww", "angelone", "fyers"));
    }
}