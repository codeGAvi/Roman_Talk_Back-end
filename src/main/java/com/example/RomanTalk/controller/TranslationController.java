package com.example.RomanTalk.controller;

import com.example.RomanTalk.dto.TranslationRequest;
import com.example.RomanTalk.dto.TranslationResponse;
import com.example.RomanTalk.entity.Translation;
import com.example.RomanTalk.service.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TranslationController {

    @Autowired
    TranslationService translationService;

    @PostMapping("/translate")
    public TranslationResponse translateIntoRomanized(@RequestBody TranslationRequest request){
        return translationService.translateIntoRomanized(request);
    }

    // conversation history endpoint
    @GetMapping("/history")
    public List<Translation> history(
            @RequestParam String email) {

        return translationService.getHistory(email);
    }
}
