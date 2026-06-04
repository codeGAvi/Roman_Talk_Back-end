package com.example.RomanTalk.controller;

import com.example.RomanTalk.dto.LoginRequest;
import com.example.RomanTalk.dto.LoginResponse;
import com.example.RomanTalk.dto.SignupRequest;
import com.example.RomanTalk.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public String signup(@RequestBody SignupRequest request) {
        return authService.signup(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}