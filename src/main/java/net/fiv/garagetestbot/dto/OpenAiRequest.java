package net.fiv.garagetestbot.dto;

import java.util.List;

public record OpenAiRequest(
        String model,
        List<Message> messages,
        double temperature
) {
    public record Message(String role, String content) {}
}
