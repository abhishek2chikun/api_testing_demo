```java
package com.example.api.tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class OrdersApiTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8002";
    }

    /**
     * Test the successful placement of an order.
     */
    @Test
    public void testPlaceOrderSuccess() {
        String orderPayload = "{ \"tradingsymbol\": \"INFY\", \"quantity\": 10, \"order_type\": \"LIMIT\", \"price\": 1500 }";

        given()
            .contentType(ContentType.JSON)
            .queryParam("broker", "upstox")
            .queryParam("user_id", 1)
            .body(orderPayload)
        .when()
            .post("/orders")
        .then()
            .statusCode(200)
            .body("order_id", notNullValue())
            .body("status", equalTo("SUCCESS"));
    }

    /**
     * Test validation error when placing an order with missing required fields.
     */
    @Test
    public void testPlaceOrderValidationError() {
        String orderPayload = "{ \"tradingsymbol\": \"INFY\", \"quantity\": 0 }"; // Invalid quantity

        given()
            .contentType(ContentType.JSON)
            .queryParam("broker", "upstox")
            .queryParam("user_id", 1)
            .body(orderPayload)
        .when()
            .post("/orders")
        .then()
            .statusCode(422)
            .body("status", equalTo("error"))
            .body("message", containsString("Validation Error"));
    }

    /**
     * Test not found error when trying to retrieve a non-existent order.
     */
    @Test
    public void testGetOrderNotFound() {
        given()
            .queryParam("broker", "upstox")
            .queryParam("user_id", 1)
        .when()
            .get("/orders/non-existent-order-id")
        .then()
            .statusCode(404)
            .body("status", equalTo("error"))
            .body("message", containsString("Order not found"));
    }

    /**
     * Test the health check endpoint.
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
     * Test the root endpoint for service metadata.
     */
    @Test
    public void testRootEndpoint() {
        when()
            .get("/")
        .then()
            .statusCode(200)
            .body("available_brokers", hasItems("upstox", "zerodha", "shoonya", "groww", "angelone", "fyers"));
    }

    /**
     * Test listing orders for a user.
     */
    @Test
    public void testListOrdersSuccess() {
        given()
            .queryParam("broker", "upstox")
            .queryParam("user_id", 1)
        .when()
            .get("/orders")
        .then()
            .statusCode(200)
            .body("orders", notNullValue())
            .body("orders", hasSize(greaterThanOrEqualTo(0)));
    }

    /**
     * Test retrieving an order with valid order_id.
     */
    @Test
    public void testGetOrderSuccess() {
        String orderId = "valid-order-id"; // Replace with a valid order ID for testing

        given()
            .queryParam("broker", "upstox")
            .queryParam("user_id", 1)
        .when()
            .get("/orders/" + orderId)
        .then()
            .statusCode(200)
            .body("order_id", equalTo(orderId))
            .body("status", equalTo("SUCCESS"));
    }
}