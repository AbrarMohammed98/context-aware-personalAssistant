package com.assistant.backend.user.service;

import com.assistant.backend.user.entity.User;
import com.assistant.backend.user.repository.UserRepository;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserCacheService {

    private final UserRepository userRepository;

    public UserCacheService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Cacheable(value = "users", key = "#googleId")
    public User getUserByGoogleId(String googleId) {
        return userRepository.findByGoogleId(googleId).orElse(null);
    }

    @CachePut(value = "users", key = "#user.googleId")
    public User createUser(User user) {
        return userRepository.save(user);
    }
}