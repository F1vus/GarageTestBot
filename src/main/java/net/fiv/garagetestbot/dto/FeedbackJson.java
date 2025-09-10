package net.fiv.garagetestbot.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.ToString;
import net.fiv.garagetestbot.model.TgUser;

@Data
@ToString
public class FeedbackJson {
    private String text;
    private String sentiment;
    private Integer criticality;
    private String solution;
    private TgUser user;
    public static FeedbackJson fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, FeedbackJson.class);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing analysis JSON: " + json, e);
        }
    }
}
