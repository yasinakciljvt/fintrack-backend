package com.jxt.fintrack.dto.request;

import com.jxt.fintrack.entity.Transaction;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransactionRequest {

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
    private Transaction.TransactionType type;

    private String note;

    @NotNull
    private LocalDate date;

    @NotNull
    private Long categoryId;
}