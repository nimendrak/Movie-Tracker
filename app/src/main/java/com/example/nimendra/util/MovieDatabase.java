package com.example.nimendra.util;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
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
        db.execSQL("create table " + DB_TABLE + " (mov_title text primary key, mov_year text, mov_director text, mov_cast text, mov_ratings text, mov_reviews text);");
        Log.i("Database", "Table Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + DB_TABLE);
        onCreate(db);
    }

    public void insertData(String mov_title, String mov_year, String mov_director, String mov_cast, String mov_ratings, String mov_reviews) {
        try {
            myDb = getWritableDatabase();
            myDb.execSQL("insert into " + DB_TABLE + " (mov_title, mov_year, mov_director, mov_cast, mov_ratings, mov_reviews) values ('" + mov_title + "' , '" + mov_year + "' , '" + mov_director + "' , '" + mov_cast + "' , '" + mov_ratings + "' , '" + mov_reviews + "');");
            Toast.makeText(context, "Data Saved", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "This ID is already exists", Toast.LENGTH_SHORT).show();
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

        return movieTitles;
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

    public void addToFave() {

    }

    public long getDbSize() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, DB_TABLE);
        System.out.println(count);
        db.close();
        return count;
    }
}


