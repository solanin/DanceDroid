package com.project2.android.dancedroid;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up Play Button
        final Button buttonPlay =
                (Button)findViewById(R.id.btnPlay);
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new Intent object, and switch activities
                Intent i = new Intent(MainActivity.this, Game.class);
                startActivity(i);
                finish();
            }       });

        // Set up Settings Button
        final Button buttonSettings =
                (Button)findViewById(R.id.btnSettings);
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new Intent object, and switch activities
                Intent i = new Intent(MainActivity.this, Settings.class);
                startActivity(i);
                finish();
            }
        });

        // Set up Credits Button
        final Button buttonCredits =
                (Button)findViewById(R.id.btnCredits);
        buttonCredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new Intent object, and switch activities
                Intent i = new Intent(MainActivity.this, Credits.class);
                startActivity(i);
                finish();
            }
        });

        // Fastest Time
        SharedPreferences prefs;
        SharedPreferences.Editor editor;
        prefs = getSharedPreferences("HiScores", MODE_PRIVATE);

        // None yet
        long fastestTime = prefs.getLong("fastestTime", 1000000);
    }
}