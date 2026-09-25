package com.assistant.backend.nlp.dto;

public record ParsedIntent(
        String title,
        String description,
        String dueAt,
        String remindAt
) { }
