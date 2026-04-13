package com.jxt.fintrack.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class NotificationService {

    public void sendBudgetAlert(String email, String categoryName, BigDecimal limit, BigDecimal spent) {
        log.warn("BÜTÇE AŞIMI - Kullanıcı: {} | Kategori: {} | Limit: {} | Harcanan: {}",
                email, categoryName, limit, spent);

        // İleride email/push notification eklenebilir
    }
}

