package com.ssharma.docmind.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OllamaInitializer {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(OllamaInitializer.class);

    private final RestClient restClient;
    private final OllamaProperties properties;

    public OllamaInitializer(OllamaProperties properties) {

        this.properties = properties;

        this.restClient = RestClient.builder()
                .baseUrl(properties.getBaseUrl())
                .build();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void verifyModels() {

        LOGGER.info("Verifying Ollama installation...");

        Map<String, Object> response;

        try {

            response = restClient.get()
                    .uri("/api/tags")
                    .retrieve()
                    .body(Map.class);

        } catch (Exception ex) {

            throw new IllegalStateException("""
                    Ollama is not running.
                    
                    Please start Ollama first.
                    
                    Expected URL:
                    %s
                    """.formatted(properties.getBaseUrl()), ex);

        }

        Set<String> installedModels = getInstalledModels(response);

        LOGGER.info("Installed Ollama Models:");

        installedModels.forEach(model ->
                LOGGER.info("  - {}", model));

        String chatModel = normalize(properties.getChatModel());
        String embeddingModel = normalize(properties.getEmbeddingModel());

        if (!installedModels.contains(chatModel)) {

            throw new IllegalStateException("""
                    Chat model not found.
                    
                    Required:
                    %s
                    
                    Install using:
                    
                    ollama pull %s
                    """.formatted(chatModel, chatModel));

        }

        if (!installedModels.contains(embeddingModel)) {

            throw new IllegalStateException("""
                    Embedding model not found.
                    
                    Required:
                    %s
                    
                    Install using:
                    
                    ollama pull %s
                    """.formatted(embeddingModel, embeddingModel));

        }

        LOGGER.info("Chat model verified.");
        LOGGER.info("Embedding model verified.");
        LOGGER.info("Ollama verification completed successfully.");
    }

    @SuppressWarnings("unchecked")
    private Set<String> getInstalledModels(Map<String, Object> response) {

        Object modelsObject = response.get("models");

        if (!(modelsObject instanceof List<?>)) {
            throw new IllegalStateException("Unable to read installed Ollama models.");
        }

        List<Map<String, Object>> models =
                (List<Map<String, Object>>) modelsObject;

        return models.stream()
                .map(model -> (String) model.get("name"))
                .map(this::normalize)
                .collect(Collectors.toSet());

    }

    private String normalize(String modelName) {

        if (modelName == null) {
            return "";
        }

        return modelName.replace(":latest", "").trim();

    }

}
