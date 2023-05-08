package com.myProject.finalProject.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum SpendingType {

    WITHDRAW("Withdraw"),
    PURCHASE("Purchase"),
    TRANSFER("Transfer");

    private final String type;

    SpendingType(String type) {
        this.type = type;
    }

    @JsonValue
    public String getType() {
        return type;
    }
}
