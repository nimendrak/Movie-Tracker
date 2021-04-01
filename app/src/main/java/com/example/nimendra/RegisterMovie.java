package com.example.nimendra;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.nimendra.util.MovieDatabase;

public class RegisterMovie extends AppCompatActivity {

    // Class name for Log tag
    private static final String LOG_TAG = RegisterMovie.class.getSimpleName();

    MovieDatabase movieDatabase;

    EditText getMovieTitle;
    EditText getMovieYear;
    EditText getMovieDirector;
    EditText getMovieCast;
    EditText getMovieRatings;
    EditText getMovieReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_movie);

        movieDatabase = MovieDatabase.getInstance(this);

        getMovieTitle = findViewById(R.id.title_input);
        getMovieYear = findViewById(R.id.year_input);
        getMovieDirector = findViewById(R.id.director_input);
        getMovieCast = findViewById(R.id.cast_input);
        getMovieRatings = findViewById(R.id.ratings_input);
        getMovieReviews = findViewById(R.id.reviews_input);

        getMovieTitle.setText("Godzilla Vs Kong");
        getMovieYear.setText("2021");
        getMovieDirector.setText("Adam Wingard");
        getMovieCast.setText("Alexander Skarsg˚ard, Millie Bobby Brown, Rebecca Hall, Brian Tyree Henry, Shun Oguri, Eiza Gonz´alez, Julian Dennison");
        getMovieRatings.setText("6");
        getMovieReviews.setText("A fast-paced action movie keeping you in suspense for 90 minutes. Excellent performance by Shun Oguri.");
    }

    public void registerMovie(View view) {

        String title = getMovieTitle.getText().toString();
        String year = getMovieYear.getText().toString();
        String director = getMovieDirector.getText().toString();
        String cast = getMovieCast.getText().toString();
        String ratings = getMovieRatings.getText().toString();
        String reviews = getMovieReviews.getText().toString();

        Log.d(LOG_TAG, " title -> "+ title);
        Log.d(LOG_TAG, " year -> "+ year);
        Log.d(LOG_TAG, " director -> "+ director);
        Log.d(LOG_TAG, " cast -> "+ cast);
        Log.d(LOG_TAG, " ratings -> "+ ratings);
        Log.d(LOG_TAG, " reviews -> "+ reviews);

        movieDatabase.insertData(title, year, director, cast, ratings, reviews);
    }
}