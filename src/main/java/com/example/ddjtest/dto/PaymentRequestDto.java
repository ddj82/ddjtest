package com.example.ddjtest.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDto {

    @NotNull(message = "사용자 ID는 필수입니다.")
    @Positive(message = "사용자 ID를 확인해주세요.")
    private Long userId;

    @NotNull(message = "충전 금액은 필수입니다.")
    @Positive(message = "충전 금액을 확인해주세요.")
    private Long amount;
}
