package com.ssharma.docmind.service.llm;

import com.ssharma.docmind.exception.ChatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class OllamaLlmService implements LlmService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(OllamaLlmService.class);

    private final ChatClient chatClient;

    public OllamaLlmService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public String generate(String prompt) {

        try {

            LOGGER.debug("Sending prompt to Ollama.");

            String response = chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();

            LOGGER.debug("Received response from Ollama.");

            return response;

        } catch (Exception ex) {

            throw new ChatException(
                    "Failed to communicate with the language model.",
                    ex
            );

        }

    }

}