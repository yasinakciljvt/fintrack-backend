package com.jxt.fintrack.controller;

import com.jxt.fintrack.dto.response.ReportResponse;
import com.jxt.fintrack.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/monthly")
    public ResponseEntity<ReportResponse> getMonthlySummary(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {

        LocalDate now = LocalDate.now();
        int m = month != null ? month : now.getMonthValue();
        int y = year != null ? year : now.getYear();

        return ResponseEntity.ok(reportService.getMonthlySummary(m, y));
    }

    @GetMapping("/last6months")
    public ResponseEntity<List<ReportResponse>> getLast6MonthsSummary() {
        return ResponseEntity.ok(reportService.getLast6MonthsSummary());
    }
}