// Updated OrdersApiTest.java with additional tests

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

    @Test
    public void testGetOrderSuccess() {
        String orderId = "12345"; // Assume this order ID exists

        given()
            .queryParam("broker", "upstox")
            .queryParam("user_id", 1)
        .when()
            .get("/orders/" + orderId)
        .then()
            .statusCode(200)
            .body("order_id", equalTo(orderId));
    }

    @Test
    public void testGetOrderNotFound() {
        String orderId = "nonexistent"; // Non-existent order ID

        given()
            .queryParam("broker", "upstox")
            .queryParam("user_id", 1)
        .when()
            .get("/orders/" + orderId)
        .then()
            .statusCode(404)
            .body("status", equalTo("error"))
            .body("message", containsString("Not Found"));
    }

    @Test
    public void testHealthCheck() {
        when()
            .get("/health")
        .then()
            .statusCode(200)
            .body("status", equalTo("healthy"));
    }

    @Test
    public void testRootEndpoint() {
        when()
            .get("/")
        .then()
            .statusCode(200)
            .body("available_brokers", hasItems("upstox", "zerodha", "shoonya", "groww", "angelone", "fyers"))
            .body("endpoint_summary", notNullValue()); // Check for service metadata
    }

    @Test
    public void testErrorResponseStructure() {
        String orderPayload = "{ \"tradingsymbol\": \"INFY\", \"quantity\": -1, \"order_type\": \"LIMIT\" }"; // Invalid quantity

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
            .body("detail", notNullValue())
            .body("path", equalTo("/orders"));
    }

    @Test
    public void testUnauthorizedAccess() {
        given()
            .queryParam("broker", "upstox")
            .queryParam("user_id", 999) // Invalid user_id
        .when()
            .get("/orders")
        .then()
            .statusCode(403)
            .body("status", equalTo("error"))
            .body("message", containsString("Unauthorized"));
    }
}
