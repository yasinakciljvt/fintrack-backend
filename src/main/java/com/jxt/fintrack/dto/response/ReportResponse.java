package com.jxt.fintrack.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponse {

    private Integer month;
    private Integer year;
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal netAmount;
    private BigDecimal savingsRate;
    private Map<String, BigDecimal> expenseByCategory;
    private String topCategory;
}