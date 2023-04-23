package com.myProject.finalProject.controller;

import com.myProject.finalProject.ConfigSpecifications;
import com.myProject.finalProject.entity.Income;
import com.myProject.finalProject.entity.Spend;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class BalanceControllerTest {

    @BeforeAll
    public static void specificationsConfig() {
        ConfigSpecifications.installSpecifications(
                ConfigSpecifications.
                        requestSpecification("http://localhost", 8080, "/api/balance"),
                ConfigSpecifications.
                        responseSpecification());
    }

    @Test
    public void testGetBalanceRequest() {
        Long balanceId = 1L;

        RestAssured
                .given()
                .when()
                .get("getBalance?id={id}", balanceId)
                .then()
                .statusCode(200)
                .body("id", Matchers.equalTo(1));
    }

    @Test
    void testPutMoneyRequest() {

        Income requestBody = Income.builder()
                .id(1L)
                .balanceId(1L)
                .income(BigDecimal.valueOf(1000))
                .build();

        RestAssured
                .given()
                .when()
                .body(requestBody)
                .post("putMoney")
                .then()
                .statusCode(200);
    }

    @Test
    public void testTakeMoneyRequest() {
        Spend requestBody = Spend.builder()
                .id(1L)
                .balanceId(1L)
                .spend(BigDecimal.valueOf(1000))
                .build();

        RestAssured
                .given()
                .when()
                .body(requestBody)
                .post("takeMoney")
                .then()
                .statusCode(200);
    }
}