package com.example.nimendra;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nimendra.util.FetchData;

public class Ratings extends AppCompatActivity {

    // Class name for Log tag
    private static final String LOG_TAG = Ratings.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);

        // Selected movie that should be search on the IMDB
        String selectedMovie = getIntent().getExtras().getString("selected_item");

        // Fetch data from the IMDB API according to the selected movie
        FetchData fetchData = new FetchData(selectedMovie, this, Ratings.this);
        fetchData.execute();
    }
}