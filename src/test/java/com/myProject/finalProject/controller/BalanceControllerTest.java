package com.myProject.finalProject.controller;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.myProject.finalProject.config.*;
import com.myProject.finalProject.entity.Income;
import com.myProject.finalProject.entity.Spend;
import com.myProject.finalProject.entity.Transaction;
import com.myProject.finalProject.enums.OperationType;
import com.myProject.finalProject.enums.SpendingType;
import io.restassured.common.mapper.TypeRef;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.equalTo;


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

        int expectedId = 1;

        given()
                .queryParam("balanceId", 1)
                .when()
                .log().all()
                .get("endpoint")
                .then()
                .statusCode(200)
                .body("id", equalTo(expectedId));
    }

    @Test
    void testPutMoneyRequest() {
        PutMoneyTestStubConfig.setupStub(wireMockServer);

        Transaction requestBody = Income.builder()
                .id(1L)
                .balanceId(1L)
                .amount(BigDecimal.valueOf(500))
                .build();

        int expectedBalance = 1000;
        int expectedBalanceId = 1;


        given()
                .when()
                .body(requestBody)
                .post("endpoint")
                .then()
                .statusCode(200)
                .body("amount", equalTo(expectedBalance),
                        "id", equalTo(expectedBalanceId));
    }

    @Test
    public void testTakeMoneyRequest() {
        TakeMoneyTestStubConfig.setupStub(wireMockServer);

        Transaction requestBody = Spend.builder()
                .id(1L)
                .balanceId(1L)
                .amount(BigDecimal.valueOf(500))
                .build();

        int expectedBalance = 500;
        int expectedBalanceId = 1;


        given()
                .when()
                .body(requestBody)
                .post("endpoint")
                .then()
                .statusCode(200)
                .body("amount", equalTo(expectedBalance),
                        "id", equalTo(expectedBalanceId));
    }

    @SneakyThrows
    @Test
    public void testGetTransactionsById() {
        ObjectMapper mapper = new ObjectMapper();

        GetTransactionsTestConfig.setupStub(wireMockServer, mapper.writeValueAsString(generateTransactions()));

        given()
                .param("balanceId", "1")
                .when()
                .log().all()
                .get("endpoint")
                .then()
                .statusCode(200)
                .body("[0].balanceId", equalTo(1))
                .body("[0].typeOfSpendingOperation", equalTo("Transfer"))
                .body("[0].amount", equalTo(1000))
                .body("[1].balanceId", equalTo(1))
                .body("[1].typeOfSpendingOperation", equalTo("Withdraw"))
                .body("[1].amount", equalTo(300))
                .body("[2].balanceId", equalTo(1))
                .body("[2].typeOfSpendingOperation", equalTo("Purchase"))
                .body("[2].amount", equalTo(500))
                .body("[3].balanceId", equalTo(1))
                .body("[3].amount", equalTo(300));
    }

    @SneakyThrows
    @Test
    public void testGetTransactionsBeforeDate() {
        Timestamp timePoint = Timestamp.valueOf("2000-01-01 00:00:00");
        List<Transaction> before = generateTransactions()
                .stream()
                .filter(transaction -> transaction.dateOfOperation().before(timePoint))
                .toList();
        ObjectMapper mapper = new ObjectMapper();

        GetTransactionsTestConfig.setupStub(wireMockServer, mapper.writeValueAsString(before));

        List<Transaction> transactions =
                given()
                        .param("balanceId", "1")
                        .when()
                        .log().all()
                        .get("endpoint")
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .as(new TypeRef<>() {
                        });

        assertThat(transactions.size()).isNotEqualTo(0);

        transactions.forEach(transaction -> {
                    assertThat(transaction.dateOfOperation()).isBefore(timePoint);
                    System.out.println(transaction);
                }
        );
    }

    @SneakyThrows
    @Test
    public void testGetTransactionsAfterDate() {
        Timestamp timePoint = Timestamp.valueOf("2000-01-01 00:00:00");
        List<Transaction> after = generateTransactions()
                .stream()
                .filter(transaction -> transaction.dateOfOperation().after(timePoint))
                .toList();
        ObjectMapper mapper = new ObjectMapper();

        GetTransactionsTestConfig.setupStub(wireMockServer, mapper.writeValueAsString(after));

        List<Transaction> transactions =
                given()
                        .param("balanceId", "1")
                        .when()
                        .log().all()
                        .get("endpoint")
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .as(new TypeRef<>() {
                        });

        assertThat(transactions.size()).isNotEqualTo(0);

        transactions.forEach(transaction -> {
                    assertThat(transaction.dateOfOperation()).isAfter(timePoint);
                    System.out.println(transaction);
                }
        );
    }

    @SneakyThrows
    @Test
    public void testTransferMoney() {

        TransferMoneyTestStubConfig.setupStub(wireMockServer);

        Transaction transfer = Spend.builder()
                .typeOfSpendingOperation(SpendingType.TRANSFER)
                .amount(BigDecimal.valueOf(100))
                .receiverId(2L)
                .balanceId(1L)
                .build();

        given()
                .when()
                .log().all()
                .body(transfer)
                .post("endpoint")
                .then()
                .statusCode(200);
    }

    @SneakyThrows
    @Test
    public void testGetTransactionsBetweenDate(){
        Timestamp firstDate = Timestamp.valueOf("1900-01-01 00:00:00");
        Timestamp secondDate = new Timestamp(System.currentTimeMillis());

        List<Transaction> between = generateTransactions();
        ObjectMapper mapper = new ObjectMapper();

        GetTransactionsTestConfig.setupStub(wireMockServer, mapper.writeValueAsString(between));

        List<Transaction> transactions =
                given()
                        .param("balanceId", "1")
                        .when()
                        .log().all()
                        .get("endpoint")
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .as(new TypeRef<>() {
                        });


        assertThat(transactions.size()).isNotEqualTo(0);

        transactions.forEach(transaction -> {
                    assertThat(transaction.dateOfOperation()).isBetween(firstDate, secondDate);
                    System.out.println(transaction);
                }
        );

    }

    private static List<Transaction> generateTransactions() {
        return List.of(
                Spend.builder()
                        .id(1L)
                        .operationType(OperationType.SPEND)
                        .balanceId(1L)
                        .typeOfSpendingOperation(SpendingType.TRANSFER)
                        .amount(BigDecimal.valueOf(1000))
                        .dateOfOperation(Timestamp.valueOf("1931-11-24 17:53:27"))
                        .build(),
                Spend.builder()
                        .id(2L)
                        .operationType(OperationType.SPEND)
                        .balanceId(1L)
                        .typeOfSpendingOperation(SpendingType.WITHDRAW)
                        .amount(BigDecimal.valueOf(300))
                        .dateOfOperation(Timestamp.valueOf("1975-01-04 10:13:47"))
                        .build(),
                Spend.builder()
                        .id(3L)
                        .operationType(OperationType.SPEND)
                        .balanceId(1L)
                        .typeOfSpendingOperation(SpendingType.PURCHASE)
                        .amount(BigDecimal.valueOf(500))
                        .dateOfOperation(Timestamp.valueOf("2023-01-01 00:00:00"))
                        .build(),
                Income.builder()
                        .operationType(OperationType.INCOME)
                        .id(4L)
                        .balanceId(1L)
                        .amount(BigDecimal.valueOf(300))
                        .dateOfOperation(Timestamp.valueOf("2003-04-22 14:22:37"))
                        .build()
        );
    }

    @AfterAll
    public static void tearDown() {
        if (wireMockServer.isRunning()) {
            wireMockServer.shutdownServer();
        }
    }
}