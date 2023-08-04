package org.gaavr.service;

import lombok.RequiredArgsConstructor;
import org.gaavr.config.TelegramConfig;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@RequiredArgsConstructor
public class TelegramBotService extends TelegramLongPollingBot {

    private final TelegramConfig telegramConfig;
    private String lastChatId;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            lastChatId = update.getMessage().getChatId().toString();
            try {
                execute(new SendMessage(lastChatId, "Привет, я ваш бот! Скоро все заработает!"));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() { return telegramConfig.getName(); }

    @Override
    public String getBotToken() { return telegramConfig.getToken(); }

    public void sendTextMessage(String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(telegramConfig.getOwnerChatId());
        sendMessage.setText(message);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
