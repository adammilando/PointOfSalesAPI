package com.livecode.ecommerce.controller;

import com.livecode.ecommerce.model.Entities.SaleDetail;
import com.livecode.ecommerce.model.Entities.Transaction;
import com.livecode.ecommerce.model.Entities.User;
import com.livecode.ecommerce.model.Request.SalesDetailRequest;
import com.livecode.ecommerce.model.Request.TransactionRequest;
import com.livecode.ecommerce.model.Request.UserRequest;
import com.livecode.ecommerce.model.Response.SuccessResponse;
import com.livecode.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping
    public ResponseEntity getAllCategories(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer size,
            @RequestParam(defaultValue = "ASC") String direction,
            @RequestParam(defaultValue = "id") String sort
    ) {
        Page<User> user = userService.getAllUser(page, size, direction, sort);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse<>("Success Get All User", user));
    }

    @GetMapping("/{id}")
    public ResponseEntity getCategoryById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse<>("Success find User with id "+id, user));
    }


    @PostMapping
    public ResponseEntity createTransaction(@RequestBody User userCreate) {
        User user = userService.createUser(userCreate);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SuccessResponse<>("Success Creating Detail Transaction", user));
    }

    @PutMapping("/{id}")
    public ResponseEntity updateCategory(@PathVariable Long id, @RequestBody UserRequest userRequest) {
        User user = userService.updateCategory(id, userRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse<>("Success Updating User", user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCategory(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse<>("Success deleting User With Id "+ id, null));
    }

}
