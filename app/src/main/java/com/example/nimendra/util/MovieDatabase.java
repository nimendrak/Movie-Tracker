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

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MovieDatabase extends SQLiteOpenHelper {
    @SuppressLint("StaticFieldLeak")
    public static MovieDatabase instance;

    private static final String DB_NAME = "MovieDatabase";
    private static final String DB_TABLE = "movies";
    private static final int DB_VERSION = 1;

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

    public void insertData(String title, int year, String director, String cast, int ratings, String reviews, View view) {
        try {
            myDb = getWritableDatabase();
            myDb.execSQL("insert into " + DB_TABLE + " (mov_title, mov_year, mov_director, mov_cast, mov_ratings, mov_reviews, isFavourite) values ('" + title + "' , '" + year + "' , '" + director + "' , '" + cast + "' , '" + ratings + "' , '" + reviews + "' , '" + 0 + "');");
            showSnackBar(view, "Data has been recorded");

            // Sample data
            myDb.execSQL("insert into " + DB_TABLE + " (mov_title, mov_year, mov_director, mov_cast, mov_ratings, mov_reviews, isFavourite) values ('" + "Zack Snyders Justice League" + "' , '" + 2021 + "' , '" + "Zack Snyder" + "' , '" + "Ben Affleck, Henry Cavil, Ezra Miller, Jason Mamoa" + "' , '" + 10 + "' , '" + "WooW-zer" + "' , '" + 0 + "');");
            myDb.execSQL("insert into " + DB_TABLE + " (mov_title, mov_year, mov_director, mov_cast, mov_ratings, mov_reviews, isFavourite) values ('" + "aaa" + "' , '" + 2021 + "' , '" + "abc" + "' , '" + "a, b, c" + "' , '" + 10 + "' , '" + "ooo" + "' , '" + 0 + "');");
            myDb.execSQL("insert into " + DB_TABLE + " (mov_title, mov_year, mov_director, mov_cast, mov_ratings, mov_reviews, isFavourite) values ('" + "bbb" + "' , '" + 2021 + "' , '" + "abc" + "' , '" + "a, b, c" + "' , '" + 10 + "' , '" + "ooo" + "' , '" + 0 + "');");

        } catch (Exception e) {
            showSnackBar(view, "Movie is already recorded");
        }
    }

    public List<String> retrieveMoviesData() {
        myDb = getReadableDatabase();
        Cursor cursor = myDb.rawQuery("Select * from " + DB_TABLE, null);

        List<String> movieTitles = new ArrayList<>();
        if (cursor.moveToFirst()) {
            movieTitles.add(cursor.getString(cursor.getColumnIndex("mov_title")));
            while (cursor.moveToNext()) {
                movieTitles.add(cursor.getString(cursor.getColumnIndex("mov_title")));
            }
        }

        Log.i("Database Movies -> ", String.valueOf(movieTitles));
        cursor.close();
        myDb.close();

        // Sorting the movies alphabetically
        Collections.sort(movieTitles);
        return movieTitles;
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

    public void delete(String title) {
        try {
            myDb = getWritableDatabase();
            myDb.delete(DB_TABLE, "mov_title=?", new String[]{title});
            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Could not find Movie Title", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateMovieData(String id, String title, int year, String director, String cast, int ratings, String reviews, View view) {
        try {
            myDb = getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put("mov_title", title);
            cv.put("mov_year", year);
            cv.put("mov_director", director);
            cv.put("mov_cast", cast);
            cv.put("mov_ratings", ratings);
            cv.put("mov_reviews", reviews);

            myDb.update(DB_TABLE, cv, "_id = ?", new String[]{id});
            showSnackBar(view, "Movie Data Updated");

        } catch (Exception e) {
            showSnackBar(view, "ID not available");
        }
    }

    public void addToFavorites(View view, List<String> favMovieList) {
        myDb = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("isFavourite", true);

        try {
            // Set all isFavorites to false and update accordingly
            myDb.execSQL("UPDATE movies SET isFavourite = 0");
            for (int i = 0; i < favMovieList.size(); i++) {
                myDb.update(DB_TABLE, cv, "mov_title = ?", new String[]{favMovieList.get(i)});
            }
            showSnackBar(view, "Favorite Movies List Updated");
        } catch (Exception e) {
            e.printStackTrace();
            showSnackBar(view, "Favorite Movies List Updated");
        }
    }

    public long getDbSize() {
        myDb = getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(myDb, DB_TABLE);
        myDb.close();
        return count;
    }

    // And shows a SnackBar to the consumer with a proper message
    public void showSnackBar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.setDuration(2500);
        snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        snackbar.show();
    }

    public void showAll() {
        myDb = getReadableDatabase();

        @SuppressLint("Recycle")
        Cursor cr = myDb.rawQuery("Select * from " + DB_TABLE, null);

        while (cr.moveToNext()) {
            String s1 = cr.getString(0);
            String s2 = cr.getString(1);
            String s3 = cr.getString(2);
            String s4 = cr.getString(3);
            String s5 = cr.getString(4);
            String s6 = cr.getString(5);
            String s7 = cr.getString(6);
            String s8 = cr.getString(7);

            System.out.println(s1 + " - " + s2 + ", isFavourite -> " + s8);

        }
        myDb.close();
    }
}


