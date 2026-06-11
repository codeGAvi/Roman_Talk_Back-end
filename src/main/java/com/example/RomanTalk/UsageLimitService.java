package com.example.RomanTalk;

import com.example.RomanTalk.entity.User;
import com.example.RomanTalk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;

@Service
public class UsageLimitService {


    private final UserRepository userRepository;

    private final RedisTemplate<String, String> redisTemplate;
    private static final int FREE_LIMIT = 20;

    public UsageLimitService(RedisTemplate<String, String> redisTemplate, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.redisTemplate = redisTemplate;
    }

    // Key format: usage:email:date
    private String getKey(String email) {
        return "usage:" + email + ":" + LocalDate.now();
    }

    public boolean isLimitReached(String email) {

        User user = userRepository.findByEmail(email).orElse(null);
        if(user !=null && user.isPro()) return false; // Pro users ke liye limit nahi

        String key = getKey(email);
        String count = redisTemplate.opsForValue().get(key);
        if (count == null) return false;
        return Integer.parseInt(count) >= FREE_LIMIT;
    }

    public void increment(String email) {
        String key = getKey(email);
        Long count = redisTemplate.opsForValue().increment(key);

        System.out.println("📊 COUNTER INCREMENT — " + email + " → count: " + count);

        // Pehli baar increment pe 24hr expiry set
        if (count == 1) {
            redisTemplate.expire(key, Duration.ofDays(1));
        }
    }

    public int getUsageCount(String email) {
        String key = getKey(email);
        String count = redisTemplate.opsForValue().get(key);
        return count == null ? 0 : Integer.parseInt(count);
    }
}
