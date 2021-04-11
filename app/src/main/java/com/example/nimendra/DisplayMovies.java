package com.example.nimendra;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.drawable.GradientDrawable;
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

import com.example.nimendra.util.MovieModel;
import com.example.nimendra.util.MovieDatabase;

import java.util.ArrayList;
import java.util.List;

public class DisplayMovies extends AppCompatActivity {

    // Class name for Log tag
    private static final String LOG_TAG = DisplayMovies.class.getSimpleName();

    private List<String> movieTitles = new ArrayList<>();
    private final List<String> favMoviesTitles = new ArrayList<>();

    private final ArrayList<Boolean> checkboxesStatus = new ArrayList<>();

    MovieDatabase movieDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_movies);

        movieDatabase = MovieDatabase.getInstance(this);
        movieDatabase.showAll();

        // Generate movie titles list
        List<MovieModel> movieModelData = movieDatabase.retrieveMoviesData();

        for (MovieModel m : movieModelData) {
            movieTitles.add(m.getTitle());

            int isFav = m.getIsFav();
            checkboxesStatus.add(isFav == 1);
        }

        Log.i(LOG_TAG + "check box status", String.valueOf(checkboxesStatus));

        CustomAdapter adapter = new CustomAdapter();
        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        // Set ListView divider color programmatically
        int[] colors = {0, 0xFFFFFFFF, 0};
        listView.setDivider(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors));
        listView.setDividerHeight(2);
    }

    private class CustomAdapter extends ArrayAdapter<String> {
        public CustomAdapter() {
            super(DisplayMovies.this, R.layout.list_view_row_des1, movieTitles);
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            View rowView = view;
            if (rowView == null) {
                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(R.layout.list_view_row_des1, null, true);
            }
            TextView text = rowView.findViewById(R.id.label);
            text.setText(movieTitles.get(position));

            CheckBox checkBox = rowView.findViewById(R.id.checkbox);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        checkboxesStatus.set(position, isChecked);
                        if (isChecked) {
                            // Every iteration add only one to the arr
                            if (!favMoviesTitles.contains(movieTitles.get(position))) {
                                favMoviesTitles.add(movieTitles.get(position));
                            }
                        } else {
                            // Remove non checked movie
                            favMoviesTitles.remove(movieTitles.get(position));
                            checkboxesStatus.remove(checkboxesStatus.get(position));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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

    public void resetStatus(View view) {
    }
}