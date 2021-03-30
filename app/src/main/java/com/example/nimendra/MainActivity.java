package com.example.nimendra;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    // Class name for Log tag
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    // Hold the value of Switch Stats

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void launchRegisterMovie(View view) {
        Intent intent = new Intent(this, RegisterMovie.class);
        startActivity(intent);
        Log.i(LOG_TAG, "Launched Register Movie");
    }
}