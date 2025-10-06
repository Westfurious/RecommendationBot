package service;

import model.Game;
import java.util.List;

/**
 * Основной сервис для работы с играми.
 * Используется Telegram-ботом для поиска и получения информации.
 */
public class GameService {
    private final GameBrainApiClient gameBrainApiClient = new  GameBrainApiClient();
    /**
     * Ищет игры по запросу (например, "strategy", "racing")
     * @param query текст запроса
     * @return список найденных игр
     */
    public List<Game> searchGames(String query) {
        try {
            return gameBrainApiClient.searchGames(query);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при поиске игр",e);
        }
    }
    //Получает игру по ID
    public Game getGameById(int id) {
        try {
            return gameBrainApiClient.getGameById(id);
        } catch (Exception e) {
            throw new RuntimeException("this id is invalid " + id, e);
        }
    }
}
