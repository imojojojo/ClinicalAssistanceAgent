package com.example.healthrag;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/health")
public class HealthCheckController {

    private final ChatClient chatClient;
    private final EmbeddingModel embeddingModel;

    public HealthCheckController(ChatClient.Builder chatClientBuilder, EmbeddingModel embeddingModel) {
        this.chatClient = chatClientBuilder.build();
        this.embeddingModel = embeddingModel;
    }

    // Step 4: Verifies Gemini chat connectivity
    @GetMapping("/ping")
    public Map<String, String> ping() {
        String response = chatClient.prompt()
                .user("What is RAG in healthcare? Answer in one sentence.")
                .call()
                .content();
        return Map.of("response", response);
    }

    // Step 5: Verifies embedding model — returns vector dimensions
    @GetMapping("/embed")
    public Map<String, Object> embed(@RequestParam(defaultValue = "fever") String text) {
        float[] vector = embeddingModel.embed(text);
        return Map.of(
                "text", text,
                "dimensions", vector.length,
                "first5Values", new float[]{vector[0], vector[1], vector[2], vector[3], vector[4]}
        );
    }
}
