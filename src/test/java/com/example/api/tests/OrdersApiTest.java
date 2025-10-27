// Updated test file with additional tests and validations

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
    public void testPlaceOrderMissingFields() {
        String orderPayload = "{ \"tradingsymbol\": \"INFY\" }";

        given()
            .contentType(ContentType.JSON)
            .queryParam("broker", "upstox")
            .queryParam("user_id", 1)
            .body(orderPayload)
        .when()
            .post("/orders")
        .then()
            .statusCode(422)
            .body("status", equalTo(422))
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
            .body("orders", notNullValue())
            .body("orders", hasSize(greaterThanOrEqualTo(0)));
    }

    @Test
    public void testListOrdersNoOrders() {
        given()
            .queryParam("broker", "upstox")
            .queryParam("user_id", 1)
        .when()
            .get("/orders")
        .then()
            .statusCode(200)
            .body("orders", hasSize(0));
    }

    @Test
    public void testListOrdersInvalidUserId() {
        given()
            .queryParam("broker", "upstox")
            .queryParam("user_id", -1)
        .when()
            .get("/orders")
        .then()
            .statusCode(404)
            .body("status", equalTo(404))
            .body("message", containsString("Not Found"));
    }

    @Test
    public void testGetOrderSuccess() {
        String orderId = "validOrderId";

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

    @Test
    public void testGetOrderNotFound() {
        String orderId = "nonExistentOrderId";

        given()
            .queryParam("broker", "upstox")
            .queryParam("user_id", 1)
        .when()
            .get("/orders/" + orderId)
        .then()
            .statusCode(404)
            .body("status", equalTo(404))
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
            .body("available_brokers", hasItems("upstox", "zerodha", "shoonya", "groww", "angelone", "fyers"));
    }

    @Test
    public void testAuthenticationRequired() {
        String orderPayload = "{ \"tradingsymbol\": \"INFY\", \"quantity\": 10, \"order_type\": \"LIMIT\", \"price\": 1500 }";

        given()
            .contentType(ContentType.JSON)
            .queryParam("broker", "upstox")
            .queryParam("user_id", 1)
            .body(orderPayload)
        .when()
            .post("/orders")
        .then()
            .statusCode(404)
            .body("status", equalTo(404))
            .body("message", containsString("Not Found"));
    }
}
