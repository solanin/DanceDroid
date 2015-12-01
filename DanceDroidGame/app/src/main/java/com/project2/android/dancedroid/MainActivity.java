package com.project2.android.dancedroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  getSystemService(Context.AUDIO_SERVICE).getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE);
        // Set up Play Button


        final ImageButton buttonPlay =
                (ImageButton)findViewById(R.id.btnPlay);
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new Intent object, and switch activities
                Intent i = new Intent(MainActivity.this, Game.class);
                startActivity(i);
                finish();

            }
        });

        // Set up Settings Button
        final ImageButton buttonSettings =
                (ImageButton)findViewById(R.id.btnSettings);
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
        final ImageButton buttonCredits =
                (ImageButton)findViewById(R.id.btnCredits);
        buttonCredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new Intent object, and switch activities
                Intent i = new Intent(MainActivity.this, Credits.class);
                startActivity(i);
                finish();
            }
        });

        final ImageButton buttonExit =
                (ImageButton)findViewById(R.id.btnExit);
        buttonExit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //close app completely
                finish();
                System.exit(0);
            }
        });

        // Fastest Time
        SharedPreferences prefs;
        SharedPreferences.Editor editor;
        prefs = getSharedPreferences("HiScores", MODE_PRIVATE);

        // None yet
        long fastestTime = prefs.getLong("fastestTime", 1000000);
    }//onCreate

}