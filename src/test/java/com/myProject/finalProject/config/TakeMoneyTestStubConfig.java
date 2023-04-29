package com.myProject.finalProject.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.myProject.finalProject.entity.Balance;
import com.myProject.finalProject.entity.Spend;
import lombok.SneakyThrows;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class TakeMoneyTestStubConfig {

    @SneakyThrows
    public static void setupStub(WireMockServer wireMockServer) {


        ObjectMapper mapper = new ObjectMapper();
        wireMockServer.stubFor(post(urlEqualTo("/api/test/endpoint"))
                .withRequestBody(equalToJson(mapper.writeValueAsString(getSpend())))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(mapper.writeValueAsString(getBalance()))));
    }

    private static Spend getSpend() {
        return Spend.builder()
                .id(1L)
                .balanceId(1L)
                .spend(BigDecimal.valueOf(500))
                .build();
    }

    private static Balance getBalance() {
        return Balance.builder()
                .id(1L)
                .balance(BigDecimal.valueOf(500))
                .build();
    }
}
