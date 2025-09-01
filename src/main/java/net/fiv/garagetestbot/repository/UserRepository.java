package net.fiv.garagetestbot.repository;

import net.fiv.garagetestbot.model.TgUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<TgUser, Long> {
    Optional<TgUser> findByTelegramUserId(Long userId);
}
