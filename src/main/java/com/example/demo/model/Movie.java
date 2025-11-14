package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class Movie {

    private  int num;
    private String title;
    private int year;
    private double imdb_rating;
    private List<String> genre;
    private String imdb_id;
    private String imdb_url;
    private String description;

    // Getters and setters
    public int getNum() {
        return num;
    }
    public int setNum(int num) {
        return this.num = num;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }

    public double getImdb_rating() {
        return imdb_rating;
    }
    public void setImdb_rating(double imdb_rating) {
        this.imdb_rating = imdb_rating;
    }

    public List<String> getGenre() {
        return genre;
    }

    public void setGenre(List<String> genre) {
        this.genre = genre;
    }

    public boolean isGenre(String genre) {
        // Handle null or empty data safely
        if (this.genre == null || this.genre.isEmpty() || genre == null) {
            return false;
        }

        // Flatten the movie's genre list into a single searchable string
        String genreText = String.join(" ", this.genre);

        // Collect search tokens by splitting on commas
        String[] parts = genre.split(",");
        List<String> tokens = new ArrayList<>();
        for (String part : parts) {
            if (!StringUtils.isBlank(part)) {
                tokens.add(part.trim());
            }
        }

        // If no tokens derived, fall back to the original containsIgnoreCase behavior
        if (tokens.isEmpty()) {
            return StringUtils.containsIgnoreCase(genreText, genre);
        }

        // Require that ALL tokens are found (case-insensitive) within the movie's genre text
        for (String token : tokens) {
            if (!StringUtils.containsIgnoreCase(genreText, token)) {
                return false;
            }
        }
        return true;
    }

    public boolean isInRange(Integer yearStart, Integer yearEnd) {
        return yearStart != null && this.year >= yearStart && yearEnd != null && this.year <= yearEnd;
    }

    public String getImdb_id() {
        return imdb_id;
    }
    public void setImdb_id(String imdb_id) {
        this.imdb_id = imdb_id;
    }

    public String getImdb_url() {
        return imdb_url;
    }
    public void setImdb_url(String imdb_url) {
        this.imdb_url = imdb_url;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "num=" + num +
                "  title='" + title + '\'' +
                ", year=" + year +
                ", imdb_rating=" + imdb_rating +
                ", genre=" + genre +
                ", imdb_id='" + imdb_id + '\'' +
                ", imdb_url='" + imdb_url + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
