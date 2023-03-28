package com.livecode.ecommerce.controller;

import com.livecode.ecommerce.model.Request.LoginRequest;
import com.livecode.ecommerce.model.Request.RegisterRequest;
import com.livecode.ecommerce.model.Response.SuccessResponse;
import com.livecode.ecommerce.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequest registerRequest){
        String token = authService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>("Success Registration", token));
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest){
        String token = authService.login(loginRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>("Success login", token));
    }

}
