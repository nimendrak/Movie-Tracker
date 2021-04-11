package com.example.nimendra;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nimendra.util.FetchData;

import java.util.ArrayList;
import java.util.List;

public class Ratings extends AppCompatActivity {

    // Class name for Log tag
    private static final String LOG_TAG = Ratings.class.getSimpleName();
    private final String API_KEY = getResources().getString(R.string.MY_API_KEY);

    List<String> movieTitles = new ArrayList<>();
    List<String> movieRatings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);

        final String selectedMovie = getIntent().getExtras().getString("selected_item");
        Log.i(LOG_TAG + "Selected Movie ", selectedMovie);

        FetchData fetchData = new FetchData(selectedMovie, API_KEY);

        movieTitles = fetchData.getMovieTitles();
        movieRatings = fetchData.getMovieRatings();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CustomAdapter adapter = new CustomAdapter();
                ListView listView = findViewById(R.id.list_view);
                listView.setAdapter(adapter);
            }
        });
    }

    private class CustomAdapter extends ArrayAdapter<String> {
        public CustomAdapter() {
            super(Ratings.this, R.layout.list_view_row_des3, movieTitles);
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            View rowView = view;
            if (rowView == null) {
                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(R.layout.list_view_row_des3, null, true);
            }
            TextView text = rowView.findViewById(R.id.title);
            TextView rating = rowView.findViewById(R.id.rating);

            text.setText(movieTitles.get(position));
            rating.setText(movieRatings.get(position));

            return rowView;
        }
    }
}