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
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    ArrayList<Beat> beats = new ArrayList<Beat>();
    private int NUM_BEATS = 5;

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

        SoundManager.getInstance().SetMusic(R.raw.pianotune, context);
        startGame();
    }

    private void startGame(){

        // Set up accuracy
        int centerVertical = screenY-(screenY/4);
        tapboxBoo = new Rect(0, centerVertical-75, screenX, centerVertical+75);
        tapboxGood = new Rect(0, centerVertical-50, screenX, centerVertical+50);
        tapboxGreat =  new Rect(0, centerVertical-25, screenX, centerVertical+25);
        tapboxPerfect =  new Rect(0, centerVertical-5, screenX, centerVertical+5);

        for (int i = 0; i < NUM_BEATS; i++){
            beats.add(new Beat(context, screenX, screenY));
        }
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

    private void update() {
        // Update the player & enemies
        beats.get(0).update();
        if(timeTaken > 1000)
            beats.get(1).update();
        if(timeTaken > 1400)
            beats.get(2).update();
        if(timeTaken > 1800)
            beats.get(3).update();
        if(timeTaken > 2200)
            beats.get(4).update();


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

            // Draw Columns
            paint.setColor(Color.argb(100, 255, 255, 255));
            canvas.drawLine(0, 0, 0, screenY, paint);
            canvas.drawLine(screenX*.25f, 0, screenX*.25f, screenY, paint);
            canvas.drawLine(screenX*.50f, 0, screenX*.50f, screenY, paint);
            canvas.drawLine(screenX*.75f, 0, screenX*.75f, screenY, paint);
            canvas.drawLine(screenX, 0, screenX, screenY, paint);

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
            paint.setColor(Color.argb(255, 255, 255, 255));
            canvas.drawRect(tapboxPerfect.left,
                    tapboxPerfect.top,
                    tapboxPerfect.right,
                    tapboxPerfect.bottom,
                    paint);
            for (int i = 0; i< beats.size(); i++){
                if (beats.get(i).getTapped()) {
                    paint.setColor(Color.argb(255, 0, 0, 150));
                    canvas.drawRect(beats.get(i).getHitbox().left,
                            beats.get(i).getHitbox().top,
                            beats.get(i).getHitbox().right,
                            beats.get(i).getHitbox().bottom,
                            paint);
                }
            }

            // Draw player & enemies
            for (int i = 0; i < beats.size(); i++){
                canvas.drawBitmap(beats.get(i).getBitmap(), beats.get(i).getX(), beats.get(i).getY(), paint);
            }

            if(!gameEnded) {
                // Draw the hud
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setColor(Color.argb(255, 255, 255, 255));
                paint.setTextSize(40);
                canvas.drawText("Score:" + beatsTapped, 10, 40, paint);
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
                String text = "hit";
                int toastColor = Color.WHITE;
                Integer beatIndex = null;
                boolean foundBeat = false;
                // COLLISION//// TODO: 12/7/2015 Make all beat checking into a loop
                for (int i = 0; i < beats.size(); i++){
                    if (motionEvent.getX() > beats.get(i).getHitbox().left &&
                            motionEvent.getX() <  beats.get(i).getHitbox().right &&
                            motionEvent.getY() <  beats.get(i).getHitbox().bottom &&
                            motionEvent.getY() >  beats.get(i).getHitbox().top) {
                        beatIndex = i;
                        foundBeat = true;
                        break;
                     }
                }//for
                if(!foundBeat){
                    // break combo
                    beatsCombo = 0;
                    beatIndex = null;
                    text = "Miss";
                    toastColor = Color.rgb(255,0,0);
                }
                else {
                    if (beatIndex != null) {
                        if (Rect.intersects(beats.get(beatIndex).getHitbox(), tapboxBoo)) {
                            beats.get(beatIndex).tapped();
                            text = "Boo!";
                            toastColor = Color.rgb(255, 0, 255);
                            if (Rect.intersects(beats.get(beatIndex).getHitbox(), tapboxGood)) {
                                beatsCombo++;
                                text = "Good!";
                                toastColor = Color.rgb(0, 255, 255);
                                if (Rect.intersects(beats.get(beatIndex).getHitbox(), tapboxGreat)) {
                                    currentMultipler = GREAT_MULTIPLIER;
                                    text = "Great!";
                                    toastColor = Color.rgb(0, 255, 0);
                                    if (Rect.intersects(beats.get(beatIndex).getHitbox(), tapboxPerfect)) {
                                        currentMultipler = PERFECT_MULTIPLIER;
                                        text = "Perfect!";
                                        toastColor = Color.rgb(255, 255, 0);
                                    }
                                }

                                beatsTapped += beatsCombo * currentMultipler;
                            } else {
                                beatsCombo = 0;
                            }
                        } else {
                            // break combo
                            beatsCombo = 0;

                            text = "Miss";
                            toastColor = Color.rgb(255, 0, 0);
                        }
                    }//if beatIndex != null
                }//else
                Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                v.setTextColor(toastColor);
                toast.getView().setBackgroundColor(Color.TRANSPARENT);
                toast.show();
                break;
        }//switch
        return true;
    }//function

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

