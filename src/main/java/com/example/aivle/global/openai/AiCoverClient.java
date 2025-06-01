package com.example.aivle.global.openai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class AiCoverClient {

    private final WebClient webClient;
    private final String model;
    private final String size;

    public AiCoverClient(
            WebClient.Builder builder,
            @Value("${openai.api-key}")        String apiKey,   // ← 프로퍼티 주입
            @Value("${openai.image.model}")    String model,
            @Value("${openai.image.size}")     String size) {

        this.model = model;
        this.size  = size;

        this.webClient = builder
                .baseUrl("https://api.openai.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .build();
    }

    public String createCover(String title, String content) {
        String prompt = """
            Create a professional Korean book cover.
            Title: %s
            Short description: %s
            Style: minimal, modern, bold typography, flat illustration, no watermark, centered title
            """.formatted(title, content);

        OpenAiImageResponse res = webClient.post()
                .uri("/v1/images/generations")
                .bodyValue(new OpenAiImageRequest(model, prompt, 1, size))
                .retrieve()
                .bodyToMono(OpenAiImageResponse.class)
                .block();                 // sync

        return res.data().get(0).url();
    }


    public record OpenAiImageRequest(
            String model,
            String prompt,
            int n,
            String size) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record OpenAiImageResponse(List<ImageData> data) {
        public record ImageData(String url) {}
    }
}