package com.jxt.fintrack.repository;

import com.jxt.fintrack.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    List<Budget> findByUser_IdAndMonthAndYear(Long userId, Integer month, Integer year);

    Optional<Budget> findByUser_IdAndCategory_IdAndMonthAndYear(
            Long userId, Long categoryId, Integer month, Integer year);

    // Bildirim gönderilmemiş ve limiti aşılmış bütçeleri bul
    @Query("""
            SELECT b FROM Budget b
            WHERE b.user.id = :userId
            AND b.month = :month
            AND b.year = :year
            AND b.notified = false
            """)
    List<Budget> findUnnotifiedBudgets(
            @Param("userId") Long userId,
            @Param("month") Integer month,
            @Param("year") Integer year);
}