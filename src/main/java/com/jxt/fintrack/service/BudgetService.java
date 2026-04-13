package com.jxt.fintrack.service;

import com.jxt.fintrack.dto.request.BudgetRequest;
import com.jxt.fintrack.dto.response.BudgetResponse;
import com.jxt.fintrack.entity.Budget;
import com.jxt.fintrack.entity.Category;
import com.jxt.fintrack.entity.Transaction;
import com.jxt.fintrack.entity.User;
import com.jxt.fintrack.repository.BudgetRepository;
import com.jxt.fintrack.repository.CategoryRepository;
import com.jxt.fintrack.repository.TransactionRepository;
import com.jxt.fintrack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
    }

    public List<BudgetResponse> getCurrentMonthBudgets() {
        User user = getCurrentUser();
        LocalDate now = LocalDate.now();
        return budgetRepository
                .findByUser_IdAndMonthAndYear(user.getId(), now.getMonthValue(), now.getYear())
                .stream()
                .map(b -> toResponse(b, now))
                .toList();
    }

    @Transactional
    public BudgetResponse create(BudgetRequest request) {
        User user = getCurrentUser();

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı"));

        budgetRepository.findByUser_IdAndCategory_IdAndMonthAndYear(
                        user.getId(), category.getId(), request.getMonth(), request.getYear())
                .ifPresent(b -> { throw new RuntimeException("Bu kategoriye ait bütçe zaten var"); });

        Budget budget = Budget.builder()
                .limitAmount(request.getLimitAmount())
                .month(request.getMonth())
                .year(request.getYear())
                .notified(false)
                .user(user)
                .category(category)
                .build();

        LocalDate date = LocalDate.of(request.getYear(), request.getMonth(), 1);
        return toResponse(budgetRepository.save(budget), date);
    }

    @Transactional
    public BudgetResponse update(Long id, BudgetRequest request) {
        User user = getCurrentUser();

        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bütçe bulunamadı"));

        if (!budget.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bu bütçeye erişim yetkiniz yok");
        }

        budget.setLimitAmount(request.getLimitAmount());
        budget.setNotified(false);

        LocalDate date = LocalDate.of(budget.getYear(), budget.getMonth(), 1);
        return toResponse(budgetRepository.save(budget), date);
    }

    @Transactional
    public void delete(Long id) {
        User user = getCurrentUser();

        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bütçe bulunamadı"));

        if (!budget.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bu bütçeye erişim yetkiniz yok");
        }

        budgetRepository.delete(budget);
    }

    private BudgetResponse toResponse(Budget b, LocalDate date) {
        BigDecimal spent = transactionRepository.sumByUserIdAndTypeAndDateBetween(
                b.getUser().getId(),
                Transaction.TransactionType.EXPENSE,
                date.withDayOfMonth(1),
                date.withDayOfMonth(date.lengthOfMonth()));

        spent = spent != null ? spent : BigDecimal.ZERO;
        BigDecimal remaining = b.getLimitAmount().subtract(spent);

        return BudgetResponse.builder()
                .id(b.getId())
                .limitAmount(b.getLimitAmount())
                .spentAmount(spent)
                .remainingAmount(remaining)
                .month(b.getMonth())
                .year(b.getYear())
                .categoryId(b.getCategory().getId())
                .categoryName(b.getCategory().getName())
                .notified(b.getNotified())
                .build();
    }
}