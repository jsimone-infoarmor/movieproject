package com.example.demo;

import com.example.demo.service.MovieService;
import java.util.Map;

/**
 * A simple utility class to check for duplicate movie titles in the movies.json file.
 */
public class DuplicateTitleChecker {
    
    public static void main(String[] args) {
        MovieService movieService = new MovieService();
        
        // Check for duplicate titles
        Map<String, Integer> duplicates = movieService.findDuplicateTitles();
        boolean hasDuplicates = movieService.hasDuplicateTitles();
        
        // Print results
        System.out.println("Has duplicate titles: " + hasDuplicates);
        
        if (hasDuplicates) {
            System.out.println("Duplicate titles found:");
            for (Map.Entry<String, Integer> entry : duplicates.entrySet()) {
                System.out.println("Title: '" + entry.getKey() + "' appears " + entry.getValue() + " times");
            }
        } else {
            System.out.println("No duplicate titles found in movies.json");
        }
        
        // Print total number of movies for reference
        System.out.println("Total number of movies: " + movieService.getAllMovies().size());
    }
}