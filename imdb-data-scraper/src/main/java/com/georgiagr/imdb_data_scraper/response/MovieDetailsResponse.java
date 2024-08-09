package com.georgiagr.imdb_data_scraper.response;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.georgiagr.imdb_data_scraper.model.Movie;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieDetailsResponse {

    @JsonProperty("title")
    private String title;

    @JsonProperty("runningTimeInMinutes")
    private Integer runningTime;

    @JsonProperty("year")
    private Integer year;

    @JsonProperty("actors")
    private String actors;

    public MovieDetailsResponse(String title, Integer runningTime, Integer year, String actors) {
        this.title = title;
        this.runningTime = runningTime;
        this.year = year;
        this.actors = actors;
    }

    public MovieDetailsResponse() {
    }

    /*
         Map the Movie entity to the MovieDetailsResponse
    */

    public static MovieDetailsResponse map(Movie entity) {
        MovieDetailsResponse response = new MovieDetailsResponse();
        response.setTitle(entity.getTitle());
        response.setRunningTime(entity.getRunningTime());
        response.setYear(entity.getYear());
        response.setActors(entity.getActors());
        return response;
    }

    public static List<MovieDetailsResponse> map(List<Movie> entities) {
        List<MovieDetailsResponse> result = new ArrayList<>();
        for (Movie entity : entities) {
            result.add(map(entity));
        }
        return result;
    }

    /*
        Getters
    */

    public String getTitle() {
        return title;
    }

    public Integer getRunningTime() {
        return runningTime;
    }

    public Integer getYear() {
        return year;
    }

    public String getActors() {
        return actors;
    }

    /*
        Setters
    */

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRunningTime(Integer runningTime) {
        this.runningTime = runningTime;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }
}
