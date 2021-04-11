package com.example.nimendra.util;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FetchData extends AsyncTask<String, Void, String> {

    // Class name for Log tag
    private static final String LOG_TAG = FetchData.class.getSimpleName();

    private final String API_KEY;
    private String data = "";
    private final String selectedMovie;

    private final List<String> movieIds = new ArrayList<>();

    private final List<String> movieTitles = new ArrayList<>();
    private final List<String> movieRatings = new ArrayList<>();
    private final List<byte[]> moviePoster = new ArrayList<>();

    public FetchData(String selectedMovie, String API_KEY) {
        this.selectedMovie = selectedMovie;
        this.API_KEY = API_KEY;
        fetchData();
    }

    public void fetchData() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String baseURL = "https://imdb-api.com/en/API/SearchTitle/" + API_KEY + "/" + selectedMovie;
                    Uri builtURI = Uri.parse(baseURL);
                    URL requestURL = new URL(builtURI.toString());
                    HttpURLConnection conn = (HttpURLConnection) requestURL.openConnection();

                    InputStream inputStream = conn.getInputStream();

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line = "";
                    while (line != null) {
                        line = bufferedReader.readLine();
                        data = data + line;
                    }

                    JSONObject returnedData = new JSONObject(data);
                    JSONArray results = returnedData.getJSONArray("results");

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject resultData = results.getJSONObject(i);

                        String movieTitle = resultData.getString("title");
                        String movieYear = resultData.getString("description");
                        movieTitles.add(movieTitle + " " + movieYear);

                        String id = resultData.getString("id");
                        movieIds.add(id);
                    }

                    for (String index : movieIds) {
                        try {
                            baseURL = "https://imdb-api.com/en/API/UserRatings/k_q8ltbyxn/" + index;
                            builtURI = Uri.parse(baseURL);
                            requestURL = new URL(builtURI.toString());
                            conn = (HttpURLConnection) requestURL.openConnection();

                            inputStream = conn.getInputStream();

                            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                            line = "";
                            while (line != null) {
                                line = bufferedReader.readLine();
                                data = data + line;
                            }

                            returnedData = new JSONObject(data);
                            results = returnedData.getJSONArray("totalRatings");

                            for (int i = 0; i < results.length(); i++) {
                                JSONObject resultData = results.getJSONObject(i);
                                String totalRatings = resultData.getString("totalRatings");
                                movieRatings.add(totalRatings);
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                System.out.println();
                Log.i(LOG_TAG + " Movie Titles ", String.valueOf(movieTitles));
                Log.i(LOG_TAG + " Movie Ratings ", String.valueOf(movieRatings));
            }
        });
        thread.start();
    }

    public List<String> getMovieTitles() {
        return movieTitles;
    }

    public List<String> getMovieRatings() {
        return movieRatings;
    }

    @Override
    protected String doInBackground(String... strings) {
        return null;
    }
}
