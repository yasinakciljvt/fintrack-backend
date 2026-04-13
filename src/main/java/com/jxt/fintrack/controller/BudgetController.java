package com.jxt.fintrack.controller;

import com.jxt.fintrack.dto.request.BudgetRequest;
import com.jxt.fintrack.dto.response.BudgetResponse;
import com.jxt.fintrack.service.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    @GetMapping
    public ResponseEntity<List<BudgetResponse>> getCurrentMonthBudgets() {
        return ResponseEntity.ok(budgetService.getCurrentMonthBudgets());
    }

    @PostMapping
    public ResponseEntity<BudgetResponse> create(
            @Valid @RequestBody BudgetRequest request) {
        return ResponseEntity.ok(budgetService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BudgetResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody BudgetRequest request) {
        return ResponseEntity.ok(budgetService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        budgetService.delete(id);
        return ResponseEntity.noContent().build();
    }
}