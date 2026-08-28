package com.assistant.backend.task.repository;

import com.assistant.backend.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

}
