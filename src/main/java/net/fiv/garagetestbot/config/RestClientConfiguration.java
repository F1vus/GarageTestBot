package net.fiv.garagetestbot.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfiguration {
    @Bean
    @Qualifier("openAiClient")
    public RestClient openAiRestClient(RestClient.Builder builder, @Value("${openai.token}") final String token) {
        return builder
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Authorization", "Bearer " + token)
                .build();
    }


    @Bean
    @Qualifier("trelloClient")
    public RestClient trelloRestClient(RestClient.Builder builder) {
        return builder
                .baseUrl("https://api.trello.com/1")
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
