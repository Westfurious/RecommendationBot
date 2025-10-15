package service;

import model.Game;
import model.Rating;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecommendationEngineTest {

    // Вспомогательный метод: создаём игру через СЕТТЕРЫ (как у тебя)
    private Game createGame(String name, double meanRating, double ratingCount) {
        Game game = new Game();
        game.setName(name);
        game.setShortDescription("Описание для " + name);

        Rating rating = new Rating();
        rating.mean = meanRating;   // поле public — можно напрямую
        rating.count = ratingCount; // так делает Jackson
        game.setRating(rating);

        return game;
    }

    @Test
    @DisplayName("Точное совпадение: запрос 'Dota 2' → FOUND")
    void shouldReturnFoundForExactMatch() throws IOException {
        // Given: мок-клиент возвращает одну игру
        GameSearchClient mockClient = query -> List.of(
                createGame("Dota 2", 8.5, 5000)
        );
        RecommendationEngine engine = new RecommendationEngine(mockClient);

        // When
        var result = engine.findBestGame("Dota 2");

        // Then
        assertTrue(result.isFound(), "Должен быть найден");
        assertNotNull(result.getGame(), "Игра не должна быть null");
        assertEquals("Dota 2", result.getGame().getName(), "Название должно совпадать");
    }

    @Test
    @DisplayName("Неоднозначный запрос 'GTA' → MULTIPLE_MATCHES")
    void shouldReturnMultipleMatchesForAmbiguousQuery() throws IOException {
        // Given: несколько игр, начинающихся с "GTA"
        GameSearchClient mockClient = query -> List.of(
                createGame("GTA V", 9.0, 10000),
                createGame("GTA: San Andreas", 8.8, 8000),
                createGame("GTA III", 8.5, 6000)
        );
        RecommendationEngine engine = new RecommendationEngine(mockClient);

        // When
        var result = engine.findBestGame("GTA");

        // Then
        assertTrue(result.isMultiMatch(), "Должно быть несколько совпадений");
        assertEquals(3, result.getAlternatives().size(), "Должно быть 3 альтернативы");
        assertTrue(result.getAlternatives().stream()
                .allMatch(g -> g.getName().startsWith("GTA")), "Все игры должны начинаться с 'GTA'");
    }

    @Test
    @DisplayName("Короткий запрос 'aa' → не считается неоднозначным → возвращает одну игру")
    void shouldNotTreatShortQueryAsAmbiguous() throws IOException {
        GameSearchClient mockClient = query -> List.of(
                createGame("AaA: A Way Out", 8.0, 2000),
                createGame("AaBbCc", 7.5, 1500)
        );
        RecommendationEngine engine = new RecommendationEngine(mockClient);

        var result = engine.findBestGame("aa");

        assertTrue(result.isFound(), "Должна быть найдена одна игра");
        assertFalse(result.isMultiMatch(), "Не должно быть MULTIPLE_MATCHES для короткого запроса");
    }

    @Test
    @DisplayName("Пустой результат от API → NOT_FOUND")
    void shouldReturnNotFoundWhenApiReturnsEmpty() throws IOException {
        GameSearchClient mockClient = query -> List.of();
        RecommendationEngine engine = new RecommendationEngine(mockClient);

        var result = engine.findBestGame("несуществующая игра");

        assertFalse(result.isFound(), "Не должно быть найдено");
        assertFalse(result.isMultiMatch(), "Не должно быть нескольких");
        assertEquals(RecommendationEngine.SearchResults.SearchStatus.NOT_FOUND, result.getStatus());
    }

    @Test
    @DisplayName("Все игры отфильтрованы (рейтинг < 100) → NOT_FOUND")
    void shouldReturnNotFoundWhenAllGamesHaveLowRating() throws IOException {
        GameSearchClient mockClient = query -> List.of(
                createGame("Низкий рейтинг", 5.0, 50) // count = 50 < 100 → будет отфильтровано
        );
        RecommendationEngine engine = new RecommendationEngine(mockClient);

        var result = engine.findBestGame("что-то");

        assertFalse(result.isFound(), "Игры с низким рейтингом должны быть отброшены");
        assertEquals(RecommendationEngine.SearchResults.SearchStatus.NOT_FOUND, result.getStatus());
    }

    @Test
    @DisplayName("'Почти точное' совпадение: запрос 'Dota' → находит 'Dota 2'")
    void shouldFindApproximateMatch() throws IOException {
        GameSearchClient mockClient = query -> List.of(
                createGame("Dota 2", 8.5, 10000)
        );
        RecommendationEngine engine = new RecommendationEngine(mockClient);

        var result = engine.findBestGame("Dota");

        assertTrue(result.isFound(), "Должна быть найдена игра по частичному совпадению");
        assertEquals("Dota 2", result.getGame().getName(), "Должна быть найдена Dota 2");
    }
}