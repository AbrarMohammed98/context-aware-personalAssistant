package com.assistant.backend.reminder.service;

import com.assistant.backend.reminder.entity.Reminder;
import com.assistant.backend.reminder.repository.ReminderRepository;
import com.assistant.backend.task.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReminderService {
    private final ReminderRepository reminderRepository;
    @Autowired
    public ReminderService(ReminderRepository reminderRepository){
        this.reminderRepository=reminderRepository;
    }
    public Reminder createReminder(Reminder reminder){
        return reminderRepository.save(reminder);
    }
    public List<Reminder> getAllReminders(){
        return reminderRepository.findAll();
    }

    public Reminder getReminderById(Long id) {
        return reminderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reminder not found with id: " + id));
    }
    public List<Reminder> getRemindersByTask(Task task){
        return reminderRepository.findByTaskId(task.getId());
    }
    public List<Reminder> getDueReminders(){
        return reminderRepository.findByRemindAtBeforeAndSentFalse(LocalDateTime.now());
    }

}
