package com.jxt.fintrack.dto.response;

import com.jxt.fintrack.entity.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {

    private Long id;
    private BigDecimal amount;
    private Transaction.TransactionType type;
    private String note;
    private LocalDate date;
    private Long categoryId;
    private String categoryName;
    private LocalDateTime createdAt;
}