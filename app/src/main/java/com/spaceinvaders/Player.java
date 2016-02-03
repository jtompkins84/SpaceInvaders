package com.spaceinvaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.util.Log;

import com.tutorials.joseph.spaceinvaders.R;

public class Player extends Sprite{

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
        Log.v("SCREEN SIZE", "width = " + screenWidth + " :: height = " + screenHeight);



        // initialize the player graphics
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
        // ratio of width / height for calculating resize based on screen resolution
        float ratio = screenWidth / screenHeight;

        // resize the image to fit resolution
        bitmap = Bitmap.createScaledBitmap(bitmap,
                (int)(bitmap.getWidth() * (ratio / 1.5f)), (int)(bitmap.getHeight() * (ratio / 1.5f)), false);

        this.x = (screenWidth / 2) - (bitmap.getWidth() / 2);
        this.y = screenHeight - 1000;

        // initialize player movement speed
        speed = 350;
    }

    public void update(long fps) {
        if(movement == Movement.LEFT) {
            x = x + speed / fps;
        }
        if(movement == Movement.LEFT) {
            x = x + speed / fps;
        }
        // update hitbox location
        hitBox.offsetTo(x, y);
    }
}
