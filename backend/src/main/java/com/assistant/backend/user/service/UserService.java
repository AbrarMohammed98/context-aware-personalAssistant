package com.assistant.backend.user.service;

import com.assistant.backend.user.entity.User;
import com.assistant.backend.user.repository.UserRepository;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
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

    public User findOrCreateUser(String googleId, String email, String name, String pictureUrl) {
        User existing = getUserByGoogleId(googleId);
        if (existing != null) {
            return existing;
        }

        User newUser = new User();
        newUser.setGoogleId(googleId);
        newUser.setEmail(email);
        newUser.setName(name);
        newUser.setPictureUrl(pictureUrl);
        return createUser(newUser);
    }
}