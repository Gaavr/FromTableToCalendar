package org.gaavr.controller;

import org.gaavr.service.TelegramBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update; // Используйте этот импорт
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@RestController
@RequestMapping("/telegram")
public class TelegramBotController {

    private final TelegramBotService telegramBotService;

    @Autowired
    public TelegramBotController(TelegramBotService telegramBotService) {
        this.telegramBotService = telegramBotService;
    }

    @PostMapping("/webhook")
    public void onUpdateReceived(@RequestBody Update update) {
        telegramBotService.onUpdateReceived(update);
    }

    @PostMapping("/send-message")
    public void sendMessage(@RequestBody SendMessage message) {
        try {
            telegramBotService.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/send-text-message")
    public void sendTextMessage(@RequestBody String message) {
        telegramBotService.sendTextMessage(message);
    }

}
