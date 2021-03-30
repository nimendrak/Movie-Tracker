package com.example.nimendra.util;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

class MovieDatabase extends SQLiteOpenHelper {
    @SuppressLint("StaticFieldLeak")
    public static MovieDatabase instance;

    private static final String DB_NAME = "MovieDatabase";
    private static final String DB_TABLE = "movies";
    private static final int DB_VERSION = 1;

    Context ctx;
    SQLiteDatabase myDb;

    private MovieDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        ctx = context;
    }

    public static MovieDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new MovieDatabase(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DB_TABLE + " (mov_id text primary key, mov_title text, mov_year text, mov_director text, mov_ratings text, mov_reviews text);");
        Log.i("Database", "Table Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + DB_TABLE);
        onCreate(db);
    }

    public void insertData(String mov_id, String mov_title, String mov_year, String mov_director, String mov_cast, String mov_ratings, String mov_reviews) {
        try {
            myDb = getWritableDatabase();
            myDb.execSQL("insert into " + DB_TABLE + " (mov_id,mov_title, mov_year, mov_director, mov_cast, mov_ratings, mov_reviews) values ('" + mov_id + "' , '"
                    + mov_title + "' , '" + mov_year + "' ,'" + mov_director + "' , '" + mov_cast + "' , '" + mov_ratings + "' , '" + mov_reviews + "');");
            Toast.makeText(ctx, "Data Saved", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(ctx, "This ID is already exists", Toast.LENGTH_SHORT).show();
        }
    }

    public String getAll() {
        myDb = getReadableDatabase();
        Cursor cr = myDb.rawQuery("Select * from " + DB_TABLE, null);
        StringBuilder str = new StringBuilder();

        while (cr.moveToNext()) {
            String s1 = cr.getString(0);
            String s2 = cr.getString(1);
            String s3 = cr.getString(2);
            String s4 = cr.getString(3);
            String s5 = cr.getString(4);
            String s6 = cr.getString(5);
            String s7 = cr.getString(6);
            str.append(s1).append(": ");
            str.append(s2).append(": ");
            str.append(s3).append(": ");
            str.append(s4).append(": ");
            str.append(s5).append(": ");
            str.append(s6).append(": ");
            str.append(s7).append("\n");
        }

        return str.toString();
    }

    public void delete(String id) {
        try {
            myDb = getWritableDatabase();
            myDb.delete(DB_TABLE, "mov_id=?", new String[]{id});
            Toast.makeText(ctx, "Deleted", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(ctx, "Could not find ID", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(ctx, "Updated", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(ctx, "Could not find ID", Toast.LENGTH_SHORT).show();
        }
    }
}


