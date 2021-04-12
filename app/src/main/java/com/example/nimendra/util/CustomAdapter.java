package com.example.nimendra.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nimendra.R;

import java.util.List;

/**
 * This CustomAdapter takes the resource layout as list_view_row_des3
 * And the Obj Arrays are movieTitles, movieRatings and moviePosters
 */
public class CustomAdapter extends ArrayAdapter<String> {

    int resourceLayout;
    Context context;

    // Stores all the returned movieTitles
    private List<String> movieTitles;

    // Stores all the returned movieRatings
    private List<String> movieRatings;

    // Stores all the returned moviePosters as link
    private List<String> moviePosters;

    public CustomAdapter(Context context, int resource, List<String> movieTitles, List<String> movieRatings, List<String> moviePosters) {
        super(context, resource, movieTitles);

        this.resourceLayout = resource;
        this.context = context;

        this.movieTitles = movieTitles;
        this.movieRatings = movieRatings;
        this.moviePosters = moviePosters;
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        View rowView = view;

        try {
            if (rowView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                rowView = inflater.inflate(resourceLayout, null, true);
            }
            TextView text = rowView.findViewById(R.id.title);
            TextView rating = rowView.findViewById(R.id.rating);
            ImageView image = rowView.findViewById(R.id.image);

            text.setText(movieTitles.get(position));
            rating.setText("IMDB Rating : " + movieRatings.get(position));

            // Images will inject to the holders through a AsyncTask
            new InjectImages(image).execute(moviePosters.get(position));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rowView;
    }
}
