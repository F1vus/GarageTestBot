package net.fiv.garagetestbot.service;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import net.fiv.garagetestbot.dto.FeedbackJson;
import net.fiv.garagetestbot.model.Feedback;
import net.fiv.garagetestbot.model.TgUser;
import net.fiv.garagetestbot.model.enums.UserRole;
import net.fiv.garagetestbot.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class FeedbackService {
    @Value("${trello.key}")
    private String apiKey;

    @Value("${trello.token}")
    private String apiToken;

    @Value("${trello.idList}")
    private String idList;

    private final FeedbackRepository feedbackRepository;
    private final RestClient trelloClient;

    public FeedbackService(@Qualifier("trelloClient") RestClient trelloClient, FeedbackRepository feedbackRepository) {
        this.trelloClient = trelloClient;
        this.feedbackRepository = feedbackRepository;
    }

    public List<Feedback> filter(String branch, UserRole role, Integer criticality) {
        return feedbackRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<Feedback, TgUser> userJoin = root.join("user", JoinType.INNER);

            if (branch != null && !branch.isBlank()) {
                predicates.add(cb.equal(userJoin.get("branch").get("name"), branch));
            }

            if (role != null) {
                predicates.add(cb.equal(userJoin.get("role"), role));
            }

            if (criticality != null) {
                predicates.add(cb.equal(root.get("criticality"), criticality));
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        });
    }


    public void save(FeedbackJson feedback) {
        var feedbackEntity = new Feedback();

        feedbackEntity.setCriticality(feedback.getCriticality());
        feedbackEntity.setText(feedback.getText());
        feedbackEntity.setSentiment(feedback.getSentiment());
        feedbackEntity.setSolution(feedback.getSolution());
        feedbackEntity.setFeedbackDate(LocalDateTime.now());
        feedbackEntity.setUser(feedback.getUser());

        if (feedbackEntity.getCriticality() >= 4) {
            createTrelloCard(
                    "Критичний відгук від філії: " + feedbackEntity.getUser().getBranch().getName(),
                    feedbackEntity.getText()
            );
        }

        feedbackRepository.save(feedbackEntity);
    }

    private void createTrelloCard(String title, String description){
        String url = "https://api.trello.com/1/cards";

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("idList", idList)
                .queryParam("name", title)
                .queryParam("desc", description)
                .queryParam("key", apiKey)
                .queryParam("token", apiToken);

        ResponseEntity<String> response = trelloClient.post()
                .uri(builder.build().toUri())
                .retrieve()
                .toEntity(String.class);
        System.out.println(response.getBody());
    }
}
