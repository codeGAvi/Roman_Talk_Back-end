package com.example.RomanTalk.service;

import com.example.RomanTalk.dto.TranslationRequest;
import com.example.RomanTalk.dto.TranslationResponse;
import com.example.RomanTalk.entity.Translation;
import com.example.RomanTalk.repository.TranslationRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TranslationService {

    private final ChatClient chatClient;
    private final TranslationRepository translationRepository;


    public TranslationService(
            ChatClient.Builder chatClientBuilder,
            TranslationRepository translationRepository) {

        this.chatClient = chatClientBuilder.build();
        this.translationRepository = translationRepository;
    }

    public TranslationResponse translateIntoRomanized(TranslationRequest request){

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
