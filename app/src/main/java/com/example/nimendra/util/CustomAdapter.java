package com.example.nimendra.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nimendra.R;

import java.io.InputStream;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<String> {

    int resourceLayout;
    Context context;

    private List<String> movieTitles;
    private List<String> movieRatings;
    private List<String> moviePosters;

    public CustomAdapter(Context context, int resource, List<String> movieTitles, List<String> movieRatings, List<String> moviePosters) {
        super(context, resource, movieTitles);

        this.resourceLayout = resource;
        this.context = context;

        this.movieTitles = movieTitles;
        this.movieRatings = movieRatings;
        this.moviePosters = moviePosters;

        System.out.println(movieRatings);
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
            new DownloadImageTask(image).execute(moviePosters.get(position));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rowView;
    }
}

class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    @SuppressLint("StaticFieldLeak")
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}