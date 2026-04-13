package com.jxt.fintrack.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendBudgetAlert(String email, String categoryName,
                                BigDecimal limit, BigDecimal spent) {
        String message = String.format(
                "BUDGET_ALERT::%s::%s::%.2f::%.2f",
                email, categoryName, limit, spent);

        kafkaTemplate.send("budget-alerts", email, message);
        log.info("Bütçe aşım bildirimi gönderildi: {}", email);
    }
}