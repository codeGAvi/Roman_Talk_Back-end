package com.example.RomanTalk.repository;

import com.example.RomanTalk.entity.Translation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TranslationRepository
        extends JpaRepository<Translation, Long> {

    List<Translation>
    findByUserEmailOrderByCreatedAtDesc(String userEmail);

}