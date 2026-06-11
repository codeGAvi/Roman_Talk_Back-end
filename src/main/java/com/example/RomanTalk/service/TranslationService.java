package com.example.RomanTalk.service;

import com.example.RomanTalk.UsageLimitService;
import com.example.RomanTalk.dto.TranslationRequest;
import com.example.RomanTalk.dto.TranslationResponse;
import com.example.RomanTalk.entity.Translation;
import com.example.RomanTalk.repository.TranslationRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TranslationService {

    @Autowired
    @Lazy
    private TranslationService self; // Self-injection for calling @Cacheable method

    private final ChatClient chatClient;
    private final TranslationRepository translationRepository;
    private final UsageLimitService usageLimitService;


    public TranslationService(
            ChatClient.Builder chatClientBuilder,
            TranslationRepository translationRepository,
            UsageLimitService usageLimitService){

        this.chatClient = chatClientBuilder.build();
        this.translationRepository = translationRepository;
        this.usageLimitService = usageLimitService;
    }

    public TranslationResponse translate(TranslationRequest request) {

        // Limit check
        if (usageLimitService.isLimitReached(request.getUserEmail())) {
            return new TranslationResponse("Daily limit reached! Upgrade to Pro.");
        }

        // call the method that has @Cacheable
        TranslationResponse response = self.translateIntoRomanized(request);

        // ✅ Counter increment — sirf cache miss pe
        // (yeh hum alag track karenge)
        return response;
    }

    @Cacheable(value = "translations", key = "#request.inputText + '_' + #request.targetLanguage")
    public TranslationResponse translateIntoRomanized(TranslationRequest request){

        // check if user has reached daily limit
        if (usageLimitService.isLimitReached(request.getUserEmail())) {
            return new TranslationResponse("Daily limit reached! Upgrade to Pro for unlimited translations.");
        }

        // verify if gemini is being called
        System.out.println("Gemini is being called for translation..." + request.getInputText());

        // Input validation
        if (request.getInputText() == null || request.getInputText().trim().isEmpty()) {
            return new TranslationResponse("Please enter some text.");
        }


        String text = request.getInputText();
        String language = request.getTargetLanguage();

        var systemMessage = new SystemMessage("You are a Intelligent translator." +
                "Detect the language of the input automatically" +
                "Translate input text into the target language specified by the user. " +
                "Output MUST be in Roman script (English letters only). " +
                "Do NOT use any native script. " +
                "Keep translation natural and conversational. " +
                "Return ONLY translated sentence. ");

        var userMessage = new UserMessage(
                String.format("Convert the following text: '%s' into romanized %s", text, language)
        );

        Prompt prompt =new Prompt(List.of(systemMessage,userMessage));

        String response = chatClient.prompt(prompt).call().content();

        // Increase usage count for the user
        usageLimitService.increment(request.getUserEmail());

        // Save translation to database
        Translation translation = new Translation();

        translation.setUserEmail(request.getUserEmail());

        translation.setInputText(text);

        translation.setTranslatedText(response);

        translation.setTargetLanguage(language);

        translation.setCreatedAt(LocalDateTime.now());

        translationRepository.save(translation);

        return new TranslationResponse(response);

    }

    // Conversation history retrieval
    public List<Translation> getHistory(String email) {
        return translationRepository
                .findByUserEmailOrderByCreatedAtDesc(email);
    }
}
