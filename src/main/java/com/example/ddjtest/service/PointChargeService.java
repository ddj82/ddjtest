package com.example.ddjtest.service;

import com.example.ddjtest.Exception.PaymentException;
import com.example.ddjtest.client.TossPaymentsClient;
import com.example.ddjtest.dto.PaymentConfirmDto;
import com.example.ddjtest.dto.PaymentRequestDto;
import com.example.ddjtest.dto.TossPaymentResponse;
import com.example.ddjtest.entity.PointCharge;
import com.example.ddjtest.entity.User;
import com.example.ddjtest.entity.enums.PaymentStatus;
import com.example.ddjtest.repository.PointChargeRepository;
import com.example.ddjtest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PointChargeService {

    private final PointChargeRepository pointChargeRepository;
    private final UserRepository userRepository;
    private final TossPaymentsClient tossPaymentsClient;

    // 결제 요청 (클라이언트에서 결제 위젯 띄우기 전)
    public String createPaymentRequest(PaymentRequestDto request) {
        // 입력값 검증
        validatePaymentRequest(request);

        // 사용자 존재 확인
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new PaymentException("USER_NOT_FOUND", "사용자를 찾을 수 없습니다."));

        // 주문 ID 생성
        String orderId = generateOrderId();

        // 결제 요청 정보 저장
        PointCharge pointCharge = PointCharge.builder()
                .userId(request.getUserId())
                .amount(request.getAmount())
                .orderId(orderId)
                .status(PaymentStatus.PENDING)
                .build();

        pointChargeRepository.save(pointCharge);

        log.info("결제 요청 생성 완료: userId={}, amount={}, orderId={}",
                request.getUserId(), request.getAmount(), orderId);

        return orderId;
    }

    // 결제 승인 및 포인트 충전 (클라이언트 결제 완료 후 호출)
    public void confirmPaymentAndChargePoints(PaymentConfirmDto request) {
        log.info("결제 승인 시작: orderId={}, amount={}", request.getOrderId(), request.getAmount());

        // 주문 정보 조회
        PointCharge pointCharge = pointChargeRepository.findByOrderId(request.getOrderId())
                .orElseThrow(() -> new PaymentException("ORDER_NOT_FOUND", "주문 정보를 찾을 수 없습니다."));

        // 이미 처리된 결제인지 확인
        if (pointCharge.getStatus() == PaymentStatus.COMPLETED) {
            throw new PaymentException("ALREADY_PROCESSED", "이미 처리된 결제입니다.");
        }

        // 결제 금액 검증
        if (!pointCharge.getAmount().equals(request.getAmount())) {
            throw new PaymentException("AMOUNT_MISMATCH", "결제 금액이 일치하지 않습니다.");
        }

        // 사용자 조회
        User user = userRepository.findById(pointCharge.getUserId())
                .orElseThrow(() -> new PaymentException("USER_NOT_FOUND", "사용자를 찾을 수 없습니다."));

        try {
            // 클라이언트에서 결제 완료 후 서버 검증 (Mock)
            TossPaymentResponse paymentResponse = tossPaymentsClient.confirmPayment(request);

            // 결제 성공 시 포인트 충전
            if ("DONE".equals(paymentResponse.getStatus())) {
                // 포인트 충전
                user.chargePoints(pointCharge.getAmount());

                // 결제 내역 완료 처리
                pointCharge.completePayment(request.getPaymentKey());

                log.info("✅ 결제 검증 및 포인트 충전 완료: userId={}, amount={}, newPoint={}",
                        user.getId(), pointCharge.getAmount(), user.getPoint());

            } else {
                // 결제 실패 처리
                pointCharge.failPayment();
                throw new PaymentException("PAYMENT_FAILED", "결제 검증이 실패했습니다.");
            }

        } catch (PaymentException e) {
            // 결제 실패 처리
            pointCharge.failPayment();
            log.error("결제 처리 실패: orderId={}, error={}", request.getOrderId(), e.getMessage());
            throw e;
        } catch (Exception e) {
            // 예상치 못한 오류 처리
            pointCharge.failPayment();
            log.error("결제 처리 중 예상치 못한 오류 발생", e);
            throw new PaymentException("UNEXPECTED_ERROR", "결제 처리 중 오류가 발생했습니다.");
        }
    }

    // 사용자 포인트 조회
    @Transactional(readOnly = true)
    public Long getUserPoints(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new PaymentException("USER_NOT_FOUND", "사용자를 찾을 수 없습니다."));
        return user.getPoint();
    }

    private void validatePaymentRequest(PaymentRequestDto request) {
        if (request.getUserId() == null || request.getUserId() <= 0) {
            throw new PaymentException("INVALID_USER_ID", "유효하지 않은 사용자 ID입니다.");
        }

        if (request.getAmount() == null || request.getAmount() <= 0) {
            throw new PaymentException("INVALID_AMOUNT", "충전 금액은 0보다 커야 합니다.");
        }

        if (request.getAmount() > 1000000) {
            throw new PaymentException("AMOUNT_LIMIT_EXCEEDED", "한 번에 충전할 수 있는 최대 금액은 100만원입니다.");
        }
    }

    private String generateOrderId() {
        return "ORDER_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8);
    }
}