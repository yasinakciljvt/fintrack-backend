package com.jxt.fintrack.repository;

import com.jxt.fintrack.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> findByUser_Id(Long userId, Pageable pageable);

    List<Transaction> findByUser_IdOrderByDateDesc(Long userId);

    List<Transaction> findByUser_IdAndDateBetweenOrderByDateDesc(
            Long userId, LocalDate start, LocalDate end);

    List<Transaction> findByUser_IdAndCategory_Id(Long userId, Long categoryId);

    List<Transaction> findByUser_IdAndType(Long userId, Transaction.TransactionType type);

    List<Transaction> findByUser_IdAndTypeAndDateBetween(
            Long userId, Transaction.TransactionType type,
            LocalDate start, LocalDate end);

    @Query("""
            SELECT SUM(t.amount) FROM Transaction t
            WHERE t.user.id = :userId
            AND t.type = :type
            AND t.date BETWEEN :start AND :end
            """)
    BigDecimal sumByUserIdAndTypeAndDateBetween(
            @Param("userId") Long userId,
            @Param("type") Transaction.TransactionType type,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end);
}