package com.livecode.ecommerce.controller;

import com.livecode.ecommerce.model.Entities.Category;
import com.livecode.ecommerce.model.Request.CategoryRequest;
import com.livecode.ecommerce.model.Response.SuccessResponse;
import com.livecode.ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoriesController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity getAllCategories() {
        List<Category> categories = categoryService.getAllCategory();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse<>("Success Get All Categories", categories));
    }

    @GetMapping("/{id}")
    public ResponseEntity getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse<>("Success find Categories with id "+id, category));
    }

    @PostMapping
    public ResponseEntity createCategory(@RequestBody CategoryRequest categoryRequest) {
        Category category = categoryService.createCategory(categoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SuccessResponse<>("Success Creating Categories", category));
    }

    @PutMapping("/{id}")
    public ResponseEntity updateCategory(@PathVariable Long id, @RequestBody CategoryRequest categoryDetail) {
        Category category = categoryService.updateCategory(id, categoryDetail);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse<>("Success Updating Category", category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse<>("Success deleting Category With Id "+ id, null));
    }

}

