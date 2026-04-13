package com.jxt.fintrack.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BudgetRequest {

    @NotNull
    @Positive
    private BigDecimal limitAmount;

    @NotNull
    @Min(1) @Max(12)
    private Integer month;

    @NotNull
    @Min(2000)
    private Integer year;

    @NotNull
    private Long categoryId;
}