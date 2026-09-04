package com.assistant.backend.task.service;

import com.assistant.backend.auth.util.SecurityUtil;
import com.assistant.backend.task.entity.Task;
import com.assistant.backend.task.repository.TaskRepository;
import com.assistant.backend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    @Autowired
    public TaskService(TaskRepository taskRepository, UserRepository userRepository){
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }
    public Task createTask(Task task) {
        task.setUser(userRepository.findById(SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new RuntimeException("User not found")));
        return taskRepository.save(task);
    }
    public Task getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

        if (!task.getUser().getId().equals(SecurityUtil.getCurrentUserId())) {
            throw new RuntimeException("You do not have access to this task");
        }

        return task;
    }
    public List<Task> getTasksForUser(Long userId) {
        return taskRepository.findByUserId(userId);
    }
    public void deleteTask(Long id) {
        Task task = getTaskById(id);
        taskRepository.deleteById(task.getId());
    }
}
