package com.example.ddjtest.client;

import com.example.ddjtest.dto.PaymentConfirmDto;
import com.example.ddjtest.dto.TossPaymentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Component
@RequiredArgsConstructor
@Slf4j
public class TossPaymentsClient {

    @Value("${toss.payments.secretKey}")
    private String secretKey;

    @Value("${toss.payments.baseUrl}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    // 결제 승인 요청
    public TossPaymentResponse confirmPayment(PaymentConfirmDto request) {

        // 클라이언트 API가 완성되어 있다고 가정 (Mock)
        log.info("✅ 클라이언트에서 결제 완료 가정 - 포인트 충전 진행: orderId={}, amount={}",
                request.getOrderId(), request.getAmount());

        // 성공 응답
        return new TossPaymentResponse(
                request.getPaymentKey(),
                request.getOrderId(),
                "DONE",  // 토스 성공 상태
                request.getAmount(),
                "카드"
        );


        /*

        String url = baseUrl + "/payments/confirm";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic " + encodeSecretKey());

        Map<String, Object> body = Map.of(
                "paymentKey", request.getPaymentKey(),
                "orderId", request.getOrderId(),
                "amount", request.getAmount()
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<TossPaymentResponse> response = restTemplate.postForEntity(
                    url, entity, TossPaymentResponse.class);
            return response.getBody();
        } catch (Exception e) {
            throw new PaymentException("TOSS_PAYMENT_ERROR", e.getMessage());
        }

        */
    }

    private String encodeSecretKey() {
        return Base64.getEncoder().encodeToString((secretKey + ":").getBytes());
    }
}