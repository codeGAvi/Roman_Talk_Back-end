package com.example.RomanTalk.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class HealthController {

    @GetMapping("/")
    public String home() {
        return "RomanTalk Backend Running";
    }

    @GetMapping("/health")
    public String health() {
        return "UP";
    }
}
