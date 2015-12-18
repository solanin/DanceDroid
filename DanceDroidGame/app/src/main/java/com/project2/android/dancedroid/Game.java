package com.project2.android.dancedroid;

import android.app.Activity;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;

public class Game extends Activity {

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
        SoundManager.getInstance().PlayMusic();
       setContentView(gameView);
    }

    @Override
    protected void onPause() {
        // Pause Thread
        super.onPause();
        gameView.pause();
        SoundManager.getInstance().StopMusic();
    }

    @Override
    protected void onResume() {
        // Resume Thread
        super.onResume();
        gameView.resume();
        SoundManager.getInstance().ResumeMusic();
    }
}
