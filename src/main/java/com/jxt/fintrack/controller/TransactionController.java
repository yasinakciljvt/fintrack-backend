package com.jxt.fintrack.controller;

import com.jxt.fintrack.dto.request.TransactionRequest;
import com.jxt.fintrack.dto.response.TransactionResponse;
import com.jxt.fintrack.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponse> create(
            @Valid @RequestBody TransactionRequest request) {
        return ResponseEntity.ok(transactionService.create(request));
    }

    @GetMapping
    public ResponseEntity<Page<TransactionResponse>> getAll(
            @PageableDefault(size = 20, sort = "date") Pageable pageable) {
        return ResponseEntity.ok(transactionService.getAll(pageable));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<TransactionResponse>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(transactionService.getByDateRange(start, end));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<TransactionResponse>> getByCategory(
            @PathVariable Long categoryId) {
        return ResponseEntity.ok(transactionService.getByCategory(categoryId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody TransactionRequest request) {
        return ResponseEntity.ok(transactionService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        transactionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}