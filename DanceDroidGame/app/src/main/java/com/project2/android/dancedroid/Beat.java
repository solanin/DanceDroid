package com.project2.android.dancedroid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

/**
 * Created by Jake on 11/14/2015.
 */
public class Beat {

    private Bitmap bitmap;
    private String result;
    private int color;

    private int x, y;
    private int speed = 16;

    // Spawn beats within the screen
    private int maxX;
    private int minX;
    private int maxY;
    private int minY;
    private int offset;

    private static int catCounter = 0;
    private static int dogCounter = 0;
    private static int bearCounter = 0;
    private static int bunCounter = 0;

    Context c;
    // A hit box for collision detection
    private Rect hitBox;
    private boolean tapped;

    //Getters and Setters
    public Bitmap getBitmap(){ return bitmap; }
    public int getX() { return x; }
    public int getY() { return y; }
    public String getResult(){ return result; }
    public int getColor(){ return color; }
    public Rect getHitbox(){ return hitBox; }
    public boolean getTapped(){ return tapped; }

    public void setSpeed(int value){
        speed = value;
    }
    public int getSpeed(){
        return speed;
    }
    public void tapped(){ tapped = true; }

    // Tapp
    public void tapped(String text, int toastColor){
        tapped = true;
        result = text;
        color = toastColor;
    }

    // This is used by the TDView update() method to
    // Make an enemy out of bounds and force a re-spawn
    public void setX(int x) {
        this.x = x; }

    // Constructor
    public Beat (Context context, int screenX, int screenY) {
        //needs to stay for getBitmap()
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.beat);
        scaleBitmap(screenX);

        // Set up
        Random generator = new Random();
        speed = 10;
        c = context;

        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;

        // Columns
        x = generator.nextInt(maxX) - bitmap.getWidth();
        x = setColumn(x, c);

        // Spawn Height
        y = 0-bitmap.getHeight();

        // Init the hit box
        hitBox = new Rect(x-offset, y, bitmap.getWidth()+offset, bitmap.getHeight());
        tapped = false;
    }

    public void update() {

        y += speed;

        //do switch or if cases to change generation speed based on song time

        // Respawn
        if ( y < minY - bitmap.getHeight() || y > maxY ) {
            Random generator = new Random();

            // New Location
            x = generator.nextInt(maxX) - bitmap.getWidth();
            x = setColumn(x, c);
            y = 0-bitmap.getHeight();

            tapped = false;
        }

        // Refresh
        hitBox.left = x - offset;
        hitBox.top = y;
        hitBox.right = x + bitmap.getWidth() + offset;
        hitBox.bottom = y + bitmap.getHeight();
    }

    public void scaleBitmap(int x){
        if (x < 1000) {
            bitmap = Bitmap.createScaledBitmap(bitmap,
                    bitmap.getWidth() / 3,
                    bitmap.getHeight() / 3,
                    false);
        } else if (x < 1200) {
            bitmap = Bitmap.createScaledBitmap(bitmap,
                    bitmap.getWidth() / 2,
                    bitmap.getHeight() / 2,
                    false);
        }
    }

    public int setColumn(int x, Context context){
        offset = (maxX / 4); // size of 1 column
        if (offset > bitmap.getWidth()) { // if columns are larger than the image then adjust
            offset = (offset - bitmap.getWidth()) /2;
        }

        if(x < maxX * .25){
            if(catCounter >= 3){
                x = (maxX / 4) + offset;
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.doge);
                dogCounter++;
                catCounter = 0;
            }
            else{
                x = offset;
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.cat);
                catCounter++;

                dogCounter = 0;
                bunCounter = 0;
                bearCounter = 0;
            }

        }
        else if(x < maxX * .50){
            if(dogCounter >=3){
                x = (maxX / 2) + offset;
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bear);
                bearCounter++;
                dogCounter = 0;
            }
            else{
                x = (maxX / 4) + offset;
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.doge);
                dogCounter++;

                catCounter = 0;
                bunCounter = 0;
                bearCounter = 0;
            }
        }
        else if(x < maxX * .75){
            if(bearCounter >= 3){
                x = (int) (maxX * .75) + offset;
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bun);
                bunCounter++;
                bearCounter = 0;
            }
            else{
                x = (maxX / 2) + offset;
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bear);
                bearCounter++;

                catCounter = 0;
                dogCounter = 0;
                bunCounter = 0;
            }
        }
        else {
            if(bunCounter >= 3){
                x = offset;
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.cat);
                catCounter++;
                bunCounter = 0;
            }
            else{
                x = (int) (maxX * .75) + offset;
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bun);
                bunCounter++;

                catCounter = 0;
                dogCounter = 0;
                bearCounter = 0;
            }
        }
        scaleBitmap(maxX); // We changed the image
        return x;
    }
}
