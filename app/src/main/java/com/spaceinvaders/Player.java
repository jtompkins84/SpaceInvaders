package com.spaceinvaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

import com.tutorials.joseph.spaceinvaders.R;

public class Player {

    // The player ship will be represented by a Bitmap
    private Bitmap bitmap;

    // How long and high our hit-box will be
    private float length;
    private float height;

    // (x, y) makes the top left coordinate of the player
    private float x, y;

    // player hit-box
    RectF hitBox;

    // pixel/second speed of the player
    private float speed;

    // enumeration of player movement directions
    private enum Movement {STOPPED, LEFT, RIGHT};

    // variable tracks current movement of player
    private Movement playerMotion = Movement.STOPPED;

    /**
     * <code><b><i>Player</i></b></code><p>
     * <code>Player</code> contructor.
     * @param context the context that this <code>Player</code> belongs to
     * @param x <code>float</code> - left-most coordinate
     * @param y <code>float</code> - top-most coordinate
     */
    public Player(Context context, float x, float y) {
        // Initialize the hit-box
        hitBox = new RectF();

        // initialize the player graphics
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);

        // resize the image to fit resolution
        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2, false);
    }
}
