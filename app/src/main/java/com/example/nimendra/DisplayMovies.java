package com.example.nimendra;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nimendra.util.MovieDatabase;

import java.util.ArrayList;
import java.util.List;

public class DisplayMovies extends AppCompatActivity {

    // Class name for Log tag
    private static final String LOG_TAG = DisplayMovies.class.getSimpleName();

    private List<String> movieTitles;
    private List<String> favMoviesTitles = new ArrayList<>();

    private final String sharedPrefFile = "com.project.movies";
    private ArrayList<Boolean> checkboxesStatus = new ArrayList<>();

    MovieDatabase movieDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_movies);

        movieDatabase = MovieDatabase.getInstance(this);
        movieDatabase.showAll();

        // Movie titles list
        movieTitles = movieDatabase.retrieveData();

        SharedPreferences preferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        for (int i = 0; i < movieTitles.size(); i++) {
            checkboxesStatus.add(preferences.getBoolean(String.format("checkbox %s", i), false));
        }

        CustomAdapter adapter = new CustomAdapter();
        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences preferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        for (int i = 0; i < movieTitles.size(); i++) {
            preferencesEditor.putBoolean(String.format("checkbox %s", i), checkboxesStatus.get(i));
        }
        preferencesEditor.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ListView listView = findViewById(R.id.list_view);
        listView.removeAllViewsInLayout();

//        checkboxesStatus.clear();
//        movieTitles.clear();
//        favMoviesTitles.clear();

        Log.i(LOG_TAG, "Destroyed");
    }

    private class CustomAdapter extends ArrayAdapter<String> {
        public CustomAdapter() {
            super(DisplayMovies.this, R.layout.list_view_row, movieTitles);
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            View rowView = view;
            if (rowView == null) {
                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(R.layout.list_view_row, null, true);
            }
            TextView text = rowView.findViewById(R.id.label);
            text.setText(movieTitles.get(position));

            CheckBox checkBox = rowView.findViewById(R.id.checkbox);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    checkboxesStatus.set(position, isChecked);
                    if (isChecked) {
                        for (int i = 0; i < movieTitles.size(); i++) {
                            // Add checked movie
                            if (checkboxesStatus.get(i)) {
                                // Add only once to the arr
                                if (!favMoviesTitles.contains(movieTitles.get(i))) {
                                    favMoviesTitles.add(movieTitles.get(i));
                                }
                            }
                        }
                    } else {
                        for (int i = 0; i < movieTitles.size(); i++) {
                            // Remove non checked movie
                            if (!checkboxesStatus.get(i)) {
                                favMoviesTitles.remove(movieTitles.get(i));
                            }
                        }
                    }
                    System.out.println(favMoviesTitles);
                }
            });
            checkBox.setChecked(checkboxesStatus.get(position));
            return rowView;
        }
    }

    public void AddToFav(View view) {
        movieDatabase.addToFavorites(findViewById(R.id.display_movie), favMoviesTitles);
        movieDatabase.showAll();
    }


}