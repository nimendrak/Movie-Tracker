package com.example.nimendra.util;

public class Movie {
    private int id;
    private String title;
    private int year;
    private String director;
    private String cast;
    private int ratings;
    private String reviews;
    private int isFav;

    public Movie(int id, String title, int year, String director, String cast, int ratings, String reviews, int isFav) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.director = director;
        this.cast = cast;
        this.ratings = ratings;
        this.reviews = reviews;
        this.isFav = isFav;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public int getRatings() {
        return ratings;
    }

    public void setRatings(int ratings) {
        this.ratings = ratings;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    public int getIsFav() {
        return isFav;
    }

    @Override
    public String toString() {
        return title;
    }
}
