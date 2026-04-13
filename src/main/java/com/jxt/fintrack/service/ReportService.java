package com.jxt.fintrack.service;

import com.jxt.fintrack.dto.response.ReportResponse;
import com.jxt.fintrack.entity.Transaction;
import com.jxt.fintrack.entity.User;
import com.jxt.fintrack.repository.TransactionRepository;
import com.jxt.fintrack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
    }

    public ReportResponse getMonthlySummary(int month, int year) {
        User user = getCurrentUser();

        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        BigDecimal totalIncome = transactionRepository.sumByUserIdAndTypeAndDateBetween(
                user.getId(), Transaction.TransactionType.INCOME, start, end);

        BigDecimal totalExpense = transactionRepository.sumByUserIdAndTypeAndDateBetween(
                user.getId(), Transaction.TransactionType.EXPENSE, start, end);

        totalIncome = totalIncome != null ? totalIncome : BigDecimal.ZERO;
        totalExpense = totalExpense != null ? totalExpense : BigDecimal.ZERO;

        BigDecimal netAmount = totalIncome.subtract(totalExpense);

        // Kategori bazlı harcama dağılımı
        List<Transaction> expenses = transactionRepository
                .findByUser_IdAndTypeAndDateBetween(
                        user.getId(), Transaction.TransactionType.EXPENSE, start, end);

        Map<String, BigDecimal> expenseByCategory = expenses.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getCategory() != null ? t.getCategory().getName() : "Diğer",
                        Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)
                ));

        // En çok harcanan kategori
        String topCategory = expenseByCategory.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("-");

        // Tasarruf oranı
        BigDecimal savingsRate = totalIncome.compareTo(BigDecimal.ZERO) > 0
                ? netAmount.divide(totalIncome, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        return ReportResponse.builder()
                .month(month)
                .year(year)
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .netAmount(netAmount)
                .savingsRate(savingsRate)
                .expenseByCategory(expenseByCategory)
                .topCategory(topCategory)
                .build();
    }

    public List<ReportResponse> getLast6MonthsSummary() {
        LocalDate now = LocalDate.now();
        return java.util.stream.IntStream.rangeClosed(0, 5)
                .mapToObj(i -> now.minusMonths(i))
                .map(date -> getMonthlySummary(date.getMonthValue(), date.getYear()))
                .toList();
    }
}