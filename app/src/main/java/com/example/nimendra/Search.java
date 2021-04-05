package com.example.nimendra;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nimendra.util.Movie;
import com.example.nimendra.util.MovieDatabase;

import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity {

    MovieDatabase movieDatabase;
    List<Movie> searchResults;
    List<String> indexOfMovies = new ArrayList<>();

    EditText getSearchChar;
    Button searchMovie;

    CustomAdapter customAdapter;
    ListView listView;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSearchChar = findViewById(R.id.search_input);
        searchMovie = findViewById(R.id.search_movie_btn);

        movieDatabase = MovieDatabase.getInstance(this);
        movieDatabase.showAll();

        listView = findViewById(R.id.list_view);

        final Intent intent = new Intent(this, EditMovie.class);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("When Clicked -> " + listView.getAdapter().getItem(position).toString());
                intent.putExtra("selected_item", listView.getAdapter().getItem(position).toString());
                startActivity(intent);
            }
        });

        // Set ListView divider color programmatically
        int[] colors = {0, 0xFFFFFFFF, 0};
        listView.setDivider(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors));
        listView.setDividerHeight(1);
    }

    @SuppressLint("DefaultLocale")
    public void searchMovies(View view) {
        try {
            String inputStr = getSearchInput(getSearchChar);

            searchResults = movieDatabase.getSearchResults(inputStr);
            System.out.println(searchResults);

            indexOfMovies.clear();
            for (int i = 0; i < searchResults.size(); i++) {
                indexOfMovies.add(String.format("%03d", (i + 1)));
            }
            System.out.println(indexOfMovies);
            customAdapter = new CustomAdapter();
            listView.setAdapter(customAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getSearchInput(final EditText holder) {
        final String[] strInputs = new String[1];
        final InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);

        if (holder != null) {
            // In case, user didn't consume imOptions
            final String currentInput = holder.getText().toString();
            strInputs[0] = currentInput;

            // Using imOption
            holder.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    boolean handled = false;
                    if (actionId == EditorInfo.IME_ACTION_SEND) {
                        strInputs[0] = holder.getText().toString();
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        handled = true;
                    }
                    return handled;
                }
            });
        }
        return strInputs[0];
    }

    private class CustomAdapter extends ArrayAdapter<Movie> {
        public CustomAdapter() {
            super(Search.this, R.layout.list_view_row_des2, searchResults);
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
            title.setText(searchResults.get(position).getTitle());

            return rowView;
        }
    }
}