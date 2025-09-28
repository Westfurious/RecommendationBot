package model;

// Вложенный класс в Game так как API возвращает JSON и там есть object rating с полями mean and count
public class Rating {
    public double mean;
    public int count;

    public double getMean() { return mean; }
    public int getCount() { return count; }
}
