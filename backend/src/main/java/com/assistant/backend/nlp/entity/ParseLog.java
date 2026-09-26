package com.assistant.backend.nlp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "parse_logs")
public class ParseLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String rawInput;

    @Column(columnDefinition = "TEXT")
    private String parsedResult;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRawInput() {
        return rawInput;
    }

    public void setRawInput(String rawInput) {
        this.rawInput = rawInput;
    }

    public String getParsedResult() {
        return parsedResult;
    }

    public void setParsedResult(String parsedResult) {
        this.parsedResult = parsedResult;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}