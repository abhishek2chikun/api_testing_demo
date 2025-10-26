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
     * Test for placing an order successfully.
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
     * Test for placing an order with missing required fields.
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
     * Test for retrieving orders for a user successfully.
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
     * Test for retrieving orders with invalid user_id.
     */
    @Test
    public void testListOrdersNotFound() {
        given()
            .queryParam("broker", "upstox")
            .queryParam("user_id", 9999) // Non-existent user_id
        .when()
            .get("/orders")
        .then()
            .statusCode(404)
            .body("status", equalTo("error"))
            .body("message", containsString("Not Found"));
    }

    /**
     * Test for retrieving a specific order successfully.
     */
    @Test
    public void testGetOrderSuccess() {
        String orderId = "12345"; // Assume this order_id exists

        given()
            .queryParam("broker", "upstox")
            .queryParam("user_id", 1)
            .pathParam("order_id", orderId)
        .when()
            .get("/orders/{order_id}")
        .then()
            .statusCode(200)
            .body("order_id", equalTo(orderId))
            .body("status", equalTo("SUCCESS"));
    }

    /**
     * Test for retrieving a non-existent order.
     */
    @Test
    public void testGetOrderNotFound() {
        String orderId = "nonexistent"; // Non-existent order_id

        given()
            .queryParam("broker", "upstox")
            .queryParam("user_id", 1)
            .pathParam("order_id", orderId)
        .when()
            .get("/orders/{order_id}")
        .then()
            .statusCode(404)
            .body("status", equalTo("error"))
            .body("message", containsString("Not Found"));
    }

    /**
     * Test for health check endpoint.
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
     * Test for root endpoint to get service metadata.
     */
    @Test
    public void testRootEndpoint() {
        when()
            .get("/")
        .then()
            .statusCode(200)
            .body("available_brokers", notNullValue());
    }
}