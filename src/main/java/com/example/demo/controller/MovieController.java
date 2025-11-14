package com.example.demo.controller;

import com.example.demo.model.Movie;
import com.example.demo.service.MovieService;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    // GET /api/movies -> all movies
    @GetMapping
    public List<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }

    @GetMapping("/search")
    public List<Movie> searchByTitle(@RequestParam(value = "q", required = false) String title,
                                     @RequestParam(value = "sort", required = false) String sort,
                                     @RequestParam(value = "genre", required = false) String genre,
                                     @RequestParam(value = "minRating", required = false) Double minRating,
                                     @RequestParam(value = "yearStart", required = false) Integer yearStart,
                                     @RequestParam(value = "yearEnd", required = false) Integer yearEnd) {
        if (StringUtils.isEmpty(sort)) {
            sort = "title";
        }
        return movieService.filterMovies(title, genre, minRating, yearStart,  yearEnd,  sort);
    }

    @GetMapping("/genre/{genre}")
    public List<Movie> getByGenre(@PathVariable String genre) {
        return movieService.getMovieByGenre(genre);
    }

    @GetMapping("/rating/{rating}")
    public List<Movie> getByRating(@PathVariable("rating") Double rating) {
        return movieService.getMovieByRating(rating);
    }

    @GetMapping("/toprating/{limit}")
    public List<Movie> getTopRatingWithLimit(@PathVariable("limit") Integer limit) {
        return movieService.getMoviesTopNbyYear(limit);
    }

    @GetMapping("/year/{year}")
    public List<Movie> getByYear(@PathVariable int year) {
        return movieService.getMoviesByYear(year);
    }

    // GET /api/movies/duplicates -> map of duplicate title to count
    @GetMapping("/duplicates")
    public Map<String, Integer> getDuplicateTitles() {
        return movieService.findDuplicateTitles();
    }

    // GET /api/movies/duplicates/exists -> true/false
    @GetMapping("/duplicates/exists")
    public boolean hasDuplicateTitles() {
        return movieService.hasDuplicateTitles();
    }
}
