package com.spaceinvaders;

import android.graphics.RectF;

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
    public Player(SpriteImage image, int screenWidth, int screenHeight) {
        // sets the bitmap image and initializes the size of the hit-box.
        // hit-box values are derived by opening the original image in and image editor
        // and determining the dimension of the hit-box in pixels from there.
        super(image, new RectF(9.0f, 21.0f, 99.0f, 56.0f));

        // set postion relative to device resolution
        setPosition(screenWidth / 2, screenHeight - (screenHeight / 12));

        // initialize player movement speed
        speed = 350;

        // set the boundaries for the player relative to screen resolution
        lBoundary = screenWidth / 24;
        rBoundary = screenWidth - (screenWidth / 24);
    }

    @Override
    public void update(long fps) {
        RectF hitBox = getHitBox();
        if(hitBox != null) {
            if(movement == Movement.RIGHT && hitBox.right < rBoundary) {
                move(speed / fps, 0.0f);
            }
            if(movement == Movement.LEFT && hitBox.left > lBoundary) {
                move(-(speed / fps), 0.0f);
            }
        }
    }
}
