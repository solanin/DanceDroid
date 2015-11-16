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

    // Detect mines leaving the screen
    private int maxX;
    private int minX;

    // Spawn mines within screen bounds
    private int maxY;
    private int minY;

    // A hit box for collision detection
    private Rect hitBox;

    //Getters and Setters
    public Bitmap getBitmap(){ return bitmap; }
    public int getX() { return x; }
    public int getY() { return y; }
    public Rect getHitbox(){ return hitBox; }

    // This is used by the TDView update() method to
    // Make an enemy out of bounds and force a re-spawn
    public void setX(int x) { this.x = x; }

    // Constructor
    public Beat(Context context, int screenX, int screenY){
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.beat);

        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;

        Random generator = new Random();
        speed = 10;
        scaleBitmap(screenX);

        x = generator.nextInt(maxX) - bitmap.getWidth();
        if (x < bitmap.getWidth()) { x = bitmap.getWidth(); }
        y = 0-bitmap.getHeight();

        // Init the hit box
        hitBox = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public void update() {
        y += speed;

        // Respawn
        if (y < minY - bitmap.getHeight()|| y > maxY + bitmap.getHeight()) {
            Random generator = new Random();
            speed = 10;
            x = generator.nextInt(maxX) - bitmap.getWidth();
            if (x < bitmap.getWidth()) { x = bitmap.getWidth(); }
            y = 0-bitmap.getHeight();
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
