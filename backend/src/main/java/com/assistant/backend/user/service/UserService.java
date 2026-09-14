package com.assistant.backend.user.service;

import com.assistant.backend.user.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserCacheService userCacheService;

    public UserService(UserCacheService userCacheService) {
        this.userCacheService = userCacheService;
    }

    public User findOrCreateUser(String googleId, String email, String name, String pictureUrl) {
        User existing = userCacheService.getUserByGoogleId(googleId);
        if (existing != null) {
            return existing;
        }

        User newUser = new User();
        newUser.setGoogleId(googleId);
        newUser.setEmail(email);
        newUser.setName(name);
        newUser.setPictureUrl(pictureUrl);
        return userCacheService.createUser(newUser);
    }
}