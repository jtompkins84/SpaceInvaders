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
    public Player(Bitmap bitmap, int screenWidth, int screenHeight) {
        super(bitmap, 18.0f, 42.0f, 180.0f, 68.0f);

        // set postion relative to device resolution
        setPosition(screenWidth / 2, screenHeight - (screenHeight / 24));

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