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
    private int x, y;
    private int speed = 10;

    // Spawn beats within the screen
    private int maxX;
    private int minX;
    private int maxY;
    private int minY;
    private int offset;

    // A hit box for collision detection
    private Rect hitBox;
    private boolean tapped;

    //Getters and Setters
    public Bitmap getBitmap(){ return bitmap; }
    public int getX() { return x; }
    public int getY() { return y; }
    public Rect getHitbox(){ return hitBox; }
    public boolean getTapped(){ return tapped; }
    public void tapped(){ tapped = true; }

    // This is used by the TDView update() method to
    // Make an enemy out of bounds and force a re-spawn
    public void setX(int x) { this.x = x; }

    // Constructor
    public Beat(Context context, int screenX, int screenY){
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.beat);
        Random generator = new Random();
        speed = 10;
        scaleBitmap(screenX);

        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;
        offset = (maxX / 4); // size of 1 column
        if (offset > bitmap.getWidth()) { // if columns are larger than the image then adjust
            offset = (offset - bitmap.getWidth()) /2;
        }

        // Columns
        x = generator.nextInt(maxX) - bitmap.getWidth();
        if(x < maxX * .25){ x = offset; }
        else if(x < maxX * .50){ x = (maxX / 4) + offset; }
        else if(x < maxX * .75){ x = (maxX / 2) + offset; }
        else { x = (int) (maxX * .75) + offset; }

        y = 0-bitmap.getHeight();

        // Init the hit box
        hitBox = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
        tapped = false;
    }

    public void update() {
        y += speed;

        // Respawn
        if (y < minY - bitmap.getHeight()|| y > maxY + bitmap.getHeight()) {
            Random generator = new Random();
            speed = 10;

            // Columns
            x = generator.nextInt(maxX) - bitmap.getWidth();
            if(x < maxX * .25){ x = offset; }
            else if(x < maxX * .50){ x = (maxX / 4) + offset; }
            else if(x < maxX * .75){ x = (maxX / 2) + offset; }
            else { x = (int) (maxX * .75) + offset; }

            y = 0-bitmap.getHeight();

            //check combo
            if (!tapped) { TDView.breakCombo(); }

            tapped = false;
        }

        // Refresh
        hitBox.left = x;
        hitBox.top = y;
        hitBox.right = x + bitmap.getWidth();
        hitBox.bottom = y + bitmap.getHeight();
    }

    public void scaleBitmap(int x){
        if(x < 1000) {
            bitmap = Bitmap.createScaledBitmap(bitmap,
                    bitmap.getWidth() / 3,
                    bitmap.getHeight() / 3,
                    false);
        }else if(x < 1200){
            bitmap = Bitmap.createScaledBitmap(bitmap,
                    bitmap.getWidth() / 2,
                    bitmap.getHeight() / 2,
                    false);
        }
    }
}
