package com.livecode.ecommerce.controller;

import com.livecode.ecommerce.model.Entities.Transaction;
import com.livecode.ecommerce.model.Request.TransactionRequest;
import com.livecode.ecommerce.model.Response.SuccessResponse;
import com.livecode.ecommerce.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/detail")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public ResponseEntity getAllTransaction(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer size,
            @RequestParam(defaultValue = "ASC") String direction,
            @RequestParam(defaultValue = "id") String sort
    ) {
        Page<Transaction> transactions = transactionService.getAllTransaction(page, size, direction, sort);
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
    public ResponseEntity createTransaction(@RequestBody TransactionRequest saleTransactionRequest) {
        Transaction transaction = transactionService.createTransaction(saleTransactionRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SuccessResponse<>("Success Creating Detail Transaction", transaction));
    }

    @PutMapping("/{id}")
    public ResponseEntity updateTransaction(@PathVariable Long id, @RequestBody TransactionRequest transactionRequest) {
        Transaction transaction = transactionService.updateTransaction(id, transactionRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse<>("Success Updating Transaction", transaction));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCategory(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse<>("Success deleting Transaction With Id "+ id, null));
    }
}
