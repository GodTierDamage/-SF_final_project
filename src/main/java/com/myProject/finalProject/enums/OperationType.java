package com.myProject.finalProject.enums;

public enum OperationType {

    INCOME(1),
    SPEND(2);

    private final int type;

    OperationType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
