package com.example.RomanTalk.dto;

public class TranslationRequest {
    private String InputText;
    private String TargetLanguage;
    private String UserEmail;

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }





    // Constructor, getters, and setters
    public TranslationRequest() {}

    public TranslationRequest(String inputText, String targetLanguage) {
        InputText = inputText;
        TargetLanguage = targetLanguage;
    }

    public String getInputText() {
        return InputText;
    }

    public void setInputText(String inputText) {
        InputText = inputText;
    }



    public String getTargetLanguage() {
        return TargetLanguage;
    }

    public void setTargetLanguage(String targetLanguage) {
        TargetLanguage = targetLanguage;
    }
}
