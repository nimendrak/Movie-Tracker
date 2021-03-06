package com.example.nimendra.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.io.IOException;
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
    private final List<Bitmap> moviePosters = new ArrayList<Bitmap>();

    public FetchData(String selectedMovie, Activity activity, Context context) {
        this.selectedMovie = selectedMovie;
        this.activity = activity;
        this.context = context;

        API_KEY = context.getResources().getString(R.string.MY_API_KEY);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        ProgressDialog progressDialog = ProgressDialog.show(context, "Fetching..", "Fetching Data from IMDB server..");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dlg) {
                FetchData.this.cancel(true);
            }
        });
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
                try {
                    URL url = new URL(imgURL);
                    Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    moviePosters.add(image);
                } catch(IOException e) {
                    System.out.println(e);
                }
            }

            // Fetch movie ratings from API
            for (String id : movieIds) {
                // TODO : Ratings should change to TotalRating
                String baseUrlMovieRatings = "https://imdb-api.com/en/API/Ratings/" + API_KEY + "/" + id;
                fetchData(baseUrlMovieRatings);

                JSONObject resultsRatings = new JSONObject(data);

                // TODO : imDb should change to totalRating
                String totalRatings = resultsRatings.getString("imDb");
                movieRatings.add(totalRatings);
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        System.out.println();
        Log.i(LOG_TAG + "Movie IDs ", String.valueOf(movieIds));
        Log.i(LOG_TAG + "Movie Titles ", String.valueOf(movieTitles));
        Log.i(LOG_TAG + "Movie Ratings ", String.valueOf(movieRatings));
        Log.i(LOG_TAG + "Movie Posters ", String.valueOf(moviePosters));

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        // Populate data on the ListView using CustomAdapter
        CustomAdapter customAdapter = new CustomAdapter(context, R.layout.list_view_row_des3, movieTitles, movieRatings, moviePosters);
        ListView listView = activity.findViewById(R.id.list_view);
        listView.setAdapter(customAdapter);

        // Set ListView divider color programmatically
        int[] colors = {0, 0xFFFFFFFF, 0};
        listView.setDivider(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors));
        listView.setDividerHeight(2);

        // When user clicks on a row, the movie poster will be enlarged
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
                    imageView.setImageBitmap(moviePosters.get(position));
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

    /**
     * Data will be fetched from the given baseURL
     * And, holds all the JSON obj/ arr by data variable
     * @param baseURL - API url to get preferred data
     */
    public void fetchData(String baseURL) {
        // Clear data before re-populating
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
            Log.e(LOG_TAG, e.getMessage());
        }
    }
}

