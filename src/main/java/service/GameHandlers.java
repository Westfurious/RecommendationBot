package service;

import model.Game;
import model.Platform;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class GameHandlers {
    private final GameBrainApiClient gameClient;
    private final Random random;
    private final String[] randomQueries = {
            "action", "adventure", "rpg", "strategy", "shooter", "sports",
            "racing", "puzzle", "horror", "simulation", "indie", "open world",
            "fantasy", "sci-fi", "mystery", "survival", "mmo", "platformer"
    };

    public GameHandlers() {
        this.gameClient = new GameBrainApiClient();
        this.random = new Random();
    }

    public String handleSearchGames(String query) {
        try {
            List<Game> games = gameClient.searchGames(query);

            if (games.isEmpty()) {
                return "Игры по запросу \"" + query + "\" не найдены";
            }

            StringBuilder response = new StringBuilder();
            response.append("Найдено игр: ").append(games.size()).append("\n\n");

            for (int i = 0; i < Math.min(games.size(), 5); i++) {
                Game game = games.get(i);
                response.append(formatGameInfo(game, i + 1)).append("\n");
            }

            return response.toString();

        } catch (IOException e) {
            return "Ошибка при поиске игр: " + e.getMessage();
        }
    }

    public String handleRandomGame() {
        try {
            // Случайный жанр из списка
            String randomQuery = randomQueries[random.nextInt(randomQueries.length)];
            List<Game> games = gameClient.searchGames(randomQuery);

            // Если по этому жанру нет игр, пробуем другой
            if (games.isEmpty()) {
                return handleRandomGame();
            }

            Game randomGame = games.get(random.nextInt(games.size()));
            StringBuilder response = new StringBuilder();

            response.append("🎲Случайная игра (").append(randomQuery).append("):\n\n");
            response.append(formatGameInfo(randomGame, -1));

            return response.toString();

        } catch (IOException e) {
            return "Ошибка при получении случайной игры: " + e.getMessage();
        }
    }

    private String formatGameInfo(Game game, int number) {
        StringBuilder info = new StringBuilder();

        if (number > 0) {
            info.append(number).append(". ");
        }
        info.append("Название: ").append(game.getName()).append("\n");

        // Год
        if (game.getYear() > 0) {
            info.append("Год: ").append(game.getYear()).append("\n");
        }

        // Жанр
        if (game.getGenre() != null && !game.getGenre().isEmpty()) {
            info.append("Жанр: ").append(game.getGenre()).append("\n");
        }

        // Рейтинг
        if (game.getRating() != null && game.getRating().getMean() > 0) {
            info.append("Рейтинг: ").append(String.format("%.1f", game.getRating().getMean())).append("\n");
        }

        // Платформы
        if (game.getPlatforms() != null && !game.getPlatforms().isEmpty()) {
            String platforms = game.getPlatforms().stream()
                    .map(Platform::getName)
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");
            info.append("Платформы: ").append(platforms).append("\n");
        }

        // Описание
        if (game.getShortDescription() != null && !game.getShortDescription().isEmpty()) {
            String desc = game.getShortDescription().length() > 100 ?
                    game.getShortDescription().substring(0, 100) + "..." : game.getShortDescription();
            info.append("Описание: ").append(desc).append("\n");
        }

        return info.toString();
    }
}