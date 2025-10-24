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
     * Test placing an order successfully with valid data.
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
     * Test placing an order with missing required fields.
     */
    @Test
    public void testPlaceOrderMissingFields() {
        String orderPayload = "{ \"tradingsymbol\": \"INFY\", \"quantity\": 0 }"; // Missing order_type and price

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
     * Test retrieving orders for a user successfully.
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
            .body("orders", not(emptyArray()));
    }

    /**
     * Test retrieving orders with invalid user_id.
     */
    @Test
    public void testListOrdersInvalidUserId() {
        given()
            .queryParam("broker", "upstox")
            .queryParam("user_id", -1) // Invalid user_id
        .when()
            .get("/orders")
        .then()
            .statusCode(404)
            .body("status", equalTo("error"))
            .body("message", containsString("User not found"));
    }

    /**
     * Test retrieving a specific order successfully.
     */
    @Test
    public void testGetOrderSuccess() {
        String orderId = "validOrderId"; // Replace with a valid order ID

        given()
            .pathParam("order_id", orderId)
            .queryParam("broker", "upstox")
            .queryParam("user_id", 1)
        .when()
            .get("/orders/{order_id}")
        .then()
            .statusCode(200)
            .body("order_id", equalTo(orderId));
    }

    /**
     * Test retrieving a non-existent order.
     */
    @Test
    public void testGetOrderNotFound() {
        String orderId = "nonExistentOrderId"; // Non-existent order ID

        given()
            .pathParam("order_id", orderId)
            .queryParam("broker", "upstox")
            .queryParam("user_id", 1)
        .when()
            .get("/orders/{order_id}")
        .then()
            .statusCode(404)
            .body("status", equalTo("error"))
            .body("message", containsString("Order not found"));
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