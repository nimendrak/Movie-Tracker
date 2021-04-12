package com.example.nimendra;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.nimendra.util.MovieDatabase;
import com.example.nimendra.util.ShowSnackBar;

import java.util.ArrayList;
import java.util.List;

public class EditMovie extends AppCompatActivity {

    // Class name for Log tag
    private static final String LOG_TAG = EditMovie.class.getSimpleName();

    // Holds the values of Selected Movie
    List<String> currentMovieData = new ArrayList<>();

    // Initialize SQLite helper class
    MovieDatabase movieDatabase;

    EditText getMovieTitle;
    EditText getMovieYear;
    EditText getMovieDirector;
    EditText getMovieCast;
    EditText getMovieRatings;
    EditText getMovieReviews;
    CheckBox isFavorite;

    TextView movieIndex;
    RatingBar ratingBar;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_movie);

        // Refers the already declared movieDatabase instance
        movieDatabase = MovieDatabase.getInstance(this);

        // EditText -> Inputs
        getMovieTitle = findViewById(R.id.search_input);
        getMovieYear = findViewById(R.id.year_input);
        getMovieDirector = findViewById(R.id.director_input);
        getMovieCast = findViewById(R.id.cast_input);
        getMovieRatings = findViewById(R.id.ratings_input);
        getMovieReviews = findViewById(R.id.reviews_input);

        // Checkbox -> isFav status
        isFavorite = findViewById(R.id.isFav);

        // TextView -> Movie ID
        movieIndex = findViewById(R.id.movie_index);

        // RatingBar -> Out of 10 stars
        ratingBar = findViewById(R.id.ratingBar);

        // Get the selected_item from EditMovieMenu
        String selectedMovie = getIntent().getExtras().getString("selected_item");

        // Get current data as a List<String>
        currentMovieData = movieDatabase.retrieveSpecificMovie(selectedMovie);

        // Set current data into the EditMovie activity
        movieIndex.setText(String.format("%03d", Integer.parseInt(currentMovieData.get(0))));

        // Set selected movie data as the EditText values
        getMovieTitle.setText(currentMovieData.get(1));
        getMovieYear.setText(currentMovieData.get(2));
        getMovieDirector.setText(currentMovieData.get(3));
        getMovieCast.setText(currentMovieData.get(4));
        getMovieReviews.setText(currentMovieData.get(6));

        // Set ratings bar stars according to the given rating
        ratingBar.setRating(Float.parseFloat(currentMovieData.get(5)));
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            }
        });

        // Set isFav status by given data
        if (Integer.parseInt(currentMovieData.get(7)) == 1) {
            isFavorite.setChecked(true);
            isFavorite.setText(R.string.fav_movie_textView);
        } else {
            isFavorite.setChecked(false);
            isFavorite.setText(R.string.not_fav_movie_textView);
        }

        // Change isFav text according to the state of the check box
        isFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isFavorite.setText(R.string.fav_movie_textView);
                } else {
                    isFavorite.setText(R.string.not_fav_movie_textView);
                }

            }
        });
    }

    /**
     * When user clicks save movie data and wanted to undo the data to previous status
     * Original data is already stores on the currentMovieData list
     * Repopulate EditText accordingly from the currentMovieData list
     * @param view - Current Layout
     */
    public void undoData(View view) {
        getMovieTitle.setText(currentMovieData.get(1));
        getMovieYear.setText(currentMovieData.get(2));
        getMovieDirector.setText(currentMovieData.get(3));
        getMovieCast.setText(currentMovieData.get(4));
        ratingBar.setRating(Float.parseFloat(currentMovieData.get(5)));
        getMovieReviews.setText(currentMovieData.get(6));

        if (Integer.parseInt(currentMovieData.get(7)) == 1) {
            isFavorite.setChecked(true);
            isFavorite.setText(R.string.fav_movie_textView);
        } else {
            isFavorite.setChecked(false);
            isFavorite.setText(R.string.not_fav_movie_textView);
        }

        new ShowSnackBar(findViewById(R.id.edit_movie), "Resetting Movie Data..");
    }

    /**
     * Save updated movie data on database
     * @param view - Current Layout
     */
    @SuppressLint("DefaultLocale")
    public void saveMovie(View view) {
        // In order to update database movie year should be larger than 1895
        if (validateYear(getMovieYear)) {
            String title = getMovieTitle.getText().toString();
            int year = Integer.parseInt(getMovieYear.getText().toString());
            String director = getMovieDirector.getText().toString();
            String cast = getMovieCast.getText().toString();
            int ratings = (int) ratingBar.getRating();
            String reviews = getMovieReviews.getText().toString();

            int isFav;

            if (isFavorite.isChecked())
                isFav = 1;
            else
                isFav = 0;

            movieDatabase.updateMovieData(currentMovieData.get(0), title, year, director, cast, ratings, reviews, isFav, findViewById(R.id.edit_movie));
            movieIndex.setText(String.format("%03d", movieDatabase.getDbSize()));

            new ShowSnackBar(findViewById(R.id.edit_movie), "Movie Data Updated");
        } else {
            new ShowSnackBar(findViewById(R.id.edit_movie), "Prompted Year is below 1895");
            getMovieYear.getText().clear();
        }
    }

    /**
     * Get input and validate from the EditText and holds it on a array of 1 element
     * Once user clicks the submit button, soft keyboard will disappear
     * @param holder - EditText that should be validated (getYearInput)
     * @return - True, if prompted year is larger than 1895
     */
    public boolean validateYear(final EditText holder) {
        final int[] validatedAnswer = new int[1];
        final InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);

        if (holder != null) {
            // In case, user didn't consume imOptions
            final int tempYear = Integer.parseInt(holder.getText().toString());
            validatedAnswer[0] = tempYear;

            // Using imOption
            holder.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    boolean handled = false;
                    if (actionId == EditorInfo.IME_ACTION_SEND) {
                        if (tempYear > 1895)
                            validatedAnswer[0] = tempYear;
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        handled = true;
                    }
                    return handled;
                }
            });
        }
        return validatedAnswer[0] > 1895;
    }
}