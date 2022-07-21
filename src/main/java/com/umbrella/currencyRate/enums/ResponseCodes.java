package com.umbrella.currencyRate.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum ResponseCodes {

    SUCCESS(2000),
    INTERNAL_ERROR(2001),
    UNKNOWN_ERROR(2002),
    DATA_FOUND(2003);

    private final int value;
    private static Map<Integer, ResponseCodes> map = new HashMap<>();

    ResponseCodes(int i) {
        this.value = i;
    }

    static {
        for (ResponseCodes responseCode : ResponseCodes.values()) {
            map.put(responseCode.value, responseCode);
        }
    }

    @JsonCreator
    public static ResponseCodes valueOf(int code) {
        return (ResponseCodes) map.get(code);
    }

    @JsonValue
    public int getValue() {
        return value;
    }
}
