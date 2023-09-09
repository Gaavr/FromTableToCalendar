package org.gaavr.service;

import lombok.RequiredArgsConstructor;
import org.gaavr.config.TelegramConfig;
import org.gaavr.controller.GoogleCalendarController;
import org.gaavr.controller.GoogleSheetsController;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TelegramBotService extends TelegramLongPollingBot {

    private final GoogleCalendarController googleCalendarController;
    private final GoogleSheetsController googleSheetsController;

    private final TelegramConfig telegramConfig;
    private String lastChatId;

    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            handleNewMessage(update.getMessage());
        }
    }

    private void handleNewMessage(Message message) {
        lastChatId = message.getChatId().toString();
        String messageText = message.getText();
        SendMessage responseMessage = new SendMessage(lastChatId, "Привет, выберите необходимые действия!");

        if ("Посмотреть все в таблице".equalsIgnoreCase(messageText)) {
            String result = null;
            try {
                result = googleSheetsController.getListOfEvents().toString();
            } catch (IOException | GeneralSecurityException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            responseMessage.setText(result);
        } else if ("Посмотреть все в календаре".equalsIgnoreCase(messageText)) {
            String calendarResult = googleCalendarController.getEvents().toString();
            sendLongMessage(calendarResult, lastChatId);
        } else if ("Добавить все в календарь".equalsIgnoreCase(messageText)) {
            String addResult = googleCalendarController.createEvents();
            responseMessage.setText(addResult);
        } else if ("Удалить все из календаря".equalsIgnoreCase(messageText)) {
            String deleteResult = googleCalendarController.deleteAllEvents();
            responseMessage.setText(deleteResult);
        }

        responseMessage.setReplyMarkup(createKeyboard());
        try {
            execute(responseMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ReplyKeyboardMarkup createKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow showKeyboardRow = new KeyboardRow();
        showKeyboardRow.add("Посмотреть все в календаре");
        showKeyboardRow.add("Посмотреть все в таблице");

        KeyboardRow actionKeyboardRow = new KeyboardRow();
        actionKeyboardRow.add("Добавить все в календарь");
        actionKeyboardRow.add("Удалить все из календаря");

        keyboard.add(showKeyboardRow);
        keyboard.add(actionKeyboardRow);

        keyboardMarkup.setKeyboard(keyboard);

        return keyboardMarkup;
    }


    @Override
    public String getBotUsername() {
        return telegramConfig.getName();
    }

    @Override
    public String getBotToken() {
        return telegramConfig.getToken();
    }

    public void sendTextMessage(String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(telegramConfig.getOwnerChatId());
        sendMessage.setText(message);

        try {
            execute(sendMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendLongMessage(String longMessage, String chatId) {
        int maxMessageLength = 4096; // Максимальная длина сообщения в Telegram

        // Разбиваем длинное сообщение на части
        List<String> messageParts = new ArrayList<>();
        for (int i = 0; i < longMessage.length(); i += maxMessageLength) {
            int endIndex = Math.min(i + maxMessageLength, longMessage.length());
            messageParts.add(longMessage.substring(i, endIndex));
        }

        // Отправляем каждую часть сообщения
        for (String part : messageParts) {
            SendMessage message = new SendMessage(chatId, part);
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}
