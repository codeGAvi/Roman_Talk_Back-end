package com.example.RomanTalk.controller;

import com.example.RomanTalk.UsageLimitService;
import com.example.RomanTalk.dto.TranslationRequest;
import com.example.RomanTalk.dto.TranslationResponse;
import com.example.RomanTalk.entity.Translation;
import com.example.RomanTalk.service.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class TranslationController {

    @Autowired
    TranslationService translationService;

    @Autowired
    UsageLimitService usageLimitService;

    @PostMapping("/translate")
    public TranslationResponse translateIntoRomanized(@RequestBody TranslationRequest request){
        return translationService.translate(request);
    }

    // conversation history endpoint
    @GetMapping("/history")
    public List<Translation> history(
            @RequestParam String email) {

        return translationService.getHistory(email);
    }

    // show user's remaining translations for the day
    @GetMapping("/usage")
    public Map<String,Integer> getUsage(@RequestParam String email){
        int count = usageLimitService.getUsageCount(email);
        return Map.of(
                "used", count,
                "limit",20,
                "remaining", 20 - count
        );
    }
}
