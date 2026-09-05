package com.assistant.backend.device.controller;

import com.assistant.backend.device.entity.DeviceToken;
import com.assistant.backend.device.service.DeviceTokenService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/device")
public class DeviceController {

    private final DeviceTokenService deviceTokenService;

    public DeviceController(DeviceTokenService deviceTokenService) {
        this.deviceTokenService = deviceTokenService;
    }

    @PostMapping("/register-token")
    public DeviceToken registerToken(@RequestBody RegisterTokenRequest request) {
        return deviceTokenService.registerToken(request.token(), request.platform());
    }

    public record RegisterTokenRequest(String token, String platform) {}
}