package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class GameMovieBuddyBot extends TelegramLongPollingBot {
    private final MessageHandler messageHandler = new MessageHandler();

    @Override
    public String getBotUsername() {
        return BotConfig.BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BotConfig.BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            String text = update.getMessage().getText();

            // Получаем ответ от обработчика
            var message = messageHandler.handleMessage(chatId, text);

            try {
                execute(message);
                System.out.println("Отправлено сообщение пользователю " + chatId + ": " + text);
            } catch (TelegramApiException e) {
                System.err.println("Ошибка отправки: " + e.getMessage());
            }
        }
    }
}
