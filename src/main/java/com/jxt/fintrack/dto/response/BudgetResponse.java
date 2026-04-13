package com.jxt.fintrack.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BudgetResponse {

    private Long id;
    private BigDecimal limitAmount;
    private BigDecimal spentAmount;
    private BigDecimal remainingAmount;
    private Integer month;
    private Integer year;
    private Long categoryId;
    private String categoryName;
    private Boolean notified;
}