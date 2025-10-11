package com.game.movie.buddy;

import service.GameService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class MessageHandler {
    private final GameService gameService;

    public MessageHandler() {
        this.gameService = new GameService();
    }

    public SendMessage handleMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));

        if (text.startsWith("/")) {
            switch (text) {
                case "/start":
                    message.setText(getStartMessage());
                    break;
                case "/help":
                    message.setText(getHelpMessage());
                    break;
                case "/games":
                    message.setText("Введите /search <название> для поиска игр");
                    break;
                case "/random":
                    String randomGame = gameService.handleRandomGame();
                    message.setText(randomGame);
                    break;
                case "/search":
                    message.setText("Используйте: /search <название игры>");
                    break;
                default:
                    if (text.startsWith("/search ")) {
                        String query = text.substring(8).trim();
                        if (!query.isEmpty()) {
                            String searchResult = gameService.handleSearchGames(query);
                            message.setText(searchResult);
                        } else {
                            message.setText("Введите название игры после /search");
                        }
                    } else {
                        message.setText("Неизвестная команда. Используйте /help");
                    }
            }
        } else {
            message.setText("🤖 Используйте команды для взаимодействия:\n/help - список команд");
        }

        return message;
    }

    private String getStartMessage() {
        return String.format("Добро пожаловать в Game & Movie Buddy!%n%n" +
                "Используйте команды:%n" +
                "/search - поиск игр%n" +
                "/random - случайная игра%n" +
                "/help - справка");
    }

    private String getHelpMessage() {
        return String.format("Доступные команды:%n" +
                "/search <название> - поиск игр%n" +
                "/random - случайная игра%n" +
                "/help - эта справка");
    }
}