package com.example.RomanTalk.controller;

import com.example.RomanTalk.dto.LoginRequest;
import com.example.RomanTalk.dto.LoginResponse;
import com.example.RomanTalk.dto.SignupRequest;
import com.example.RomanTalk.entity.User;
import com.example.RomanTalk.repository.UserRepository;
import com.example.RomanTalk.service.AuthService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

    public AuthController(AuthService authService, UserRepository userRepository) {
        this.userRepository = userRepository;
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

    // user status
    @GetMapping("/status")
    public Map<String, Object> getUserStatus(@RequestParam String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Map<String, Object> response = new HashMap<>();
        response.put("isPro", user.isPro());
        response.put("name", user.getName());
        response.put("email", user.getEmail());
        return response;
    }

}