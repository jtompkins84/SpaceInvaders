package com.spaceinvaders;

import android.graphics.Bitmap;
import android.util.Log;

public class Player extends Sprite {

    // projectile origin (x, y) relative to bitmap origin
    private float projectileX, projectileY;

    // x-position of player movement boundaries relative to the screen's resolution
    private float lBoundary, rBoundary;

    /**
     * <code><b><i>Player</i></b></code><p>
     * <code>Player</code> contructor.
     * @param screenWidth <code>float</code> - screen's current width
     * @param screenHeight <code>float</code> - screen's current height
     */
    public Player(Bitmap bitmap, float screenWidth, float screenHeight) {
        super(bitmap, screenWidth, screenHeight, 34.0f, 80.0f, 365.0f, 140.0f);
        Log.v("hitBoxOffset", hitBoxOffset.toString());

        // resize the image to fit device resolution
        resizeToResolution();

        // set postion relative to device resolution
        setPosition((screenWidth / 2),
                screenHeight - (screenHeight / 2.75f));

        // initialize player movement speed
        speed = 350;

        // set the boundaries for the player relative to screen resolution
        lBoundary = screenWidth / 24;
        rBoundary = screenWidth - (screenWidth / 24);
    }

    @Override
    public void update(long fps) {
        if(movement == Movement.RIGHT && hitBox.right < rBoundary) {
            x = x + speed / fps;
        }
        if(movement == Movement.LEFT && hitBox.left > lBoundary) {
            x = x - speed / fps;
        }
        // update hitbox location
        hitBox.offsetTo(x, y);
    }
}