package com.example.ddjtest.Exception;

import lombok.Getter;

@Getter
public class PaymentException extends RuntimeException {
    private final String code;

    public PaymentException(String code, String message) {
        super(message);
        this.code = code;
    }

    public PaymentException(String message) {
        super(message);
        this.code = "PAYMENT_ERROR";
    }
}
