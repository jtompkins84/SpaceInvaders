package com.spaceinvaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.util.Log;

import com.tutorials.joseph.spaceinvaders.R;

public class Player extends Sprite {

    // projectile origin (x, y) relative to bitmap origin
    private float projectileX, projectileY;

    // x-position of player movement boundaries relative to the screen's resolution
    private float lBoundary, rBoundary;

    /**
     * <code><b><i>Player</i></b></code><p>
     * <code>Player</code> contructor.
     * @param context the context that this <code>Player</code> belongs to
     * @param screenWidth <code>float</code> - screen's current width
     * @param screenHeight <code>float</code> - screen's current height
     */
    public Player(Context context, float screenWidth, float screenHeight) {
        // Initialize the hit-box
        hitBox = new RectF();

        // set the boundaries for the player relative to screen resolution
        lBoundary = 0.0f + (screenWidth / 24);
        rBoundary = screenWidth - (screenWidth / 24);

        // initialize the player graphics
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
        // ratio of width / height for calculating resize based on screen resolution
        float ratio = screenWidth / screenHeight;

        // resize the image to fit device resolution
        bitmap = Bitmap.createScaledBitmap(bitmap,
                (int)(bitmap.getWidth() * (ratio / 1.5f)), (int)(bitmap.getHeight() * (ratio / 1.5f)), false);

        // set postion relative to device resolution
        this.x = (screenWidth / 2) - (bitmap.getWidth() / 2);
        this.y = screenHeight - (screenHeight / 2.75f);

        // initialize player movement speed
        speed = 350;
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
