package com.assistant.backend.task.service;

import com.assistant.backend.task.entity.Task;
import com.assistant.backend.task.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    @Autowired
    public TaskService(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }
    public Task createTask(Task task){
        return taskRepository.save(task);
    }
    public List<Task> getAllTasks(){
        return taskRepository.findAll();
    }
    public Task getTaskById(Long id){
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: "+ id));
    }
    public void deleteTask(Long id){
        taskRepository.deleteById(id);
    }
}
