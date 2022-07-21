package com.umbrella.currencyRate.model;

import com.umbrella.currencyRate.enums.ResponseCodes;
import lombok.Data;

@Data
public class ApiResponse {

    private ResponseCodes code;
    private Object response;

    public ApiResponse() {
        this.code = ResponseCodes.INTERNAL_ERROR;
    }
}