package com.example;

import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.vectorstore.InMemoryVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class AiConfig {

    @Value("classpath:/cricket-worldcup-2023.txt")
    private Resource news;

    @Bean
    VectorStore vectorStore(EmbeddingClient embeddingClient) {
        return new InMemoryVectorStore(embeddingClient);
    }

    @Bean
    ApplicationRunner loadNews(VectorStore vectorStore) {
        return args -> {
            TextReader textReader = new TextReader(news);
            vectorStore.add(textReader.get());
        };
    }

}