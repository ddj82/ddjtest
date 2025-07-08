package com.example.ddjtest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TossPaymentResponse {
    private String paymentKey;
    private String orderId;
    private String status;
    private Long totalAmount;
    private String method;
}
