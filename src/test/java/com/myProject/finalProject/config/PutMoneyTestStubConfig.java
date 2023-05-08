package com.myProject.finalProject.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.myProject.finalProject.entity.Balance;
import com.myProject.finalProject.entity.Income;
import lombok.SneakyThrows;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

public class PutMoneyTestStubConfig {


    @SneakyThrows
    public static void setupStub(WireMockServer wireMockServer) {


        ObjectMapper mapper = new ObjectMapper();
        wireMockServer.stubFor(post(urlEqualTo("/api/test/endpoint"))
                .withRequestBody(equalToJson(mapper.writeValueAsString(getIncome())))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(mapper.writeValueAsString(getBalance()))));
    }

    private static Income getIncome() {
        return Income.builder()
                .id(1L)
                .balanceId(1L)
                .amount(BigDecimal.valueOf(500))
                .build();
    }

    private static Balance getBalance() {
        return Balance.builder()
                .id(1L)
                .amount(BigDecimal.valueOf(1000))
                .build();
    }
}
