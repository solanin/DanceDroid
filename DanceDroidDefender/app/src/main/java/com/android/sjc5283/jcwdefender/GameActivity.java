package com.android.sjc5283.jcwdefender;

import android.app.Activity;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;

public class GameActivity extends Activity {

    private TDView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get screen size
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        // The Game View
        gameView = new TDView(this, size.x, size.y);

        setContentView(gameView);
    }

    @Override
    protected void onPause() {
        // Pause Thread
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onResume() {
        // Resume Thread
        super.onResume();
        gameView.resume();
    }
}
