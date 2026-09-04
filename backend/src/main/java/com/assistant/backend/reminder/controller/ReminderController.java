package com.assistant.backend.reminder.controller;

import com.assistant.backend.reminder.entity.Reminder;
import com.assistant.backend.reminder.service.ReminderService;
import com.assistant.backend.task.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reminders")

public class ReminderController {
    private final ReminderService reminderService;
    @Autowired
    public ReminderController(ReminderService reminderService){
        this.reminderService=reminderService;
    }
    @PostMapping
    public Reminder createReminder(@RequestBody Reminder reminder){
        return reminderService.createReminder(reminder);
    }

    @GetMapping("/{id}")
    public Reminder getReminderById(@PathVariable Long id){
        return reminderService.getReminderById(id);
    }
    @GetMapping("/task/{taskId}")
    public List<Reminder> getReminderByTask(@PathVariable Long taskId){

        return reminderService.getRemindersByTask(taskId);
    }

}
