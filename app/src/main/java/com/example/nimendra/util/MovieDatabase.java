package com.example.nimendra.util;

import android.annotation.SuppressLint;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import static android.provider.BaseColumns._ID;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MovieDatabase extends SQLiteOpenHelper {
    @SuppressLint("StaticFieldLeak")
    public static MovieDatabase instance;

    private static final String DB_NAME = "MovieDatabase";
    private static final String DB_TABLE = "movies";
    private static final int DB_VERSION = 3;

    Context context;
    SQLiteDatabase myDb;

    private MovieDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    public static MovieDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new MovieDatabase(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DB_TABLE + " (" + _ID + " INTEGER primary key autoincrement, mov_title TEXT unique, mov_year INTEGER, mov_director TEXT, mov_cast TEXT, mov_ratings INTEGER, mov_reviews TEXT, isFavourite BOOLEAN);");
        Log.i("Database", "Table Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + DB_TABLE);
        onCreate(db);
    }

    public void insertData(String title, int year, String director, String cast, int ratings, String reviews) {
        try {
            myDb = getWritableDatabase();
            myDb.execSQL("insert into " + DB_TABLE + " (mov_title, mov_year, mov_director, mov_cast, mov_ratings, mov_reviews, isFavourite) values ('" + title + "' , '" + year + "' , '" + director + "' , '" + cast + "' , '" + ratings + "' , '" + reviews + "' , '" + 0 + "');");

            // Sample data
            myDb.execSQL("insert into " + DB_TABLE + " (mov_title, mov_year, mov_director, mov_cast, mov_ratings, mov_reviews, isFavourite) values ('" + "Zack Snyder Justice League" + "' , '" + 2021 + "' , '" + "Zack Snyder" + "' , '" + "Ben Affleck, Henry Cavil, Ezra Miller, Jason Mamoa" + "' , '" + 10 + "' , '" + "t’s bloodier. More profane. Its quasi-spirituality can be perplexing. And then there’s this: It’s just a darker movier" + "' , '" + 0 + "');");
            myDb.execSQL("insert into " + DB_TABLE + " (mov_title, mov_year, mov_director, mov_cast, mov_ratings, mov_reviews, isFavourite) values ('" + "Tenet" + "' , '" + 2020 + "' , '" + "Chrsitopher Nolan" + "' , '" + "John Washington, Robert Pattinson, Elizabeth Debicki" + "' , '" + 8 + "' , '" + "Tenet is far from Nolans finest work" + "' , '" + 0 + "');");
            myDb.execSQL("insert into " + DB_TABLE + " (mov_title, mov_year, mov_director, mov_cast, mov_ratings, mov_reviews, isFavourite) values ('" + "Enola Holmes" + "' , '" + 2020 + "' , '" + "Harry Bradbeer" + "' , '" + "Millie Bobby Brown, Henry Cavil, Sam Claffin" + "' , '" + 6 + "' , '" + "Enola Holmes delivers mostly positive messages about individuality, equality and freedom." + "' , '" + 0 + "');");

        } catch (Exception e) {
            e.printStackTrace();
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
                            cursor.getInt(0),
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
            Log.i("MovieData", movieModelData.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.sort(movieModelData);
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
                            cursor.getInt(0),
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
                return movieModelData;
            }
            return movieModelData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movieModelData;
    }

    public void updateMovieData(String id, String title, int year, String director, String cast, int ratings, String reviews, int isFav, View view) {
        try {
            myDb = getWritableDatabase();

            System.out.println(id);
            ContentValues cv = new ContentValues();
            cv.put("mov_title", title);
            cv.put("mov_year", year);
            cv.put("mov_director", director);
            cv.put("mov_cast", cast);
            cv.put("mov_ratings", ratings);
            cv.put("mov_reviews", reviews);
            cv.put("isFavourite", isFav);

            myDb.update(DB_TABLE, cv, "_id = ?", new String[]{id});
            new ShowSnackBar(view, "Movie Data Updated");

        } catch (Exception e) {
            new ShowSnackBar(view, "ID not available");
        }
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long getDbSize() {
        myDb = getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(myDb, DB_TABLE);
        myDb.close();
        return count;
    }

    public void showAll() {
        myDb = getReadableDatabase();

        @SuppressLint("Recycle")
        Cursor cr = myDb.rawQuery("Select * from " + DB_TABLE, null);

        System.out.println("ShowAll");
        while (cr.moveToNext()) {
            String s1 = cr.getString(0);
            String s2 = cr.getString(1);
            String s3 = cr.getString(2);
            String s4 = cr.getString(3);
            String s5 = cr.getString(4);
            String s6 = cr.getString(5);
            String s7 = cr.getString(6);
            String s8 = cr.getString(7);

            System.out.print(s1 + " - " + s2 + ", isFavourite -> " + s8 + " | ");

        }
        myDb.close();
    }
}


