package com.android.sjc5283.jcwdefender;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity
        implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up Play Button
        final Button buttonPlay =
                (Button)findViewById(R.id.btnPlay);
        buttonPlay.setOnClickListener(this);

        // Fastest Time
        SharedPreferences prefs;
        SharedPreferences.Editor editor;
        prefs = getSharedPreferences("HiScores", MODE_PRIVATE);

        // None yet
        long fastestTime = prefs.getLong("fastestTime", 1000000);

        // Set
        final TextView textFastestTime = (TextView)findViewById(R.id.txtHighScore);
        textFastestTime.setText("Fastest Time:" + fastestTime);
    }

    @Override
    public void onClick(View v) {
        // The Play Btn (Its the only button er wet up)
        // Create a new Intent object, and switch activities
        Intent i = new Intent(this, GameActivity.class);
        startActivity(i);
        finish();
    }
}
