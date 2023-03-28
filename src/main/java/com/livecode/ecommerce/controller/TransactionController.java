package com.livecode.ecommerce.controller;

import com.livecode.ecommerce.model.Entities.SaleDetail;
import com.livecode.ecommerce.model.Request.SalesDetailRequest;
import com.livecode.ecommerce.model.Response.SuccessResponse;
import com.livecode.ecommerce.service.SalesDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction/")
public class TransactionController {
    @Autowired
    private SalesDetailsService salesDetailsService;

    @GetMapping
    public ResponseEntity getAllCategories() {
        List<SaleDetail> saleDetails = salesDetailsService.getAll();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse<>("Success Get All Categories", saleDetails));
    }

    @GetMapping("/{id}")
    public ResponseEntity getCategoryById(@PathVariable Long id) {
        SaleDetail salesById = salesDetailsService.getSalesById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse<>("Success find Categories with id "+id, salesById));
    }

    @PostMapping
    public ResponseEntity createCategory(@RequestBody SalesDetailRequest salesDetailRequest) {
        SaleDetail saleDetail = salesDetailsService.createTransaction(salesDetailRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SuccessResponse<>("Success Creating Categories", saleDetail));
    }

    @PutMapping("/{id}")
    public ResponseEntity updateCategory(@PathVariable Long id, @RequestBody SalesDetailRequest salesDetailRequest) {
        SaleDetail saleDetail = salesDetailsService.updateTransaction(id, salesDetailRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse<>("Success Updating Category", saleDetail));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCategory(@PathVariable Long id) {
        salesDetailsService.deleteTransaction(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse<>("Success deleting Category With Id "+ id, null));
    }
}
