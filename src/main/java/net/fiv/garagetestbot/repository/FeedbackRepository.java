package net.fiv.garagetestbot.repository;

import net.fiv.garagetestbot.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
