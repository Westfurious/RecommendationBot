package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Используется Jackson для парсинга из JSON API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Rating {
    public double mean;
    public int count;
    public double meanPlayers;
    public double meanCritics;
    public double countPlayers;
    public double countCritics;

    /**
     * Вспомогательный метод для получения целого количества отзывов
     */
    public int getCountAsInt() { return count; }
}