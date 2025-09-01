package net.fiv.garagetestbot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.fiv.garagetestbot.model.enums.UserRole;
import net.fiv.garagetestbot.service.FeedbackAnalyzerService;
import net.fiv.garagetestbot.service.FeedbackService;
import net.fiv.garagetestbot.service.GoogleDocsService;
import net.fiv.garagetestbot.service.UserStateService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Component
@Slf4j
@RequiredArgsConstructor
public class UpdateConsumer implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;
    private final UserStateService userStateService;
    private final FeedbackAnalyzerService feedbackAnalyzerService;
    private final GoogleDocsService googleDocsService;
    private final FeedbackService feedbackService;

    @Override
    public void consume(Update update) {
        Long chatId;
        Long userTelegramId;

        if (update.hasMessage()) {
            userTelegramId = update.getMessage().getFrom().getId();
            chatId = update.getMessage().getChatId();
        } else if (update.hasCallbackQuery()) {
            userTelegramId = update.getCallbackQuery().getFrom().getId();
            chatId = update.getCallbackQuery().getMessage().getChatId();
        } else {
            return;
        }

        var state = userStateService.findOrSaveTgUser(userTelegramId);
        state.setChatId(chatId);

        if (update.hasMessage() && update.getMessage().hasText()) {
            var text = update.getMessage().getText();

            if (!state.getActive()) {
                if (state.getRole() == null) {
                    askRole(chatId);
                } else if (state.getBranch() == null) {
                    askBranch(chatId);
                    state.setBranch(text);
                    state.setActive(true);
                    log.info("New registration. userTelegramId: {}, role: {}, branch: {}", chatId, state.getRole(), state.getBranch());
                    sendMessage(chatId, "Дякую! Ви зареєстровані як " + state.getRole().getDisplayName() +
                            " у філії " + state.getBranch() + ". Тепер ви можете надсилати відгуки");
                }
            } else {
                if (!text.isBlank()) {
                    log.info("Feedback from chatId: {}, message: {}", chatId, text);
                    sendMessage(chatId, "Відгук отримано.");
                    try {
                        var analysis = feedbackAnalyzerService.analyzeFeedback(text);
                        analysis.setUserTelegramId(userTelegramId);
                        analysis.setText(text);

                        googleDocsService.appendFeedback(text, analysis);
                        feedbackService.save(analysis);

                        log.info("Feedback from {}", analysis);
                        sendMessage(chatId, "Відгук обролено!");
                    } catch (IOException e) {
                        sendMessage(chatId, "При обробці відгуку сталася помилка!");
                        log.error(e.getMessage());
                        throw new RuntimeException(e);
                    }
                } else {
                    sendMessage(chatId, "Повідомлення не відповідає формату");
                }

            }
        } else if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();
            if (data.startsWith("ROLE_")) {
                String roleName = data.replace("ROLE_", "");
                UserRole role = UserRole.fromString(roleName);
                state.setRole(role);
                sendMessage(chatId, "Ви обрали роль: " + role.getDisplayName());
                askBranch(chatId);
            }
        }

        userStateService.save(state);
    }

    private void askRole( Long chatId) {
        List<InlineKeyboardRow> keyboard = new ArrayList<>();

        for (UserRole role : UserRole.values()) {
            InlineKeyboardButton button = InlineKeyboardButton.builder()
                    .text(role.getDisplayName())
                    .callbackData("ROLE_" + role.name())
                    .build();

            keyboard.add(new InlineKeyboardRow(button));
        }

        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder()
                .keyboard(keyboard)
                .build();

        sendMessageWithKeyboard(chatId, markup);
    }


    private void askBranch(Long chatId) {
        sendMessage(chatId, "Введіть назву вашої філії:");
    }

    private void sendMessage(Long chatId, String text) {
        try {
            telegramClient.execute(SendMessage.builder()
                    .chatId(chatId.toString())
                    .text(text)
                    .build());
        } catch (Exception e) {
            log.error("Error sending message: {}", e.getMessage());
        }
    }

    private void sendMessageWithKeyboard(Long chatId, InlineKeyboardMarkup markup) {
        try {
            telegramClient.execute(SendMessage.builder()
                    .chatId(chatId.toString())
                    .text("Оберіть вашу посаду:")
                    .replyMarkup(markup)
                    .build());
        } catch (Exception e) {
            log.error("Error sending keyboard: {}", e.getMessage());
        }
    }
}
