package com.android.sjc5283.jcwdefender;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Credits extends Activity
        implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        // Set up Back Button
        final Button buttonPlay =
                (Button)findViewById(R.id.btnBack);
        buttonPlay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // The Back Btn (Its the only button er wet up)
        // Create a new Intent object, and switch activities
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
