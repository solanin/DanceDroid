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

    private int beatsTapped;
    private static int beatsCombo;
    private long timeTaken;
    private long timeStarted;
    private long fastestTime;

    volatile boolean playing;
    Thread gameThread = null;

    // Game Objs
    public Beat beat1;
    public Rect tapboxPerfect;
    public Rect tapboxGreat;
    public Rect tapboxGood;
    public Rect tapboxBoo;

    public int PERFECT_MULTIPLIER = 10;
    public int GREAT_MULTIPLIER = 5;

    // Drawing Objs
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder ourHolder;

    // For saving and loading the high score
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;


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
        // if not available our highscore = 0
        fastestTime = prefs.getLong("fastestTime", 0);

        SoundManager.getInstance().SetMusic(R.raw.pianowav, context);


        startGame();
    }

    private void startGame(){

        // Set up accuracy
        int centerVertical = screenY-(screenY/4);
        tapboxBoo = new Rect(0, centerVertical-75, screenX, centerVertical+75);
        tapboxGood = new Rect(0, centerVertical-50, screenX, centerVertical+50);
        tapboxGreat =  new Rect(0, centerVertical-25, screenX, centerVertical+25);
        tapboxPerfect =  new Rect(0, centerVertical-5, screenX, centerVertical+5);

        beat1 = new Beat(context, screenX, screenY);

        // Reset time and distance
        timeTaken = 0;
        beatsTapped = 0;
        beatsCombo = 0;

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

        // Play Sound
        SoundManager.getInstance().PlayMusic(1.0f);

        if(!gameEnded) {
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

            // Draw Targets
            paint.setColor(Color.argb(75, 255, 255, 255));
            canvas.drawRect(tapboxBoo.left,
                    tapboxBoo.top,
                    tapboxBoo.right,
                    tapboxBoo.bottom,
                    paint);
            paint.setColor(Color.argb(75, 255, 255, 255));
            canvas.drawRect(tapboxGood.left,
                    tapboxGood.top,
                    tapboxGood.right,
                    tapboxGood.bottom,
                    paint);
            paint.setColor(Color.argb(75, 255, 255, 255));
            canvas.drawRect(tapboxGreat.left,
                    tapboxGreat.top,
                    tapboxGreat.right,
                    tapboxGreat.bottom,
                    paint);
            paint.setColor(Color.argb(75, 255, 255, 255));
            canvas.drawRect(tapboxPerfect.left,
                    tapboxPerfect.top,
                    tapboxPerfect.right,
                    tapboxPerfect.bottom,
                    paint);




            //Tap indicator
            if (beat1.getTapped()) {
                paint.setColor(Color.argb(255, 0, 0, 150));
                canvas.drawRect(beat1.getHitbox().left,
                        beat1.getHitbox().top,
                        beat1.getHitbox().right,
                        beat1.getHitbox().bottom,
                        paint);
            }

            // Draw player & enemies
            canvas.drawBitmap(beat1.getBitmap(), beat1.getX(), beat1.getY(), paint);

            if(!gameEnded) {
                // Draw the hud
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setColor(Color.argb(255, 255, 255, 255));
                paint.setTextSize(40);
                canvas.drawText("Beats:" + beatsTapped, 10, 40, paint);
                canvas.drawText("Time:" + formatTime(timeTaken), (screenX /2)-100, 40, paint);
                canvas.drawText("Combo:" + beatsCombo, screenX - 200, 40, paint);
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

                int currentMultipler = 1;

                // COLLISION DETECTION
                if (Rect.intersects(beat1.getHitbox(), tapboxBoo)) {
                    beat1.tapped();
                    beatsCombo = 0;
                    if (Rect.intersects(beat1.getHitbox(), tapboxGood)) {
                        beatsCombo++;
                        if (Rect.intersects(beat1.getHitbox(), tapboxGreat)) {
                            currentMultipler = GREAT_MULTIPLIER;
                            if (Rect.intersects(beat1.getHitbox(), tapboxPerfect)) {
                                currentMultipler = PERFECT_MULTIPLIER;
                            }
                        }

                        beatsTapped += beatsCombo * currentMultipler;
                    }
                }
                else {
                    // break combo
                    beatsCombo = 0;
                }
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

    public static void breakCombo(){
        beatsCombo = 0;
    }
}

