package model;

public class Game {
    private int id;
    private String name;
    private String slug;
    private int released;
    private double rating;
    private String background_image;

    public int getId() { return id; }
    public void setID(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public int getReleased() { return released; }
    public void setReleased(int released) { this.released = released; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public String getBackground_image() { return background_image; }
    public void setBackground_image(String background_image) { this.background_image = background_image; }

    @Override
    public String toString() {
        return  String.format("\uD83C\uDFAE %s (%d) — Рейтинг: %.1f",name,released,rating);
    }
}
