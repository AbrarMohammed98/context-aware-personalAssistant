package com.assistant.backend.notification.producer;

import com.assistant.backend.notification.event.ReminderDueEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ReminderEventProducer {

    private static final String TOPIC = "reminder.due";
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ReminderEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishReminder(ReminderDueEvent event) {
        kafkaTemplate.send(TOPIC, String.valueOf(event.userId()), event);
    }
}