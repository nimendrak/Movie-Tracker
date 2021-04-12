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
import com.example.nimendra.util.ShowSnackBar;

import java.util.ArrayList;
import java.util.List;

public class DisplayMovies extends AppCompatActivity {

    // Class name for Log tag
    private static final String LOG_TAG = DisplayMovies.class.getSimpleName();

    // Stores all the movie titles
    private List<String> movieTitles = new ArrayList<>();

    // Stores only the favorites
    private final List<String> favMoviesTitles = new ArrayList<>();

    // Stores the favorite movies isFav status to show in checkboxes
    private final ArrayList<Boolean> checkboxesStatus = new ArrayList<>();

    // Initialize SQLite helper class
    MovieDatabase movieDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_movies);

        // Refers the already declared movieDatabase instance
        movieDatabase = MovieDatabase.getInstance(this);

        // Get all movie data from the database
        List<MovieModel> movieModelData = movieDatabase.retrieveMoviesData();

        // Populating movieTitles and checkboxesStatus arrays accordingly
        for (MovieModel m : movieModelData) {
            movieTitles.add(m.getTitle());

            int isFav = m.getIsFav();
            checkboxesStatus.add(isFav == 1);
        }

        // Initialize and declare CustomerAdapter to display movies
        CustomAdapter adapter = new CustomAdapter();
        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        // Set ListView divider color programmatically
        int[] colors = {0, 0xFFFFFFFF, 0};
        listView.setDivider(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors));
        listView.setDividerHeight(2);
    }

    /**
     * This CustomAdapter takes the resource layout as list_view_row_des1
     * And Obj Arr is movieTitles
     */
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
            CheckBox checkBox = rowView.findViewById(R.id.checkbox);

            text.setText(movieTitles.get(position));
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

    /**
     * Update selected movies as favorite movies in database
     * And display a SnackBar accordingly
     * @param view - Current Layout
     */
    public void AddToFav(View view) {
        try {
            movieDatabase.addToFavorites(favMoviesTitles);
            new ShowSnackBar(view, "Favorite Movies List Updated");
        } catch (Exception e) {
            new ShowSnackBar(view, "Favorite Movies List Updated");
        }
    }

    /**
     * Reset values of the checkboxesStatus and clear favoriteTitles
     * Then, update the listView
     * @param view - Current Layout
     */
    public void resetStatus(View view) {
        for (int i = 0; i < checkboxesStatus.size(); i++) {
            checkboxesStatus.set(i, false);
        }
        favMoviesTitles.clear();
        CustomAdapter adapter = new CustomAdapter();
        ListView listView = findViewById(R.id.list_view);

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}