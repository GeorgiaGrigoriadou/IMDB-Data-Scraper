package com.georgiagr.imdb_data_scraper.repository;

import com.georgiagr.imdb_data_scraper.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MovieRepository  extends JpaRepository<Movie, Integer> {
    Movie findByTitle(String title);

    List<Movie> findAllByTitleContaining(String title);
}

