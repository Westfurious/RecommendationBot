package service;

import model.Game;

import java.io.IOException;
import java.util.List;

/**
 * Абстракция для поиска игр по запросу
 * Позволяет подменить реализацию (мок, кеш, другой API)
   */
public interface GameSearchClient {
    List<Game> searchGames(String query) throws IOException;
}
