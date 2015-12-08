package com.project2.android.dancedroid;

/**
 * Created by Jake on 11/14/2015.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TDView extends SurfaceView implements Runnable{

    // Game
    volatile boolean playing;
    Thread gameThread = null;
    private Context context;

    // Game over Controls
    private boolean gameEnded;
    private int MAX_LIVES = 5;
    public int lives = 0;

    // Screen
    private int screenX;
    private int screenY;

    // Score
    private int beatsTapped;
    private int beatsCombo;
    private int highestCombo;
    private long timeTaken;
    private long timeStarted;
    private long fastestTime;

    // Beats
    ArrayList<Beat> beats = new ArrayList<Beat>();
    private int NUM_BEATS = 7;

    // Accuracy
    public Rect tapboxPerfect;
    public Rect tapboxGreat;
    public Rect tapboxGood;
    public Rect tapboxBoo;
    public int counterPerfect;
    public int counterGreat;
    public int counterGood;
    public int counterBoo;
    public int counterMiss;

    // Score
    private int currentMultiplier;
    public int PERFECT_MULTIPLIER = 10;
    public int GREAT_MULTIPLIER = 5;
    public int GOOD_MULTIPLIER = 1;
    public int BOO_MULTIPLIER = 0;

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

        // Lives
        lives = MAX_LIVES;
        gameEnded = false;

        // Set up accuracy
        int centerVertical = screenY-(screenY/4);
        tapboxBoo = new Rect(0, centerVertical-75, screenX, centerVertical+75);
        tapboxGood = new Rect(0, centerVertical-50, screenX, centerVertical+50);
        tapboxGreat =  new Rect(0, centerVertical-25, screenX, centerVertical+25);
        tapboxPerfect =  new Rect(0, centerVertical-5, screenX, centerVertical+5);
        counterPerfect = 0;
        counterGreat = 0;
        counterGood = 0;
        counterBoo = 0;
        counterMiss = 0;

        for (int i = 0; i < NUM_BEATS; i++){
            beats.add(new Beat(context, screenX, screenY));
        }
        // Reset time and distance
        timeTaken = 0;
        beatsTapped = 0;
        beatsCombo = 0;
        highestCombo = 0;

        // Get start time
        timeStarted = System.currentTimeMillis();
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

        // Play Sound
       // SoundManager.getInstance().PlayMusic(1.0f);

        if (lives <= 0) {
            gameEnded = true;
        }

        if(!gameEnded) {
            //How long has the player been flying
            timeTaken = System.currentTimeMillis() - timeStarted;

            // Check if beats hit bottom untapped
            for (int i = 0; i < beats.size(); i++) {
                if (!beats.get(i).getTapped()){
                    if (beats.get(i).getY() > tapboxBoo.bottom) {
                        currentMultiplier = BOO_MULTIPLIER;
                        counterMiss++;
                        beatsCombo = 0;

                        // LOOSE LIFE
                        lives--;

                        // Tap
                        beats.get(i).tapped("Miss", Color.rgb(255, 0, 0));
                    }
                }
            }


            // Update the player & enemies
            beats.get(0).update();
            if(timeTaken > 1200)
                beats.get(1).update();
            if(timeTaken > 1800)
                beats.get(2).update();
            if(timeTaken > 2400)
                beats.get(3).update();
            if(timeTaken > 3300)
                beats.get(4).update();
            if(timeTaken > 4000)
                beats.get(5).update();
            if(timeTaken > 4700)
                beats.get(6).update();
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
            canvas.drawLine(screenX * .25f, 0, screenX*.25f, screenY, paint);
            canvas.drawLine(screenX * .50f, 0, screenX * .50f, screenY, paint);
            canvas.drawLine(screenX * .75f, 0, screenX * .75f, screenY, paint);
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

            // Draw player & enemies
            for (int i = 0; i < beats.size(); i++){

                // Blue BG
                if (beats.get(i).getTapped()) {
                    paint.setColor(Color.argb(255, 0, 0, 150));
                    canvas.drawRect(beats.get(i).getHitbox().left,
                            beats.get(i).getHitbox().top,
                            beats.get(i).getHitbox().right,
                            beats.get(i).getHitbox().bottom,
                            paint);
                }

                canvas.drawBitmap(beats.get(i).getBitmap(), beats.get(i).getX(), beats.get(i).getY(), paint);

                // if Clicked show result
                if (beats.get(i).getTapped()) {
                    paint.setTextAlign(Paint.Align.CENTER);
                    paint.setColor(beats.get(i).getColor());
                    paint.setTextSize(40);
                    canvas.drawText(beats.get(i).getResult(), beats.get(i).getX() +
                            (beats.get(i).getBitmap().getWidth()/2), beats.get(i).getY() +
                            beats.get(i).getBitmap().getHeight() + 40, paint);
                }
            }

            if(!gameEnded) {
                // Draw the hud
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setColor(Color.argb(255, 255, 255, 255));
                paint.setTextSize(40);
                canvas.drawText("Score:" + beatsTapped, 10, 40, paint);
                canvas.drawText("Lives:" + lives, (screenX /2)-100, 40, paint);
                //canvas.drawText("Time:" + formatTime(timeTaken), (screenX /2)-100, 40, paint);
                canvas.drawText("Combo:" + beatsCombo, screenX - 200, 40, paint);

                paint.setColor(Color.argb(255, 150, 150, 150));
                canvas.drawText("x" + currentMultiplier,10, 90, paint);
                canvas.drawText("(" + highestCombo + ")", screenX - 200, 90, paint);

            }else{
                // Show Results screen
                canvas.drawColor(Color.argb(255, 0, 0, 0));
                paint.setColor(Color.argb(255, 255, 255, 255));
                paint.setTextSize(80);
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText("Game Over", screenX / 2, (screenY / 2) - 430, paint);
                paint.setTextSize(40);
                paint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText("Time: ", (screenX /2)-200, (screenY /2) -350, paint);
                canvas.drawText("Score: ", (screenX /2)-200, (screenY /2) -300, paint);
                canvas.drawText("Combo: ", (screenX /2)-200, (screenY /2) -250, paint);
                canvas.drawText("Perfect: ", (screenX /2)-200, (screenY /2) -200, paint);
                canvas.drawText("Great: ", (screenX /2)-200, (screenY /2) -150, paint);
                canvas.drawText("Good: ", (screenX /2)-200, (screenY /2) -100, paint);
                canvas.drawText("Boo: ", (screenX /2)-200, (screenY /2) -50, paint);
                canvas.drawText("Miss: ", (screenX /2)-200, (screenY /2), paint);
                canvas.drawText(formatTime(timeTaken) + " s", (screenX /2)+100, (screenY /2) -350, paint);
                canvas.drawText(""+beatsTapped, (screenX /2)+100, (screenY /2) -300, paint);
                canvas.drawText(""+highestCombo, (screenX /2)+100, (screenY /2) -250, paint);
                canvas.drawText(""+counterPerfect, (screenX /2)+100, (screenY /2) -200, paint);
                canvas.drawText(""+counterGreat, (screenX /2)+100, (screenY /2) -150, paint);
                canvas.drawText(""+counterGood, (screenX /2)+100, (screenY /2) -100, paint);
                canvas.drawText(""+counterBoo, (screenX /2)+100, (screenY /2) -50, paint);
                canvas.drawText(""+counterMiss, (screenX /2)+100, (screenY /2), paint);

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
        String text = "default";
        int toastColor = Color.DKGRAY;
        // There are many different events in MotionEvent
        // We care about just 2 - for now.
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            // Has the player lifted there finger up?
            case MotionEvent.ACTION_UP:
                break;

            // Has the player touched the screen?
            case MotionEvent.ACTION_DOWN:
                Integer beatIndex = null;
                boolean foundBeat = false;

                // COLLISION

                // Did you tap a beat?
                for (int i = 0; i < beats.size(); i++){
                    if (motionEvent.getX() > beats.get(i).getHitbox().left &&
                            motionEvent.getX() <  beats.get(i).getHitbox().right &&
                            motionEvent.getY() <  beats.get(i).getHitbox().bottom &&
                            motionEvent.getY() >  beats.get(i).getHitbox().top) {
                        beatIndex = i;
                        foundBeat = true;
                        break;
                     }

                }// end for


                // No, you did not tap a beat
                if(!foundBeat){
                    text = "Miss";

                    // For now, Do Nothing
                }
                // Yes, You tapped a beat
                else {
                    if (!beats.get(beatIndex).getTapped()) {
                        // How accurate were you?
                        if (Rect.intersects(beats.get(beatIndex).getHitbox(), tapboxBoo)) {
                            text = "Boo!";
                            if (Rect.intersects(beats.get(beatIndex).getHitbox(), tapboxGood)) {
                                text = "Good!";
                                if (Rect.intersects(beats.get(beatIndex).getHitbox(), tapboxGreat)) {
                                    text = "Great!";
                                    if (Rect.intersects(beats.get(beatIndex).getHitbox(), tapboxPerfect)) {
                                        text = "Perfect!";
                                    }
                                }
                            }
                        }
                        // You tapped, but missed the bar
                        else {
                            text = "Miss";
                        }

                        switch (text){
                            case "Miss":
                                currentMultiplier = BOO_MULTIPLIER;
                                counterMiss++;
                                toastColor = Color.rgb(255, 0, 0);
                                beatsCombo = 0;

                                // LOOSE LIFE
                                lives--;

                                break;
                            case "Boo!":
                                currentMultiplier = BOO_MULTIPLIER;
                                counterBoo++;
                                toastColor = Color.rgb(255, 0, 255);
                                beatsCombo = 0;
                                break;
                            case "Good!":
                                currentMultiplier = GOOD_MULTIPLIER;
                                counterGood++;
                                toastColor = Color.rgb(0, 255, 255);
                                beatsCombo++;
                                break;
                            case "Great!":
                                currentMultiplier = GREAT_MULTIPLIER;
                                counterGreat++;
                                toastColor = Color.rgb(0, 255, 0);
                                beatsCombo++;
                                break;
                            case "Perfect!":
                                currentMultiplier = PERFECT_MULTIPLIER;
                                counterPerfect++;
                                toastColor = Color.rgb(255, 255, 0);
                                beatsCombo++;
                                break;
                        }

                        // Check highest
                        if (beatsCombo > highestCombo) {
                            highestCombo = beatsCombo;
                        }

                        beatsTapped += beatsCombo * currentMultiplier;
                        beats.get(beatIndex).tapped(text, toastColor);
                    }
                }// end if beat was tapped
                break;
        }//switch
        return true;
    } //end function

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

    public void myToast(String text, int color) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        v.setTextColor(color);
        toast.getView().setBackgroundColor(Color.TRANSPARENT);
        toast.show();
    }
}

