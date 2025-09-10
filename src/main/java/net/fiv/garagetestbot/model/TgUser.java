package net.fiv.garagetestbot.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.fiv.garagetestbot.model.enums.UserRole;
import net.fiv.garagetestbot.model.enums.converter.RoleConverter;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class TgUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "telegram_user_id", nullable = false)
    private Long telegramUserId;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "role")
    @Convert(converter = RoleConverter.class)
    private UserRole role;

    @ManyToOne
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    private Branch branch;

    @Column(name="is_active")
    private Boolean active;

    @OneToMany(mappedBy = "user")
    private List<Feedback> feedbackList;
}
