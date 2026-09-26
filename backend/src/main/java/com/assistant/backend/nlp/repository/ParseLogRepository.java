package com.assistant.backend.nlp.repository;

import com.assistant.backend.nlp.entity.ParseLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParseLogRepository extends JpaRepository<ParseLog, Long> {
}
