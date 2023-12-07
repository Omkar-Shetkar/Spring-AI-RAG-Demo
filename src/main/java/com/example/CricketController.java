package com.example;

import org.springframework.ai.client.AiClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CricketController {

    private final AiClient aiClient;

    public CricketController(AiClient aiClient) {
        this.aiClient = aiClient;
    }

    @PostMapping(value = "/news", produces = "text/plain")
    public String getNews(@RequestBody Question question) {
        return aiClient.generate(question.question());
    }

}

record Question(String question) {
}