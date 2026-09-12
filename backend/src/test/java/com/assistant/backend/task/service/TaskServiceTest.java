package com.assistant.backend.task.service;

import com.assistant.backend.task.entity.Task;
import com.assistant.backend.task.repository.TaskRepository;
import com.assistant.backend.user.entity.User;
import com.assistant.backend.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
    }

    @Test
    void createTask_shouldAssignCurrentUserAndSave() {
        Task inputTask = new Task();
        inputTask.setTitle("Write report");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(taskRepository.save(inputTask)).thenReturn(inputTask);


    }

    @Test
    void getTaskById_shouldThrowWhenTaskNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> taskService.getTaskById(99L)
        );

        assertEquals("Task not found with id: 99", exception.getMessage());
    }
}