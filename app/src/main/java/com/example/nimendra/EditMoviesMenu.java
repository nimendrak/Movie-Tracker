package com.example.nimendra;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

import com.example.nimendra.util.MovieDatabase;

import java.util.ArrayList;
import java.util.List;

public class EditMoviesMenu extends AppCompatActivity {

    // Class name for Log tag
    private static final String LOG_TAG = DisplayMovies.class.getSimpleName();

    private List<String> movieTitles;
    private List<String> indexOfMovies = new ArrayList<>();

    MovieDatabase movieDatabase;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_movies_menu);

        movieDatabase = MovieDatabase.getInstance(this);
        movieDatabase.showAll();

        // Movie titles list
        movieTitles = movieDatabase.retrieveMoviesData();

        for (int i = 0; i < movieTitles.size(); i++) {
            indexOfMovies.add(String.format("%03d", (i+1)));
        }
        System.out.println(indexOfMovies);

        CustomAdapter customAdapter = new CustomAdapter();
        final ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(customAdapter);

        final Intent intent = new Intent(this, EditMovie.class);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent.putExtra("selected_item", listView.getAdapter().getItem(position).toString());
                startActivity(intent);
            }
        });

        // Set ListView divider color programmatically
        int[] colors = {0, 0xFFFFFFFF, 0};
        listView.setDivider(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors));
        listView.setDividerHeight(1);
    }

    private class CustomAdapter extends ArrayAdapter<String> {
        public CustomAdapter() {
            super(EditMoviesMenu.this, R.layout.list_view_row_des1, movieTitles);
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            View rowView = view;
            if (rowView == null) {
                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(R.layout.list_view_row_des2, null, true);
            }
            TextView index = rowView.findViewById(R.id.index);
            TextView title = rowView.findViewById(R.id.title);

            index.setText(indexOfMovies.get(position));
            title.setText(movieTitles.get(position));

            return rowView;
        }
    }
}