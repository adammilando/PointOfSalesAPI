package com.livecode.ecommerce.service;

import com.livecode.ecommerce.model.Entities.Auth;
import com.livecode.ecommerce.model.Entities.User;
import com.livecode.ecommerce.model.Request.LoginRequest;
import com.livecode.ecommerce.model.Request.RegisterRequest;
import com.livecode.ecommerce.repository.AuthRepository;
import com.livecode.ecommerce.utils.JwtUtil;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserService userService;

    @Mock
    private AuthRepository authRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private JwtUtil jwtUtil;

    private RegisterRequest registerRequest;
    private Auth auth;
    private User user;
    private LoginRequest loginRequest;

    @BeforeEach
    public void setUp() {
        registerRequest = new RegisterRequest("Test", "user@example.com", "pssword", "0858123456");
        auth = new Auth("user@example.com", "password");
        user = new User("Test", "0858123456", auth);

        loginRequest = new LoginRequest("user@example.com", "password");
    }

    @Test
    public void testRegister() {
        when(modelMapper.map(registerRequest, Auth.class)).thenReturn(auth);
        when(authRepository.save(auth)).thenReturn(auth);
        when(modelMapper.map(registerRequest, User.class)).thenReturn(user);
        doNothing().when(userService).createUser(user);
        when(jwtUtil.generateToken(user.getAuth().getEmail())).thenReturn("sample_token");

        String token = authService.register(registerRequest);

        assertEquals("sample_token", token);
    }

    @Test
    public void testRegister_DataAccessException() {
        when(modelMapper.map(registerRequest, Auth.class)).thenReturn(auth);
        when(authRepository.save(auth)).thenThrow(DataAccessException.class);

        assertThrows(EntityExistsException.class, () -> authService.register(registerRequest));
    }

    @Test
    public void testLogin() {
        when(authRepository.findById(loginRequest.getEmail())).thenReturn(Optional.of(auth));
        when(jwtUtil.generateToken(loginRequest.getEmail())).thenReturn("sample_token");

        String token = authService.login(loginRequest);

        assertEquals("sample_token", token);
    }

    @Test
    public void testLogin_UserNotFound() {
        when(authRepository.findById(loginRequest.getEmail())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> authService.login(loginRequest));
        assertEquals("user is not found", exception.getMessage());
    }

    @Test
    public void testLogin_PasswordNotMatch() {
        auth.setPassword("wrong_password");
        when(authRepository.findById(loginRequest.getEmail())).thenReturn(Optional.of(auth));

        Exception exception = assertThrows(RuntimeException.class, () -> authService.login(loginRequest));
        assertEquals("Password is not match", exception.getMessage());
    }
}

