package com.assistant.backend.nlp.service;

import com.assistant.backend.nlp.dto.ParsedIntent;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class IntentParsingService {

    private final WebClient webClient;
    private final String apiKey;
    private final String model;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public IntentParsingService(@Value("${groq.api-key}") String apiKey,
                                @Value("${groq.model}") String model) {
        this.apiKey = apiKey;
        this.model = model;
        this.webClient = WebClient.builder()
                .baseUrl("https://api.groq.com")
                .build();
    }

    public ParsedIntent parse(String userInput) {
        String currentDateTime = LocalDateTime.now().toString();

        String systemPrompt = """
                You are a task/reminder parser. Extract structured data from the user's text.
                Current date and time is: %s

                Return ONLY valid JSON, no markdown, no explanation, in exactly this shape:
                {"title": "...", "description": null, "dueAt": "ISO-8601 datetime or null", "remindAt": "ISO-8601 datetime or null"}

                If no due date or reminder time is mentioned, use null for that field.
                """.formatted(currentDateTime);

        Map<String, Object> requestBody = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userInput)
                ),
                "temperature", 0.2
        );

        String response = webClient.post()
                .uri("/openai/v1/chat/completions")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(
                        httpStatus -> !httpStatus.is2xxSuccessful(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .map(errorBody -> new RuntimeException(
                                        "Groq API error " + clientResponse.statusCode() + ": " + errorBody))
                )
                .bodyToMono(String.class)
                .block();

        return extractParsedIntent(response);
    }

    private ParsedIntent extractParsedIntent(String rawResponse) {
        try {
            JsonNode root = objectMapper.readTree(rawResponse);
            String text = root.path("choices").get(0)
                    .path("message").path("content").asText();

            String cleanJson = text.replaceAll("```json", "").replaceAll("```", "").trim();
            return objectMapper.readValue(cleanJson, ParsedIntent.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse NLP response: " + e.getMessage() + " | raw: " + rawResponse);
        }
    }
}
