package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * Модель игры из GameBrain API.
 * Содержит основную информацию: название, жанр, рейтинг, платформы, описание.
 */
@JsonIgnoreProperties(ignoreUnknown = true) // Без этой аннотации Jackson упадёт если увидит поле в Json которого нету в классе
public class Game {
    private String xUrl;
    private int id;
    private int year;
    private String name;
    private String genre;
    private String image;
    private String link;
    private Rating rating;
    private boolean adultOnly;
    private String shortDescription;
    private List<String> screenshots;
    private String microTrailer;
    private String gameplay;
    private List<Platform> platforms;

    // Пустой конструктор для Jackson
    public Game() {}

    // Геттеры и сеттеры
    public String getXUrl() { return xUrl; }
    public void setXUrl(String xUrl) { this.xUrl = xUrl; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    public Rating getRating() { return rating; }
    public void setRating(Rating rating) { this.rating = rating; }

    public boolean isAdult_only() { return adultOnly; }
    public void setAdult_only(boolean adultOnly) { this.adultOnly = adultOnly; }

    public String getShortDescription() { return shortDescription; }
    public void setShortDescription(String shortDescription) { this.shortDescription = shortDescription; }

    public List<String> getScreenshots() { return screenshots; }
    public void setScreenshots(List<String> screenshots) { this.screenshots = screenshots; }

    public String getMicroTrailer() { return microTrailer; }
    public void setMicroTrailer(String microTrailer) { this.microTrailer = microTrailer; }

    public String getGameplay() { return gameplay; }
    public void setGameplay(String gameplay) { this.gameplay = gameplay; }

    public List<Platform> getPlatforms() { return platforms; }
    public void setPlatforms(List<Platform> platforms) { this.platforms = platforms; }

    @Override
    public String toString() {
        return String.format(
                "   %s (%d)\n" +
                "   Жанр: %s\n" +
                "   Рейтинг: %.2f (%d отзывов)\n" +
                "   Платформы: %s\n" +
                "   Описание: %s\n",
                name, year, genre,
                rating != null ? rating.mean : 0.0, //Выражения ? : Чтобы не упало с NullPointerException
                (int) (rating != null ? rating.count : 0),
                platforms != null ? platforms.stream() //.stream() Превращает platform в поток данных
                        .map(p -> p.name) // лямбда выражение берёт каждый эл-т p из потока и извлекает p.name
                        .reduce((a,b) -> a + "," + b) // склеиваем строки
                        .orElse("-") : "-", //если platforms пуст или null вернёт прочерк
                shortDescription != null && shortDescription.length() > 100 ? // обрезка описания
                        shortDescription.substring(0, 100) + "..." : shortDescription
        );
    }
}
