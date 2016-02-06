package com.spaceinvaders;

import android.content.Context;
import android.graphics.Bitmap;

public class DefenseBrick extends Sprite {
    // NOTE: Defense block will be able to be hit 4 times before being destroyed
    private short health = 4;

    // TODO this is just a placeholder constructor
    public DefenseBrick(Context context, Bitmap bitmap) {
        super(context, bitmap);
    }

    @Override
    public void update(long fps) {
        // TODO implement update
    }
}
