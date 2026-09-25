package com.assistant.backend.nlp.controller;

import com.assistant.backend.nlp.dto.ParsedIntent;
import com.assistant.backend.nlp.service.IntentParsingService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/nlp")
public class NlpController {

    private final IntentParsingService intentParsingService;

    public NlpController(IntentParsingService intentParsingService) {
        this.intentParsingService = intentParsingService;
    }

    @PostMapping("/parse")
    public ParsedIntent parse(@RequestBody Map<String, String> body) {
        return intentParsingService.parse(body.get("text"));
    }
}