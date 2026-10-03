package com.assistant.backend.task.service;

import com.assistant.backend.task.entity.Task;
import com.assistant.backend.task.repository.TaskRepository;
import com.assistant.backend.user.entity.User;
import com.assistant.backend.user.repository.UserRepository;
import com.assistant.backend.reminder.repository.ReminderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceOwnershipTest {

    @Mock private TaskRepository taskRepository;
    @Mock private UserRepository userRepository;
    @Mock private ReminderRepository reminderRepository;

    @InjectMocks private TaskService taskService;

    private User owner;
    private User otherUser;
    private Task task;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setId(1L);

        otherUser = new User();
        otherUser.setId(2L);

        task = new Task();
        task.setId(10L);
        task.setTitle("My task");
        task.setUser(owner);
    }

    @Test
    void getTaskById_shouldReturnTask_whenCurrentUserIsOwner() {
        when(taskRepository.findById(10L)).thenReturn(Optional.of(task));

        try (MockedStatic<com.assistant.backend.auth.util.SecurityUtil> mockedSecurity =
                     Mockito.mockStatic(com.assistant.backend.auth.util.SecurityUtil.class)) {
            mockedSecurity.when(com.assistant.backend.auth.util.SecurityUtil::getCurrentUserId).thenReturn(1L);

            Task result = taskService.getTaskById(10L);

            assertEquals("My task", result.getTitle());
        }
    }

    @Test
    void getTaskById_shouldThrow_whenCurrentUserIsNotOwner() {
        when(taskRepository.findById(10L)).thenReturn(Optional.of(task));

        try (MockedStatic<com.assistant.backend.auth.util.SecurityUtil> mockedSecurity =
                     Mockito.mockStatic(com.assistant.backend.auth.util.SecurityUtil.class)) {
            mockedSecurity.when(com.assistant.backend.auth.util.SecurityUtil::getCurrentUserId).thenReturn(2L);

            assertThrows(RuntimeException.class, () -> taskService.getTaskById(10L));
        }
    }
}
