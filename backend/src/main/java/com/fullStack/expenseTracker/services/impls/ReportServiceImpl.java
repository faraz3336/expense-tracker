package com.fullStack.expenseTracker.services.impls;

import com.fullStack.expenseTracker.dto.reponses.ApiResponseDto;
import com.fullStack.expenseTracker.enums.ApiResponseStatus;
import com.fullStack.expenseTracker.dto.reponses.TransactionsMonthlySummaryDto;
import com.fullStack.expenseTracker.repository.TransactionRepository;
import com.fullStack.expenseTracker.services.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public ResponseEntity<ApiResponseDto<?>> getTotalByTransactionTypeAndUser(Long userId, int transactionTypeId, int month, int year) {
        log.info("Dashboard report endpoint=getTotalIncomeOrExpense userId={} transactionTypeId={} month={} year={}",
                userId, transactionTypeId, month, year);
        try {
            Double total = transactionRepository.findTotalByUserAndTransactionType(userId, transactionTypeId, month, year);
            return okNumber(total);
        } catch (Exception e) {
            log.error("Dashboard report failed endpoint=getTotalIncomeOrExpense userId={} transactionTypeId={} month={} year={}",
                    userId, transactionTypeId, month, year, e);
            return okNumber(0);
        }
    }

    @Override
    public ResponseEntity<ApiResponseDto<?>> getTotalNoOfTransactionsByUser(Long userId,  int month, int year) {
        log.info("Dashboard report endpoint=getTotalNoOfTransactions userId={} month={} year={}", userId, month, year);
        try {
            Long total = transactionRepository.findTotalNoOfTransactionsByUser(userId, month, year);
            return okNumber(total);
        } catch (Exception e) {
            log.error("Dashboard report failed endpoint=getTotalNoOfTransactions userId={} month={} year={}",
                    userId, month, year, e);
            return okNumber(0);
        }
    }

    @Override
    public ResponseEntity<ApiResponseDto<?>> getTotalExpenseByCategoryAndUser(String email, int categoryId, int month, int year) {
        log.info("Dashboard report endpoint=getTotalByCategory email={} categoryId={} month={} year={}",
                email, categoryId, month, year);
        try {
            Double total = transactionRepository.findTotalByUserAndCategory(email, categoryId, month, year);
            return okNumber(total);
        } catch (Exception e) {
            log.error("Dashboard report failed endpoint=getTotalByCategory email={} categoryId={} month={} year={}",
                    email, categoryId, month, year, e);
            return okNumber(0);
        }
    }

    @Override
    public ResponseEntity<ApiResponseDto<?>> getMonthlySummaryByUser(String email) {
        log.info("Dashboard report endpoint=getMonthlySummaryByUser email={}", email);
        try {
            List<Object[]> result = transactionRepository.findMonthlySummaryByUser(email, LocalDate.now().minusMonths(5));

            List<TransactionsMonthlySummaryDto> transactionsMonthlySummary = result.stream()
                    .map(data -> new TransactionsMonthlySummaryDto(
                            toInt(data[0]),
                            toDouble(data[1]),
                            toDouble(data[2])
                    )).toList();

            return ok(transactionsMonthlySummary);
        } catch (Exception e) {
            log.error("Dashboard report failed endpoint=getMonthlySummaryByUser email={}", email, e);
            return ok(new ArrayList<>());
        }
    }

    private ResponseEntity<ApiResponseDto<?>> okNumber(Number value) {
        return ok(value == null ? 0 : value);
    }

    private ResponseEntity<ApiResponseDto<?>> ok(Object response) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponseDto<>(ApiResponseStatus.SUCCESS, HttpStatus.OK, response)
        );
    }

    private int toInt(Object value) {
        return value instanceof Number number ? number.intValue() : 0;
    }

    private double toDouble(Object value) {
        return value instanceof Number number ? number.doubleValue() : 0;
    }
}
