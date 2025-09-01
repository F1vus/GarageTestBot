package net.fiv.garagetestbot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfiguration {
    @Bean
    public RestClient openAiRestClient(RestClient.Builder builder, @Value("${openai.token}") final String token) {
        return builder
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Authorization", "Bearer " + token)
                .build();
    }
}
