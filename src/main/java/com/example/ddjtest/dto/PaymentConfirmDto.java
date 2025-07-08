package com.example.ddjtest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentConfirmDto {

    @NotBlank(message = "결제 키는 필수입니다.")
    private String paymentKey;

    @NotBlank(message = "주문 번호는 필수입니다.")
    private String orderId;

    @NotNull(message = "결제 금액은 필수입니다.")
    @Positive(message = "결제 금액을 확인해주세요.")
    private Long amount;
}