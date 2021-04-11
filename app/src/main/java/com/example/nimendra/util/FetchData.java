package com.example.nimendra.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

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
                // Ratings should change to TotalRating
                String baseUrlMovieRatings = "https://imdb-api.com/en/API/Ratings/" + API_KEY + "/" + id;
                fetchData(baseUrlMovieRatings);

                JSONObject resultsRatings = new JSONObject(data);

                // imDb should change to totalRating
                String totalRatings = resultsRatings.getString("imDb");
                movieRatings.add(totalRatings);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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

        // Set ListView divider color programmatically
        int[] colors = {0, 0xFFFFFFFF, 0};
        listView.setDivider(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors));
        listView.setDividerHeight(2);

        try {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Dialog builder = new Dialog(context);
                    builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    builder.getWindow().setBackgroundDrawable(
                            new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            //nothing;
                        }
                    });

                    ImageView imageView = new ImageView(context);
                    new InjectImages(imageView).execute(moviePosters.get(position));
                    builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
                    builder.show();
                }
            });
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }

    public void fetchData(String baseURL) {
        // Clear get data before re-populating
        data = "";
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

