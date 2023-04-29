package com.myProject.finalProject.controller;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.myProject.finalProject.entity.Balance;
import lombok.SneakyThrows;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class GetBalanceTestStubConfig {

    @SneakyThrows
    public static void setupStub(WireMockServer wireMockServer) {
        ObjectMapper objectMapper = new ObjectMapper();

        wireMockServer.stubFor(get(urlEqualTo("/api/test/endpoint?id=1"))
                .willReturn(aResponse().
                        withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(objectMapper.writeValueAsString(initializeTestBalance()))));
    }

    private static Balance initializeTestBalance() {
        return Balance.
                builder().
                id(1L).
                balance(BigDecimal.valueOf(0)).
                build();
    }
}
