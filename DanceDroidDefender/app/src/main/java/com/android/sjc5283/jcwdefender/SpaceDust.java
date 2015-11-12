package com.android.sjc5283.jcwdefender;

import java.util.Random;

/**
 * Created by Jake on 10/26/2015.
 */
public class SpaceDust {

    // Info
    private int x, y;
    private int speed;

    // Screen Limits
    private int maxX;
    private int maxY;
    private int minX;
    private int minY;

    //Getters and Setters
    public int getX() { return x; }
    public int getY() { return y; }

    // Constructor
    public SpaceDust(int screenX, int screenY){

        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;

        Random generator = new Random();
        speed = generator.nextInt(10);

        x = generator.nextInt(maxX);
        y = generator.nextInt(maxY);
    }

    public void update(int playerSpeed){
        x -= playerSpeed;
        x -= speed;

        // Respawn Dust
        if(x < 0){
            x = maxX;
            Random generator = new Random();
            y = generator.nextInt(maxY);
            speed = generator.nextInt(15);
        }
    }
}
