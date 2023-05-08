package com.myProject.finalProject.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum OperationType {

    INCOME(1),
    SPEND(2);

    private final int type;

    OperationType(int type) {
        this.type = type;
    }

    @JsonValue
    public int getType() {
        return type;
    }

    @JsonCreator
    public static OperationType fromType(int type) {
        for (OperationType value : values()) {
            if (value.type == type) {
                return value;
            }
        }
        throw new IllegalArgumentException("Unknown OperationType type: " + type);
    }
}
