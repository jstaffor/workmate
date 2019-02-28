package com.workmate.server.model.enums;

public enum ENUM_active
{
    INACTIVE(0), ACTIVE(1);

    private final int value;
    private ENUM_active(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
