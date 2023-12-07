package com.example;

import org.springframework.ai.client.AiClient;
import org.springframework.ai.client.AiResponse;
import org.springframework.ai.document.Document;
import org.springframework.ai.prompt.Prompt;
import org.springframework.ai.prompt.SystemPromptTemplate;
import org.springframework.ai.prompt.messages.Message;
import org.springframework.ai.prompt.messages.UserMessage;
import org.springframework.ai.retriever.VectorStoreRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CricketRagService {

    @Value("classpath:/system-news-prompt.st")
    private Resource systemNewsPrompt;

    private final AiClient aiClient;

    private final VectorStore vectorStore;

    public CricketRagService(AiClient aiClient, VectorStore vectorStore) {
        this.aiClient = aiClient;
        this.vectorStore = vectorStore;
    }

    public String generateResponse(String message) {
        List<Document> similarDocuments =
                new VectorStoreRetriever(vectorStore).retrieve(message);

        UserMessage userMessage = new UserMessage(message);
        Message systemMessage = getSystemMessage(similarDocuments);

        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));
        AiResponse response = aiClient.generate(prompt);
        return response.getGeneration().getText();
    }

    private Message getSystemMessage(List<Document> similarDocuments) {
        String documents = similarDocuments.stream()
                .map(entry -> entry.getContent())
                .collect(Collectors.joining("\n"));

        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemNewsPrompt);
        return systemPromptTemplate.createMessage(Map.of("documents", documents));
    }
}
