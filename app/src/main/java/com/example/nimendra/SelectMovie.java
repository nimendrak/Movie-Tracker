package com.example.nimendra;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nimendra.db.MovieModel;
import com.example.nimendra.db.MovieDatabase;
import com.example.nimendra.util.FetchData;

import java.util.ArrayList;
import java.util.List;

public class SelectMovie extends AppCompatActivity {

    // Class name for Log tag
    private static final String LOG_TAG = DisplayMovies.class.getSimpleName();

    // Initialize SQLite helper class
    MovieDatabase movieDatabase;

    private List<String> movieTitles = new ArrayList<>();

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_movie);

        // Refers the already declared movieDatabase instance
        movieDatabase = MovieDatabase.getInstance(this);

        // Populate movieTitles arr using the movieModelData
        List<MovieModel> movieModelData = movieDatabase.retrieveMoviesData();
        for (MovieModel m : movieModelData) {
            movieTitles.add(m.getTitle());
        }

        CustomAdapter customAdapter = new CustomAdapter();
        final ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(customAdapter);

        TextView title = findViewById(R.id.activity_title);
        final Intent intent;
        // Based on the Extra user will be redirected either Ratings Activity or EditMovie Activity
        if (getIntent().getExtras().getBoolean("ratings")) {
            title.setText(R.string.ratings_title);

            intent = new Intent(this, Ratings.class);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    intent.putExtra("selected_item", listView.getAdapter().getItem(position).toString());
                    startActivity(intent);
                }
            });
        } else {
            title.setText(R.string.edit_movie_data_title);

            intent = new Intent(this, EditMovie.class);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    intent.putExtra("selected_item", listView.getAdapter().getItem(position).toString());
                    startActivity(intent);
                }
            });
        }

        // Set ListView divider color programmatically
        int[] colors = {0, 0xFFFFFFFF, 0};
        listView.setDivider(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors));
        listView.setDividerHeight(2);
    }

    /**
     * This CustomAdapter takes the resource layout as list_view_row_des2
     * And Obj Arr is movieTitles
     */
    private class CustomAdapter extends ArrayAdapter<String> {
        public CustomAdapter() {
            super(SelectMovie.this, R.layout.list_view_row_des1, movieTitles);
        }

        @SuppressLint({"InflateParams", "DefaultLocale"})
        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            View rowView = view;
            if (rowView == null) {
                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(R.layout.list_view_row_des2, null, true);
            }
            TextView index = rowView.findViewById(R.id.index);
            TextView title = rowView.findViewById(R.id.title);

            index.setText(String.format("%03d", position));
            title.setText(movieTitles.get(position));

            return rowView;
        }
    }
}