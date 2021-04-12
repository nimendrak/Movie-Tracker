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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nimendra.util.MovieModel;
import com.example.nimendra.db.MovieDatabase;
import com.example.nimendra.util.ShowSnackBar;

import java.util.List;

public class Search extends AppCompatActivity {

    // Class name for Log tag
    private static final String LOG_TAG = Search.class.getSimpleName();

    // Stores all the movies data
    List<MovieModel> searchResults;

    // Gets input to proceed with search
    EditText getSearchChar;

    // Triggers search function
    Button searchMovie;

    // Custom adapter to show id, title
    CustomAdapter customAdapter;

    // ListView for the Search Activity
    ListView listView;

    // Initialize SQLite helper class
    MovieDatabase movieDatabase;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Refers the already declared movieDatabase instance
        movieDatabase = MovieDatabase.getInstance(this);

        getSearchChar = findViewById(R.id.search_input);
        searchMovie = findViewById(R.id.search_movie_btn);
        listView = findViewById(R.id.list_view);

        // If user to wants to edit movie entry, user can clicks on the movie title name and
        // It redirects user to the EditMovie Activity
        final Intent intent = new Intent(this, EditMovie.class);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent.putExtra("selected_item", listView.getAdapter().getItem(position).toString());
                startActivity(intent);
            }
        });

        // Clear and reset EditText to search another movies
        getSearchChar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSearchChar.getText().clear();
                listView.setVisibility(View.INVISIBLE);
            }
        });

        // Set ListView divider color programmatically
        int[] colors = {0, 0xFFFFFFFF, 0};
        listView.setDivider(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors));
        listView.setDividerHeight(2);
    }

    /**
     * Perform a search throughout the db according to the given input
     * @param view - Current Layout
     */
    @SuppressLint("DefaultLocale")
    public void search(View view) {
        try {
            // Hide keyboard when user clicks Lookup Btn
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

            searchResults = movieDatabase.getSearchResults(getSearchChar.getText().toString());

            if (searchResults.isEmpty()) {
                new ShowSnackBar(findViewById(R.id.imdb_data), "No Search Results");
            }

            customAdapter = new CustomAdapter();
            listView.setAdapter(customAdapter);

            listView.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This CustomAdapter takes the resource layout as list_view_row_des2
     * And Obj Arr is searchResults
     */
    private class CustomAdapter extends ArrayAdapter<MovieModel> {
        public CustomAdapter() {
            super(Search.this, R.layout.list_view_row_des2, searchResults);
        }

        @SuppressLint({"InflateParams", "DefaultLocale", "LongLogTag"})
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
            title.setText(searchResults.get(position).getTitle());

            return rowView;
        }
    }
}