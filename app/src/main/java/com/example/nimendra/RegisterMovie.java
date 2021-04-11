package com.example.nimendra;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nimendra.util.MovieDatabase;
import com.example.nimendra.util.ShowSnackBar;

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

    TextView movieIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_movie);

        movieDatabase = MovieDatabase.getInstance(this);

        getMovieTitle = findViewById(R.id.search_input);
        getMovieYear = findViewById(R.id.year_input);
        getMovieDirector = findViewById(R.id.director_input);
        getMovieCast = findViewById(R.id.cast_input);
        getMovieRatings = findViewById(R.id.ratings_input);
        getMovieReviews = findViewById(R.id.reviews_input);

        movieIndex = findViewById(R.id.movie_index);

        getMovieTitle.setText("Godzilla vs Kong");
        getMovieYear.setText("2021");
        getMovieDirector.setText("Adam Wingard");
        getMovieCast.setText("Alexander Skarsg˚ard, Millie Bobby Brown, Rebecca Hall, Brian Tyree Henry, Shun Oguri, Eiza Gonz´alez, Julian Dennison");
        getMovieRatings.setText("6");
        getMovieReviews.setText("A fast-paced action movie keeping you in suspense for 90 minutes. Excellent performance by Shun Oguri.");

        validateYear(getMovieYear);
    }

    @SuppressLint("DefaultLocale")
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void registerMovie(View view) {
        if (validateYear(getMovieYear)) {
            String title = getMovieTitle.getText().toString();
            int year = Integer.parseInt(getMovieYear.getText().toString());
            String director = getMovieDirector.getText().toString();
            String cast = getMovieCast.getText().toString();
            int ratings = Integer.parseInt(getMovieRatings.getText().toString());
            String reviews = getMovieReviews.getText().toString();

            Log.i(LOG_TAG, "Validated ans -> " + year);

            movieDatabase.insertData(title, year, director, cast, ratings, reviews, findViewById(R.id.register_movie));
            movieIndex.setText(String.format("%03d", movieDatabase.getDbSize()));
            resetData(findViewById(R.id.register_movie));
        } else {
            new ShowSnackBar(findViewById(R.id.register_movie), "Prompted Year is below 1895");
            getMovieYear.getText().clear();
        }
    }

    public void resetData(View view) {
        getMovieTitle.getText().clear();
        getMovieYear.getText().clear();
        getMovieDirector.getText().clear();
        getMovieCast.getText().clear();
        getMovieRatings.getText().clear();
        getMovieReviews.getText().clear();
    }

    // Get input and validate from the EditText and holds it on a array of 1 element
    // Once user clicks the submit button, soft keyboard will disappear
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