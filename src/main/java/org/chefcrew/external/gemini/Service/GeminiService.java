package org.chefcrew.external.gemini.Service;

import org.chefcrew.external.gemini.dto.request.GeminiRequest;
import org.chefcrew.external.gemini.dto.response.GeminiResponse;
import org.chefcrew.recipe.dto.request.GetReplicableFoodRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class GeminiService {
    public static final String GEMINI_FLASH = "gemini-2.0-flash";
    public static final String DEFAULT_REQUEST = "을 대체할 수 있는 식재료를 식재료명으로만 답변해줘. 여러 식재료가 존재한다면 ','로 구분해서 제시해줘";

    private final WebClient webClient;

    public GeminiService(@Value("${gemini.api.base-url}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    private GeminiResponse getCompletion(GeminiRequest request) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1beta/models/{model}:generateContent")
                        .build(GEMINI_FLASH))
                .bodyValue(request)
                .retrieve()
                .bodyToMono(GeminiResponse.class)
                .block();
    }

    public String getAlternativeFood(GetReplicableFoodRequest getReplicableFoodRequest) {
        GeminiRequest geminiRequest = getGeminiRequest(getReplicableFoodRequest.menuName(), getReplicableFoodRequest.replaceFoodName());
        GeminiResponse response = getCompletion(geminiRequest);

        return response.getCandidates()
                .stream()
                .findFirst().flatMap(candidate -> candidate.getContent().getParts()
                        .stream()
                        .findFirst()
                        .map(GeminiResponse.TextPart::getText))
                .orElse(null);
    }

    public GeminiRequest getGeminiRequest(String recipeName, String foodName) {
        String request = recipeName + "에서 " + foodName + DEFAULT_REQUEST;
        return new GeminiRequest(request);
    }
}
