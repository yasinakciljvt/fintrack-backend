package com.jxt.fintrack.scheduler;

import com.jxt.fintrack.repository.BudgetRepository;
import com.jxt.fintrack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class MonthlyReportScheduler {

    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;

    // Her ayın 1'inde gece 00:00'da çalışır
    @Scheduled(cron = "0 0 0 1 * *")
    @Transactional
    public void resetMonthlyBudgets() {
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        log.info("Aylık bütçe sıfırlama başladı: {}/{}", lastMonth.getMonthValue(), lastMonth.getYear());

        userRepository.findAll().forEach(user -> {
            var budgets = budgetRepository.findByUser_IdAndMonthAndYear(
                    user.getId(), lastMonth.getMonthValue(), lastMonth.getYear());

            budgets.forEach(budget -> {
                budget.setNotified(false);
                budgetRepository.save(budget);
            });
        });

        log.info("Aylık bütçe sıfırlama tamamlandı");
    }
}