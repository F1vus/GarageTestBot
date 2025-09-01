package net.fiv.garagetestbot.service;

import lombok.RequiredArgsConstructor;
import net.fiv.garagetestbot.model.TgUser;
import net.fiv.garagetestbot.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserStateService {

    private final UserRepository userRepository;

    @Transactional
    public TgUser findOrSaveTgUser(Long id) {

        var optional = userRepository.findByTelegramUserId(id);

        if (optional.isEmpty()) {
            TgUser user = new TgUser();
            user.setTelegramUserId(id);
            user.setActive(false);
            return userRepository.save(user);
        }
        return optional.get();
    }

    public void save(TgUser tgUser) {
        userRepository.save(tgUser);
    }

}
