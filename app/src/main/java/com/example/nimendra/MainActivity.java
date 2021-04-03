package com.example.nimendra;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.nimendra.util.MovieDatabase;

public class MainActivity extends AppCompatActivity {

    // Class name for Log tag
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    MovieDatabase movieDatabase;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    public void editMovies(View view) {
        Intent intent = new Intent(this, SelectMovieToEdit.class);
        startActivity(intent);
    }
}