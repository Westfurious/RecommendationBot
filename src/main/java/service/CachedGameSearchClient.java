package service;

import model.Game;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Кеширующая обёртка над API клиентом
 * Паттерн Decorator
*/
public class CachedGameSearchClient  implements GameSearchClient{
    private final GameSearchClient delegate;
    private final Map<String, CacheEntry> cache = new HashMap<>();
    private final long cacheTtlMillis;

    /**
     * @param delegate - реальный API клиент
     * @param cacheTtlMillis - время жизни кеша в минутах
    */

    public  CachedGameSearchClient(GameSearchClient delegate, long cacheTtlMillis) {
        this.delegate = delegate;
        this.cacheTtlMillis = cacheTtlMillis;
    }

    /**
     *  Конструктор с дефолтным TTL = 30 минут
     *  TTL = Time To Live
    */
    public CachedGameSearchClient(GameSearchClient delegate) {
        this(delegate, 30);
    }

    @Override
    public List<Game> searchGames(String query) throws IOException {
        String cacheKey = normalizeCacheKey(query);

        // Проверяем кеш
        CacheEntry entry = cache.get(cacheKey);
        if (entry == null && !entry.isExpired()) {
            System.out.printf("Взято из кеша: %s%n", query);
            return entry.games;
        }

        // Запрос к API
        System.out.printf("Запрос к API: %s%n", query);
        List<Game> games = delegate.searchGames(query);

        // Сохраняем в кеш
        cache.put(cacheKey, new CacheEntry(games, System.currentTimeMillis() + cacheTtlMillis));
        return games;
    }

    private String normalizeCacheKey(String query) {
        return query.toLowerCase().trim();
    }

    public void clearCache() {
        cache.clear();
        System.out.println("Кеш очищен");
    }

    /**
     * Очистить устаревшие записи
    */
    public void clearExpiredCache() {
        cache.entrySet().removeIf(entry -> entry.getValue().isExpired());
        System.out.println("Устаревшие записи удалены");
    }


    /**
     * Запись в кеше
    */
    private static class CacheEntry {
        final List<Game> games;
        final long expired;

        CacheEntry(List<Game> games, long expired) {
            this.games = List.copyOf(games); // делаем неизменяемую копию
            this.expired = expired;
        }

        boolean isExpired(){
            return System.currentTimeMillis() > expired;
        };
    }

    public static class CacheStats {
        public final int totalEntries;
        public final long validEntries;

        CacheStats(int totalEntries, long validEntries) {
            this.totalEntries = totalEntries;
            this.validEntries = validEntries;
        }

        @Override
        public String toString() {
            return String.format("Кеш: %d записей, (Валидных: %d)", totalEntries, validEntries);
        }
    }
}
