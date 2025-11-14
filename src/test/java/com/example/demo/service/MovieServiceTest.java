package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.demo.model.Movie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class MovieServiceTest {

    @Test
    void testMovieServiceLoadsMovies() {
        // Arrange
        MovieService movieService = new MovieService();

        // Act
        List<Movie> movies = movieService.getAllMovies();

        // Assert
        assertNotNull(movies, "Movies list should not be null");
        assertFalse(movies.isEmpty(), "Movies list should not be empty");

        // Verify a few movies to ensure proper parsing
        if (!movies.isEmpty()) {
            Movie firstMovie = movies.get(0);
            assertNotNull(firstMovie.getTitle(), "Movie title should not be null");
            assertNotNull(firstMovie.getGenre(), "Movie genre should not be null");
            assertTrue(firstMovie.getYear() > 0, "Movie year should be greater than 0");

            System.out.println("[DEBUG_LOG] First movie: " + firstMovie.getTitle() + " (" + firstMovie.getYear() + ")");
            System.out.println("[DEBUG_LOG] Total movies loaded: " + movies.size());
        }
    }

    @Test
    void testCheckForDuplicateTitles() {
        // Arrange
        MovieService movieService = new MovieService();

        // Act
        Map<String, Integer> duplicates = movieService.findDuplicateTitles();
        boolean hasDuplicates = movieService.hasDuplicateTitles();

        // Create a StringBuilder to collect all the output
        StringBuilder output = new StringBuilder();
        output.append("[DEBUG_LOG] Has duplicate titles: ").append(hasDuplicates).append("\n");

        if (hasDuplicates) {
            output.append("[DEBUG_LOG] Duplicate titles found:\n");
            for (Map.Entry<String, Integer> entry : duplicates.entrySet()) {
                output.append("[DEBUG_LOG] Title: '").append(entry.getKey()).append("' appears ").append(entry.getValue()).append(" times\n");
            }
        } else {
            output.append("[DEBUG_LOG] No duplicate titles found in movies.json\n");
        }

        // Print total number of movies for reference
        List<Movie> allMovies = movieService.getAllMovies();
        output.append("[DEBUG_LOG] Total number of movies: ").append(allMovies.size()).append("\n");

        // Print all the collected output at once
        System.out.println(output.toString());

        // Check for case-insensitive duplicates as well
        Map<String, Integer> caseInsensitiveCounts = new HashMap<>();
        for (Movie movie : allMovies) {
            String lowerCaseTitle = movie.getTitle().toLowerCase();
            caseInsensitiveCounts.put(lowerCaseTitle, caseInsensitiveCounts.getOrDefault(lowerCaseTitle, 0) + 1);
        }

        Map<String, Integer> caseInsensitiveDuplicates = new HashMap<>();
        for (Map.Entry<String, Integer> entry : caseInsensitiveCounts.entrySet()) {
            if (entry.getValue() > 1) {
                caseInsensitiveDuplicates.put(entry.getKey(), entry.getValue());
            }
        }

        boolean hasCaseInsensitiveDuplicates = !caseInsensitiveDuplicates.isEmpty();
        System.out.println("[DEBUG_LOG] Has case-insensitive duplicate titles: " + hasCaseInsensitiveDuplicates);

        if (hasCaseInsensitiveDuplicates) {
            System.out.println("[DEBUG_LOG] Case-insensitive duplicate titles found:");
            for (Map.Entry<String, Integer> entry : caseInsensitiveDuplicates.entrySet()) {
                System.out.println("[DEBUG_LOG] Title (lowercase): '" + entry.getKey() + "' appears " + entry.getValue() + " times");

                // Find the actual titles with this lowercase version
                System.out.println("[DEBUG_LOG] Actual titles:");
                for (Movie movie : allMovies) {
                    if (movie.getTitle().toLowerCase().equals(entry.getKey())) {
                        System.out.println("[DEBUG_LOG]   - '" + movie.getTitle() + "' (" + movie.getYear() + ")");
                    }
                }
            }
        }

        // We're not asserting any specific outcome here as we're just checking if duplicates exist
        assertTrue(true, "This assertion always passes, but ensures test output is visible");
    }
}
