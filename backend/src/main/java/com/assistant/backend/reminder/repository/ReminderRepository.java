package com.assistant.backend.reminder.repository;

import com.assistant.backend.reminder.entity.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    List<Reminder> findByRemindAtBeforeAndSentFalse(LocalDateTime time);
    List<Reminder> findByTaskId(Long taskId);
}
