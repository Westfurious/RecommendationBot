package service;

import model.Game;
import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * Основной сервис для работы с играми.
 * Используется Telegram-ботом для поиска и получения информации.
 */
public class GameService {
    private final GameBrainApiClient gameBrainApiClient;
    private final Random random;
    private final String[] randomQueries = {
            "action", "adventure", "rpg", "strategy", "shooter", "sports",
            "racing", "puzzle", "horror", "simulation", "indie", "open world",
            "fantasy", "sci-fi", "mystery", "survival", "mmo", "platformer"
    };

    public GameService() {
        this.gameBrainApiClient = new GameBrainApiClient();
        this.random = new Random();
    }

    public List<Game> searchGames(String query) {
        try {
            return gameBrainApiClient.searchGames(query);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при поиске игр", e);
        }
    }

    /**
     * Получает игру по ID
     */
    public Game getGameById(int id) {
        try {
            return gameBrainApiClient.getGameById(id);
        } catch (Exception e) {
            throw new RuntimeException("Неверный ID: " + id, e);
        }
    }

    /**
     * Обрабатывает поиск игр и возвращает форматированную строку для бота
     */
    public String handleSearchGames(String query) {
        try {
            List<Game> games = gameBrainApiClient.searchGames(query);

            if (games.isEmpty()) {
                return String.format("Игры по запросу \"%s\" не найдены", query);
            }

            Game game = games.get(0); // Берём первую игру

            StringBuilder response = new StringBuilder();
            response.append(String.format("Результат поиска: \"%s\"%n%n", query));
            response.append(game.toString());

            return response.toString();

        } catch (IOException e) {
            return String.format("Ошибка при поиске игр: %s", e.getMessage());
        }
    }

    /**
     * Обрабатывает запрос случайной игры и возвращает форматированную строку
     */
    public String handleRandomGame() {
        try {
            String randomQuery = randomQueries[random.nextInt(randomQueries.length)];
            List<Game> games = gameBrainApiClient.searchGames(randomQuery);

            if (games.isEmpty()) {
                return handleRandomGame(); // рекурсивно пробуем другой жанр
            }

            Game randomGame = games.get(random.nextInt(games.size()));
            return String.format("🎲 Случайная игра (жанр: %s):%n%n%s",
                    randomQuery, randomGame.toString());

        } catch (IOException e) {
            return String.format("Ошибка при получении случайной игры: %s", e.getMessage());
        }
    }
}