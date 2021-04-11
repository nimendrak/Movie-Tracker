package com.example.nimendra;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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