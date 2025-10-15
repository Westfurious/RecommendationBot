package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// Вложенный класс в Game так как API возвращает JSON и там есть object rating с полями mean and count
@JsonIgnoreProperties(ignoreUnknown = true) // Без этой аннотации Jackson упадёт если увидит поле в Json которого нету в классе
public class Rating {
    public double mean;
    public double count;
    private double meanPlayers;
    private double meanCritics;
    private double countPlayers;
    private double countCritics;

    public double getMean() { return mean; }
    public double getCount() { return count; }
}
