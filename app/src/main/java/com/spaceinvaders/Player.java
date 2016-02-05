package com.spaceinvaders;

import android.graphics.Bitmap;

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
        super(bitmap, 108.0f, 66.0f, screenWidth, screenHeight, 9.0f, 21.0f, 90.0f, 34.0f);

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
            move(speed / fps, 0.0f);
        }
        if(movement == Movement.LEFT && hitBox.left > lBoundary) {
            move(-(speed / fps), 0.0f);
        }
    }
}