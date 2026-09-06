package com.assistant.backend.notification.consumer;

import com.assistant.backend.device.repository.DeviceTokenRepository;
import com.assistant.backend.notification.event.ReminderDueEvent;
import com.assistant.backend.notification.service.FcmService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ReminderEventConsumer {

    private final DeviceTokenRepository deviceTokenRepository;
    private final FcmService fcmService;

    public ReminderEventConsumer(DeviceTokenRepository deviceTokenRepository, FcmService fcmService) {
        this.deviceTokenRepository = deviceTokenRepository;
        this.fcmService = fcmService;
    }

    @KafkaListener(topics = "reminder.due", groupId = "assistant-group")
    public void consume(ReminderDueEvent event) {
        deviceTokenRepository.findByUserId(event.userId()).forEach(deviceToken -> fcmService.sendPush(
                deviceToken.getToken(),
                "Reminder: " + event.taskTitle(),
                "This task is due now"
                )
        );
    }
}