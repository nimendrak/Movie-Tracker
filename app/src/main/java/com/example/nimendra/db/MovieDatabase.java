package com.example.nimendra.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.provider.BaseColumns._ID;

public class MovieDatabase extends SQLiteOpenHelper {
    @SuppressLint("StaticFieldLeak")
    public static MovieDatabase instance;

    // Class name for Log tag
    private static final String LOG_TAG = MovieDatabase.class.getSimpleName();

    private static final String DB_NAME = "MovieDatabase";
    private static final String DB_TABLE = "movies";
    private static final int DB_VERSION = 1;

    Context context;
    SQLiteDatabase myDb;

    private MovieDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    // Using singleton design pattern
    // In order to consume same MovieDatabase throughout the project
    public static MovieDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new MovieDatabase(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DB_TABLE + " (" + _ID + " INTEGER primary key autoincrement, mov_title TEXT unique, mov_year INTEGER, mov_director TEXT, mov_cast TEXT, mov_ratings INTEGER, mov_reviews TEXT, isFavourite BOOLEAN);");
        Log.i(LOG_TAG, " Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + DB_TABLE);
        onCreate(db);
    }

    public void insertData(MovieModel movieModel) {
        try {
            myDb = getWritableDatabase();
            myDb.execSQL("insert into " + DB_TABLE + " (mov_title, mov_year, mov_director, mov_cast, mov_ratings, mov_reviews, isFavourite) values ('" + movieModel.getTitle() + "' , '" + movieModel.getYear() + "' , '" + movieModel.getDirector() + "' , '" + movieModel.getCast() + "' , '" + movieModel.getRatings() + "' , '" + movieModel.getReviews() + "' , '" + movieModel.getIsFav() + "');");

            // Sample data
            myDb.execSQL("insert into " + DB_TABLE + " (mov_title, mov_year, mov_director, mov_cast, mov_ratings, mov_reviews, isFavourite) values ('" + "Zack Snyder Justice League" + "' , '" + 2021 + "' , '" + "Zack Snyder" + "' , '" + "Ben Affleck, Henry Cavil, Ezra Miller, Jason Mamoa" + "' , '" + 10 + "' , '" + "It’s bloodier. More profane. Its quasi-spirituality can be perplexing. And then there’s this: It’s just a darker movier" + "' , '" + 0 + "');");
            myDb.execSQL("insert into " + DB_TABLE + " (mov_title, mov_year, mov_director, mov_cast, mov_ratings, mov_reviews, isFavourite) values ('" + "Tenet" + "' , '" + 2020 + "' , '" + "Chrsitopher Nolan" + "' , '" + "John Washington, Robert Pattinson, Elizabeth Debicki" + "' , '" + 8 + "' , '" + "Tenet is far from Nolans finest work" + "' , '" + 0 + "');");
            myDb.execSQL("insert into " + DB_TABLE + " (mov_title, mov_year, mov_director, mov_cast, mov_ratings, mov_reviews, isFavourite) values ('" + "Enola Holmes" + "' , '" + 2020 + "' , '" + "Harry Bradbeer" + "' , '" + "Millie Bobby Brown, Henry Cavil, Sam Claffin" + "' , '" + 6 + "' , '" + "Enola Holmes delivers mostly positive messages about individuality, equality and freedom." + "' , '" + 0 + "');");

            Log.i(LOG_TAG, " Movie Data Inserted");
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }

    public List<MovieModel> retrieveMoviesData() {
        myDb = getReadableDatabase();
        Cursor cursor = myDb.rawQuery("Select * from " + DB_TABLE, null);

        List<MovieModel> movieModelData = new ArrayList<>();
        try {
            if (cursor != null) {
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    movieModelData.add(new MovieModel(
                            cursor.getString(1),
                            cursor.getInt(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getInt(5),
                            cursor.getString(6),
                            cursor.getInt(7)
                    ));
                    cursor.moveToNext();
                }
                cursor.close();
                myDb.close();
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        Collections.sort(movieModelData);
        Log.i(LOG_TAG, " retrieveMoviesData() returned");
        return movieModelData;
    }

    public List<String> retrieveFavoriteData() {
        myDb = getReadableDatabase();

        String query = "SELECT * FROM " + DB_TABLE + " where isFavourite='" + 1 + "'";
        Cursor cursor = myDb.rawQuery(query, null);

        List<String> favoriteMovieList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            favoriteMovieList.add(cursor.getString(cursor.getColumnIndex("mov_title")));
            while (cursor.moveToNext()) {
                favoriteMovieList.add(cursor.getString(cursor.getColumnIndex("mov_title")));
            }
        }

        cursor.close();
        myDb.close();

        // Sorting the movies alphabetically
        Collections.sort(favoriteMovieList);
        Log.i(LOG_TAG, " Favorite Movies Data returned");
        return favoriteMovieList;
    }

    public List<String> retrieveSpecificMovie(String movieTitle) {
        myDb = getReadableDatabase();
        String query = "SELECT * FROM " + DB_TABLE + " where mov_title='" + movieTitle + "'";
        Cursor cursor = myDb.rawQuery(query, null);

        List<String> movieData = new ArrayList<>();

        if (cursor.moveToFirst()) {
            movieData.add(String.valueOf(cursor.getInt(cursor.getColumnIndex("_id"))));
            movieData.add(cursor.getString(cursor.getColumnIndex("mov_title")));
            movieData.add(cursor.getString(cursor.getColumnIndex("mov_year")));
            movieData.add(cursor.getString(cursor.getColumnIndex("mov_director")));
            movieData.add(cursor.getString(cursor.getColumnIndex("mov_cast")));
            movieData.add(cursor.getString(cursor.getColumnIndex("mov_ratings")));
            movieData.add(cursor.getString(cursor.getColumnIndex("mov_reviews")));
            movieData.add(cursor.getString(cursor.getColumnIndex("isFavourite")));
        }

        cursor.close();
        myDb.close();

        Log.i(LOG_TAG, " Specific Movies Data returned");
        return movieData;
    }

    public List<MovieModel> getSearchResults(String searchInput) {
        myDb = getReadableDatabase();

        String query = "SELECT * FROM " + DB_TABLE + " where mov_title like ? or mov_director like ? or mov_cast like ?";
        String[] params = {"%" + searchInput + "%", "%" + searchInput + "%", "%" + searchInput + "%"};
        Cursor cursor = myDb.rawQuery(query, params);

        List<MovieModel> movieModelData = new ArrayList<>();

        // Clear before repopulating
        movieModelData.clear();

        try {
            if (cursor != null) {
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    movieModelData.add(new MovieModel(
                            cursor.getString(1),
                            cursor.getInt(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getInt(5),
                            cursor.getString(6),
                            cursor.getInt(7)
                    ));
                    cursor.moveToNext();
                }
                cursor.close();
                myDb.close();

                Log.i(LOG_TAG, " Search Results returned");
                return movieModelData;
            }
            return movieModelData;
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        return movieModelData;
    }

    public void updateMovieData(String id, MovieModel movieModel) {
        try {
            myDb = getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put("mov_title", movieModel.getTitle());
            cv.put("mov_year", movieModel.getYear());
            cv.put("mov_director", movieModel.getDirector());
            cv.put("mov_cast", movieModel.getCast());
            cv.put("mov_ratings", movieModel.getRatings());
            cv.put("mov_reviews", movieModel.getReviews());
            cv.put("isFavourite", movieModel.getIsFav());

            myDb.update(DB_TABLE, cv, "_id = ?", new String[]{id});

        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        Log.i(LOG_TAG, " Movie Data Updated");
    }

    public void addToFavorites(List<String> favMovieList) {
        myDb = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("isFavourite", true);

        try {
            // Set all isFavorites to false and update accordingly
            myDb.execSQL("UPDATE movies SET isFavourite = 0");
            for (int i = 0; i < favMovieList.size(); i++) {
                myDb.update(DB_TABLE, cv, "mov_title = ?", new String[]{favMovieList.get(i)});
            }

            Log.i(LOG_TAG, " Added to Favorite Movies");
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }

    public long getDbSize() {
        myDb = getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(myDb, DB_TABLE);
        myDb.close();
        return count;
    }
}


