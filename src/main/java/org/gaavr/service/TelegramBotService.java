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

    @Override
    public void onUpdateReceived(Update update) {
        // Обработка полученного обновления (сообщения от пользователя, команды и т.д.)
        // Здесь вы можете реализовать логику вашего бота
        try {
            // Пример ответа на сообщение пользователя
            execute(new SendMessage(update.getMessage().getChatId().toString(), "Привет, я ваш бот!"));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        // Укажите имя вашего бота (выбранное вами при создании бота в BotFather)
        return telegramConfig.getName();
    }

    @Override
    public String getBotToken() {
        // Укажите токен вашего бота (полученный от BotFather)
        return telegramConfig.getToken();
    }
}

