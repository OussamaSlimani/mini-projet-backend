package com.example.agents.util;

import org.springframework.stereotype.Component;
import java.security.SecureRandom;

@Component
public class UserIdGenerator {
    
    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "123456789";
    private static final SecureRandom random = new SecureRandom();
    
    public String generateUserId() {
        StringBuilder userId = new StringBuilder();
        
        // 2 lettres aléatoires
        for (int i = 0; i < 2; i++) {
            userId.append(LETTERS.charAt(random.nextInt(LETTERS.length())));
        }
        
        // 5 chiffres aléatoires
        for (int i = 0; i < 5; i++) {
            userId.append(NUMBERS.charAt(random.nextInt(NUMBERS.length())));
        }
        
        return userId.toString();
    }
}