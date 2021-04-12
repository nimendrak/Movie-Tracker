package com.example.nimendra.db;

/**
 * MovieModel contains all the data a single movie holds
 * Implemented the Comparable interface to sort movies by titles
 */
public class MovieModel implements Comparable<MovieModel> {
    private String title;
    private int year;
    private String director;
    private String cast;
    private int ratings;
    private String reviews;
    private int isFav;

    public MovieModel(String title, int year, String director, String cast, int ratings, String reviews, int isFav) {
        this.title = title;
        this.year = year;
        this.director = director;
        this.cast = cast;
        this.ratings = ratings;
        this.reviews = reviews;
        this.isFav = isFav;
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

    @Override
    public int compareTo(MovieModel o) {
        return this.title.compareTo(o.getTitle());
    }
}
