package com.livecode.ecommerce.controller;

import com.livecode.ecommerce.model.Entities.Product;
import com.livecode.ecommerce.model.Request.ProductRequest;
import com.livecode.ecommerce.model.Response.SuccessResponse;
import com.livecode.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity getAllCategories() {
        List<Product> product = productService.getAllProduct();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse<>("Success Get All Products", product));
    }

    @GetMapping("/{id}")
    public ResponseEntity getCategoryById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse<>("Success find Product with id "+id, product));
    }

    @PostMapping
    public ResponseEntity createCategory(@RequestBody ProductRequest productRequest) {
        Product product = productService.createProduct(productRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SuccessResponse<>("Success Creating Product", product));
    }

    @PutMapping("/{id}")
    public ResponseEntity updateCategory(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
        Product product = productService.updateProduct(id, productRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse<>("Success Updating Product", product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCategory(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse<>("Success deleting Product With Id "+ id, null));
    }
}
