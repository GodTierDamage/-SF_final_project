package com.myProject.finalProject.controller;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.myProject.finalProject.config.ConfigSpecifications;
import com.myProject.finalProject.config.GetBalanceTestStubConfig;
import com.myProject.finalProject.config.PutMoneyTestStubConfig;
import com.myProject.finalProject.config.TakeMoneyTestStubConfig;
import com.myProject.finalProject.entity.Income;
import com.myProject.finalProject.entity.Spend;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class BalanceControllerTest {

    private static final int PORT = 9000;
    private static WireMockServer wireMockServer;

    @BeforeAll
    public static void setup() {
        wireMockServer = new WireMockServer(PORT);
        wireMockServer.start();
    }

    @BeforeAll
    public static void specificationsConfig() {
        ConfigSpecifications.installSpecifications(
                ConfigSpecifications.
                        requestSpecification("http://localhost", PORT, "/api/test"),
                ConfigSpecifications.
                        responseSpecification());
    }

    @Test
    public void testGetBalanceRequest() {
        GetBalanceTestStubConfig.setupStub(wireMockServer);

        int expectedBalanceId = 1;

        RestAssured
                .given()
                .when()
                .get("endpoint?id={id}", expectedBalanceId)
                .then()
                .statusCode(200)
                .body("id", Matchers.equalTo(expectedBalanceId));
    }

    @Test
    void testPutMoneyRequest() {
        PutMoneyTestStubConfig.setupStub(wireMockServer);

        Income requestBody = Income.builder()
                .id(1L)
                .balanceId(1L)
                .income(BigDecimal.valueOf(500))
                .build();

        int expectedBalance = 1000;
        int expectedBalanceId = 1;

        RestAssured
                .given()
                .when()
                .body(requestBody)
                .post("endpoint")
                .then()
                .statusCode(200)
                .body("balance", Matchers.equalTo(expectedBalance),
                        "id", Matchers.equalTo(expectedBalanceId));
    }

    @Test
    public void testTakeMoneyRequest() {
        TakeMoneyTestStubConfig.setupStub(wireMockServer);

        Spend requestBody = Spend.builder()
                .id(1L)
                .balanceId(1L)
                .spend(BigDecimal.valueOf(500))
                .build();

        int expectedBalance = 500;
        int expectedBalanceId = 1;

        RestAssured
                .given()
                .when()
                .body(requestBody)
                .post("endpoint")
                .then()
                .statusCode(200)
                .body("balance", Matchers.equalTo(expectedBalance),
                "id", Matchers.equalTo(expectedBalanceId));
    }

    @AfterAll
    public static void tearDown() {
        if(wireMockServer.isRunning()) {
            wireMockServer.shutdownServer();
        }
    }
}