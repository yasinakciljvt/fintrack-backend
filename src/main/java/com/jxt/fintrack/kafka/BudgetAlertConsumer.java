package com.jxt.fintrack.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BudgetAlertConsumer {

    @KafkaListener(topics = "budget-alerts", groupId = "fintrack-group")
    public void consume(String message) {
        // Format: BUDGET_ALERT::email::categoryName::limit::spent
        String[] parts = message.split("::");

        if (parts.length == 5) {
            String email = parts[1];
            String categoryName = parts[2];
            String limit = parts[3];
            String spent = parts[4];

            log.warn("BUTCE ASIMI - Kullanici: {} | Kategori: {} | Limit: {} | Harcanan: {}",
                    email, categoryName, limit, spent);

            // Buraya ileride email servisi eklenecek
            // mailService.sendBudgetAlert(email, categoryName, limit, spent);
        }
    }
}