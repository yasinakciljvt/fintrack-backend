package com.jxt.fintrack.service;

import com.jxt.fintrack.dto.request.TransactionRequest;
import com.jxt.fintrack.dto.response.TransactionResponse;
import com.jxt.fintrack.entity.Category;
import com.jxt.fintrack.entity.Transaction;
import com.jxt.fintrack.entity.User;
import com.jxt.fintrack.kafka.KafkaProducerService;
import com.jxt.fintrack.repository.BudgetRepository;
import com.jxt.fintrack.repository.CategoryRepository;
import com.jxt.fintrack.repository.TransactionRepository;
import com.jxt.fintrack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final BudgetRepository budgetRepository;
    private final KafkaProducerService kafkaProducerService;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
    }

    @Transactional
    public TransactionResponse create(TransactionRequest request) {
        User user = getCurrentUser();

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı"));

        Transaction transaction = Transaction.builder()
                .amount(request.getAmount())
                .type(request.getType())
                .note(request.getNote())
                .date(request.getDate())
                .user(user)
                .category(category)
                .build();

        transactionRepository.save(transaction);

        // Bütçe kontrolü — sadece gider işlemlerinde
        if (request.getType() == Transaction.TransactionType.EXPENSE) {
            checkBudgetAndNotify(user, category, request.getDate());
        }

        return toResponse(transaction);
    }

    public Page<TransactionResponse> getAll(Pageable pageable) {
        User user = getCurrentUser();
        return transactionRepository.findByUser_Id(user.getId(), pageable)
                .map(this::toResponse);
    }

    public List<TransactionResponse> getByDateRange(LocalDate start, LocalDate end) {
        User user = getCurrentUser();
        return transactionRepository
                .findByUser_IdAndDateBetweenOrderByDateDesc(user.getId(), start, end)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<TransactionResponse> getByCategory(Long categoryId) {
        User user = getCurrentUser();
        return transactionRepository
                .findByUser_IdAndCategory_Id(user.getId(), categoryId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public TransactionResponse update(Long id, TransactionRequest request) {
        User user = getCurrentUser();

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("İşlem bulunamadı"));

        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bu işleme erişim yetkiniz yok");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı"));

        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setNote(request.getNote());
        transaction.setDate(request.getDate());
        transaction.setCategory(category);

        return toResponse(transactionRepository.save(transaction));
    }

    @Transactional
    public void delete(Long id) {
        User user = getCurrentUser();

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("İşlem bulunamadı"));

        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bu işleme erişim yetkiniz yok");
        }

        transactionRepository.delete(transaction);
    }

    private void checkBudgetAndNotify(User user, Category category, LocalDate date) {
        budgetRepository
                .findByUser_IdAndCategory_IdAndMonthAndYear(
                        user.getId(),
                        category.getId(),
                        date.getMonthValue(),
                        date.getYear())
                .ifPresent(budget -> {
                    var totalSpent = transactionRepository.sumByUserIdAndTypeAndDateBetween(
                            user.getId(),
                            Transaction.TransactionType.EXPENSE,
                            date.withDayOfMonth(1),
                            date.withDayOfMonth(date.lengthOfMonth()));

                    if (totalSpent != null && totalSpent.compareTo(budget.getLimitAmount()) >= 0
                            && !budget.getNotified()) {
                        kafkaProducerService.sendBudgetAlert(user.getEmail(), category.getName(),
                                budget.getLimitAmount(), totalSpent);
                        budget.setNotified(true);
                    }
                });
    }

    private TransactionResponse toResponse(Transaction t) {
        return TransactionResponse.builder()
                .id(t.getId())
                .amount(t.getAmount())
                .type(t.getType())
                .note(t.getNote())
                .date(t.getDate())
                .categoryId(t.getCategory() != null ? t.getCategory().getId() : null)
                .categoryName(t.getCategory() != null ? t.getCategory().getName() : null)
                .createdAt(t.getCreatedAt())
                .build();
    }
}