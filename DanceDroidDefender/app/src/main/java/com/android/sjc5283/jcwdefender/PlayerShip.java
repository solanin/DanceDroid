package com.android.sjc5283.jcwdefender;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

/**
 * Created by Jake on 10/23/2015.
 */
public class PlayerShip {

    private Bitmap bitmap;
    private int x, y;
    private int speed;

    private int shieldStrength;
    private boolean boosting;
    private final int GRAVITY = -12;

    // Stop ship leaving the screen
    private int maxY;
    private int minY;

    private final int MIN_SPEED = 1;
    private final int MAX_SPEED = 20;

    // A hit box for collision detection
    private Rect hitBox;

    //Getters
    public Bitmap getBitmap() { return bitmap; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getSpeed() { return speed; }
    public Rect getHitbox(){ return hitBox; }
    public int getShieldStrength() { return shieldStrength; }

    // Make the Ship
    public PlayerShip(Context context, int screenX, int screenY) {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ship);
        x = 50;
        y = 50;
        shieldStrength = 10000;
        speed = 1;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ship);

        maxY = screenY - bitmap.getHeight();
        minY = 0;

        // Initi the hit box
        hitBox = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    // Boost control
    public void setBoosting() { boosting = true; }
    public void stopBoosting() { boosting = false; }
    public void reduceShieldStrength(){ shieldStrength --; }

    public void update() {
        if (boosting) {
            speed += 2;
        } else {
            speed -= 5;
        }

        if (speed > MAX_SPEED) {
            speed = MAX_SPEED;
        }

        if (speed < MIN_SPEED) {
            speed = MIN_SPEED;
        }

        // fly up or down
        y -= speed + GRAVITY;

        // Don't let ship stray off screen
        if (y < minY) {
            y = minY;
        }
        if (y > maxY) {
            y = maxY;
        }

        // Refresh hit box location
        hitBox.left = x;
        hitBox.top = y;
        hitBox.right = x + bitmap.getWidth();
        hitBox.bottom = y + bitmap.getHeight();
    }
}

