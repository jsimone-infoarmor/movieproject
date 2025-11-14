package com.example.demo.service;

import com.example.demo.model.Movie;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class MovieService {

    private final List<Movie> movies;

    public MovieService() {
        this.movies = loadMoviesFromJson();
    }

    private List<Movie> loadMoviesFromJson() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("movies.json");

            if (inputStream == null) {
                throw new IOException("Could not find movies.json");
            }

            return objectMapper.readValue(inputStream, new TypeReference<ArrayList<Movie>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Movie> getAllMovies() {
        return filterMovies(null, null, null, null, null, null);
    }

    public List<Movie> getMovieByPartialTitle(String title, String sort) {
        if (sort == null) {
            sort = "title";
        }
        return filterMovies(title, null, null, null, null, sort);
    }

    /**
     * Checks if there are any movies with duplicate titles in the collection.
     *
     * @return A map where the key is the duplicate title and the value is the count of occurrences
     */
    public Map<String, Integer> findDuplicateTitles() {
        Map<String, Integer> titleCounts = new HashMap<>();
        Map<String, Integer> duplicates = new HashMap<>();

        // Count occurrences of each title
        for (Movie movie : movies) {
            String title = movie.getTitle();
            titleCounts.put(title, titleCounts.getOrDefault(title, 0) + 1);
        }

        // Filter out titles that appear more than once
        for (Map.Entry<String, Integer> entry : titleCounts.entrySet()) {
            if (entry.getValue() > 1) {
                duplicates.put(entry.getKey(), entry.getValue());
            }
        }

        return duplicates;
    }

    /**
     * Checks if there are any duplicate movie titles in the collection.
     *
     * @return true if there are duplicate titles, false otherwise
     */
    public boolean hasDuplicateTitles() {
        return !findDuplicateTitles().isEmpty();
    }

    public List<Movie> getMovieByGenre(String genre) {
        if (StringUtils.isBlank(genre)) {
            return new ArrayList<>();
        } else {
            return filterMovies(null, genre, null, null, null, "title");
        }
    }

    public List<Movie> getMovieByRating(Double imdbRating) {
        return filterMovies(null, null, imdbRating, null, null, "title");
    }

    public List<Movie> getMoviesByYear(Integer year) {
        return filterMovies(null, null, null, year, null, "title");
    }

    public List<Movie> getMoviesTopNbyYear(Integer numberMovies) {
        if (numberMovies < 1) {
            throw new IllegalArgumentException("numberMovies must be 1 or more");
        }
        int pastYear = LocalDate.now().plusYears(-1).getYear();
        List<Movie> filteredMovies = filterMovies(null, null, null, pastYear, pastYear, "rating");
        return filteredMovies.stream().limit(numberMovies).toList();
    }

    public List<Movie> sortMovies(List<Movie> moviesToSort, String sort) {
        Stream<Movie> stream = moviesToSort.stream();
        if ("title".equalsIgnoreCase(sort)) {
            return stream
                    .sorted(java.util.Comparator.comparing(Movie::getTitle, String.CASE_INSENSITIVE_ORDER))
                    .toList();
        } else if ("year".equalsIgnoreCase(sort)) {
            return stream
                    .sorted(java.util.Comparator.comparingInt(Movie::getYear))
                    .toList();
        } else if ("rating".equalsIgnoreCase(sort)) {
            return stream
                    .sorted(java.util.Comparator.comparingDouble(Movie::getImdb_rating).reversed())
                    .toList();
        }
        return stream.toList();
    }

    public List<Movie> filterMovies(String title, String genre, Double minRating, Integer yearStart, Integer yearEnd, String sort) {
        if (sort == null) {
            sort = "title";
        }
        if (yearEnd == null) {
            yearEnd = yearStart;
        }

        if (!Objects.equals(sort, "title") && !Objects.equals(sort, "year") && !Objects.equals(sort, "rating")) {
            throw new IllegalArgumentException("sort must be either title or year or rating");
        }
        int futureYear = LocalDate.now().plusYears(5).getYear(); // 5 years in the future
        if (yearStart != null && (yearStart < 1878 || yearStart > futureYear)) {
            throw new IllegalArgumentException(String.format("year start must be between 1878 and %s", futureYear));
        }
        if (yearEnd != null && (yearEnd < 1878 || yearEnd > futureYear)) {
            throw new IllegalArgumentException(String.format("year end must be between 1878 and %s", futureYear));
        }
        if (yearStart != null && yearEnd != null && yearEnd < yearStart ) {
            throw new IllegalArgumentException(String.format("year start %s must be before year end %s", yearStart, yearEnd));
        }

        final Integer finalYearEnd = yearEnd;
        List<Movie> filteredMovies = movies.stream()
                .filter(movie -> title == null || StringUtils.containsIgnoreCase(movie.getTitle(), title))
                .filter(movie -> genre == null || movie.isGenre(genre))
                .filter(movie -> minRating == null || movie.getImdb_rating() >= minRating)
                .filter(movie -> yearStart == null || movie.isInRange(yearStart, finalYearEnd))
                .toList();
        return numberMovies(sortMovies(filteredMovies, sort));
    }

    public List<Movie> filterMovies(String title, String genre, Double minRating, Integer yearStart, Integer yearEnd) {
        return filterMovies(title, genre, minRating, yearStart, yearEnd, "title");
    }

    public List<Movie> numberMovies(List<Movie> movies) {
        if (movies == null) {
            return new ArrayList<>();
        }

        final java.util.concurrent.atomic.AtomicInteger idx = new java.util.concurrent.atomic.AtomicInteger(1);
        movies.stream()
                .filter(Objects::nonNull)
                .forEach(m -> m.setNum(idx.getAndIncrement()));
        return movies;
    }
}
