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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MovieDatabase extends SQLiteOpenHelper {
    @SuppressLint("StaticFieldLeak")
    public static MovieDatabase instance;

    private static final String DB_NAME = "MovieDatabase";
    private static final String DB_TABLE = "movies";
    private static final int DB_VERSION = 2;

    Snackbar snackbar;

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
        } catch (Exception e) {
            showSnackBar(view, "Movie is already recorded");
        }
    }

    public List<String> retrieveData() {
        myDb = getReadableDatabase();
        Cursor cursor = myDb.rawQuery("Select * from " + DB_TABLE, null);

        List<String> movieTitles = new ArrayList<>();
        if (cursor.moveToFirst()) {
            movieTitles.add(cursor.getString(cursor.getColumnIndex("mov_title")));
            while (cursor.moveToNext()) {
                movieTitles.add(cursor.getString(cursor.getColumnIndex("mov_title")));
            }
        }
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

//        Cursor cursor = myDb.rawQuery("Select * from " + DB_TABLE, null);

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

    public void delete(String title) {
        try {
            myDb = getWritableDatabase();
            myDb.delete(DB_TABLE, "mov_title=?", new String[]{title});
            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Could not find Movie Title", Toast.LENGTH_SHORT).show();
        }
    }

    public void update(String emp_id, String emp_name, String emp_address, String emp_age, String emp_position) {
        try {
            myDb = getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put("emp_name", emp_name);
            cv.put("emp_address", emp_address);
            cv.put("emp_age", emp_age);
            cv.put("emp_position", emp_position);

            myDb.update(DB_TABLE, cv, "emp_id = ?", new String[]{emp_id});
            Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(context, "Could not find ID", Toast.LENGTH_SHORT).show();
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
        }
    }

    public long getDbSize() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, DB_TABLE);
        System.out.println(count);
        db.close();
        return count;
    }

    // And shows a SnackBar to the consumer with a proper message
    public void showSnackBar(View view, String message) {
        snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
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
    }
}


