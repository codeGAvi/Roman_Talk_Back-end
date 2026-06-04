package com.example.RomanTalk.dto;

public class TranslationResponse {
    String TranslatedText;

        public TranslationResponse(String translatedText) {
            TranslatedText = translatedText;
        }
    public String getTranslatedText() {
        return TranslatedText;
    }

    public void setTranslatedText(String translatedText) {
        TranslatedText = translatedText;
    }
}
