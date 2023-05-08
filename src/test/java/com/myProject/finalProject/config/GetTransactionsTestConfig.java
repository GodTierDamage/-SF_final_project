package com.myProject.finalProject.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.SneakyThrows;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class GetTransactionsTestConfig {

    @SneakyThrows
    public static void setupStub(WireMockServer wireMockServer, String body) {
        wireMockServer.stubFor(get(urlMatching("/api/test/endpoint\\?balanceId=\\d+"))
                .withQueryParam("balanceId", equalTo("1"))
                .willReturn(aResponse().
                        withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(body)));
    }
}
