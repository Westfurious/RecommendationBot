package com.game.movie.buddy;

import service.GameHandlers;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class MessageHandler {
    private final GameHandlers gameHandlers;

    public MessageHandler() {
        this.gameHandlers = new GameHandlers();
    }

    public SendMessage handleMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));

        switch (text) {
            case "/start":
                message.setText("Добро пожаловать в Game & Movie Buddy!\n\n" +
                        "Доступные команды:\n" +
                        "/games - поиск игр\n" +
                        "/random - случайная игра\n" +
                        "/help - справка");
                break;

            case "/help":
                message.setText("Доступные команды:\n" +
                        "/games - поиск игр\n" +
                        "/random - случайная игра\n" +
                        "/help - эта справка\n\n");
                break;

            case "/games":
                message.setText("Введите название игры для поиска:\n" +
                        "Например: The Witcher, Minecraft, GTA");
                break;

            case "/random":
                String randomGame = gameHandlers.handleRandomGame();
                message.setText(randomGame);
                break;

            default:
                if (text.startsWith("/")) {
                    message.setText("Не понимаю команду. Используй /help для справки");
                } else {
                    String searchResult = gameHandlers.handleSearchGames(text);
                    message.setText(searchResult);
                }
        }

        return message;
    }
}