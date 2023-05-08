package com.myProject.finalProject.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.myProject.finalProject.entity.Balance;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class GetBalanceTestStubConfig {

    @SneakyThrows
    public static void setupStub(WireMockServer wireMockServer) {
        ObjectMapper objectMapper = new ObjectMapper();

        wireMockServer.stubFor(get(urlMatching("/api/test/endpoint\\?balanceId=\\d+"))
                        .withQueryParam("balanceId", equalTo("1"))
                .willReturn(aResponse().
                        withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(objectMapper.writeValueAsString(getBalance()))));
    }

    private static Balance getBalance() {
        return Balance.
                builder().
                id(1L).
                amount(BigDecimal.valueOf(0)).
                build();
    }
}
