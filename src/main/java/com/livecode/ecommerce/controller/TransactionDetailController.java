package com.livecode.ecommerce.controller;

import com.livecode.ecommerce.model.Entities.Transaction;
import com.livecode.ecommerce.model.Request.TransactionRequest;
import com.livecode.ecommerce.model.Response.SuccessResponse;
import com.livecode.ecommerce.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detail")
public class TransactionDetailController {
    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public ResponseEntity getAllTransaction() {
        List<Transaction> transactions = transactionService.getAllTransaction();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse<>("Success Get All detail", transactions));
    }

    @GetMapping("/{id}")
    public ResponseEntity getTransactionById(@PathVariable Long id) {
        Transaction transaction = transactionService.getTransactionById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse<>("Success Get transaction With Id "+ id, transaction));
    }

    @PostMapping
    public ResponseEntity createReport(@RequestBody TransactionRequest saleTransactionRequest) {
        Transaction transaction = transactionService.createTransaction(saleTransactionRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SuccessResponse<>("Success Creating Detail Transaction", transaction));
    }

//    @GetMapping("/daily")
//    public ResponseEntity getDailyReport(@RequestParam ("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateTime) {
//        List<Transaction> transactions = transactionService.getDailyReport(dateTime);
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(new SuccessResponse<>("Success get Daily report", transactions));
//    }
//
//    @GetMapping("/monthly")
//    public ResponseEntity getMonthlyReport(@RequestParam int month, @RequestParam int year) {
//        List<Transaction> transactions = transactionService.getMonthlyReport(month, year);
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(new SuccessResponse<>("Success Get Monthly report", transactions));
//    }
}
