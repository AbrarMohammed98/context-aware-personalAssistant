package com.assistant.backend.task.controller;

import com.assistant.backend.auth.util.SecurityUtil;
import com.assistant.backend.task.entity.Task;
import com.assistant.backend.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    @PostMapping
    public Task createTask(@RequestBody Task task){
        return taskService.createTask(task);
    }

    @GetMapping
    public List<Task> getMyTasks(){
        return taskService.getTasksForUser(SecurityUtil.getCurrentUserId());
    }

    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long id){
        return taskService.getTaskById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id){
        taskService.deleteTask(id);
    }
}