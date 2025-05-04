package org.chefcrew.external.gemini.Service;

import org.chefcrew.external.gemini.GeminiInterface;
import org.chefcrew.external.gemini.dto.request.GeminiRequest;
import org.chefcrew.external.gemini.dto.response.GeminiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeminiService {
    public static final String GEMINI_FLASH = "gemini-2.0-flash";
    public static final String DEFAULT_REQUEST = "을 대체할 수 있는 식재료를 식재료명으로만 답변해줘. 여러 식재료가 존재한다면 ','로 구분해서 제시해줘";

    private final GeminiInterface geminiInterface;

    @Autowired
    public GeminiService(GeminiInterface geminiInterface) {
        this.geminiInterface = geminiInterface;
    }

    private GeminiResponse getCompletion(GeminiRequest request) {
        return geminiInterface.getCompletion(GEMINI_FLASH, request);
    }

    public String getCompletion(String text) {
        GeminiRequest geminiRequest = new GeminiRequest(text);
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
        String request = recipeName+"에서 "+foodName+DEFAULT_REQUEST;
        return new GeminiRequest(request);
    }
}
