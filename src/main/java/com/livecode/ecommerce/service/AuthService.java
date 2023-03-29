package com.livecode.ecommerce.service;

import com.livecode.ecommerce.model.Entities.Auth;
import com.livecode.ecommerce.model.Entities.User;
import com.livecode.ecommerce.model.Request.LoginRequest;
import com.livecode.ecommerce.model.Request.RegisterRequest;
import com.livecode.ecommerce.repository.AuthRepository;
import com.livecode.ecommerce.utils.JwtUtil;
import jakarta.persistence.EntityExistsException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private UserService userService;

    private AuthRepository authRepository;

    private ModelMapper modelMapper;

    private JwtUtil jwtUtil;
    @Autowired
    public AuthService(UserService userService, AuthRepository authRepository, ModelMapper modelMapper, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authRepository = authRepository;
        this.modelMapper = modelMapper;
        this.jwtUtil = jwtUtil;
    }

    public String register(RegisterRequest registerRequest){
        try {
            Auth auth = modelMapper.map(registerRequest, Auth.class);
            Auth authRequest = authRepository.save(auth);

            User user = modelMapper.map(registerRequest, User.class);
            user.setAuth(authRequest);
            userService.createUser(user);

            String token = jwtUtil.generateToken(user.getAuth().getEmail());
            return  token;
        }catch (DataAccessException e){
            throw new EntityExistsException(e);
        }
    }

    public String login(LoginRequest loginRequest){
        try {
            Optional<Auth> auth = authRepository.findById(loginRequest.getEmail());
            if (auth.isEmpty()) {
                throw new RuntimeException("user is not found");
            }
            if (!auth.get().getPassword().equals(loginRequest.getPassword())){
                throw new RuntimeException("Password is not match");
            }
            String token = jwtUtil.generateToken(loginRequest.getEmail());
            return token;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
