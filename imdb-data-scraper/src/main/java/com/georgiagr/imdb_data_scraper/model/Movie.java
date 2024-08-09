package com.georgiagr.imdb_data_scraper.model;
import javax.persistence.*;

@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="title", columnDefinition = "TEXT")
    private String title;

    @Column(name="running_time")
    private Integer runningTime;


    @Column(name="year")
    private Integer year;


    @Column(name="actor")
    private String actors;


    public Movie() {
    }

    public Movie(String title, Integer runningTime, Integer year, String actors) {
        this.title = title;
        this.runningTime = runningTime;
        this.year = year;
        this.actors = actors;
    }

    /*
        Getters
    */

    public Integer getId() {
        return id;
    }

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

    public void setId(Integer id) {
        this.id = id;
    }

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



