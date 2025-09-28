package org.example;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class MessageHandler {

    public SendMessage handleMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));

        switch (text) {
            case "/start":
                message.setText("Добро пожаловать в Game & Movie Buddy!\n\n" +
                        "Используй /help для списка команд!");
                break;

            case "/help":
                message.setText("Доступные команды:\n" +
                        "/games - рекомендации игр\n" +
                        "/movies - рекомендации фильмов\n" +
                        "/help - эта справка");
                break;

            case "/games":
                message.setText("Раздел игр в разработке...");
                break;

            case "/movies":
                message.setText("Раздел фильмов в разработке...");
                break;

            default:
                message.setText("Не понимаю команду. Используй /help для справки");
        }

        return message;
    }
}