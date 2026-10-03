package com.assistant.backend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class FirebaseConfig {

    private static final String CREDENTIALS_PATH = "src/main/resources/firebase-service-account.json";

    @PostConstruct
    public void init() {
        Path path = Path.of(CREDENTIALS_PATH);

        if (!Files.exists(path)) {
            System.out.println("Firebase credentials not found at " + CREDENTIALS_PATH + " — push notifications will be disabled.");
            return;
        }

        try (FileInputStream serviceAccount = new FileInputStream(path.toFile())) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        } catch (IOException e) {
            System.err.println("Failed to initialize Firebase: " + e.getMessage() + " — push notifications will be disabled.");
        }
    }
}