package net.fiv.garagetestbot.service;

import net.fiv.garagetestbot.dto.FeedbackJson;
import net.fiv.garagetestbot.dto.OpenAiRequest;
import net.fiv.garagetestbot.dto.OpenAiResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class FeedbackAnalyzerService {

    private final RestClient openAiRestClient;

    public FeedbackAnalyzerService(@Qualifier("openAiClient") RestClient openAiRestClient) {
        this.openAiRestClient = openAiRestClient;
    }

    public FeedbackJson analyzeFeedback(String feedback) {
        OpenAiRequest request = new OpenAiRequest(
                "gpt-4o-mini",
                List.of(
                        new OpenAiRequest.Message("system",
                                "Ти — асистент для аналізу відгуків. " +
                                        "Твоє завдання: 1) визначити тональність (негативний, нейтральний, позитивний) " +
                                        "2) оцінити критичність за шкалою від 1 (неважливо) до 5 (дуже серйозно) " +
                                        "3) як можна вирішити дане питання." +
                                        "Відповідай тільки у JSON форматі: {\"sentiment\": \"...\", \"criticality\": N, \"solution\": \"...\"}"),
                        new OpenAiRequest.Message("user", feedback)), 0.2);

        OpenAiResponse response = openAiRestClient.post()
                .uri("/chat/completions")
                .body(request)
                .retrieve()
                .body(OpenAiResponse.class);

        if (response != null && !response.choices().isEmpty()) {
            String content =response.choices().getFirst().message().content();

            return FeedbackJson.fromJson(content);
        }
        throw new RuntimeException("OpenAI response is null");
    }
}
