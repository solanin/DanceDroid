package com.project2.android.dancedroid;

/**
 * Created by Jake on 11/14/2015.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.ArrayList;

public class TDView extends SurfaceView implements Runnable{

    private boolean gameEnded;

    private Context context;

    private int screenX;
    private int screenY;

    private float distanceRemaining;
    private long timeTaken;
    private long timeStarted;
    private long fastestTime;

    volatile boolean playing;
    Thread gameThread = null;

    // Game Objs
    public Beat beat1;

    // Drawing Objs
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder ourHolder;

    // For saving and loading the high score
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    //Access SoundManager
    SoundManager mSoundManager;


    public TDView(Context context, int x, int y) {
        super(context);
        this.context  = context;

        screenX = x;
        screenY = y;

        // Init Drawing
        ourHolder = getHolder();
        paint = new Paint();

        // Load fastest time
        prefs = context.getSharedPreferences("HiScores", context.MODE_PRIVATE);
        // Initialize the editor ready
        editor = prefs.edit();
        // Load fastest time
        // if not available our highscore = 1000000
        fastestTime = prefs.getLong("fastestTime", 1000000);

        mSoundManager = new SoundManager(R.raw.pianotune, context);


        startGame();
    }

    private void startGame(){

        beat1 = new Beat(context, screenX, screenY);

        // Reset time and distance
        distanceRemaining = 10000;// 10 km
        timeTaken = 0;

        // Get start time
        timeStarted = System.currentTimeMillis();

        gameEnded = false;
    }

    @Override
    public void run() {
        // Game Loop
        while (playing) {
            update();
            draw();
            control();
        }
    }

    private void update(){

        // Update the player & enemies
        beat1.update();
        mSoundManager.PlayMusic(1.0f);
        if(!gameEnded) {
            //subtract distance to home planet based on current speed
            distanceRemaining -= 1;

            //How long has the player been flying
            timeTaken = System.currentTimeMillis() - timeStarted;
        }
    }

    private void draw(){

        if (ourHolder.getSurface().isValid()) {

            // Lock
            canvas = ourHolder.lockCanvas();

            // Clear last frame
            canvas.drawColor(Color.argb(255, 0, 0, 0));

            // DEBUGGING
            // Draw Hit boxes
            /*
            // Switch to white pixels
            paint.setColor(Color.argb(255, 255, 255, 255));
            canvas.drawRect(player.getHitbox().left,
                    player.getHitbox().top,
                    player.getHitbox().right,
                    player.getHitbox().bottom,
                    paint);
            */

            // Draw player & enemies
            canvas.drawBitmap(beat1.getBitmap(), beat1.getX(), beat1.getY(), paint);

            if(!gameEnded) {
                // Draw the hud
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setColor(Color.argb(255, 255, 255, 255));
                paint.setTextSize(25);
            }else{
                // Show pause screen
                paint.setTextSize(80);
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText("Game Over", screenX / 2, 100, paint);
                paint.setTextSize(25);
            }

            // Unlock & Draw
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void control(){
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {

        }
    }

    // SurfaceView allows us to handle the onTouchEvent
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        // There are many different events in MotionEvent
        // We care about just 2 - for now.
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            // Has the player lifted there finger up?
            case MotionEvent.ACTION_UP:
                break;

            // Has the player touched the screen?
            case MotionEvent.ACTION_DOWN:
                break;
        }
        return true;
    }

    // Stop thread on quit
    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {

        }
    }

    // Start new thread
    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    private String formatTime(long time){
        long seconds = (time) / 1000;
        long thousandths = (time) - (seconds * 1000);
        String strThousandths = "" + thousandths;
        if (thousandths < 100){strThousandths = "0" + thousandths;}
        if (thousandths < 10){strThousandths = "0" + strThousandths;}
        String stringTime = "" + seconds + "." + strThousandths;
        return stringTime;
    }
}

