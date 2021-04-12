package com.example.nimendra;

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

import androidx.appcompat.app.AppCompatActivity;

import com.example.nimendra.util.MovieDatabase;
import com.example.nimendra.util.ShowSnackBar;

import java.util.ArrayList;
import java.util.List;

public class FavoriteMovies extends AppCompatActivity {

    // Class name for Log tag
    private static final String LOG_TAG = DisplayMovies.class.getSimpleName();

    private List<String> favMoviesTitles;
    private List<String> favMoviesTitlesTemp;

    private ArrayList<Boolean> checkboxesStatus = new ArrayList<>();

    CustomAdapter adapter;
    ListView listView;

    // Initialize SQLite helper class
    MovieDatabase movieDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_movies);

        // Refers the already declared movieDatabase instance
        movieDatabase = MovieDatabase.getInstance(this);

        // Favorite Movie titles list
        favMoviesTitles = movieDatabase.retrieveFavoriteData();

        // Gets a copy of the favMoviesTitles
        // This will use if user wants to undo favorite movie data
        favMoviesTitlesTemp = new ArrayList<>(favMoviesTitles);

        // Populating checkboxesStatus with default value false
        for (int i = 0; i < favMoviesTitles.size(); i++) {
            checkboxesStatus.add(false);
        }

        // Initialize and declare CustomerAdapter to display movies
        adapter = new CustomAdapter();
        listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        // Set ListView divider color programmatically
        int[] colors = {0, 0xFFFFFFFF, 0};
        listView.setDivider(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors));
        listView.setDividerHeight(2);
    }

    /**
     * This CustomAdapter takes the resource layout as list_view_row_des1
     * And Obj Arr is favMoviesTitles
     */
    private class CustomAdapter extends ArrayAdapter<String> {
        public CustomAdapter() {
            super(FavoriteMovies.this, R.layout.list_view_row_des1, favMoviesTitles);
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            View rowView = view;
            try {
                if (rowView == null) {
                    LayoutInflater inflater = getLayoutInflater();
                    rowView = inflater.inflate(R.layout.list_view_row_des1, null, true);
                }
                TextView text = rowView.findViewById(R.id.label);
                CheckBox checkBox = rowView.findViewById(R.id.checkbox);

                text.setText(favMoviesTitles.get(position));
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        try {
                            checkboxesStatus.set(position, isChecked);
                            if (isChecked) {
                                // Remove non checked movie
                                favMoviesTitles.remove(favMoviesTitles.get(position));
                                checkboxesStatus.remove(checkboxesStatus.get(position));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return rowView;
        }
    }

    /**
     * Remove selected movies from the favorites and refresh the ListView
     * Then, shows the SnackBar
     * @param view - Current Layout
     */
    public void removeFromFavorites(View view) {
        try {
            movieDatabase.addToFavorites(favMoviesTitles);

            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            new ShowSnackBar(view, "Favorite Movies Updated");
        } catch (Exception e) {
            new ShowSnackBar(view, "Favorite Movies Updated");
        }
    }

    /**
     * Reset favorite movie data into the previous state and refresh the ListView
     * Then, shows the SnackBar
     * @param view - Current Layout
     */
    public void resetStatus(View view) {
        checkboxesStatus.clear();
        for (int i = 0; i < favMoviesTitlesTemp.size(); i++) {
            if (!favMoviesTitles.contains(favMoviesTitlesTemp.get(i))) {
                favMoviesTitles.add(favMoviesTitlesTemp.get(i));
            }
            checkboxesStatus.add(false);
        }

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        new ShowSnackBar(view, "Resetting Favorite Movies");
    }
}