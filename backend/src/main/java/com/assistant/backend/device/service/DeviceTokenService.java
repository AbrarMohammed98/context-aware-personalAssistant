package com.assistant.backend.device.service;

import com.assistant.backend.auth.util.SecurityUtil;
import com.assistant.backend.device.entity.DeviceToken;
import com.assistant.backend.device.repository.DeviceTokenRepository;
import com.assistant.backend.user.entity.User;
import com.assistant.backend.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DeviceTokenService {

    private final DeviceTokenRepository deviceTokenRepository;
    private final UserRepository userRepository;

    public DeviceTokenService(DeviceTokenRepository deviceTokenRepository, UserRepository userRepository) {
        this.deviceTokenRepository = deviceTokenRepository;
        this.userRepository = userRepository;
    }

    public DeviceToken registerToken(String token, String platform) {
        return deviceTokenRepository.findByToken(token)
                .map(existing -> {
                    existing.setPlatform(platform);
                    return deviceTokenRepository.save(existing);
                })
                .orElseGet(() -> {
                    DeviceToken deviceToken = new DeviceToken();
                    User user = userRepository.findById(SecurityUtil.getCurrentUserId())
                            .orElseThrow(() -> new RuntimeException("User not found"));
                    deviceToken.setUser(user);
                    deviceToken.setToken(token);
                    deviceToken.setPlatform(platform);
                    return deviceTokenRepository.save(deviceToken);
                });
    }

    public List<DeviceToken> getTokensForUser(Long userId) {
        return deviceTokenRepository.findByUserId(userId);
    }
}