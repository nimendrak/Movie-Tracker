package com.example.nimendra;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nimendra.util.MovieDatabase;

public class MainActivity extends AppCompatActivity {

    MovieDatabase movieDatabase;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize SQLite Database using singleton
        movieDatabase = MovieDatabase.getInstance(this);
    }

    public void registerMovie(View view) {
        Intent intent = new Intent(this, RegisterMovie.class);
        startActivity(intent);
    }

    public void displayMovies(View view) {
        Intent intent = new Intent(this, DisplayMovies.class);
        startActivity(intent);
    }

    public void displayFavorites(View view) {
        Intent intent = new Intent(this, FavoriteMovies.class);
        startActivity(intent);
    }

    /**
     * Both EditMovies and Ratings activities use the same 1st Screen
     * Which is used to display all the movies in Database
     * Each activity will launch accordingly to the putExtra boolean (ratings)
     * @param view - Current Layout
     */

    public void editMovies(View view) {
        Intent intent = new Intent(this, SelectMovie.class);
        intent.putExtra("ratings", false);
        startActivity(intent);
    }

    public void searchMovies(View view) {
        Intent intent = new Intent(this, Search.class);
        startActivity(intent);
    }

    public void ratings(View view) {
        Intent intent = new Intent(this, SelectMovie.class);
        intent.putExtra("ratings", true);
        startActivity(intent);
    }
}