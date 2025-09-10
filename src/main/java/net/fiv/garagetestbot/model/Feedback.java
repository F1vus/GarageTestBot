package net.fiv.garagetestbot.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="feedbacks")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="text")
    private String text;

    @Column(name = "sentiment")
    private String sentiment;

    @Column(name = "criticality")
    private Integer criticality;

    @Column(name = "solution")
    private String solution;

    @Column(name="feedback_date")
    private LocalDateTime feedbackDate;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private TgUser user;
}
