package com.example.nimendra.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import com.example.nimendra.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("StaticFieldLeak")
public class FetchData extends AsyncTask<Void, Void, Void> {

    // Class name for Log tag
    private static final String LOG_TAG = FetchData.class.getSimpleName();

    Context context;
    Activity activity;

    private String data = "";
    private final String API_KEY;
    private final String selectedMovie;

    private final List<String> movieIds = new ArrayList<>();

    private final List<String> movieTitles = new ArrayList<>();
    private final List<String> movieRatings = new ArrayList<>();
    private final List<String> moviePosters = new ArrayList<>();

    public FetchData(String selectedMovie, Activity activity, Context context) {
        this.selectedMovie = selectedMovie;
        this.activity = activity;
        this.context = context;

        API_KEY = context.getResources().getString(R.string.MY_API_KEY);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            // Fetch movie titles from API
            String baseUrlMovieTitles = "https://imdb-api.com/en/API/SearchTitle/" + API_KEY + "/" + selectedMovie;
            fetchData(baseUrlMovieTitles);

            JSONObject returnedDataTitles = new JSONObject(data);
            JSONArray resultsTitles = returnedDataTitles.getJSONArray("results");

            for (int i = 0; i < resultsTitles.length(); i++) {
                JSONObject resultDataTitles = resultsTitles.getJSONObject(i);

                String movieTitle = resultDataTitles.getString("title");
                String movieYear = resultDataTitles.getString("description");
                movieTitles.add(movieTitle + " " + movieYear);

                String id = resultDataTitles.getString("id");
                movieIds.add(id);

                String imgURL = resultDataTitles.getString("image");
                moviePosters.add(imgURL);
            }

            // Fetch movie ratings from API
            for (String id : movieIds) {
                String baseUrlMovieRatings = "https://imdb-api.com/en/API/Ratings/" + API_KEY + "/" + id;
                fetchData(baseUrlMovieRatings);

                JSONObject resultsRatings = new JSONObject(data);

                String totalRatings = resultsRatings.getString("imDb");
                movieRatings.add(totalRatings);
            }
        } catch (Exception ex) {
            ex.printStackTrace();

            // delete later
            if (movieRatings.isEmpty()) {
                for (int i = 0; i < movieTitles.size(); i++) {
                    movieRatings.add("9.9");
                }
            }

        }

        System.out.println();
        Log.i(LOG_TAG + " Movie IDs ", String.valueOf(movieIds));
        Log.i(LOG_TAG + " Movie Titles ", String.valueOf(movieTitles));
        Log.i(LOG_TAG + " Movie Ratings ", String.valueOf(movieRatings));
        Log.i(LOG_TAG + " Movie Posters ", String.valueOf(moviePosters.size()));

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        CustomAdapter customAdapter = new CustomAdapter(context, R.layout.list_view_row_des3, movieTitles, movieRatings, moviePosters);
        ListView listView = activity.findViewById(R.id.list_view);
        listView.setAdapter(customAdapter);
    }

    public void fetchData(String baseURL) {
        try {
            Uri builtURI = Uri.parse(baseURL);
            URL requestURL = new URL(builtURI.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection) requestURL.openConnection();

            InputStream inputStream = httpURLConnection.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();
                data = data + line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

