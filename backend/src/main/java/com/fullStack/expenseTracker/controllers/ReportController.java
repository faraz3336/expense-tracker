package com.fullStack.expenseTracker.controllers;

import com.fullStack.expenseTracker.dto.reponses.ApiResponseDto;
import com.fullStack.expenseTracker.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(originPatterns = {"http://localhost:3000", "https://*.vercel.app"})
@RestController
@RequestMapping("/mywallet/report")
public class ReportController {

    @Autowired
    ReportService reportService;


    @GetMapping("/getTotalIncomeOrExpense")
    @PreAuthorize(("hasRole('ROLE_USER')"))
    public ResponseEntity<ApiResponseDto<?>> getTotalIncomeOrExpense(@RequestParam("userId") Long userId,
                                                                     @RequestParam("transactionTypeId") int transactionTypeId,
                                                                     @RequestParam("month") int month,
                                                                     @RequestParam("year") int year) {
        return reportService.getTotalByTransactionTypeAndUser(userId, transactionTypeId, month, year);
    }

    @GetMapping("/getTotalNoOfTransactions")
    @PreAuthorize(("hasRole('ROLE_USER')"))
    public ResponseEntity<ApiResponseDto<?>> getTotalNoOfTransactions(@RequestParam("userId") Long userId,
                                                                      @RequestParam("month") int month,
                                                                      @RequestParam("year") int year) {
        return reportService.getTotalNoOfTransactionsByUser(userId, month, year);
    }

    @GetMapping("/getTotalByCategory")
    @PreAuthorize(("hasRole('ROLE_USER')"))
    public ResponseEntity<ApiResponseDto<?>> getTotalByCategory(@RequestParam("email") String email,
                                                                @RequestParam("categoryId") int categoryId,
                                                                @RequestParam("month") int month,
                                                                @RequestParam("year") int year) {
        return reportService.getTotalExpenseByCategoryAndUser(email, categoryId, month, year);
    }

    @GetMapping("/getMonthlySummaryByUser")
    @PreAuthorize(("hasRole('ROLE_USER')"))
    public ResponseEntity<ApiResponseDto<?>> getMonthlySummaryByUser(@RequestParam("email") String email) {
        return reportService.getMonthlySummaryByUser(email);
    }

}
