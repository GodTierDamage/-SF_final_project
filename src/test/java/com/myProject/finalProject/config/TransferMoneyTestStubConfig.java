package com.myProject.finalProject.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.myProject.finalProject.entity.Spend;
import com.myProject.finalProject.entity.Transaction;
import com.myProject.finalProject.enums.SpendingType;
import lombok.SneakyThrows;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

public class TransferMoneyTestStubConfig {

    @SneakyThrows
    public static void setupStub(WireMockServer wireMockServer) {
        Transaction transfer = getTransfer();

        ObjectMapper mapper = new ObjectMapper();

        wireMockServer.stubFor(post(urlEqualTo("/api/test/endpoint"))
                .withRequestBody(equalToJson(mapper.writeValueAsString(transfer)))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody("Operation completed successfully")));
    }

    private static Transaction getTransfer() {
        return Spend.builder()
                .typeOfSpendingOperation(SpendingType.TRANSFER)
                .amount(BigDecimal.valueOf(100))
                .receiverId(2L)
                .balanceId(1L)
                .build();
    }
}
