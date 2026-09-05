package com.assistant.backend.notification.consumer;

import com.assistant.backend.notification.event.ReminderDueEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ReminderEventConsumer {

    @KafkaListener(topics = "reminder.due", groupId = "assistant-group")
    public void consume(ReminderDueEvent event) {
        System.out.println("Received due reminder: " + event.reminderId()
                + " for user " + event.userId()
                + " - task: " + event.taskTitle());
    }
}