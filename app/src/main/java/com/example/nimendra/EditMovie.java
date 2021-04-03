package com.example.nimendra;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nimendra.util.MovieDatabase;

import java.util.ArrayList;
import java.util.List;

public class EditMovie extends AppCompatActivity {

    // Class name for Log tag
    private static final String LOG_TAG = EditMovie.class.getSimpleName();

    List<String> currentMovieData = new ArrayList<>();

    MovieDatabase movieDatabase;

    EditText getMovieTitle;
    EditText getMovieYear;
    EditText getMovieDirector;
    EditText getMovieCast;
    EditText getMovieRatings;
    EditText getMovieReviews;

    TextView movieIndex;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_movie);

        movieDatabase = MovieDatabase.getInstance(this);

        getMovieTitle = findViewById(R.id.title_input);
        getMovieYear = findViewById(R.id.year_input);
        getMovieDirector = findViewById(R.id.director_input);
        getMovieCast = findViewById(R.id.cast_input);
        getMovieRatings = findViewById(R.id.ratings_input);
        getMovieReviews = findViewById(R.id.reviews_input);

        movieIndex = findViewById(R.id.movie_index);

        // Get the switch_stats from EditMovieMenu
        String selectedMovie = getIntent().getExtras().getString("selected_item");

        // Get current data as a List<String>
        currentMovieData = movieDatabase.retrieveSpecificMovie(selectedMovie);

        // Set current data into the EditMovie activity
        movieIndex.setText(String.format("%03d", Integer.parseInt(currentMovieData.get(0))));

        getMovieTitle.setText(currentMovieData.get(1));
        getMovieYear.setText(currentMovieData.get(2));
        getMovieDirector.setText(currentMovieData.get(3));
        getMovieCast.setText(currentMovieData.get(4));
        getMovieRatings.setText(currentMovieData.get(5));
        getMovieReviews.setText(currentMovieData.get(6));
    }

    public void resetData(View view) {
        getMovieDirector.getText().clear();
        getMovieCast.getText().clear();
        getMovieRatings.getText().clear();
        getMovieReviews.getText().clear();
    }

    public void saveMovie(View view) {
        String title = getMovieTitle.getText().toString();
//        int year = getInput(getMovieYear);
        int year = Integer.parseInt(getMovieYear.getText().toString());
        String director = getMovieDirector.getText().toString();
        String cast = getMovieCast.getText().toString();
        int ratings = Integer.parseInt(getMovieRatings.getText().toString());
        String reviews = getMovieReviews.getText().toString();

        movieDatabase.updateMovieData(currentMovieData.get(0), title, year, director, cast, ratings, reviews, findViewById(R.id.edit_movie));
    }
}