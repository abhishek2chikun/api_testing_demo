// Updated OrdersApiTest.java
package com.example.api.tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class OrdersApiTest {

    private static final String BASE_URL = "http://localhost:8002";
    private static final String BROKER = "upstox";
    private static final int USER_ID = 12345; // Example user ID
    private static final String VALID_ORDER_ID = "order123"; // Example valid order ID

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = BASE_URL;
    }

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

    @Test
    public void testPlaceOrderMissingFields() {
        String orderPayload = "{ \"tradingsymbol\": \"INFY\", \"quantity\": 0 }"; // Missing order_type and price

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

    @Test
    public void testPlaceOrderInvalidType() {
        String orderPayload = "{ \"tradingsymbol\": \"INFY\", \"quantity\": 10, \"order_type\": \"INVALID\", \"price\": 1500 }";

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

    @Test
    public void testListOrdersSuccess() {
        given()
            .queryParam("broker", BROKER)
            .queryParam("user_id", USER_ID)
        .when()
            .get("/orders")
        .then()
            .statusCode(200)
            .body("orders", not(emptyArray()));
    }

    @Test
    public void testGetOrderSuccess() {
        given()
            .queryParam("broker", BROKER)
            .queryParam("user_id", USER_ID)
        .when()
            .get("/orders/" + VALID_ORDER_ID)
        .then()
            .statusCode(200)
            .body("order_id", equalTo(VALID_ORDER_ID));
    }

    @Test
    public void testGetOrderNotFound() {
        given()
            .queryParam("broker", BROKER)
            .queryParam("user_id", USER_ID)
        .when()
            .get("/orders/nonexistent_order_id")
        .then()
            .statusCode(404)
            .body("status", equalTo("error"))
            .body("message", containsString("Order not found"));
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
            .body("available_brokers", hasItems("upstox", "zerodha", "shoonya", "groww", "angelone", "fyers"));
    }

    @Test
    public void testErrorResponseStructure() {
        String orderPayload = "{ \"tradingsymbol\": \"INFY\", \"quantity\": 10, \"order_type\": \"LIMIT\" }";

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
            .body("message", notNullValue())
            .body("detail", notNullValue())
            .body("path", notNullValue());
    }
}
