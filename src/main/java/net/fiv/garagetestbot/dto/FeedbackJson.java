package net.fiv.garagetestbot.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class FeedbackJson {
    private Long userTelegramId;
    private String text;
    private String sentiment;
    private String criticality;
    private String solution;
    public static FeedbackJson fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, FeedbackJson.class);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing analysis JSON: " + json, e);
        }
    }
}
