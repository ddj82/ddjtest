package com.example.ddjtest.controller;

import com.example.ddjtest.Exception.PaymentException;
import com.example.ddjtest.dto.PaymentConfirmDto;
import com.example.ddjtest.dto.PaymentRequestDto;
import com.example.ddjtest.dto.PaymentRequestResponseDto;
import com.example.ddjtest.service.PointChargeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/points")
@RequiredArgsConstructor
@Slf4j
public class PointChargeController {

    private final PointChargeService pointChargeService;

    // 결제 요청 생성 (클라이언트에서 결제 위젯 띄우기 전)
    @PostMapping("/charge/request")
    public ResponseEntity<PaymentRequestResponseDto> createPaymentRequest(
            @Valid @RequestBody PaymentRequestDto request) {

        try {
            String orderId = pointChargeService.createPaymentRequest(request);
            PaymentRequestResponseDto response = new PaymentRequestResponseDto(orderId);
            return ResponseEntity.ok(response);

        } catch (PaymentException e) {
            log.error("결제 요청 생성 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    // 결제 승인 및 포인트 충전 (클라이언트 결제 완료 후)
    @PostMapping("/charge/confirm")
    public ResponseEntity<String> confirmPayment(
            @Valid @RequestBody PaymentConfirmDto request) {

        try {
            pointChargeService.confirmPaymentAndChargePoints(request);
            return ResponseEntity.ok("결제가 완료되고 포인트가 충전되었습니다.");

        } catch (PaymentException e) {
            log.error("결제 승인 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 사용자 포인트 조회
    @GetMapping("/balance/{userId}")
    public ResponseEntity<Long> getUserPoints(@PathVariable Long userId) {
        try {
            Long points = pointChargeService.getUserPoints(userId);
            return ResponseEntity.ok(points);

        } catch (PaymentException e) {
            log.error("포인트 조회 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}