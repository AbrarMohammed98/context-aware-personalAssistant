package com.assistant.backend.reminder.service;

import com.assistant.backend.auth.util.SecurityUtil;
import com.assistant.backend.reminder.entity.Reminder;
import com.assistant.backend.reminder.repository.ReminderRepository;
import com.assistant.backend.task.entity.Task;
import com.assistant.backend.task.repository.TaskRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReminderService {
    private final ReminderRepository reminderRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public ReminderService(ReminderRepository reminderRepository, TaskRepository taskRepository) {
        this.reminderRepository = reminderRepository;
        this.taskRepository = taskRepository;
    }

    public Reminder createReminder(Reminder reminder) {
        Task task = taskRepository.findById(reminder.getTask().getId())
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!task.getUser().getId().equals(SecurityUtil.getCurrentUserId())) {
            throw new RuntimeException("You do not have access to this task");
        }

        reminder.setTask(task);
        return reminderRepository.save(reminder);
    }

    public Reminder getReminderById(Long id) {
        Reminder reminder = reminderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reminder not found with id: " + id));
        if (!reminder.getTask().getUser().getId().equals(SecurityUtil.getCurrentUserId())) {
            throw new RuntimeException("You do not have access to this reminder");
        }
        return reminder;
    }

    public List<Reminder> getRemindersByTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));

        if (!task.getUser().getId().equals(SecurityUtil.getCurrentUserId())) {
            throw new RuntimeException("You do not have access to this task");
        }

        return reminderRepository.findByTaskId(taskId);
    }

    public List<Reminder> getDueReminders() {
        return reminderRepository.findByRemindAtBeforeAndSentFalse(LocalDateTime.now());
    }
    @Transactional
    public void markAsSent(Long reminderId) {
        Reminder reminder = reminderRepository.findById(reminderId)
                .orElseThrow(() -> new RuntimeException("Reminder not found"));
        reminder.setSent(true);
        reminderRepository.save(reminder);
    }
}