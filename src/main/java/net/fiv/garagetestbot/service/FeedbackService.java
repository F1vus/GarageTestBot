package net.fiv.garagetestbot.service;

import lombok.RequiredArgsConstructor;
import net.fiv.garagetestbot.dto.FeedbackJson;
import net.fiv.garagetestbot.model.Feedback;
import net.fiv.garagetestbot.repository.FeedbackRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;

    public void save(FeedbackJson feedback) {
        var feedbackEntity = new Feedback();

        feedbackEntity.setCriticality(feedback.getCriticality());
        feedbackEntity.setText(feedback.getText());
        feedbackEntity.setSentiment(feedback.getSentiment());
        feedbackEntity.setSolution(feedback.getSolution());
        feedbackEntity.setUserTelegramId(feedback.getUserTelegramId());
        feedbackEntity.setFeedbackDate(LocalDateTime.now());

        feedbackRepository.save(feedbackEntity);
    }
}
