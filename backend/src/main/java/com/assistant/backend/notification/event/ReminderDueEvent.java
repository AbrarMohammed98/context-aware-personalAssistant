package com.assistant.backend.notification.event;

public record ReminderDueEvent(
        Long reminderId,
        Long userId,
        String taskTitle
) { }
