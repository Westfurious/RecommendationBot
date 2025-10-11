package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Класс для представления рейтинга игры.
 * Содержит среднюю оценку и количество отзывов.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Rating {
    private double mean;
    private double count;
    private double meanPlayers;
    private double meanCritics;
    private double countPlayers;
    private double countCritics;

    // Конструктор по умолчанию для Jackson
    public Rating() {}

    // Геттеры
    public double getMean() {
        return mean;}

    public double getCount() {
        return count;}

    public double getMeanPlayers() {
        return meanPlayers;}

    public double getMeanCritics() {
        return meanCritics;}

    public double getCountPlayers() {
        return countPlayers;}

    public double getCountCritics() {
        return countCritics;}

    // Сеттеры
    public void setMean(double mean) {
        this.mean = mean;}

    public void setCount(double count) {
        this.count = count;}

    public void setMeanPlayers(double meanPlayers) {
        this.meanPlayers = meanPlayers;}

    public void setMeanCritics(double meanCritics) {
        this.meanCritics = meanCritics;}

    public void setCountPlayers(double countPlayers) {
        this.countPlayers = countPlayers;}

    public void setCountCritics(double countCritics) {
        this.countCritics = countCritics;}


    /**
     * Возвращает целочисленное количество отзывов
     */
    public int getCountAsInt() {
        return (int) count;
    }
}