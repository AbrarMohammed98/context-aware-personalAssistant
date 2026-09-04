package com.assistant.backend.notification.scheduler;

import com.assistant.backend.notification.event.ReminderDueEvent;
import com.assistant.backend.notification.producer.ReminderEventProducer;
import com.assistant.backend.reminder.entity.Reminder;
import com.assistant.backend.reminder.service.ReminderService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReminderScheduler {

    private final ReminderService reminderService;
    private final ReminderEventProducer reminderEventProducer;

    public ReminderScheduler(ReminderService reminderService, ReminderEventProducer reminderEventProducer) {
        this.reminderService = reminderService;
        this.reminderEventProducer = reminderEventProducer;
    }

    @Scheduled(fixedRate = 60000)
    public void processDueReminders() {
        List<Reminder> dueReminders = reminderService.getDueReminders();

        for (Reminder reminder : dueReminders) {
            // We now only extract the title directly from the connected Task
            ReminderDueEvent event = new ReminderDueEvent(
                    reminder.getId(),
                    reminder.getTask().getUser().getId(),
                    reminder.getTask().getTitle()
            );

            reminderEventProducer.publishReminder(event);
            reminderService.markAsSent(reminder.getId());
        }
    }
}