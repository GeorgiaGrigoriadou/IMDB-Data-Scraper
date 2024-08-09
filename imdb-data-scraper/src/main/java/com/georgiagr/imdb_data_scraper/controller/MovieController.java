package com.georgiagr.imdb_data_scraper.controller;
import com.georgiagr.imdb_data_scraper.response.MovieDetailsResponse;
import com.georgiagr.imdb_data_scraper.service.ImdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/title/find")
public class MovieController {

    @Autowired
    private ImdbService imdbService;

    @GetMapping("/{title}")
    public ResponseEntity<List<MovieDetailsResponse>> find(@PathVariable("title") String title) {
        System.out.println("Received request for title: " + title);
        List<MovieDetailsResponse> movieResponse = imdbService.findMovieByTitle(title);
        return ResponseEntity.ok(movieResponse);
    }
}


