package com.example.aivle.global.openai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class AiCoverClient {

    private final WebClient webClient;
    private final String model;
    private final String size;

    public AiCoverClient(
            WebClient.Builder builder,
            @Value("${openai.api-key}")        String apiKey,
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

    // 프롬프트는 수정 필요함
    public String createCover(String title, String content) {
        // 추후 수정
        String mainScene = content.split("[\\.!?\\n]")[0].trim();

        if (mainScene.length() > 120)          // 너무 길면 자르기
            mainScene = mainScene.substring(0, 117) + "...";

        String color    = "warm amber and deep indigo";
        String mood     = "mysterious, hopeful";
        String artStyle = "detailed digital painting";

        String prompt = """
        Book cover, high-resolution illustration.
        Title: “%s”
        Main scene: %s
        Background: twilight city skyline blending with library interior
        Color scheme: %s
        Mood: %s
        Art style: %s
        Layout: bold title at top center, clean sans-serif; author name small at bottom
        No watermark, no text except title and author.
        """.formatted(title, mainScene, color, mood, artStyle);

        OpenAiImageResponse res = webClient.post()
                .uri("/v1/images/generations")
                .bodyValue(new OpenAiImageRequest(model, prompt, 1, size))
                .retrieve()
                .bodyToMono(OpenAiImageResponse.class)
                .block();

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
